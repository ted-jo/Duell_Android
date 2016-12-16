package com.opl.tedjohansmeyer.duell_android;

import java.util.ArrayList;
import java.util.List;
/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
/**
 * Created by Ted Johansmeyer on 11/5/2016.
 */

public class BoardModel {
    // Constants
    private static final int MAX_COL = 9;
    private static final int MAX_ROW = 8;
    // Class Variable
    private DieModel[][] gameboard;

    // Constructor
    BoardModel(){
        DieModel[][] tempboard = new DieModel[MAX_ROW][MAX_COL];

        // Fill tempboard with new Die Objects
        for(int row = 0; row < MAX_ROW; row++){
            for(int col = 0; col < MAX_COL; col++){
                tempboard[row][col] = new DieModel();
            }
        }
        // Computer Starting Positions
        tempboard[7][0] = createStartingDie(5, 6, "C");
        tempboard[7][1] = createStartingDie(1, 5, "C");
        tempboard[7][2] = createStartingDie(2, 1, "C");
        tempboard[7][3] = createStartingDie(6, 2, "C");
        tempboard[7][4] = createStartingDie(1, 1, "C");
        tempboard[7][5] = createStartingDie(6, 2, "C");
        tempboard[7][6] = createStartingDie(2, 1, "C");
        tempboard[7][7] = createStartingDie(1, 5, "C");
        tempboard[7][8] = createStartingDie(5, 6, "C");


        // Human Starting Positions
        tempboard[0][0] = createStartingDie(5, 6, "H");
        tempboard[0][1] = createStartingDie(1, 5, "H");
        tempboard[0][2] = createStartingDie(2, 1, "H");
        tempboard[0][3] = createStartingDie(6, 2, "H");
        tempboard[0][4] = createStartingDie(1, 1, "H");
        tempboard[0][5] = createStartingDie(6, 2, "H");
        tempboard[0][6] = createStartingDie(2, 1, "H");
        tempboard[0][7] = createStartingDie(1, 5, "H");
        tempboard[0][8] = createStartingDie(5, 6, "H");

        setGameboard(tempboard);
    }

    // Get & Set
    final public DieModel[][] getGameBoard() {return gameboard;};
    public void setGameboard(DieModel[][] board) {
        gameboard = board;
    }
    /**
     Function creates a die set to the starting positions
     @param topNum number on the top
     @param rightNum int, number facing the right
     @return a die with the proper sides filled
     */
    public DieModel createStartingDie(int topNum, int rightNum, String playerStr){
        DieModel d = new DieModel();
        int die[] = new int[6];
        String player;
        Boolean keyPiece = false;

        // Die[top, bottom, left, right, front, back]
        // When creating a key piece set the keypiece bool to true
        if(topNum == 1 && rightNum == 1){
            die[0] = 1;
            die[1] = 1;
            die[2] = 1;
            die[3] = 1;
            die[4] = 1;
            die[5] = 1;
            player = playerStr;
            keyPiece = true;
        }else{
            die[0] = topNum;
            die[1] = 7 - topNum;
            die[2] = 7 - rightNum;
            die[3] = rightNum;
            die[4] = 4;
            die[5] = 3;
            player = playerStr;
            keyPiece = false;
        }
        d.setDie(die);
        d.setKeyPiece(keyPiece);
        d.setPlayer(player);

        return d;
    }
    /**
     Function creates a die set to the given top, right, front, back numbers, and player
     @param topNum number on the top
     @param rightNum int, number facing the right
     @param frontNum number facing the front
     @param backNum int, number facing the back
     @param player the player
     @return a die with the proper sides and player filled
     */
    public DieModel createDie(int topNum, int rightNum, int frontNum, int backNum, String player){
        DieModel d = new DieModel();
        int die[] = new int[6];
        Boolean keypiece = false;
        // When creating a key piece set the keypiece bool to true
        if (topNum == 1 && rightNum == 1)
        {
            die[0] = topNum;
            die[1] = topNum;
            die[2] = topNum;
            die[3] = topNum;
            die[4] = topNum;
            die[5] = topNum;
            keypiece = true;
        }
        else
        {
            die[0] = topNum;
            die[1] = 7 - topNum;
            die[2] = 7 - rightNum;
            die[3] = rightNum;
            die[4] = frontNum;
            die[5] = backNum;
        }
        d.setDie(die);
        d.setPlayer(player);
        d.setKeyPiece(keypiece);

        return d;
    }
    /**
     Function creates a die used for loading game
     @param topNum number on the top
     @param rightNum int, number facing the right
     @param player the player
     @return a die with the proper sides filled
     */
    public DieModel dieSwitch(int topNum, int rightNum, String player)
    {
        // Die[top, bottom, left, right, front, back]
        DieModel d = new DieModel();

        switch (topNum) {
            case 1:
                switch (rightNum) {
                    case 1:
                        d = createDie(topNum, rightNum, 1, 1, player);
                        break;
                    case 2:
                        d = createDie(topNum, rightNum, 3, 4, player);
                        break;
                    case 3:
                        d = createDie(topNum, rightNum, 5, 2, player);
                        break;
                    case 4:
                        d = createDie(topNum, rightNum, 2, 5, player);
                        break;
                    case 5:
                        d = createDie(topNum, rightNum, 4, 3, player);
                        break;
                }
                break;
            case 2:
                switch (rightNum) {
                    case 1:
                        d = createDie(topNum, rightNum, 4, 3, player);
                        break;
                    case 3:
                        d = createDie(topNum, rightNum, 1, 6, player);
                        break;
                    case 4:
                        d = createDie(topNum, rightNum, 6, 1, player);
                        break;
                    case 6:
                        d = createDie(topNum, rightNum, 3, 4, player);
                        break;
                }
                break;
            case 3:
                switch (rightNum) {
                    case 1:
                        d = createDie(topNum, rightNum, 2, 5, player);
                        break;
                    case 2:
                        d = createDie(topNum, rightNum, 6, 1, player);
                        break;
                    case 5:
                        d = createDie(topNum, rightNum, 1, 6, player);
                        break;
                    case 6:
                        d = createDie(topNum, rightNum, 5, 2, player);
                        break;
                }
                break;
            case 4:
                switch (rightNum) {
                    case 1:
                        d = createDie(topNum, rightNum, 5, 2, player);
                        break;
                    case 2:
                        d = createDie(topNum, rightNum, 1, 6, player);
                        break;
                    case 5:
                        d = createDie(topNum, rightNum, 6, 1, player);
                        break;
                    case 6:
                        d = createDie(topNum, rightNum, 2, 5, player);
                        break;
                }
                break;
            case 5:
                switch (rightNum) {
                    case 1:
                        d = createDie(topNum, rightNum, 3, 4, player);
                        break;
                    case 3:
                        d = createDie(topNum, rightNum, 6, 1, player);
                        break;
                    case 4:
                        d = createDie(topNum, rightNum, 1, 6, player);
                        break;
                    case 6:
                        d = createDie(topNum, rightNum, 4, 3, player);
                        break;
                }
                break;
            case 6:
                switch (rightNum) {
                    case 2:
                        d = createDie(topNum, rightNum, 4, 3, player);
                        break;
                    case 3:
                        d = createDie(topNum, rightNum, 2, 5, player);
                        break;
                    case 4:
                        d = createDie(topNum, rightNum, 5, 2, player);
                        break;
                    case 5:
                        d = createDie(topNum, rightNum, 3, 4, player);
                        break;
                }
                break;
        }
        return d;
    }
    /**
     Function moves a die one space up the board
     @param row row number
     @param col int, column number
     */
    public void movePieceUp(int row, int col){
        DieModel[][] tempBoard = getGameBoard();
        String player = tempBoard[row][col].getPlayer();
        DieModel emptyDie = new DieModel();

        if(player.equals("H")){
            tempBoard[row][col].frontalMove();
        }else{
            tempBoard[row][col].backwardMove();
        }
        tempBoard[row + 1][col] = tempBoard[row][col];
        tempBoard[row][col] = emptyDie;

        setGameboard(tempBoard);
    }
    /**
     Function moves a die one space down the board
     @param row row number
     @param col int, column number
     */
    public void movePieceDown(int row, int col){
        DieModel[][] tempBoard = getGameBoard();
        String player = tempBoard[row][col].getPlayer();
        DieModel emptyDie = new DieModel();

        if(player.equals("H")){
            tempBoard[row][col].backwardMove();
        }else{
            tempBoard[row][col].frontalMove();
        }
        tempBoard[row - 1][col] = tempBoard[row][col];
        tempBoard[row][col] = emptyDie;

        setGameboard(tempBoard);
    }
    /**
     Function moves a die one space left on the board
     @param row row number
     @param col int, column number
     */
    public void movePieceLeft(int row, int col){
        DieModel[][] tempBoard = getGameBoard();
        String player = tempBoard[row][col].getPlayer();
        DieModel emptyDie = new DieModel();

        if(player.equals("H")){
            tempBoard[row][col].lateralLeftMove();
        }else{
            tempBoard[row][col].lateralRightMove();
        }
        tempBoard[row][col - 1] = tempBoard[row][col];
        tempBoard[row][col] = emptyDie;

        setGameboard(tempBoard);
    }
    /**
     Function moves a die one space right on the board
     @param row row number
     @param col int, column number
     */
    public void movePieceRight(int row, int col){
        DieModel[][] tempBoard = getGameBoard();
        String player = tempBoard[row][col].getPlayer();
        DieModel emptyDie = new DieModel();

        if(player.equals("H")){
            tempBoard[row][col].lateralRightMove();
        }else{
            tempBoard[row][col].lateralLeftMove();
        }
        tempBoard[row][col + 1] = tempBoard[row][col];
        tempBoard[row][col] = emptyDie;

        setGameboard(tempBoard);
    }



}
