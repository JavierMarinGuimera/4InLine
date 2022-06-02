package com.tocadosmarin.fourinline.managers;

import static com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager.encryptedPref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tocadosmarin.fourinline.R;
import com.tocadosmarin.fourinline.main.MainActivity;

import org.json.JSONObject;

import java.util.Map;

public class VolleyRequestManager {
    private static RequestQueue mRequestQueue;

    private VolleyRequestManager() {
    }

    public static void init(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
    }

    public static void getUserFromDB(Activity loginActivity, Context context, String user, String pwd, boolean session) {
        String loginData = JSONManager.mountUsernameAndPasswordJson(user, EncrypterManager.encryptUserPassword(pwd));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, LoginManager.LOGIN_URL, JSONManager.getJSONFromString(loginData), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (checkResponse(response.toString(), user, EncrypterManager.encryptUserPassword(pwd), session)) {
                            if (loginActivity != null) {
                                MainActivity.setBtLogin(true);
                                loginActivity.finish();
                            }
                        } else {
                            Toast.makeText(context.getApplicationContext(), context.getText(R.string.user_password_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context.getApplicationContext(), context.getText(R.string.user_password_error), Toast.LENGTH_SHORT).show();
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
    }

    public static void signUpNewUser(Context context, String user, String pwd) {
        String signUpData = JSONManager.mountUsernameAndPasswordJson(user, EncrypterManager.encryptUserPassword(pwd));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, LoginManager.LOGIN_URL, JSONManager.getJSONFromString(signUpData), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
    }

    private static boolean checkResponse(String jsonResponse, String user, String encryptedPwd, boolean session) {
        Map<String, Object> responseMap = JSONManager.getMapFromJsonString(jsonResponse);
        if (responseMap.containsKey(LoginManager.TOKEN)) {
            SharedPreferences.Editor editor = encryptedPref.edit();
            editor.putString(LoginManager.USERNAME, user);
            editor.putString(LoginManager.TOKEN, (String) responseMap.get(LoginManager.TOKEN));
            editor.putLong(LoginManager.EXPIRATION_TIME, (Long) responseMap.get(LoginManager.EXPIRATION_TIME));
            if (session) {
                editor.putString(LoginManager.PASSWORD, encryptedPwd);
            }
            editor.commit();
            return true;
        }
        return false;
    }
}
