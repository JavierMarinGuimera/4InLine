package com.tocadosmarin.fourinline.managers;

import static com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager.encryptedPref;

import android.content.Context;

public class LoginManager {
    //public static final int TOKEN_EXPIRATION_TIME = 1 * 7 * 24 * 60 * 60 * 1000;
    public static final int TOKEN_EXPIRATION_TIME = 5 * 1000;
    private static final String SERVER_URL = "http://192.168.1.41:8080";
    public static final String LOGIN_URL = SERVER_URL + "/users/login";
    public static final String SIGNUP_URL = SERVER_URL + "/users/register";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String EXPIRATION_TIME = "expiration_time";
    public static final String CREATED = "created";
    public static final String ERROR = "error";

    public static boolean checkToken(Context context) {
        //Read user and token, if the token has expired it renews it in the case that the user checked the "Stay Login" option
        //otherwhise, asks to log in
        long expiration_time = encryptedPref.getLong(EXPIRATION_TIME, -1);
        if (isValidToken(expiration_time)) {
            return true;
        } else {
            String pwd = encryptedPref.getString(PASSWORD, "");
            if (pwd.equals("")) {
                return false;
            } else {
                String user = encryptedPref.getString(USERNAME, "");
                VolleyRequestManager.makeRequest(LoginManager.LOGIN_URL, null, context.getApplicationContext(), user, pwd, true);
                return true;
            }
        }
    }

    public static Boolean isValidToken(long token) {
        return (System.currentTimeMillis() - token) < TOKEN_EXPIRATION_TIME;
    }
}

