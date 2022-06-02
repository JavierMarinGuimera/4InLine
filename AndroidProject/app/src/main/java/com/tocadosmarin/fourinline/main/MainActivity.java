package com.tocadosmarin.fourinline.main;


import static com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager.encryptedPref;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tocadosmarin.fourinline.game.BoardSelection;
import com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager;
import com.tocadosmarin.fourinline.managers.LoginManager;
import com.tocadosmarin.fourinline.managers.PreferencesActivity;
import com.tocadosmarin.fourinline.R;
import com.tocadosmarin.fourinline.login.UserLogin;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferences pref;
    public static boolean login;
    private static Button btLogOut;
    private static TextView tvUser;
    private Button btFindGame;
    private Button btExit;
    private Button btPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        btFindGame = findViewById(R.id.btFindGame);
        btExit = findViewById(R.id.btExit);
        btPreferences = findViewById(R.id.btPreferences);
        btLogOut = findViewById(R.id.btLogOut);
        tvUser = findViewById(R.id.tvUser);

        try {
            EncryptedSharedPreferencesManager.getEncryptedSharedPreferences(this);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBtLogin(LoginManager.checkToken(this));

        btFindGame.setOnClickListener(new View.OnClickListener() {
            //Read the EncryptedSharedPreferences, if the user is login and the token is valid, shows the
            // screen to select board, if not, asks to log in
            @Override
            public void onClick(View view) {
                if (login) {
                    if (LoginManager.checkToken(getApplicationContext())) {
                        Intent i = new Intent(getApplicationContext(), BoardSelection.class);
                        startActivity(i);
                    } else {
                        EncryptedSharedPreferencesManager.clearUserLoginPreferences();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                        dialog.setTitle(R.string.dialog_title);
                        dialog.setMessage(R.string.dialog_login_message);
                        dialog.setCancelable(false);
                        dialog.setPositiveButton(R.string.dialog_ok_button, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(getApplicationContext(), UserLogin.class);
                                        startActivity(i);
                                    }
                                });
                        dialog.setNegativeButton(R.string.dialog_login_cancel_bt, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        dialog.show();
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(), UserLogin.class);
                    startActivity(i);
                }
            }
        });
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(i);
            }
        });

        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle(R.string.dialog_title);
                dialog.setMessage(R.string.dialog_logout_title);
                dialog.setCancelable(false);
                dialog.setPositiveButton(R.string.btLogOut, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EncryptedSharedPreferencesManager.clearUserLoginPreferences();
                                setBtLogin(false);
                            }
                        });
                dialog.setNegativeButton(R.string.dialog_button, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                dialog.show();
            }
        });
    }

    public static void setBtLogin(boolean state) {
        if (state) {
            tvUser.setText(encryptedPref.getString(LoginManager.USERNAME,""));
            tvUser.setVisibility(View.VISIBLE);
            btLogOut.setClickable(true);
            btLogOut.setVisibility(View.VISIBLE);
            login = true;
        } else {
            tvUser.setVisibility(View.INVISIBLE);
            btLogOut.setClickable(false);
            btLogOut.setVisibility(View.INVISIBLE);
            login = false;
        }
    }
}