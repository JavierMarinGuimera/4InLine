package com.tocadosmarin.fourinline.game;

import com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager;
import com.tocadosmarin.fourinline.managers.JSONManager;
import com.tocadosmarin.fourinline.managers.LoginManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

public class ClientRunner extends Thread {
    private static final Integer TIMEOUT = 500;
    public static Integer column = null;

    private static String serverHost = "192.168.1.41";
    private static Integer port = 7777;

    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        // Socket creation
        Socket socket = null;
        BufferedReader reader = null;
        PrintStream writer = null;

        try {
            socket = new Socket(serverHost, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());

            socket.setSoTimeout(TIMEOUT);

            waitLayoutResponse();
            writer.println(JSONManager.mountUsernameAndColumnJson(EncryptedSharedPreferencesManager.encryptedPref.getString(LoginManager.USERNAME, ""), column));
            column = null;

            String serverResponseOnCreation = null;

            while (serverResponseOnCreation == null) {
                try {
                    serverResponseOnCreation = reader.readLine();
                } catch (Exception e) {
                    continue;
                }
            }

            /**
             * Check if the server get the user correctly (will return "ERROR" if its not
             * ok):
             */
            if (serverResponseOnCreation.equals("OK")) {
                Map<String, Object> jsonMap = null;
                while (jsonMap == null) {
                    try {
                        jsonMap = JSONManager.getMapFromJsonString(reader.readLine());
                    } catch (SocketTimeoutException ste) {
                        continue;
                    }
                }

                synchronized (ClientRunner.class) {
                    if (jsonMap.containsKey(JSONManager.OPONENT)) {
                        BoardSelection.hasOpponent = true;
                        Game.setPlayerPosition((Integer) jsonMap.get(JSONManager.POSITION));
                    } else {
                        BoardSelection.isServerClosed = true;
                    }
                    ClientRunner.class.notify();
                }
                // Enviar y recibir según si eres jugador 1 o 2:
                if ((Integer) jsonMap.get("position") == 1) {
                    do {
                        System.out.println("\n-----------ROUND-----------");

                        /**
                         * If jsonMap is null, does not contain "result" or does not contain
                         * "server_closed", will let the player choose another chip.
                         *
                         * Else, will show to the user that the server is closed or the oponent won the
                         * match.
                         */
                        sendColumnChoice(jsonMap, writer);

                        /**
                         * If jsonMap IS null, will read the oponent choice.
                         *
                         * Else, will mean that the game has ended and has to show on
                         * the user screen the result.
                         */
                        jsonMap = readOponentColumnChoice(jsonMap, reader);
                    } while (!jsonMap.containsKey("result") && !jsonMap.containsKey("server_closed"));
                } else {
                    jsonMap = null;

                    do {
                        System.out.println("\n-----------ROUND-----------");

                        /**
                         * If jsonMap IS null, will read the oponent choice.
                         *
                         * Else, will mean that the game has ended and has to show on
                         * the user screen the result.
                         */
                        jsonMap = readOponentColumnChoice(jsonMap, reader);

                        /**
                         * If jsonMap is null, does not contain "result" or does not contain
                         * "server_closed", will let the player choose another chip.
                         *
                         * Else, will show to the user that the server is closed or the oponent won the
                         * match.
                         */
                        sendColumnChoice(jsonMap, writer);
                    } while (!jsonMap.containsKey("result") && !jsonMap.containsKey("server_closed"));
                }

            } else {
                System.err.println("Algo ha salido mal en el test del cliente");
            }

            reader.close();
            writer.close();
            socket.close();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void waitLayoutResponse() {
        synchronized (ClientRunner.class) {
            while (column == null) {
                try {
                    ClientRunner.class.wait();
                } catch (InterruptedException e) {
                    continue;
                }
            }
        }
    }

    private void sendColumnChoice(Map<String, Object> jsonMap, PrintStream writer) {
        /**
         * User sends:
         */
        if (!jsonMap.containsKey("result")
                && !jsonMap.containsKey("server_closed")) {
            column = null;
            System.out.print("Escribe: ");
            waitLayoutResponse();
            writer.println(JSONManager.mountColumnJson(column));
            column = null;
        }
    }

    private Map<String, Object> readOponentColumnChoice(Map<String, Object> jsonMap, BufferedReader reader) {
        if (jsonMap != null && (jsonMap.containsKey("result") || jsonMap.containsKey("server_closed")))
            return jsonMap;
        jsonMap = null;
        /**
         * User reads:
         */
        System.out.print("Lectura: ");
        while (jsonMap == null) {
            try {
                jsonMap = JSONManager.getMapFromJsonString(reader.readLine());
            } catch (Exception e) {
                continue;
            }

        }
        System.out.println("column" + jsonMap);

        game.setServerResponse(jsonMap);

        return jsonMap;
    }


}

