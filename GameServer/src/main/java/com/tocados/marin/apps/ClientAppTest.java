package com.tocados.marin.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
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

        /**
         * Eliminar en cliente Android:
         */
        Socket socket2 = null;
        BufferedReader reader2 = null;
        PrintStream writer2 = null;
        try {
            socket = new Socket(serverHost, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());

            socket.setSoTimeout(TIMEOUT);

            /**
             * Eliminar en cliente Android:
             */
            socket2 = new Socket(serverHost, port);
            reader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            writer2 = new PrintStream(socket2.getOutputStream());

            writer.println(JSONManager.mountUsernameAndColumnJson("Javier", 5));

            String serverResponseOnCreation = reader.readLine();
            /**
             * Eliminar en cliente Android:
             */
            writer2.println(JSONManager.mountUsernameAndColumnJson("Sergi", 5));
            String serverResponseOnCreation2 = reader2.readLine();

            /**
             * Check if the server get the user correctly (will return "ERROR" if its not
             * ok):
             */
            if (serverResponseOnCreation.equals("OK")) {
                Map<String, Object> jsonMap1 = JSONManager.getMapFromJsonString(reader.readLine());

                /**
                 * Eliminar en cliente Android:
                 */
                Map<String, Object> jsonMap2 = JSONManager.getMapFromJsonString(reader2.readLine());

                Scanner sc = new Scanner(System.in);

                Map<String, Object> serverResponse = null;

                do {
                    // Enviar y recibir según si eres jugador 1 o 2:
                    if ((Integer) jsonMap1.get("position") == 1) {
                        /**
                         * SOLUCIÓN CLIENTE ANDROID:
                         * if (!reader.ready()) {
                         * writer.println(JSONManager.mountColumnJson(sc.nextInt()));
                         * } else {
                         * serverResponse = JSONManager.getMapFromJsonString(reader.readLine());
                         * if (serverResponse.containsKey("serverClosed")) {
                         * // TODO - Servidor cerrado por cualquier razón, terminar partida y hacer lo
                         * correspodiente.
                         * }
                         * }
                         * while (serverResponse != null) {
                         * serverResponse = JSONManager.getMapFromJsonString(reader.readLine());
                         * }
                         * 
                         * if (serverResponse != null && serverResponse.containsKey(JSONManager.RESULT))
                         * {
                         * // TODO - Mostrar 4 en raya y mensaje "Juego finalizado en el cliente"
                         * }
                         * 
                         * serverResponse = null;
                         */

                        System.out.println("\n-----------ROUND-----------");

                        if (serverResponse == null || (!serverResponse.containsKey("result")
                                && !serverResponse.containsKey("server_closed"))) {
                            serverResponse = null;

                            /**
                             * User 1 sends:
                             */
                            System.out.print("Jugador 1 escribe:");
                            writer.println(JSONManager.mountColumnJson(sc.nextInt()));
                        } else {
                            if (serverResponse.containsKey("server_closed")) {
                                System.out.println("Server cerrado");
                            } else {
                                System.out.println("Jugador 2 ha ganado.");
                            }
                        }

                        /**
                         * User 2 reads:
                         */
                        System.out.print("Jugador 2 lee:");
                        while (serverResponse == null) {
                            try {
                                serverResponse = JSONManager.getMapFromJsonString(reader2.readLine());
                            } catch (Exception e) {
                                continue;
                            }

                        }
                        // Print here on the client board the serverResponse by calling serverResponse
                        System.out.println(serverResponse);

                        if ((serverResponse == null || (!serverResponse.containsKey("result")
                                && !serverResponse.containsKey("server_closed")))) {
                            serverResponse = null;
                            /**
                             * User 2 sends:
                             */
                            System.out.print("Jugador 2 Escribe:");
                            writer2.println(JSONManager.mountColumnJson(sc.nextInt()));

                        } else {
                            if (serverResponse.containsKey("server_closed")) {
                                System.out.println("Server cerrado");
                            } else {
                                System.out.println("Jugador 2 ha ganado.");
                                serverResponse = null;
                            }
                        }

                        /**
                         * User 1 reads:
                         */
                        System.out.print("Jugador 1 lee:");
                        while (serverResponse == null) {
                            try {
                                serverResponse = JSONManager.getMapFromJsonString(reader.readLine());
                            } catch (Exception e) {
                                continue;
                            }

                        }
                        // Print here on the client board the serverResponse by calling serverResponse
                        System.out.println(serverResponse);
                    } else {
                        /**
                         * Copiar la solución de arriba e invertir el orden lectura-escritura.
                         */
                        // User 2 sends; user 1 gets:
                        writer2.println(JSONManager.mountColumnJson(sc.nextInt()));
                        serverResponse = JSONManager.getMapFromJsonString(reader.readLine());
                        // Print here on the client board the serverResponse by calling serverResponse
                        System.out.println((Integer) serverResponse.get("column"));

                        // User 1 sends; user 2 gets:
                        writer.println(JSONManager.mountColumnJson(sc.nextInt()));
                        serverResponse = JSONManager.getMapFromJsonString(reader2.readLine());
                        // Print here on the client board the serverResponse by calling serverResponse
                        System.out.println((Integer) serverResponse.get("column"));
                    }
                } while (!serverResponse.containsKey("result"));

                sc.close();
            } else {
                System.err.println("Algo ha salido mal en el test del cliente");
            }

            reader.close();
            writer.close();
            socket.close();

            reader2.close();
            writer2.close();
            socket2.close();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
