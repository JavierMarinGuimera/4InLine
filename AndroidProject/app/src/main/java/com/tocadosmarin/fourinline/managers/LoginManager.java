package com.tocadosmarin.fourinline.managers;

import static com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager.encryptedPref;

import com.tocadosmarin.fourinline.managers.VolleyRequestManager;

public class LoginManager {
    public static final int TOKEN_EXPIRATION_TIME = 1 * 7 * 24 * 60 * 60 * 1000;
    public static final String LOGIN_URL = "http://10.0.2.2:8080/users/login";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String EXPIRATION_TIME = "expiration_time";

    private LoginManager() {
    }

    public static boolean checkToken() {
        //TODO recoger usuario y token, si token esta caducado comprobar
        // si esta la contraseña en las encryptedPref, si esta renovar token
        // automaticamente, sino pedir que inicie sesión
        long expiration_time = encryptedPref.getInt(EXPIRATION_TIME, 0);
        if (isValidToken(expiration_time)) {
            return true;
        } else {
            String pwd = encryptedPref.getString(PASSWORD, "");
            if(pwd.equals("")) {
                return false;
            }else{
                VolleyRequestManager.getUserFromDB(encryptedPref.getString(USERNAME, ""), pwd, true);
                return true;
            }
        }
    }

    public static Boolean isValidToken(long token) {
        return (System.currentTimeMillis() - token) < TOKEN_EXPIRATION_TIME;
    }
}

