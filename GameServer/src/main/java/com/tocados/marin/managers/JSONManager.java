package com.tocados.marin.managers;

import java.util.HashMap;
import java.util.Map;

import com.tocados.marin.managers.MessageManager.Messages;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONManager {

    /**
     * JSON possible structure:
     * 
     * {
     * "column": 7,
     * "result": "winner",
     * "hasOponent": true
     * }
     */

    public static final String COLUMN = "column";
    public static final String RESULT = "result";
    public static final String OPONENT = "oponent";

    private JSONManager() {
    }

    public static Map<String, Object> getMapFromJsonString(String jsonString) {
        JSONArray jsonArray = new JSONArray(jsonString);
        JSONObject jsonObject = jsonArray.toJSONObject(jsonArray);
        return jsonObject.toMap();
    }

    /**
     * Mount custom json passing column.
     * 
     * @param column
     * @return
     */
    public static String mountColumnJson(Integer column) {
        return mountFullJson(column, Messages.NOTHING, false);
    }

    /**
     * Mount custom json passing column and result.
     * 
     * @param column
     * @param result
     * @return
     */
    public static String mountColumnAndResultJson(Integer column, Messages result) {
        return mountFullJson(column, result, false);
    }

    /**
     * Mount custom json passing if oponent is found.
     * 
     * @param hasOponent
     * @return
     */
    public static String mountHasOponentJson(Boolean hasOponent) {
        return mountFullJson(0, Messages.NOTHING, hasOponent);
    }

    private static String mountFullJson(Integer column, Messages result, Boolean hasOponent) {
        Map<String, Object> jsonMap = new HashMap<>();

        if (column > 0)
            jsonMap.put(COLUMN, column);

        if (!result.equals(Messages.NOTHING))
            jsonMap.put(RESULT, result);

        if (hasOponent)
            jsonMap.put(OPONENT, true);

        return new JSONObject(jsonMap).toString();
    }
}
