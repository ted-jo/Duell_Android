/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

import android.util.Log;

/**
 * Created by Ted Johansmeyer on 11/24/2016.
 */

public class Player {
    // Class Variables
    private String line1;
    private String line2;
    private String line3;
    private String message1;
    private String message2;
    protected BoardModel boardObj = new BoardModel();

    // Getters
    final public BoardModel getBoard(){return boardObj;}
    final public String getLine1(){return line1;}
    final public String getLine2(){return line2;}
    final public String getLine3(){return line3;}
    final public String getMessage1(){return message1;}
    final public String getMessage2(){return message2;}
    // Setters
    public void setBoard(BoardModel board){boardObj = board;}
    public void setLines(String msg1, String msg2, String msg3){
        line1 = msg1;
        line2 = msg2;
        line3 = msg3;
    }
    public void setMsgs(String msg1, String msg2){
        message1 = msg1;
        message2 = msg2;
    }

    /* *********************************************
               Help and display functions
     ********************************************* */

    /**
     Function sets the strings with the help for the human player
     @param startRow int, starting row position
     @param startCol int, starting column position
     @param endRow int, ending row position
     @param endCol int, ending column position
     @param direction int, direction moved first
     @param moveType String, containing the type of move executed
     */
    private void displayHelp(int startRow, int startCol, int endRow, int endCol, int direction, String moveType){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        String line1 = "";
        String line2 = "";
        String line3 = "";
        int displayRow = startRow;
        int displayCol = startCol;
        int displayEndRow = endRow;
        int displayEndCol = endCol;
        displayRow++;
        displayCol++;
        displayEndRow++;
        displayEndCol++;
        if (moveType.equals("attackPiece"))
        {
            line1 = "To attack the key piece and win the game";
        }
        else if (moveType.equals("attackSpace"))
        {
            line1 = "To attack the key space and win the game";
        }
        else if (moveType.equals("block"))
        {
            line1 = "To perform blocking move";
        }
        else if (moveType.equals("overtake"))
        {
            line1 = "To overtake a computer piece";
        }
        // Set the second line
        line2 = "Choose die " + tempBoard[startRow][startCol].displayDie()
                + " at location (" + displayRow + "," + displayCol + ")";
        if(direction == 0){
            // If there is no 90 degree turn involved set the strings accordingly
            if(startCol == endCol){
                line3 = "Roll it to location (" + displayEndRow + "," + displayEndCol
                        + ") frontally because it is the most direct clear path";

            } else {
                line3 = "Roll it to location (" + displayEndRow+ "," + displayEndCol
                        + ") laterally because it is the most direct clear path";
            }
        } else if(direction == 1){
            // If frontal then laterally is recommended
            line3 = "Roll it to location (" + displayEndRow + "," + displayEndCol
                    + ") frontally first because a frontal then lateral path is clear";
        } else if(direction == 2){
            // If lateral then frontal is recommended
            line3 = "Roll it to location (" + displayEndRow + "," + displayEndCol
                    + ") frontally first because a frontal then lateral path is clear";
        }
        // Set the private strings to be captured by the activity
        setLines(line1, line2, line3);
    }
    /**
     This function sets the strings for a computer move to display
     @param startRow int, starting row position
     @param startCol int, starting column position
     @param endRow int, ending row position
     @param endCol int, ending column position
     @param direction int, direction moved first
     @param moveType int, containing the type of move executed
     */
    private void displayMove(int startRow, int startCol, int endRow, int endCol, int direction, int moveType){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        String msg= "";
        String msg2 = "";
        int numRows = Math.abs(startRow - endRow);
        int numCols = Math.abs(startCol - endCol);
        int displayRow = startRow;
        int displayCol = startCol;
        int displayEndRow = endRow;
        int displayEndCol = endCol;
        displayRow++;
        displayCol++;
        displayEndRow++;
        displayEndCol++;

        if(moveType == 1){
            msg = "The Computer picked " + tempBoard[startRow][startCol].displayDie() + " at (" +
                    displayRow + "," + displayCol + ") to roll because this die can win the game";
        } else if(moveType == 2){
            msg = "The Computer picked " + tempBoard[startRow][startCol].displayDie() + " at (" +
                    displayRow + "," + displayCol + ") to roll because this die can block " +
                    "an attack on the key piece";
        } else if(moveType == 3){
            msg = "The Computer picked " + tempBoard[startRow][startCol].displayDie() + " at (" +
                    displayRow + "," + displayCol + ") to roll because this die overtake a human piece";
        } else {
            msg = "The Computer picked " + tempBoard[startRow][startCol].displayDie() + " at (" +
                    displayRow + "," + displayCol + ") to roll because this die can move closest " +
                    "to the human keypiece";
        }
        if(direction == 0){
            // If there is no 90 degree turn involved set the strings accordingly
            if(startCol == endCol){
                msg2 = "It moved " + numRows + " rows to (" + displayEndRow + "," + displayEndCol
                        + ") because it is the most direct clear path";
            } else {
                msg2 = "It moved " + numCols + " columns to (" + displayEndRow + "," + displayEndCol
                        + ") because it is the most direct clear path";
            }
        } else if(direction == 1){
            msg2 = "It moved " + numRows + " rows and " + numCols + " columns to (" + displayEndRow + "," + displayEndCol
                    + ") because a frontal then lateral path was clear";
        } else if(direction == 2){
            msg2 = "It moved " + numCols + " columns and " + numRows + " rows to (" + displayEndRow + "," + displayEndCol
                    + ") because a frontal first move was blocked";
        }

        setMsgs(msg, msg2);
    }

    /**********************************************
               Path checking functions
     ********************************************* */

    /**
     This function either checks or executes the move in the vertical direction
     @param startRow int, starting row position
     @param startCol int, starting column position
     @param endRow int, ending row position
     @param endCol int, ending column position
     @param execute Boolean, true to execute move, false to just check path
     @return Returns true if the check or move is complete
     */
    private Boolean checkVerticalPath(int startRow, int startCol, int endRow, int endCol, Boolean execute){
        final DieModel[][] tempBoard = boardObj.getGameBoard();
        Boolean first = true;

        if(startRow > endRow){
            while(startRow >= endRow){
                //If execute is false only check path
                if(!execute){
                    //Skip starting space
                    if(first){
                        first = false;
                        startRow--;
                        continue;
                    }
                    //If the end space is on an opposing die return true
                    if(startCol == endCol && startRow == endRow && !tempBoard[startRow][startCol].isEmpty()){
                        return true;
                    }
                    //Return false if path is blocked
                    if(!tempBoard[startRow][startCol].isEmpty()){
                        return false;
                    }
                } else {
                    if(startRow == endRow){
                        return true;
                    } else {
                        // If execute is true execute the move
                        boardObj.movePieceDown(startRow, startCol);
                    }
                }
                startRow--;
            }
            // If path is clear or move is executed return true
            return true;
        } else if(startRow < endRow){
            while(startRow <= endRow){
                if(!execute){
                    if(first){
                        first = false;
                        startRow++;
                        continue;
                    }
                    if(startCol == endCol && startRow == endRow && !tempBoard[startRow][startCol].isEmpty()){
                        return true;
                    }
                    if(!tempBoard[startRow][startCol].isEmpty()){
                        return false;
                    }
                } else {
                    if(startRow == endRow){
                        return true;
                    } else {
                        boardObj.movePieceUp(startRow, startCol);
                    }
                }
                startRow++;
            }
            return true;
        }
        // Error
        Log.e("CheckVerticalPath", "checkVerticalPath: Failure");
        return false;
    }
    /**
     This function either checks or executes the move in the horizontal direction
     @param startRow int, starting row position
     @param startCol int, starting column position
     @param endRow int, ending row position
     @param endCol int, ending column position
     @param execute Boolean, true to execute move, false to just check path
     @return Returns true if the check or move is complete
     */
    private Boolean checkHorizontalPath(int startRow, int startCol, int endRow, int endCol, Boolean execute){
        final DieModel[][] tempBoard = boardObj.getGameBoard();
        Boolean first = true;

        if(startCol > endCol){
            while(startCol >= endCol){
                //If execute is false only check path
                if(!execute){
                    //Skip starting space
                    if(first){
                        first = false;
                        startCol--;
                        continue;
                    }
                    //If the end space is on an opposing die return true
                    if(startCol == endCol && startRow == endRow && !tempBoard[startRow][startCol].isEmpty()){
                        return true;
                    }
                    //Return false if path is blocked
                    if(!tempBoard[startRow][startCol].isEmpty()){
                        return false;
                    }
                } else {
                    if(startCol == endCol){
                        return true;
                    } else {
                        boardObj.movePieceLeft(startRow, startCol);
                    }
                }
                startCol--;
            }
            return true;
        } else if(startCol < endCol){
            while(startCol <= endCol){
                if(!execute){
                    if(first){
                        first = false;
                        startCol++;
                        continue;
                    }
                    if(startCol == endCol && startRow == endRow && !tempBoard[startRow][startCol].isEmpty()){
                        return true;
                    }
                    if(!tempBoard[startRow][startCol].isEmpty()){
                        return false;
                    }
                } else {
                    if(startCol == endCol){
                        return true;
                    } else {
                        boardObj.movePieceRight(startRow, startCol);
                    }
                }
                startCol++;
            }
            return true;
        }
        // Error
        Log.e("CheckHorizontalPath", "checkHorizontalPath: Failure");
        return false;
    }
    /**
     This function finds direction to move with a clear path
     @param startRow int, starting row position
     @param startCol int, starting column position
     @param endRow int, ending row position
     @param endCol int, ending column position
     @return An int which signifies the direction chosen
     */
    private int getDirection(int startRow, int startCol, int endRow, int endCol){
        // Return 0 if strictly vertical or strictly horizontal move
        if (startCol == endCol) {
            if (checkVerticalPath(startRow, startCol, endRow, endCol, false)) {
                return 0;
            }
        } else if(startRow == endRow) {
            if (checkHorizontalPath(startRow, startCol, endRow, endCol, false)) {
                return 0;
            }
        } else if (checkVerticalPath(startRow, startCol, endRow, endCol, false)) {
            // Check forward then horizontal path and return 1
            if (checkHorizontalPath(endRow, startCol, endRow, endCol, false)) {
                return 1;
            }
        } else if (checkHorizontalPath(startRow, startCol, endRow, endCol, false)) {
            // Check lateral then forward and return 2
            if (checkVerticalPath(startRow, endCol, endRow, endCol, false)) {
                return 2;
            }
        }
        // Return -1 if no paths are clear
        return -1;
    }
    /**
     This function calls the AI functions as a human to generate the help
     */
    public void help(){
        // Check if Human has winning move
        if (keyPieceAttack("H", true));
        else if(protectKeyPiece("H", false));
        else if(protectKeyPiece("H", true));
        else if(checkOvertake("H"));
        else
        {
            String line1, line2, line3;
            line1 = line2 = line3 = "";
            line1 = "There are no critical moves to execute";
            line2 = "Move any die of your choosing";
            setLines(line1, line2, line3);
        }
    }
    /**
     Checks the board for a computer win scenario
     @return Returns true if the computer has won
     */
    public Boolean checkComputerWin(){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int[] location = getKeypieceLoc("C");
        // Check if the key square has been overtaken with keypiece
        if (tempBoard[0][4].getPlayer().equals("C") && tempBoard[0][4].getKeyPiece())
        {
            return true;
        }
        // if location[0] == 100 there is no keypiece on the board
        else if (location[0] == 100)
        {
            return true;
        }

        return false;
    }
    /**
     Checks the board for a human win scenario
     @return Returns true if the human has won
     */
    public Boolean checkHumanWin(){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int[] location = getKeypieceLoc("H");
        // Check if the key square has been overtaken with keypiece
        if (tempBoard[7][4].getPlayer().equals("H") && tempBoard[7][4].getKeyPiece())
        {
            return true;
        }
        // if location[0] == 100 there is no keypiece on the board
        else if (location[0] == 100)
        {
            return true;
        }

        return false;
    }
    /**
     This function either checks the path or executes a given move
     @param startRow int, starting row position
     @param startCol int, starting column position
     @param endRow int, ending row position
     @param endCol int, ending column position
     @param direction int, signifies the direction to travel first
     @param execute Boolean, true to execute move, false to just check path
     @return Returns true if the check or move is complete
     */
    public Boolean getPath(int startRow, int startCol, int endRow, int endCol, int direction, Boolean execute){
        final DieModel[][] tempBoard = boardObj.getGameBoard();
        String player = tempBoard[startRow][startCol].getPlayer();
        String endPlayer = tempBoard[endRow][endCol].getPlayer();

        // If endspace is occupied by Die of same player path is blocked
        if(player.equals(endPlayer)){
            return false;
        }
        // If move is strictly vertical
        if(startCol == endCol){
            // If path is clear, execute move and return true
            if(checkVerticalPath(startRow, startCol, endRow, endCol, false)){
                if(execute){
                    if(checkVerticalPath(startRow, startCol, endRow, endCol, true)){
                        return true;
                    }
                }
                return true;
            }
            // If move is strictly horizontal
        } else if (startRow == endRow){
            // If path is clear, execute move and return true
            if(checkHorizontalPath(startRow, startCol, endRow, endCol, false)){
                if(execute){
                    if(checkHorizontalPath(startRow, startCol, endRow, endCol, true)){
                        return true;
                    }
                }
                return true;
            }
            // If move is vertical then horizontal
        } else if (direction == 1){
            // Check if path is clear
            if(checkVerticalPath(startRow, startCol, endRow, endCol, false)){
                if(checkHorizontalPath(endRow, startCol, endRow, endCol, false)){
                    if(execute){
                        // Execute Move
                        if(checkVerticalPath(startRow, startCol, endRow, endCol, true)){
                            if(checkHorizontalPath(endRow, startCol, endRow, endCol, true)){
                                return true;
                            }
                        }
                    }
                    return true;
                }
            }
            // If move is Horizontal then vertical
        } else if(direction == 2){
            // Check if path is clear
            if(checkHorizontalPath(startRow, startCol, endRow, endCol, false)){
                if(checkVerticalPath(startRow, endCol, endRow, endCol, false)){
                    if(execute){
                        // Execute Move
                        if(checkHorizontalPath(startRow, startCol, endRow, endCol, true)){
                            if(checkVerticalPath(startRow, endCol, endRow, endCol, true)){
                                return true;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        // No clear Paths
        return false;
    }
    /**
     This function gets the location of the keypiece
     @param player string, player to check the keypiece
     @return Returns int[] of the coordinates of the keypiece
     */
    public int[] getKeypieceLoc(String player){
        final DieModel[][] tempBoard = boardObj.getGameBoard();
        int[] location = {100, 100};
        // Loop through board
        for(int row = 0; row <= 7; row++){
            for(int col = 0; col <=8; col++){
                if(player.equals("C")){
                    // If the location is an opponents die and it's the keypiece
                    if(tempBoard[row][col].getPlayer().equals("H") && tempBoard[row][col].getKeyPiece() == true){
                        // Set first index of array to row, second to column
                        location[0] = row;
                        location[1] = col;
                    }
                } else {
                    if(tempBoard[row][col].getPlayer().equals("C") && tempBoard[row][col].getKeyPiece() == true){
                        // Set first index of array to row, second to column
                        location[0] = row;
                        location[1] = col;
                    }
                }
            }
        }
        return location;
    }
    /**
     This function checks the number of spaces between two points
     @param startRow int, starting row position
     @param startCol int, starting column position
     @param endRow int, ending row position
     @param endCol int, ending column position
     @return Returns true if the two spaces are the correct number of spaces apart for a move
     */
    public Boolean checkNumSpaces(int startRow, int startCol, int endRow, int endCol){
        int tempRow, tempCol, tempRowCol;
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int[] die = tempBoard[startRow][startCol].getDie();
        // Get number of moves allowed
        int move = die[0];
        // Subtract the end point by the starting point and take absolute value
        // When added together it equals the total number of moves allowed
        tempRow = endRow - startRow;
        tempCol = endCol - startCol;
        tempRow = Math.abs(tempRow);
        tempCol = Math.abs(tempCol);
        tempRowCol = tempRow + tempCol;
        if (tempRowCol == move)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /* *********************************************
        Computer AI Functions
    ********************************************* */

    /**
     This function checks if there is a move to attack the opponent's keypiece
     @param player string, "Computer" to check for a copmuter move, "Human" for the help function
     @param display Boolean, true to display the help message
     @return true if there is a key piece attack move available
     */
    public Boolean keyPieceAttack(String player, Boolean display){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int[] keyPieceLocation = getKeypieceLoc(player);
        int endRow = keyPieceLocation[0];
        int endCol = keyPieceLocation[1];
        int direction;

        for(int row = 0; row <= 7; row++){
            for(int col = 0; col <= 8; col++){
                if(player.equals("C")){
                    // If Die piece is a computer piece check all possible moves
                    // to either overtake the Human keypiece or the keyspace
                    if(tempBoard[row][col].getPlayer().equals("C")){
                        // Check if the Computer Piece is the correct
                        // number of spaces from the Human Key Piece
                        if(checkNumSpaces(row, col, endRow, endCol)){
                            direction = getDirection(row, col, endRow, endCol);
                            // If direction != -1 path is clear in that direction
                            if(direction != -1){
                                displayMove(row, col, endRow, endCol, direction, 1);
                                // Execute move
                                if(getPath(row, col, endRow, endCol, direction, true)){
                                    return true;
                                }
                            }
                        } else if (tempBoard[row][col].getKeyPiece() && checkNumSpaces(row, col, 0, 4)){
                            // Check against keyspace
                            direction = getDirection(row, col, 0, 4);
                            // If direction != -1 path is clear in that direction
                            if(direction != -1){
                                displayMove(row, col, 0, 4, direction, 1);
                                if(getPath(row, col, 0, 4, direction, true)){
                                    return true;
                                }
                            }
                        }
                    }
                } else {
                    // If Die piece is a human piece check all possible moves
                    // to either overtake the computer keypiece or the keyspace
                    if(tempBoard[row][col].getPlayer().equals("H")){
                        // Check if the Computer Piece is the correct
                        // number of spaces from the Human Key Piece
                        if(checkNumSpaces(row, col, endRow, endCol)){
                            direction = getDirection(row, col, endRow, endCol);
                            if(direction != -1){
                                if(display){
                                    displayHelp(row, col, endRow, endCol, direction, "attackPiece");
                                }
                                return true;
                            }
                        } else if(tempBoard[row][col].getKeyPiece() && checkNumSpaces(row, col, 7, 4)){
                            // Check against keyspace
                            direction = getDirection(row, col, 7, 4);
                            // If direction != -1 path is clear in that direction
                            if(direction != -1){
                                if(display){
                                    displayHelp(row, col, 7, 4, direction, "attackSpace");
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        // Return false if no attack moves
        return false;
    }
    /**
     This function checks if there is a move to block the opponent's keypiece attack
     @param player string, "Computer" to check for a copmuter move, "Human" for the help function
     @param keySpace Boolean, true to check the key space
     @return true if there is a key piece block move available
     */
    public Boolean protectKeyPiece(String player, Boolean keySpace){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int direction, endRow, endCol;
        // If its a computer move and we're trying to protect the key piece
        if(player.equals("C") && !keySpace){
            int[] keyPieceLocation = getKeypieceLoc("H");
            endRow = keyPieceLocation[0];
            endCol = keyPieceLocation[1];
        } else if(player.equals("C") && keySpace){
            // If its a computer move and we're trying to protect the key space
            endRow = 7;
            endCol = 4;
        } else if (player.equals("H") && !keySpace){
            // If its a human move and we're trying to protect the key piece
            int[] keyPieceLocation = getKeypieceLoc("C");
            endRow = keyPieceLocation[0];
            endCol = keyPieceLocation[1];
        } else {
            // If its a human move and we're trying to protect the key space
            endRow = 0;
            endCol = 4;
        }

        for(int row = 7; row >= 0; row--){
            for(int col = 0; col <= 8; col++){
                // Computer Move
                if(player.equals("C")){
                    // If Die piece is a human piece check all possible moves
                    // that can overtake the computer keypiece or the keyspace
                    if (tempBoard[row][col].getPlayer().equals("H"))
                    {
                        // Check if the Human Piece is the correct
                        // number of spaces from the Computer Key Piece
                        if (checkNumSpaces(row, col, endRow, endCol)) {
                            direction = getDirection(row, col, endRow, endCol);
                            // If check path is true, then Human has a winning move
                            // Execute block and return true
                            if (direction != -1) {
                                if (direction == 0) {
                                    // If its a vertical attack and end is higher than start
                                    if (col == endCol && endRow > row) {
                                        endRow--;
                                        // For a forward attack set --endY
                                        // So block manuever is executed one space below keypiece
                                        if (executeBlock(endRow, endCol, true)) {
                                            return true;
                                        }
                                    }
                                    // If its a vertical attack and end is lower than start
                                    else if (col == endCol && endRow < row) {
                                        endRow++;
                                        if (executeBlock(endRow, endCol, true)) {
                                            return true;
                                        }
                                    }
                                    // If its a Horizontal attack and end is higher than start
                                    else if (row == endRow && endCol > col) {
                                        // Block Space to left of Keypiece
                                        if (endCol > col) {
                                            --endCol;
                                            if (executeBlock(endRow, endCol, true)) {
                                                return true;
                                            }
                                        }
                                    }
                                    // If its a Horizontal attack and end is lower than start
                                    else if (row == endRow && endCol < col) {
                                        ++endCol;
                                        if (executeBlock(endRow, endCol, true)) {
                                            return true;
                                        }
                                    }
                                }
                                // If its a forward -> lateral attack
                                else if (direction == 1) {
                                    // Block Space to left of Keypiece
                                    if (endCol > col) {
                                        --endCol;
                                        if (executeBlock(endRow, endCol, true)) {
                                            return true;
                                        }
                                    } else if (endCol < col) {
                                        ++endCol;
                                        if (executeBlock(endRow, endCol, true)) {
                                            return true;
                                        }
                                    }
                                }
                                // If its a lateral -> forward
                                else if (direction == 2) {
                                    if (endRow > row) {
                                        endRow--;
                                        // For a forward or lateral->forward attack set --endY
                                        // So block manuever is executed one space below keypiece
                                        if (executeBlock(endRow, endCol, true)) {
                                            return true;
                                        }
                                    } else if (endRow < row) {
                                        endRow++;
                                        if (executeBlock(endRow, endCol, true)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Human Move
                else {
                    // If Die piece is a Computer piece check all possible moves
                    // that can overtake the human keypiece or the keyspace
                    if (tempBoard[row][col].getPlayer().equals("C")) {
                        // Check if the Computer Piece is the correct
                        // number of spaces from the Human Key Piece
                        if (checkNumSpaces(row, col, endRow, endCol)) {
                            direction = getDirection(row, col, endRow, endCol);
                            // If check path is true, then Human has a winning move
                            // Execute block and return true
                            if (direction != -1) {
                                if (direction == 0) {
                                    // If its a vertical attack and end is higher than start
                                    if (col == endCol && endRow > row) {
                                        endRow--;
                                        // For a forward attack set --endY
                                        // So block manuever is executed one space below keypiece
                                        if (executeBlock(endRow, endCol, false)) {
                                            return true;
                                        }
                                    }
                                    // If its a vertical attack and end is lower than start
                                    else if (col == endCol && endRow < row) {
                                        endRow++;
                                        if (executeBlock(endRow, endCol, false)) {
                                            return true;
                                        }
                                    }
                                    // If its a Horizontal attack and end is higher than start
                                    else if (row == endRow && endCol > col) {
                                        // Block Space to left of Keypiece
                                        if (endCol > col) {
                                            --endCol;
                                            if (executeBlock(endRow, endCol, false)) {
                                                return true;
                                            }
                                        }
                                    }
                                    // If its a Horizontal attack and end is lower than start
                                    else if (row == endRow && endCol < col) {
                                        ++endCol;
                                        if (executeBlock(endRow, endCol, false)) {
                                            return true;
                                        }
                                    }
                                }
                                // If its a forward -> lateral attack
                                else if (direction == 1) {
                                    // Block Space to left of Keypiece
                                    if (endCol > col) {
                                        --endCol;
                                        if (executeBlock(endRow, endCol, false)) {
                                            return true;
                                        }
                                    }
                                    else if (endCol < col) {
                                        ++endCol;
                                        if (executeBlock(endRow, endCol, false)) {
                                            return true;
                                        }
                                    }
                                }
                                // If its a lateral -> forward
                                else if (direction == 2) {
                                    if (endRow > row) {
                                        endRow--;
                                        // For a forward or lateral->forward attack set --endY
                                        // So block manuever is executed one space below keypiece
                                        if (executeBlock(endRow, endCol, false)) {
                                            return true;
                                        }
                                    } else if (endRow < row) {
                                        endRow++;
                                        if (executeBlock(endRow, endCol, false)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     This function finds a piece to block and executes the blocking move
     @param endRow int, ending row position
     @param endCol int, ending column position
     @return true if there is a key piece block move available or successful move execution
     */
    public Boolean executeBlock(int endRow, int endCol, Boolean execute){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int direction;
        // Coordinates (endX, endY) is the Computer Keypiece
        // To find a candidate to block from below
        // find a Computer piece that can move to endX--
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col <= 8; col++) {
                if (execute) {
                    if (tempBoard[row][col].getPlayer().equals("C")) {
                        if (checkNumSpaces(row, col, endRow, endCol)) {
                            direction = getDirection(row, col, endRow, endCol);
                            if (direction != -1) {
                                displayMove(row, col, endRow, endCol, direction, 2);
                                if (getPath(row, col, endRow, endCol, direction, true)) {
                                    return true;
                                }
                            }
                        }
                    }
                } else {
                    if (tempBoard[row][col].getPlayer().equals("H")) {
                        if (checkNumSpaces(row, col, endRow, endCol)) {
                            direction = getDirection(row, col, endRow, endCol);
                            if (direction != -1) {
                                displayHelp(row, col, endRow, endCol, direction, "block");
                                return true;
                            }
                        }
                    }
                }
            }
        }
        // If there are no possible blocking moves return false
        return false;
    }
    /**
     This function checks if there is a move to overtake a opponent's die
     @param player string, "Computer" to check for a copmuter move, "Human" for the help function
     @return true on successful overtake move
     */
    public Boolean checkOvertake(String player){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int direction;
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col <= 8; col++) {
                // Computer Move
                if (player.equals("C")) {
                    // If Die piece is a computer piece check all possible moves
                    // to overtake a Human Piece
                    if (tempBoard[row][col].getPlayer().equals("C")) {
                        for (int endRow = 0; endRow <= 7; endRow++) {
                            for (int endCol = 0; endCol <= 8; endCol++) {
                                if (tempBoard[endRow][endCol].getPlayer().equals("H")) {
                                    if (checkNumSpaces(row, col, endRow, endCol)) {
                                        direction = getDirection(row, col, endRow, endCol);
                                        if (direction != -1) {
                                            displayMove(row, col, endRow, endCol, direction, 3);
                                            if (getPath(row, col, endRow, endCol, direction, true)) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Human move
                else {
                    // If Die piece is a Human piece check all possible moves
                    // to overtake a Computer Piece
                    if (tempBoard[row][col].getPlayer().equals("H")) {
                        for (int endRow = 7; endRow >= 0; endRow--) {
                            for (int endCol = 0; endCol <= 8; endCol++) {
                                if (tempBoard[endRow][endCol].getPlayer().equals("C")) {
                                    if (checkNumSpaces(row, col, endRow, endCol)) {
                                        direction = getDirection(row, col, endRow, endCol);
                                        if (direction != -1) {
                                            displayHelp(row, col, endRow, endCol, direction, "overtake");
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     Blitz strategy If there are no critical moves find a die that can move closest to the keypiece
     @return true on successful move
     */
    public Boolean executeBestMove(){
        DieModel[][] tempBoard = boardObj.getGameBoard();
        int[] keyPieceLocation = getKeypieceLoc("C");
        int keyRow = keyPieceLocation[0];
        int keyCol = keyPieceLocation[1];
        int score = 100;
        int direction = -1;
        int tempDirection, tempScore, startRow, startCol, finalRow, finalCol;
        tempDirection = tempScore = startRow = startCol = finalRow = finalCol = 0;
        // Loop through board finding every C piece
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 8; col++) {
                if (tempBoard[row][col].getPlayer().equals("C")) {
                    // When C piece is found loop through board again
                    // Checking all possible end locations
                    for (int endRow = 0; endRow <= 7; endRow++) {
                        for (int endCol = 0; endCol <= 8; endCol++) {
                            // If end point is proper number of spaces
                            // Check path and get direction and calculate score
                            if (checkNumSpaces(row, col, endRow, endCol)) {
                                tempDirection = getDirection(row, col, endRow, endCol);
                                // If getDirection != -1 path is clear
                                if (tempDirection != -1) {
                                    tempScore = (Math.abs(endCol - keyCol) + Math.abs(endRow - keyRow));
                                    if (tempScore < score) {
                                        score = tempScore;
                                        direction = tempDirection;
                                        startRow = row;
                                        startCol = col;
                                        finalRow = endRow;
                                        finalCol = endCol;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        displayMove(startRow, startCol, finalRow, finalCol, direction, 4);
        // If move is executed return true;
        return getPath(startRow, startCol, finalRow, finalCol, direction, true);
    }



}
