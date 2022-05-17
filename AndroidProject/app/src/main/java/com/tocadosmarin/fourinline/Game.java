package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        TextView tv2 = findViewById(R.id.tv2);
        tv2.setText(UserLogin.encryptedPref.getString("user",null));
    }
}