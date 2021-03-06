package com.tocadosmarin.fourinline.main;


import static com.tocadosmarin.fourinline.managers.EncryptedSharedPreferencesManager.encryptedPref;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import com.tocadosmarin.fourinline.loginRegister.UserLoginRegister;
import com.tocadosmarin.fourinline.managers.VolleyRequestManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferences pref;
    public static boolean login;
    public static boolean isPlaying;
    public static MediaPlayer mp;
    private static Button btLogOut;
    private static TextView tvUser;
    private Button btFindGame;
    private Button btExit;
    private Button btPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btFindGame = findViewById(R.id.btFindGame);
        btExit = findViewById(R.id.btExit);
        btPreferences = findViewById(R.id.btPreferences);
        btLogOut = findViewById(R.id.btLogOut);
        tvUser = findViewById(R.id.tvUser);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        PreferencesActivity.readPreferences();
        setMusic(isPlaying, this);

        try {
            EncryptedSharedPreferencesManager.getEncryptedSharedPreferences(this);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        VolleyRequestManager.init(getApplicationContext());
        setBtLogin(LoginManager.checkToken(this));

        btFindGame.setOnClickListener(new View.OnClickListener() {
            //Read the EncryptedSharedPreferences, if the user is login and the token is valid, shows the
            // screen to select board, if not, asks to log in
            @Override
            public void onClick(View view) {
                if (login) {
                    if (LoginManager.checkToken(getApplicationContext())) {
                        Intent i = new Intent(getApplicationContext(), BoardSelection.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
                                        Intent i = new Intent(getApplicationContext(), UserLoginRegister.class);
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
                    Intent i = new Intent(getApplicationContext(), UserLoginRegister.class);
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
            tvUser.setText(encryptedPref.getString(LoginManager.USERNAME, ""));
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

    public static void setMusic(boolean isPlaying, Context context) {
        if (isPlaying) {
            int resID = context.getResources().getIdentifier("menu_song", "raw", context.getPackageName());
            mp = MediaPlayer.create(context, resID);
            mp.start();
            mp.setLooping(true);
        } else {
            if (mp != null) {
                mp.stop();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlaying && mp != null) {
            mp.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isPlaying && mp != null) {
            mp.pause();
        }
    }
}