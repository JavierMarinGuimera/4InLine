package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game extends AppCompatActivity {
    private static final int NUMBER_IMAGES = 6;
    private static final String ICONS[] = {"icon_player_1", "icon_player_2"};
    private static final String COLORS[] = {"color_player_1", "color_player_2"};
    private static GameRunner gameRunner;
    private HashMap<Integer, LinearLayout> layouts = new HashMap<>();
    private List<String> colorsList;
    private LinearLayout mainLayout;
    private Drawable icon_player_1, icon_player_2;
    private int id_player_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mainLayout = findViewById(R.id.mainLayout);


        //TODO buscar partida con otro jugador que haya seleccionado el mismo tamaño de tablero, una vez encontrada:
        getPlayerIcons();
        getPlayerColors();
        prepareBoard();
        addLayoutListeners();
    }

    private void getPlayerIcons() {
        List<Drawable> drawableList = new ArrayList<>();
        for (String icon : ICONS) {
            String player_icon = MainActivity.pref.getString(icon, "?");
            id_player_icon = this.getResources().getIdentifier(player_icon, "drawable", this.getPackageName());
            Drawable drawable_player = this.getDrawable(id_player_icon);
            drawableList.add(drawable_player);
        }
        icon_player_1 = drawableList.get(0);
        icon_player_2 = drawableList.get(1);
    }

    private void getPlayerColors() {
        colorsList = new ArrayList<>();
        for (String color : COLORS) {
            String player_color = MainActivity.pref.getString(color, "?");
            colorsList.add(player_color);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void prepareBoard() {
        int board_size = MainActivity.pref.getInt("board_size", 0);
        for (int i = 0; i < board_size; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            linearLayout.setGravity(Gravity.BOTTOM);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            mainLayout.addView(linearLayout);
            layouts.put(i, linearLayout);

            View v = new View(this);
            v.setLayoutParams(new ViewGroup.LayoutParams(5, ViewGroup.LayoutParams.WRAP_CONTENT));
            v.setBackgroundColor(R.color.black);

            if (i != board_size - 1) {
                mainLayout.addView(v);
            }
        }
    }

    private void addLayoutListeners() {
        for (LinearLayout layout : layouts.values()) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO mandar datos al servidor
                    if (layout.getChildCount() < NUMBER_IMAGES){
                        printImage(layout);
                    }
                }
            });
        }
    }

    private void printImage(LinearLayout layout) {
        int img_height = mainLayout.getHeight() / NUMBER_IMAGES;
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageDrawable(icon_player_2);
        iv.setColorFilter(Color.parseColor(colorsList.get(1)), PorterDuff.Mode.SRC_IN);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(img_height, img_height);
        layoutParams.gravity = Gravity.CENTER;
        iv.setLayoutParams(layoutParams);
        layout.addView(iv);
    }
}