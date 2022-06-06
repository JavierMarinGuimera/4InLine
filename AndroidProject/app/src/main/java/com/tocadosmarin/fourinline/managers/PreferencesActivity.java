package com.tocadosmarin.fourinline.managers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.tocadosmarin.fourinline.R;
import com.tocadosmarin.fourinline.main.MainActivity;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new
                        PreferencesFragment()).commit();

        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("music")) {
                    readPreferences();
                    MainActivity.setMusic(MainActivity.isPlaying, getApplicationContext());
                }
            }
        };

        MainActivity.pref.registerOnSharedPreferenceChangeListener(listener);
    }

    public static class PreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    public static void readPreferences() {
        MainActivity.isPlaying = MainActivity.pref.getBoolean("music", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.isPlaying && MainActivity.mp != null) {
            MainActivity.mp.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MainActivity.isPlaying && MainActivity.mp != null) {
            MainActivity.mp.pause();
        }
    }
}