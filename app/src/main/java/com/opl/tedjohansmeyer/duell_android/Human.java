/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

/**
 * Created by Ted Johansmeyer on 11/12/2016.
 */

public class Human extends Player{
    // Class variables
    private int startRow, startCol, endRow, endCol, direction;
    // Setter
    public void setCoordinates(int startRow, int startCol, int endRow, int endCol, int direction){
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.direction = direction;
    }

    /**
     Execute a human move
     @return BoardModel, the board object with the completed move
     */
    public BoardModel play(){
        getPath(startRow, startCol, endRow, endCol, direction, true);
        return boardObj;
    }
}
