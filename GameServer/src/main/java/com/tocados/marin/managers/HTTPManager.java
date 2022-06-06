package com.tocados.marin.managers;

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
        USERS_REGISTER_PATH("/users/register", "POST"),
        USERS_LOGIN_PATH("/users/login", "POST"),
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

    private static final String REST_URL = "http://localhost:8081";
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
    public static String makeRequest(Paths path, Map<String, Object> map) {
        try {
            HttpURLConnection connection = getMountedConnection(path.getPath(), path.getRequestMethod());

            if (map != null) {
                sendToServer(JSONManager.getStringFromMap(map), connection);
            }

            return receiveFromTheServer(connection);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        PrintStream writer = new PrintStream(connection.getOutputStream());
        writer.println(json.toString());
    }

    private static String receiveFromTheServer(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return reader.readLine();
    }
}