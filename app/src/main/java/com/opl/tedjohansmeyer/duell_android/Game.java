/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

/**
 * Created by Ted Johansmeyer on 11/5/2016.
 */

public class Game extends Application{
    // Class constants
    private static final int MAX_COL = 9;
    private static final int MAX_ROW = 8;
    // Class variables
    private BoardModel board = new BoardModel();
    private Human human = new Human();
    private Computer computer = new Computer();
    private TournamentModel tournament = new TournamentModel();
    private Context context;
    private String nextPlayer = "";

    // Getters
    final public String getNextPlayer(){return nextPlayer;}
    final public BoardModel getBoard(){return board;}
    final public Human getHuman(){return human;}
    final public Computer getComputer(){return computer;}
    final public TournamentModel getTournament(){return tournament;}
    // Setters
    public void setBoard(BoardModel newBoard){
        board = newBoard;
    }
    public void setTournament(TournamentModel newTournament){tournament = newTournament;}
    public void setContext(Context newContext){context = newContext;}
    public void setNextPlayer(String newNextPlayer){nextPlayer = newNextPlayer;}

    /**
     Saves the game to a text file on the external storage
     @param filename String, filename of .txt game save
     */
    public void saveGame(String filename){
        TournamentModel tempTournament = getTournament();
        BoardModel tempBoardObj = getBoard();
        // Get the game board
        DieModel[][] tempBoard = tempBoardObj.getGameBoard();
        // Open or create the Duell Directory
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Duell");
        dir.mkdirs();
        // Create the file
        File file = new File(dir, filename);
        // Write to file
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("Board:");
            for (int i = 7; i >= 0; i--) {
                for (int j = 0; j <= 8; j++) {
                    if (tempBoard[i][j].isEmpty()) {
                        pw.printf(" 0  ");
                    } else {
                        pw.write(tempBoard[i][j].displayDie());
                        pw.printf(" ");
                    }
                }
                pw.write("\n");
            }
            pw.write("\n");
            pw.write("Computer Wins: ");
            pw.write(Integer.toString(tempTournament.getComputerWins()));
            pw.write("\n\n");
            pw.write("Human Wins: ");
            pw.write(Integer.toString(tempTournament.getHumanWins()));
            pw.write("\n\n");
            pw.write("Next Player: ");
            pw.write(getNextPlayer());
            pw.write("\n");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("writeError", "File not found. ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     Loads the game from a text file on external storage
     @param filename String, filename of .txt game save
     */
    public void loadGame(String filename){
        TournamentModel tempTournament = new TournamentModel();
        BoardModel boardObj = new BoardModel();
        DieModel[][] tempBoard = new DieModel[MAX_ROW][MAX_COL];
        String[][] stringBoard = new String[MAX_ROW][MAX_COL];
        int computerWins, humanWins, topNum, rightNum;
        int iter = 0;
        String temp, player;
        String nextPlayer = "";
        // File Input
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File (sdCard.getAbsolutePath() + "/Duell");
        File file = new File(dir, filename);
        if(file.exists()){
            try{
                FileInputStream fstream = new FileInputStream(file);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                int fileRow = 0;
                int row = 0;
                while ((line = br.readLine()) != null)   {
                    // Trim and split the line delimited by whitespace
                    String[] tokens = line.trim().split("\\s+");
                    // Fill the string board with lines
                    if(fileRow > 0 && fileRow <= MAX_ROW){
                        for(int col = 0; col < tokens.length; col++){
                            stringBoard[row][col] = tokens[col];
                        }
                        row++;
                    }
                    fileRow++;
                    // Parse the number of wins and next player
                    // and set the variables in tournament class
                    if(tokens[0].equals("Computer")){
                        computerWins = Integer.parseInt(tokens[2]);
                        tempTournament.setComputerWins(computerWins);
                    }
                    if(tokens[0].equals("Human")){
                        humanWins = Integer.parseInt(tokens[2]);
                        tempTournament.setHumanWins(humanWins);
                    }
                    if(tokens[0].equals("Next")){
                        nextPlayer = tokens[2];
                    }
                }
                in.close();
            }catch (Exception e){
                e.printStackTrace();
                Log.i("readError", "Error reading File ");
            }
            // Fill tempboard with new Die Objects
            for(int row = 0; row < MAX_ROW; row++){
                for(int col = 0; col < MAX_COL; col++){
                    tempBoard[row][col] = new DieModel();
                }
            }
            for(int i = 7; i >= 0; i--){
                for(int j = 0; j < stringBoard[i].length; j++){
                    temp = stringBoard[i][j];
                    if(!temp.equals("0")){
                        // Grab the player from the first character of the string
                        player = temp.substring(0, 1);
                        // Grab the top number from the second character
                        topNum = Integer.parseInt(temp.substring(1, 2));
                        // Grab the right number from the third character
                        rightNum = Integer.parseInt(temp.substring(2, 3));
                        // Run die switch function to create the Die to fill the board
                        tempBoard[iter][j] = boardObj.dieSwitch(topNum, rightNum, player);
                    }
                }
                iter++;
            }
            boardObj.setGameboard(tempBoard);
            setTournament(tempTournament);
            setBoard(boardObj);
            human.setBoard(boardObj);
            setNextPlayer(nextPlayer);
        } else {
            Intent intent = new Intent(context, MainMenuActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }




}
