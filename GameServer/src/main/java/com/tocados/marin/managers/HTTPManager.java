package com.tocados.marin.managers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class HTTPManager {
    private static final String REST_URL = "http://localhost:8080";

    public static void main(String[] args) {
        mountGETRequest("/users", JSONManager.getJSONFromMap(PropertiesManager.getLoginMap()).toString());
    }

    private HTTPManager() {
    }

    /**
     * REQUESTS:
     */
    public static void mountGETRequest(String path, String body) {
        StringEntity entity = new StringEntity(body,
                ContentType.APPLICATION_JSON);

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
