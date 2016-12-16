/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {

    // Game Variables
    private int startRow, startCol, endRow, endCol, direction;
    private Boolean first = true;
    private Game game = new Game();

    // UI Elements
    private static final int MAX_COL = 9;
    private static final int MAX_ROW = 8;
    private final Context c = this;
    private Button continueBtn;

    /**
     Determines if the user needs to input a direction
     @return Return true if input is needed for a direction
     */
    private Boolean needDirection(){
        return (startRow != endRow && startCol != endCol);
    }
    private void clearSelections(){
        // Clear Selections
        first = true;
        startRow = startCol = endRow = endCol = direction = 0;
        displayBoard();
        continueBtn.setEnabled(false);
    }
    /**
     Called when the activity is starting
     @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_board);
        // Get intent for load game
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            if(isStoragePermissionGranted()){
                String fileName = bundle.getString("loadGame");
                game.setContext(this);
                game.loadGame(fileName);
                startGame();
            } else {
                Toast.makeText(getApplicationContext(),
                        "File Write Permission not granted",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            firstPlayer();
        }
    }

    /**
     Primary display and verification function. Displays board and verifies input
     */
    public void displayBoard(){
        final Human tempHuman = game.getHuman();
        final BoardModel board = game.getBoard();
        final DieModel[][] tempBoard = board.getGameBoard();
        // Create continue button
        continueBtn = (Button) findViewById(R.id.continueBtn);
        if(game.getNextPlayer().equals("Human")){
            continueBtn.setEnabled(false);
        } else {
            continueBtn.setEnabled(true);
        }
        // Find TextView from XML containing the Row indexes
        TextView rowTv = (TextView)findViewById(R.id.row_nums);
        rowTv.setText("");
        // Find TableLayout for the board from XML
        TableLayout table = (TableLayout) findViewById(R.id.view_root);
        // Clear last table
        table.removeAllViews();
        // Set the board's layout parameters
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1f;
        // Create the board
        for(int y = MAX_ROW - 1 ; y >= 0; y--) {
            final int row = y;
            int displayRow = row;
            displayRow++;
            rowTv.append(Integer.toString(displayRow) +"\n\n");
            TableRow r = new TableRow(this);
            TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            table.addView(r);
            for (int x = 0; x < MAX_COL; x++) {
                final int col = x;
                Button b = new Button(this);

                // Output the grid with alternating colors
                if(x % 2 == y % 2) {
                    b.setBackgroundColor(0xFFcbbeb5);
                } else {
                    b.setBackgroundColor(0xfff5f5dc);
                }
                // Output die pieces on the board
                if(!tempBoard[y][x].isEmpty()) {
                    String tempstr1 = tempBoard[y][x].displayDie();
                    b.setText(tempstr1);
                }
                if(tempBoard[row][col].getPlayer().equals("C")){
                    // Set Computer Text Color to Red
                    b.setTextColor(Color.parseColor("#ff0000"));
                }

                b.setLayoutParams(buttonParams);
                // If its a human turn enable the buttons and verification
                if(game.getNextPlayer().equals("Human")){
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Input Validation
                            if(first && tempBoard[row][col].getPlayer().equals("H")){
                                first = false;
                                startRow = row;
                                startCol = col;
                                v.setBackgroundColor(0xffFFFF8D);
                                // If its the first selection and it's not on a human die
                            }else if(first && !tempBoard[row][col].getPlayer().equals("H")){
                                Toast.makeText(getApplicationContext(),
                                        "Please select one of your own die",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // Verification for second input
                            else if(!first && tempHuman.checkNumSpaces(startRow, startCol, row, col)){
                                endRow = row;
                                endCol = col;
                                // Set Glow
                                v.setBackgroundColor(0xffFFFF8D);
                                // Check Paths for prompt
                                Boolean forward = tempHuman.getPath(startRow, startCol, endRow, endCol, 1, false);
                                Boolean lateral =  tempHuman.getPath(startRow, startCol, endRow, endCol, 2, false);

                                // If a direction is needed display prompt
                                if(needDirection()){
                                    // If forward is clear and lateral is not; set for forward
                                    if(forward && !lateral){
                                        direction = 1;
                                        // On valid input show continue prompt
                                        executeHumanMovePrompt();
                                    } else if(!forward && lateral){
                                        direction = 2;
                                        // On valid input show continue prompt
                                        executeHumanMovePrompt();
                                    } else if (forward && lateral){
                                        // Direction dialog prompt
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Pick a direction");
                                        // Set up the buttons
                                        builder.setPositiveButton("Lateral", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                direction = 2;
                                                // On valid input show continue prompt
                                                executeHumanMovePrompt();


                                            }
                                        });
                                        builder.setNegativeButton("Forward", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                direction = 1;
                                                // On valid input show continue prompt
                                                executeHumanMovePrompt();
                                            }
                                        });
                                        builder.show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "No clear paths try again",
                                                Toast.LENGTH_SHORT).show();
                                        clearSelections();
                                    }
                                } else {
                                    if(tempHuman.getPath(startRow, startCol, endRow, endCol, 0, false)){
                                        // If strictly a forward or lateral clear move set direction to 0
                                        direction = 0;
                                        // On valid input show continue prompt
                                        executeHumanMovePrompt();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "No clear paths try again",
                                                Toast.LENGTH_SHORT).show();
                                        clearSelections();
                                    }
                                }
                            } else {
                                // Second selection is not proper number of spaces away
                                Toast.makeText(getApplicationContext(),
                                        "End space is out of range of movement",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                r.addView(b);
            }
        }
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computerMove();
            }
        });
    }
    /**
     Starts the game and sets the human and computer win textviews
     */
    public void startGame(){
        TournamentModel tempTournament = game.getTournament();
        // Human and Computer Wins Text Views
        TextView humanTv = (TextView)findViewById(R.id.human_wins);
        String humanWins = "Human Wins: "+ Integer.toString(tempTournament.getHumanWins());
        humanTv.setText(humanWins);
        TextView computerTv = (TextView)findViewById(R.id.computer_wins);
        String computerWins = "Computer Wins: " + Integer.toString(tempTournament.getComputerWins());
        computerTv.setText(computerWins);

        displayBoard();
    }
    /**
     Execute a single Human Move
     */
    public void humanMove(){
        BoardModel tempBoard = game.getBoard();
        Human tempHuman = game.getHuman();
        TournamentModel tempTournament = game.getTournament();
        // Human Move
        game.setNextPlayer("Computer");
        tempHuman.setCoordinates(startRow, startCol, endRow, endCol, direction);
        tempHuman.setBoard(tempBoard);
        game.setBoard(tempHuman.play());
        clearSelections();
        game.setBoard(tempHuman.getBoard());
        // Check win state for Human
        if(tempHuman.checkHumanWin()){
            int humanWins = tempTournament.getHumanWins();
            humanWins++;
            tempTournament.setHumanWins(humanWins);
            winner("Human");
        }
        displayBoard();
    }
    /**
     Execute a single Computer Move
     */
    public void computerMove(){
        BoardModel tempBoard = game.getBoard();
        Computer tempComputer = game.getComputer();
        TournamentModel tempTournament = game.getTournament();
        // Computer Move
        tempComputer.setBoard(tempBoard);
        game.setBoard(tempComputer.play());
        displayComputerMove(false);
        // Check win state for Computer
        if(tempComputer.checkComputerWin()){
            int compWins = tempTournament.getComputerWins();
            compWins++;
            tempTournament.setComputerWins(compWins);
            winner("Computer");
        }
        game.setNextPlayer("Human");
        Toast.makeText(getApplicationContext(),
                "It's you're turn!",
                Toast.LENGTH_SHORT).show();
        displayBoard();
    }
    /**
     Checks to see if Storage Permission is granted on Android M or greater
     @return true if permission is granted
     */
    public boolean isStoragePermissionGranted() {
        // help: Chandresh Kachariya Stack Overflow
        // http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission","Permission is granted");
                return true;
            } else {

                Log.v("Permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission","Permission is granted");
            return true;
        }
    }
    /**
     Android function callback for the result from requesting permissions.
     @param requestCode int, The request code passed in requestPermissions(android.app.Activity, String[], int)
     @param permissions  String [], The requested permissions
     @param grantResults int [], The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("Permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
    /**
     Initialize the contents of the Activity's standard options menu.
     @param menu The options menu in which you place your items.
     @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }
    /**
     This hook is called whenever an item in your options menu is selected.
     @param item The menu item that was selected.
     @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save_game:
                getFileName();
                return true;
            case R.id.help:
                showHelp();
                return true;
            case R.id.clear:
                clearSelections();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     Shows the help dialog
     */
    public void showHelp(){
        Human tempHuman = game.getHuman();
        tempHuman.help();
        String line1 = tempHuman.getLine1();
        String line2 = tempHuman.getLine2();
        String line3 = tempHuman.getLine3();
        // Inflate the layout and create dialog
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.die_roll_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);
        // Set the text views with the roll numbers
        final TextView humanTv = (TextView)mView.findViewById(R.id.human_prompt);
        humanTv.setText(line1);
        final TextView computerTv = (TextView)mView.findViewById(R.id.computer_prompt);
        computerTv.setText(line2);
        final TextView prompt = (TextView)mView.findViewById(R.id.msg_prompt);
        prompt.setText(line3);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                dialogBox.cancel();
                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
    /**
     Displays the filename prompt and gets the filename from user
     */
    public void getFileName(){
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
                        // Append .txt to filename
                        fileName = fileName + ".txt";
                        if(isStoragePermissionGranted()){
                            game.saveGame(fileName);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "File Write Permission not granted",
                                    Toast.LENGTH_SHORT).show();
                            dialogBox.cancel();
                        }
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
    /**
     Displays the prompt for the winner of the game and tournament
     @param player String the winning player
     */
    public void winner(String player){
        final TournamentModel tempTournament = game.getTournament();
        int humanWins = tempTournament.getHumanWins();
        int computerWins = tempTournament.getComputerWins();
        // Inflate the layout and create dialog
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.die_roll_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);
        // Set the text views
        final TextView winnerTv = (TextView)mView.findViewById(R.id.game_winner);
        if(player.equals("Computer")){
            winnerTv.setText(R.string.lost_game);
        } else {
            winnerTv.setText(R.string.won_game);
        }
        // Set the winner text
        final TextView humanTv = (TextView)mView.findViewById(R.id.human_prompt);
        humanTv.setText(R.string.human_wins);
        humanTv.append(" ");
        humanTv.append(Integer.toString(humanWins));
        humanTv.append(" games");
        final TextView computerTv = (TextView)mView.findViewById(R.id.computer_prompt);
        computerTv.setText(R.string.computer_wins);
        computerTv.append(" ");
        computerTv.append(Integer.toString(computerWins));
        computerTv.append(" games");
        final TextView prompt = (TextView)mView.findViewById(R.id.msg_prompt);
        if(humanWins > computerWins){
            prompt.setText(R.string.you_win);
        } else if(computerWins > humanWins){
            prompt.setText(R.string.you_lose);
        } else {
            prompt.setText(R.string.tie);
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Play a new game?
                        // Create and set new board and roll the die to start
                        BoardModel board = new BoardModel();
                        game = new Game();
                        game.setTournament(tempTournament);
                        displayComputerMove(true);
                        firstPlayer();
                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // On exit game display winner of tournament
                        int humanWins = tempTournament.getHumanWins();
                        int computerWins = tempTournament.getComputerWins();
                        // Inflate the layout and create dialog
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                        View mView = layoutInflaterAndroid.inflate(R.layout.die_roll_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                        alertDialogBuilderUserInput.setView(mView);
                        final TextView winTv = (TextView)mView.findViewById(R.id.human_prompt);
                        if(humanWins > computerWins){
                            winTv.setText(R.string.tournament_win);
                        } else if(computerWins > humanWins){
                            winTv.setText(R.string.tournament_lose);
                        } else {
                            winTv.setText(R.string.tournament_tie);
                        }
                        alertDialogBuilderUserInput
                                .setCancelable(false)
                                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        // Return to Home Page
                                        Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                                        startActivity(intent);
                                    }
                                });
                        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
    /**
     Display the dice rolls and determine first player
     */
    public void firstPlayer(){
        final String player;
        final Random rand = new Random();
        int humanRoll, computerRoll;

        // Inflate the layout and create dialog
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.die_roll_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);
        // Loop until one player rolls a higher number
        do{
            humanRoll = rand.nextInt(6) + 1;
            computerRoll = rand.nextInt(6) + 1;

        } while(humanRoll == computerRoll);

        // Set the text views with the roll numbers
        final TextView humanTv = (TextView)mView.findViewById(R.id.human_prompt);
        humanTv.setText(R.string.human_roll);
        humanTv.append(" ");
        humanTv.append(Integer.toString(humanRoll));
        final TextView computerTv = (TextView)mView.findViewById(R.id.computer_prompt);
        computerTv.setText(R.string.computer_roll);
        computerTv.append(" ");
        computerTv.append(Integer.toString(computerRoll));
        final TextView prompt = (TextView)mView.findViewById(R.id.msg_prompt);
        // Set the message of who goes first
        if(computerRoll > humanRoll){
            prompt.setText(R.string.computer_first);
            player = "Computer";
        } else {
            prompt.setText(R.string.human_first);
            player = "Human";
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        game.setNextPlayer(player);
                        startGame();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
    /**
     Display prompt to confirm human move
     */
    public void executeHumanMovePrompt(){
        // Inflate the layout and create dialog
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.die_roll_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);
        // Set the text views with the roll numbers
        final TextView humanTv = (TextView)mView.findViewById(R.id.human_prompt);
        humanTv.setText(R.string.sure);
        final TextView computerTv = (TextView)mView.findViewById(R.id.computer_prompt);
        final TextView prompt = (TextView)mView.findViewById(R.id.msg_prompt);
        // Set positve button to Execute Human Move
        // Negative button to clear selections
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        humanMove();
                    }
                })
                .setNegativeButton(R.string.clear, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        clearSelections();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
    /**
     Grabs the strings containing the computer's move
     @param clear Boolean true to wipe the TextView, false to print the messages
     */
    public void displayComputerMove(Boolean clear){
        Computer tempComputer = game.getComputer();
        TextView tv = (TextView) findViewById(R.id.message_center);
        if(clear){
            tv.setText("");
        } else {
            String message1 = tempComputer.getMessage1();
            String message2 = tempComputer.getMessage2();
            tv.append(message1 + "\n");
            tv.append(message2 + "\n\n");
        }
    }

}
