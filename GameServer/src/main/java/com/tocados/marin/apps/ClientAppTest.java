package com.tocados.marin.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;

import com.tocados.marin.managers.JSONManager;

public class ClientAppTest {
    private static final Integer TIMEOUT = 500;

    private static String serverHost = "localhost";
    private static Integer port = 7777;

    public static void main(String[] args) {
        // Socket creation
        Socket socket = null;
        BufferedReader reader = null;
        PrintStream writer = null;

        try {
            Scanner sc = new Scanner(System.in);

            socket = new Socket(serverHost, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());

            socket.setSoTimeout(TIMEOUT);

            writer.println(JSONManager.mountUsernameAndColumnJson(sc.nextLine(), 5));

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

                System.out.println(jsonMap);

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
                        sendColumnChoice(jsonMap, writer, sc);

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
                        sendColumnChoice(jsonMap, writer, sc);
                    } while (!jsonMap.containsKey("result") && !jsonMap.containsKey("server_closed"));
                }

                sc.close();
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

    private static void sendColumnChoice(Map<String, Object> jsonMap, PrintStream writer, Scanner sc) {
        /**
         * User sends:
         */
        if (!jsonMap.containsKey("result")
                && !jsonMap.containsKey("server_closed")) {

            System.out.print("Escribe: ");
            writer.println(JSONManager.mountColumnJson(sc.nextInt()));
        }
    }

    private static Map<String, Object> readOponentColumnChoice(Map<String, Object> jsonMap, BufferedReader reader) {
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

        System.out.println(jsonMap);

        if (jsonMap.containsKey("result") || jsonMap.containsKey("server_closed")) {
            if (jsonMap.containsKey("result")) {
                System.out.println("MOSTRAR AL CLIENTE EL 4 EN RAYA Y FINALIZAR LA PARTIDA.");
            } else {
                System.out.println("MOSTRAR AL CLIENTE QUE EL SERVER SE HA CERRADO INESPERADAMENTE.");
            }
        }

        return jsonMap;
    }
}
