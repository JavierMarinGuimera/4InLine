package com.tocadosmarin.fourinline.loginRegister;


import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tocadosmarin.fourinline.R;
import com.tocadosmarin.fourinline.managers.LoginManager;
import com.tocadosmarin.fourinline.managers.VolleyRequestManager;


public class UserLoginRegister extends AppCompatActivity {
    private static LinearLayout logIn;
    private static LinearLayout signUp;
    private static ScrollView scLogin;
    private EditText etLoginUser, etLoginPwd, etSignUpUser, etSignUpPwd, etConfirmPwd;
    private TextView tvSignUp, tvLogIn;
    private Button btLogIn, btSignUp;
    private CheckBox cbSession;
    private Activity father;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        this.father = this;

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

        setSignup(false);
        VolleyRequestManager.init(getApplicationContext());

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
                    VolleyRequestManager.makeRequest(LoginManager.LOGIN_URL, father, getApplicationContext(), etLoginUser.getText().toString(), etLoginPwd.getText().toString(), cbSession.isChecked());
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
                if (!etSignUpPwd.getText().toString().equals(etConfirmPwd.getText().toString())) {
                    etConfirmPwd.setError(getText(R.string.different_password_error));
                    error = true;
                }
                if (!error) {
                    VolleyRequestManager.makeRequest(LoginManager.SIGNUP_URL, null, getApplicationContext(), etSignUpUser.getText().toString(), etSignUpPwd.getText().toString(), false);
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

    public static void setLogin(boolean state) {
        if (state) {
            logIn.setClickable(true);
            fadeAnimation(logIn, 0f, 1f, View.VISIBLE);
        } else {
            logIn.setClickable(false);
            fadeAnimation(logIn, 1f, 0f, View.INVISIBLE);
        }
    }

    public static void setSignup(boolean state) {
        if (state) {
            signUp.setClickable(true);
            fadeAnimation(signUp, 0f, 1f, View.VISIBLE);
        } else {
            signUp.setClickable(false);
            fadeAnimation(signUp, 1f, 0f, View.INVISIBLE);
        }
    }

    private static void fadeAnimation(LinearLayout layout, float v1, float v2, int visibility) {
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
}