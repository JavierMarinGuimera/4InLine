package com.tocados.marin.managers;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tocados.marin.managers.MessageManager.Messages;

import org.json.JSONObject;

public class JSONManager {

    /**
     * JSON possible structure:
     * 
     * {
     * "column": 7,
     * "result": "winner",
     * "hasOponent": true,
     * "position": 1,
     * "serverClosed": true
     * }
     */

    public static final String USERNAME = "username";
    public static final String COLUMN = "column";
    public static final String RESULT = "result";
    public static final String OPONENT = "hasOponent";
    public static final String POSITION = "position";
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

    /**
     * Mount custom json passing username and columns.
     * 
     * @param column
     * @return
     */
    public static String mountUsernameAndColumnJson(String username, Integer column) {
        return mountFullJson(username, column, Messages.NOTHING, false, 0, false);
    }

    /**
     * Mount custom json passing if oponent is found.
     * 
     * @param hasOponent
     * @return
     */
    public static String mountHasOponentJson(Integer position) {
        return mountFullJson("", 0, Messages.NOTHING, true, position, false);
    }

    /**
     * Mount custom json passing column.
     * 
     * @param column
     * @return
     */
    public static String mountColumnJson(Integer column) {
        return mountFullJson("", column, Messages.NOTHING, false, 0, false);
    }

    /**
     * Mount custom json passing column and result.
     * 
     * @param column
     * @param result
     * @return
     */
    public static String mountColumnAndResultJson(Integer column, Messages result) {
        return mountFullJson("", column, result, false, 0, false);
    }

    /**
     * Mount custom json passing serverClose.
     * 
     * @param serverClose
     * @return
     */
    public static String mountServerCloseJson() {
        return mountFullJson("", 0, Messages.NOTHING, false, 0, true);
    }

    private static String mountFullJson(String username, Integer column, Messages result, Boolean hasOponent,
            Integer position, Boolean serverClose) {
        Map<String, Object> jsonMap = new HashMap<>();

        if (!username.equals(""))
            jsonMap.put(USERNAME, username);

        if (column > -1)
            jsonMap.put(COLUMN, column);

        if (!result.equals(Messages.NOTHING))
            jsonMap.put(RESULT, result.getMessage());

        if (hasOponent)
            jsonMap.put(OPONENT, true);

        if (position > 0)
            jsonMap.put(POSITION, position);

        if (serverClose)
            jsonMap.put(SERVER_CLOSED, true);

        return new JSONObject(jsonMap).toString();
    }
}
