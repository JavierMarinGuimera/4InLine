package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserLogin extends AppCompatActivity {
    public static SharedPreferences encryptedPref;
    private Button btLogIn1, btLogIn2, btSignUp1, btSignUp2, btBack;
    private LinearLayout logIn, buttons, signUp;
    private EditText etLoginUser, etLoginPwd, etSignUpUser, etSignUpPwd, etConfirmPwd;
    private CheckBox cbSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etLoginUser = findViewById(R.id.etLoginUser);
        etLoginPwd = findViewById(R.id.etLoginPwd);
        etSignUpUser = findViewById(R.id.etSignUpUser);
        etSignUpPwd = findViewById(R.id.etSignUpPwd);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);

        btLogIn1 = findViewById(R.id.btLogIn1);
        btLogIn2 = findViewById(R.id.btLogIn2);
        btSignUp1 = findViewById(R.id.btSignUp1);
        btSignUp2 = findViewById(R.id.btSignUp2);
        btBack = findViewById(R.id.btBack);
        cbSession = findViewById(R.id.cbSession);

        logIn = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);
        buttons = findViewById(R.id.buttons);

        try {
            getEncryptedSharedPreferences();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLogin(false);
        setSignup(false);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLogin(false);
                setSignup(false);
                setButtons(true);
            }
        });

        btLogIn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtons(false);
                setLogin(true);
            }
        });

        btLogIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;
                if (etLoginUser.getText().toString().equals("")) {
                    etLoginUser.setError(getText(R.string.user_null));
                    error = true;
                }
                if (etLoginPwd.getText().toString().equals("")) {
                    etLoginPwd.setError(getText(R.string.password_null));
                    error = true;
                }
                if (!error) {
                    boolean session = cbSession.isChecked();
                    //TODO recoger usuario y contraseña server y compararlo con el introducido
                    /*String user = encryptedPref.getString("user", "?");
                    String password = encryptedPref.getString("password", "?")
                    if (!user.equals(etUser.getText().toString()) || !password.equals(etPassword.getText().toString())) {

                    } else {
                        Intent i = new Intent(getApplicationContext(), Game.class);
                        startActivity(i);
                    }*/
                }

            }
        });

        btSignUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtons(false);
                setSignup(true);
            }
        });

        btSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;
                if (etSignUpUser.getText().toString().equals("")) {
                    etSignUpUser.setError(getText(R.string.user_null));
                    error = true;
                }
                if (etSignUpPwd.getText().toString().equals("")) {
                    etSignUpPwd.setError(getText(R.string.password_null));
                    error = true;
                }
                if (etConfirmPwd.getText().toString().equals("")) {
                    etConfirmPwd.setError(getText(R.string.password_null));
                    error = true;
                }
                if (!etConfirmPwd.getText().toString().equals(etSignUpPwd.getText().toString())) {
                    etConfirmPwd.setError(getText(R.string.different_password_error));
                    error = true;
                }
                if (!error) {
                    Toast.makeText(getApplicationContext(), "todo correcto", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setButtons(boolean state) {
        if (state) {
            buttons.setClickable(true);
            buttons.setVisibility(View.VISIBLE);
        } else {
            buttons.setClickable(false);
            buttons.setVisibility(View.INVISIBLE);
        }
    }

    private void setLogin(boolean state) {
        if (state) {
            btBack.setClickable(state);
            btBack.setVisibility(View.VISIBLE);
            logIn.setClickable(state);
            logIn.setVisibility(View.VISIBLE);
        } else {
            btBack.setClickable(state);
            btBack.setVisibility(View.INVISIBLE);
            logIn.setClickable(state);
            logIn.setVisibility(View.INVISIBLE);
        }
    }

    private void setSignup(boolean state) {
        if (state) {
            btBack.setClickable(state);
            btBack.setVisibility(View.VISIBLE);
            signUp.setClickable(state);
            signUp.setVisibility(View.VISIBLE);
        } else {
            btBack.setClickable(state);
            btBack.setVisibility(View.INVISIBLE);
            signUp.setClickable(state);
            signUp.setVisibility(View.INVISIBLE);
        }
    }

    private void getEncryptedSharedPreferences() throws GeneralSecurityException, IOException {
        MasterKey key = new MasterKey.Builder(this, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        encryptedPref = EncryptedSharedPreferences.create(
                getApplicationContext(),
                getString(R.string.encrypted_shared_preferences),
                key,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }
}