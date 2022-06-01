package com.tocadosmarin.fourinline.managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

public class JSONManager {

    /**
     * JSON possible structure:
     * <p>
     * {
     * "column": 7,
     * "result": "winner",
     * "hasOponent": true,
     * "position": 1
     * }
     */

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String COLUMN = "column";

    private JSONManager() {
    }

    public static String getStringFromMap(Map<String, Object> map) {
        return new JSONObject(map).toString();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonString(String jsonString) {
        try {
            return (Map<String, Object>) new ObjectMapper().readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getJSONFromString(String jsonString) {
        return new JSONObject(getMapFromJsonString(jsonString));
    }

    public static JSONObject getJSONFromMap(Map<String, Object> jsonMap) {
        return new JSONObject(jsonMap);
    }

    /**
     * Mount custom json passing username and password.
     *
     * @param username
     * @param password
     * @return
     */
    public static String mountUsernameAndPasswordJson(String username, String password) {
        return mountFullJson(username, password, 0);
    }

    /**
     * Mount custom json passing username and columns.
     *
     * @param column
     * @return
     */
    public static String mountUsernameAndColumnJson(String username, Integer column) {
        return mountFullJson(username, "", column);
    }

    /**
     * Mount custom json passing column.
     *
     * @param column
     * @return
     */
    public static String mountColumnJson(Integer column) {
        return mountFullJson("", "", column);
    }

    private static String mountFullJson(String username, String password, Integer column) {
        Map<String, Object> jsonMap = new HashMap<>();

        if (!username.equals(""))
            jsonMap.put(USERNAME, username);

        if (!password.equals(""))
            jsonMap.put(PASSWORD, password);

        if (column > 0)
            jsonMap.put(COLUMN, column);

        return new JSONObject(jsonMap).toString();
    }
}
