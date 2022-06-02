package com.tocadosmarin.fourinline.managers;

import static com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager.encryptedPref;

import android.content.Context;

public class LoginManager {
    public static final int TOKEN_EXPIRATION_TIME = 1 * 7 * 24 * 60 * 60 * 1000;
    public static final String LOGIN_URL = "http://10.0.2.2:8080/users/login";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String EXPIRATION_TIME = "expiration_time";

    public static Boolean hasServerResponse = false;

    private static LoginManager loginManager;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        if (loginManager == null) {
            loginManager = new LoginManager();
        }
        return loginManager;
    }

    public static boolean checkToken(Context context) {
        //Read user and token, if the token has expired it renews it in the case that the user checked the "Stay Login" option
        //otherwhise, asks to log in
        long expiration_time = encryptedPref.getLong(EXPIRATION_TIME, 0);
        if (isValidToken(expiration_time)) {
            //System.out.println("Token valido");
            return true;
        } else {
            String pwd = encryptedPref.getString(PASSWORD, "");
            if (pwd.equals("")) {
                //System.out.println("Contraseña " + pwd);
                return false;
            } else {
                String user = encryptedPref.getString(USERNAME, "");
                VolleyRequestManager.getUserFromDB(null, context.getApplicationContext(), user, pwd, true);
                //System.out.println("Inicio sesión automático");
                return true;
            }
        }
    }

    public static Boolean isValidToken(long token) {
        return (System.currentTimeMillis() - token) < TOKEN_EXPIRATION_TIME;
    }
}

