package com.tocadosmarin.fourinline.managers;

import static com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager.encryptedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

public class VolleyRequestManager {
    private static RequestQueue mRequestQueue;
    private static boolean hasLoginOnServer;

    private VolleyRequestManager() {
    }

    public static void init(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static boolean getUserFromDB(String user, String pwd, boolean session) {
        String loginData = JSONManager.mountUsernameAndPasswordJson(user, EncrypterManager.encryptUserPassword(pwd));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, LoginManager.LOGIN_URL, JSONManager.getJSONFromString(loginData), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hasLoginOnServer = checkResponse(response.toString(), user, EncrypterManager.encryptUserPassword(pwd), session);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hasLoginOnServer = false;
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
        return hasLoginOnServer;
    }

    private static boolean checkResponse(String jsonResponse, String user, String encryptedPwd, boolean session) {
        Map<String, Object> jsonMap = JSONManager.getMapFromJsonString(jsonResponse);
        if (((Map<String, Object>) jsonMap).containsKey(LoginManager.TOKEN)) {
            SharedPreferences.Editor editor = encryptedPref.edit();
            editor.putString(LoginManager.USERNAME, user);
            editor.putString(LoginManager.TOKEN, (String) jsonMap.get(LoginManager.TOKEN));
            editor.putLong(LoginManager.EXPIRATION_TIME, (Long) jsonMap.get(LoginManager.EXPIRATION_TIME));
            if (session) {
                editor.putString(LoginManager.PASSWORD, encryptedPwd);
            }
            editor.commit();
            return true;
        }
        return false;
    }

    /*public static boolean signUpNewUser(Context context, String user, String pwd) {
        String signUpData = JSONManager.mountUsernameAndPasswordJson(user, EncrypterManager.encryptUserPassword(pwd));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, LoginManager.LOGIN_URL, JSONManager.getJSONFromString(signUpData), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         =false;
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
        return res;
    }*/
}
