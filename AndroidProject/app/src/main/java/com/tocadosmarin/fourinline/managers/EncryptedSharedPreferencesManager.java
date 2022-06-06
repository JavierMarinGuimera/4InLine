package com.tocadosmarin.fourinline.managers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.tocadosmarin.fourinline.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptedSharedPreferencesManager extends AppCompatActivity {
    public static SharedPreferences encryptedPref;

    private EncryptedSharedPreferencesManager() {
    }

    public static void getEncryptedSharedPreferences(Context context) throws GeneralSecurityException, IOException {
        MasterKey key = new MasterKey.Builder(context.getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        encryptedPref = EncryptedSharedPreferences.create(
                context.getApplicationContext(),
                context.getString(R.string.encrypted_shared_preferences),
                key,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void clearUserLoginPreferences() {
        SharedPreferences.Editor editor = encryptedPref.edit();
        editor.clear();
        editor.commit();
    }
}
