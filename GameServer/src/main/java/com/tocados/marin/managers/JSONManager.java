package com.tocados.marin.managers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tocados.marin.managers.MessageManager.Messages;

import org.json.JSONObject;

public class JSONManager {

    public static final String USERNAME = "username";
    public static final String COLUMN = "column";
    public static final String OPONENT = "hasOponent";
    public static final String POSITION = "position";
    public static final String RESULT = "result";
    public static final String SCORE = "score";
    public static final String SERVER_CLOSED = "server_closed";

    private JSONManager() {
    }

    /**
     * Management methods:
     */
    public static String getStringFromMap(Map<String, Object> map) {
        return new JSONObject(map).toString();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMapFromJsonString(String jsonString) {
        try {
            return (Map<String, Object>) new ObjectMapper().readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static JSONObject getJSONFromMap(Map<String, Object> jsonMap) {
        return new JSONObject(jsonMap);
    }

    /**
     * -----------------------------------------------------------
     */

    public static String mountUsernameAndColumnJson(String username, Integer column) {
        return JSONManager.getStringFromMap(new HashMap<>() {
            {
                put(USERNAME, username);
                put(COLUMN, column);
            }
        });
    }

    public static String mountHasOponentJson(Integer position) {
        return JSONManager.getStringFromMap(new HashMap<>() {
            {
                put(OPONENT, true);
                put(POSITION, position);
            }
        });
    }

    public static String mountColumnJson(Integer column) {
        return JSONManager.getStringFromMap(new HashMap<>() {
            {
                put(COLUMN, column);
            }
        });
    }

    public static String mountColumnAndResultAndScoreJson(Integer column, Messages result, Integer score) {
        return JSONManager.getStringFromMap(new HashMap<>() {
            {
                put(COLUMN, column);
                put(RESULT, result.getMessage());
                put(SCORE, score);
            }
        });
    }

    public static String mountServerCloseJson() {
        return JSONManager.getStringFromMap(new HashMap<>() {
            {
                put(SERVER_CLOSED, true);
            }
        });
    }
}
