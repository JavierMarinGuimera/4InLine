package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Game extends AppCompatActivity {
    private LinearLayout mainLayout;
    private HashMap<String, LinearLayout> layouts = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mainLayout = findViewById(R.id.mainLayout);
        //Toast.makeText(this, String.valueOf(mainLayout.getGravity()), Toast.LENGTH_SHORT).show();

        //TODO buscar partida con otro jugador que haya seleccionado el mismo tamaño de tablero, una vez encontrada:
        prepareBoard();
        addLayoutListeners();
    }

    private void prepareBoard() {
        String board_size = MainActivity.pref.getString("board_size", "?");
        int size = Integer.valueOf(board_size.substring(0, 1));
        for (int i = 0; i < size; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(setLayoutParams());
            linearLayout.setGravity(81);
            TextView tv = new TextView(this);
            tv.setText("prueba");
            linearLayout.addView(tv);
            mainLayout.addView(linearLayout);
            layouts.put("linearLayout" + i, linearLayout);
        }
    }

    private LinearLayout.LayoutParams setLayoutParams() {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        return param;
    }

    private void addLayoutListeners() {
        for (LinearLayout layout : layouts.values()) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tv = new TextView(view.getContext());
                    tv.setText("TextOnclick");
                    layout.addView(tv);
                }
            });
        }
    }
}