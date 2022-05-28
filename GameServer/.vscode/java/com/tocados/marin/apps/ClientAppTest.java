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
    private static String serverHost = "localhost";
    private static Integer port = 7777;

    public static void main(String[] args) {
        // Socket creation
        Socket socket = null;
        BufferedReader reader = null;
        PrintStream writer = null;

        Socket socket2 = null;
        BufferedReader reader2 = null;
        PrintStream writer2 = null;
        try {
            socket = new Socket(serverHost, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());

            /**
             * Eliminar en cliente Android:
             */
            socket2 = new Socket(serverHost, port);
            reader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            writer2 = new PrintStream(socket2.getOutputStream());

            writer.println(JSONManager.mountUsernameAndColumnJson("Javier", 7));

            /**
             * Eliminar en cliente Android:
             */
            writer2.println(JSONManager.mountUsernameAndColumnJson("Sergi", 7));

            /**
             * Check if the server get the user correctly (will return "ERROR" if its not
             * ok):
             */
            if (reader.readLine().equals("OK")) {
                Map<String, Object> jsonMap1 = JSONManager.getMapFromJsonString(reader.readLine());

                Scanner sc = new Scanner(System.in);

                Map<String, Object> serverResponse = null;

                do {
                    // TODO - Enviar y recibir según si eres jugador 1 o 2:
                    if ((Integer) jsonMap1.get("position") == 1) {
                        /**
                         * SOLUCIÓN CLIENTE ANDROID:
                         * writer.println(JSONManager.mountColumnJson(sc.nextInt()));
                         * while (serverResponse != null) {
                         * serverResponse = JSONManager.getMapFromJsonString(reader.readLine());
                         * }
                         * 
                         * if (serverResponse != null && serverResponse.containsKey(JSONManager.RESULT))
                         * {
                         * // TODO - Mostrar 4 en raya y mensaje "Juego finalizado en el cliente"
                         * }
                         */

                        // User 1 sends; user 2 gets:
                        writer.println(JSONManager.mountColumnJson(sc.nextInt()));
                        serverResponse = JSONManager.getMapFromJsonString(reader2.readLine());
                        // Print here on the client board the serverResponse by calling serverResponse
                        System.out.println((Integer) serverResponse.get("column"));

                        // User 2 sends; user 1 gets:
                        writer2.println(JSONManager.mountColumnJson(sc.nextInt()));
                        serverResponse = JSONManager.getMapFromJsonString(reader.readLine());
                        // Print here on the client board the serverResponse by calling serverResponse
                        System.out.println((Integer) serverResponse.get("column"));
                    } else {
                        /**
                         * SOLUCIÓN CLIENTE ANDROID:
                         * while (serverResponse != null) {
                         * serverResponse = JSONManager.getMapFromJsonString(reader.readLine());
                         * }
                         * 
                         * if (serverResponse != null && serverResponse.containsKey(JSONManager.RESULT))
                         * {
                         * // TODO - Mostrar 4 en raya y mensaje "Juego finalizado en el cliente"
                         * }
                         * writer.println(JSONManager.mountColumnJson(sc.nextInt()));
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
                } while (serverResponse != null && !serverResponse.containsKey("result"));

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
