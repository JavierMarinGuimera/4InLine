package com.tocadosmarin.fourinline.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HTTPManager {

    public enum Paths {
        USERS_GET_ALL_PATH("/users", "GET"),
        USERS_REGISTER_PATH("/users/register", "POST"),
        USERS_LOGIN_PATH("/users/login", "POST"),
        USERS_UPDATE_PATH("/users/update", "PUT"),
        SCORES_INSERT_ONE("/scores/insert_score", "POST");


        private String path;
        private String requestMethod;

        Paths(String path, String requestMethod) {
            this.path = path;
            this.requestMethod = requestMethod;
        }

        public String getPath() {
            return path;
        }

        public String getRequestMethod() {
            return requestMethod;
        }
    }

    private static final String REST_URL = "http://10.0.2.2:8080";
    private static final Integer TIMEOUT = 5000;

    private HTTPManager() {
    }

    /**
     * Creates a custom request using the Paths enum for creating them and passing
     * the adequate map for each operation.
     *
     * @param path The request that you need (declared on the Paths enum).
     * @param map  The data to send to the server. Null if we dont need to send
     *             data.
     * @return The json as String.
     */
    public static String makeRequest(Paths path, String mapAsString) {
        try {
            HttpURLConnection connection = getMountedConnection(path.getPath(), path.getRequestMethod());

            if (mapAsString != null){
                System.out.println(mapAsString);
                sendToServer(mapAsString, connection);
            }

            return receiveFromTheServer(connection);

        } catch (MalformedURLException e) {
        } catch (IOException e) {
            System.out.println(e);
        }

        return "";
    }

    private static HttpURLConnection getMountedConnection(String path, String requestMethod)
            throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(REST_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(TIMEOUT);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(requestMethod);
        return connection;
    }

    private static void sendToServer(String json, HttpURLConnection connection) throws IOException {
        System.out.println(connection);
        PrintStream writer = new PrintStream(connection.getOutputStream());
        writer.println(json);
    }

    private static String receiveFromTheServer(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.readLine();
        System.out.println(response);
        return response;
    }
}
