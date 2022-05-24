package com.tocadosmarin.fourinline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class BoardSelection extends AppCompatActivity {
    private static boolean hasOpponent = false;
    private Button btPlayGame;
    private RadioGroup rgBoardSize;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_selection);

        btPlayGame = findViewById(R.id.btPlayGame);
        rgBoardSize = findViewById(R.id.rgBoardSize);

        btPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedOption = rgBoardSize.getCheckedRadioButtonId();
                if (selectedOption == -1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                    dialog.setTitle(R.string.dialog_title);
                    dialog.setMessage(R.string.dialog_message);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton(R.string.dialog_button, new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo1, int id) {
                                }
                            });
                    dialog.show();
                } else {
                    radioButton = (RadioButton) findViewById(selectedOption);

                    SharedPreferences.Editor editor = MainActivity.pref.edit();
                    editor.putInt("board_size", Integer.parseInt(radioButton.getText().toString()));
                    editor.commit();

                    //GameRunner.
                    //TODO crear animacion espera

                    /*synchronized (BoardSelection.class) {
                        while (!hasOpponent) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }*/
                    Toast.makeText(getApplicationContext(), getString(R.string.match_found), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Game.class);
                    startActivity(i);
                }
            }
        });
    }
}