package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserLogin extends AppCompatActivity {
    public static SharedPreferences sharedPreferences;
    private Button btLogin;
    private EditText etUser, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);

        try {
            getEncryptedSharedPreferences();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUser.getText().equals("")) {
                    etUser.setError(getText(R.string.user_null));
                } else if (etPassword.getText().equals("")) {
                    etPassword.setError(getText(R.string.password_null));
                } else {
                    Toast.makeText(getApplicationContext(), "usuario/pass correcto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getEncryptedSharedPreferences() throws GeneralSecurityException, IOException {
        MasterKey key = new MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

    }
}