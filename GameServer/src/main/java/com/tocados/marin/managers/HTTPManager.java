package com.tocados.marin.managers;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HTTPManager {

    public enum Paths {
        USERS_REGISTER_PATH("/users/register"),
        USERS_LOGIN_PATH("/users/login"),
        SCORES_INSERT_ONE("/scores/insert_score");

        private String path;

        Paths(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    private static final String REST_URL = "http://localhost:8080";

    public static void main(String[] args) {
        mountGETRequest(Paths.USERS_LOGIN_PATH.getPath(),
                PropertiesManager.getLoginMap());
    }

    private HTTPManager() {
    }

    /**
     * REQUESTS:
     */
    public static void mountGETRequest(String path, Map<String, Object> map) {
        // StringEntity entity = new StringEntity(map,
        // ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(REST_URL + path);
        // if (body != null)
        // request.set;

        HttpResponse response;
        try {
            response = httpClient.execute(request);

            System.out.println(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mountPOSTRequest(String body) {

    }

    public static void mountPUTRequest(String body) {

    }

    public static void mountDELETERequest(String body) {

    }

    /**
     * RESPONSES:
     */

    public static void getResponse() {

    }
}
