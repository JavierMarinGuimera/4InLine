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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class UserLogin extends AppCompatActivity {
    public static SharedPreferences encryptedPref;
    private static final String URL = "http://10.0.2.2:8080/users/login";
    private LinearLayout logIn, signUp;
    private ScrollView scLogin;
    private EditText etLoginUser, etLoginPwd, etSignUpUser, etSignUpPwd, etConfirmPwd;
    private TextView tvSignUp, tvLogIn;
    private Button btLogIn, btSignUp;
    private CheckBox cbSession;
    private RequestQueue mRequestQueue;
    private JsonObjectRequest jsonObjectRequest;

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
                    getUserFromDB(etLoginUser.getText().toString(), etLoginPwd.getText().toString());
                    //Toast.makeText(getApplicationContext(), "No error", Toast.LENGTH_SHORT).show();
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

    private void getUserFromDB(String user, String pwd) {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", user);
        String encryptedPwd = EncrypterManager.encryptUserPassword(pwd);
        loginMap.put("password", encryptedPwd);

        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, URL, new JSONObject(loginMap), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Response: " + response.toString(), Toast.LENGTH_LONG).show();
                        checkResponse(response.toString(), loginMap, encryptedPwd);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        mRequestQueue.add(jsonObjectRequest);
    }

    private void checkResponse(String res, Map<String, String> loginMap, String encryptedPwd) {
        if (res.equals("false")) {
            Toast.makeText(getApplicationContext(), "User/Password Incorrect!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = MainActivity.pref.edit();
            editor.putString("username", (String) loginMap.get("username"));
            editor.putString("password", encryptedPwd);
            Intent i = new Intent(getApplicationContext(), BoardSelection.class);
            startActivity(i);
        }
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