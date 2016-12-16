        /****************************************************
        * Name:  Ted Johansmeyer                            *
        * Project:  Duell Android Project                   *
        * Class:  OPL CMPS 366                              *
        * Date:  December 3rd 2016                          *
        *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainMenuActivity extends AppCompatActivity {

    // UI elements
    private Button newGameBtn;
    private Button loadGameBtn;
    private final Context c = this;

    /**
     Called when the activity is starting
     @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        newGameBtn = (Button) findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        loadGameBtn = (Button) findViewById(R.id.loadGameBtn);
        loadGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFileName();
            }
        });
    }
    /**
     Displays the filename prompt and gets the filename from user
     */
    private void getFileName() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String fileName;
                        Editable value = userInputDialogEditText.getText();
                        fileName = value.toString();
                        fileName = fileName + ".txt";
                        Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                        intent.putExtra("loadGame", fileName);
                        startActivity(intent);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}