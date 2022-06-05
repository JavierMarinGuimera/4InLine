package com.tocadosmarin.fourinline.game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tocadosmarin.fourinline.R;
import com.tocadosmarin.fourinline.main.MainActivity;
import com.tocadosmarin.fourinline.managers.JSONManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game extends AppCompatActivity {
    private static final int NUMBER_IMAGES = 6;
    private static final String SHARED_PREF_ICONS[] = {"icon_player_1", "icon_player_2"};
    private static final String SHARED_PREF_COLORS[] = {"color_player_1", "color_player_2"};

    public static Integer playerPosition;
    public static Integer opponentPosition;

    private static HashMap<Integer, LinearLayout> layouts = new HashMap<>();
    private static List<Drawable> drawableList = new ArrayList<>();
    private static LinearLayout mainLayout;
    private static List<String> colorsList;
    private static boolean canIWrite;
    private TextView tvTurn;
    private int id_player_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mainLayout = findViewById(R.id.mainLayout);
        tvTurn = findViewById(R.id.tvTurn);

        BoardSelection.clientRunner.setGame(this);

        getPlayerIcons();
        getPlayerColors();
        prepareBoard();
        addLayoutListeners();
    }

    private void getPlayerIcons() {
        for (String icon : SHARED_PREF_ICONS) {
            String player_icon = MainActivity.pref.getString(icon, "?");
            id_player_icon = this.getResources().getIdentifier(player_icon, "drawable", this.getPackageName());
            Drawable drawable_player = this.getDrawable(id_player_icon);
            drawableList.add(drawable_player);
        }
    }

    private void getPlayerColors() {
        colorsList = new ArrayList<>();
        for (String color : SHARED_PREF_COLORS) {
            String player_color = MainActivity.pref.getString(color, "?");
            colorsList.add(player_color);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void prepareBoard() {
        if(playerPosition == 0){
            tvTurn.setText(R.string.tv_your_turn);
        }else{
            tvTurn.setText(R.string.tv_opponent_turn);
        }
        int board_size = MainActivity.pref.getInt("board_size", 0);
        for (int i = 0; i < board_size; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
            linearLayout.setGravity(Gravity.BOTTOM);
            linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            mainLayout.addView(linearLayout);
            layouts.put(i, linearLayout);

            View v = new View(this);
            v.setLayoutParams(new ViewGroup.LayoutParams(7, ViewGroup.LayoutParams.WRAP_CONTENT));
            v.setBackgroundColor(R.color.black);

            if (i != board_size - 1) {
                mainLayout.addView(v);
            }
        }
    }

    private void addLayoutListeners() {
        for (Map.Entry<Integer, LinearLayout> layout : layouts.entrySet()) {
            layout.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), canIWrite + "", Toast.LENGTH_SHORT).show();
                    if (canIWrite && layout.getValue().getChildCount() < NUMBER_IMAGES) {
                        synchronized (ClientRunner.class) {
                            ClientRunner.column = layout.getKey();
                            ClientRunner.class.notify();
                        }

                        printImage(layout.getValue(), drawableList.get(playerPosition), playerPosition);

                        canIWrite = false;
                        tvTurn.setText(R.string.tv_opponent_turn);
                    }
                }
            });
        }
    }

    public void setServerResponse(Map<String, Object> response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                printImage(layouts.get((Integer) response.get(JSONManager.COLUMN)), drawableList.get(opponentPosition), opponentPosition);
                if (response.containsKey("result") || response.containsKey("server_closed")) {
                    String infoToPlayer;
                    if (response.containsKey("result")) {
                        infoToPlayer = "Has perido";
                    } else {
                        infoToPlayer = "La partida se ha cerrado inesperadamente";
                    }
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
                    dialog.setTitle("Fin de la partida");
                    dialog.setMessage(infoToPlayer);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton(R.string.dialog_button, new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    dialog.show();
                } else {
                    canIWrite = true;
                    tvTurn.setText(R.string.tv_your_turn);
                }
            }
        });
    }

    private void printImage(LinearLayout layout, Drawable icon_player, int position) {
        int img_height = mainLayout.getHeight() / NUMBER_IMAGES;
        ImageView iv = new ImageView(getApplicationContext());
        iv.setImageDrawable(icon_player);
        iv.setColorFilter(Color.parseColor(colorsList.get(position)), PorterDuff.Mode.SRC_IN);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(img_height, img_height);
        layoutParams.gravity = Gravity.CENTER;
        iv.setLayoutParams(layoutParams);
        layout.addView(iv, 0);
    }

    public static void setPlayerPosition(Integer position) {
        playerPosition = position - 1;
        opponentPosition = (playerPosition == 0 ? 1 : 0);
        canIWrite = playerPosition < opponentPosition;
    }
}