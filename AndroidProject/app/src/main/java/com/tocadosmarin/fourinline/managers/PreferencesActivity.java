package com.tocadosmarin.fourinline.managers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new
                        PreferencesFragment()).commit();
    }
}