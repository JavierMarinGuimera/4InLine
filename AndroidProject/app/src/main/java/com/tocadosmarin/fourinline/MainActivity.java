package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static SharedPreferences pref;
    private Button btFindGame, btExit, btPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        btFindGame = findViewById(R.id.btFindGame);
        btExit = findViewById(R.id.btExit);
        btPreferences = findViewById(R.id.btPreferences);

        btFindGame.setOnClickListener(new View.OnClickListener() {
            //TODO leer EncryptedSharedPreferences, si el usuario esta login mostrar pantalla tablero, sino mostrar actividad logine
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(i);
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


    }
}