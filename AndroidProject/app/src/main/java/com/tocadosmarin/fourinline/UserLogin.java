package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserLogin extends AppCompatActivity {
    public static SharedPreferences encryptedPref;
    private Button btLogIn, btSignUp;
    private LinearLayout logIn, signUp;
    private EditText etLoginUser, etLoginPwd, etSignUpUser, etSignUpPwd, etConfirmPwd;
    private CheckBox cbSession;
    private TextView tvSignUp, tvLogIn;
    private ScrollView scLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        etLoginUser = findViewById(R.id.etLoginUser);
        etLoginPwd = findViewById(R.id.etLoginPwd);
        etSignUpUser = findViewById(R.id.etSignUpUser);
        etSignUpPwd = findViewById(R.id.etSignUpPwd);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);

        btLogIn = findViewById(R.id.btLogIn);
        btSignUp = findViewById(R.id.btSignUp);
        cbSession = findViewById(R.id.cbSession);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvLogIn = findViewById(R.id.tvLogIn);
        scLogin = findViewById(R.id.scLogin);

        logIn = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);

        try {
            getEncryptedSharedPreferences();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSignup(false);

        btLogIn.setOnClickListener(new View.OnClickListener() {
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
                    String user = encryptedPref.getString("user", "?");
                    String password = encryptedPref.getString("password", "?");
                    if (!user.equals(etLoginUser.getText().toString()) || !password.equals(etLoginPwd.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "User/Password Incorrect!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(getApplicationContext(), BoardSelection.class);
                        startActivity(i);
                    }
                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLogin(false);
                setSignup(true);
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
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
                    //TODO introducir datos usuario en el servidor
                    Toast.makeText(getApplicationContext(), "todo correcto", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignup(false);
                setLogin(true);
            }
        });
    }

    private void setLogin(boolean state) {
        if (state) {
            logIn.setClickable(true);
            fadeAnimation(logIn, 0f, 1f, View.VISIBLE);
        } else {
            logIn.setClickable(false);
            fadeAnimation(logIn, 1f, 0f, View.INVISIBLE);
        }
    }

    private void setSignup(boolean state) {
        if (state) {
            signUp.setClickable(true);
            fadeAnimation(signUp, 0f, 1f, View.VISIBLE);
        } else {
            signUp.setClickable(false);
            fadeAnimation(signUp, 1f, 0f, View.INVISIBLE);
        }
    }

    private void fadeAnimation(LinearLayout layout, float v1, float v2, int visibility) {
        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(layout, "alpha", v1, v2);
        fadeAnim.setDuration(1000);
        fadeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationEnd(animation);
                scLogin.fullScroll(ScrollView.FOCUS_UP);
                layout.setVisibility(visibility);
            }
        });
        fadeAnim.start();
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