/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

/**
 * Created by Ted Johansmeyer on 11/28/2016.
 */

public class Computer extends Player {
    /**
     Executes a single computer move
     @return BoardModel, the board object with the completed move
     */
    public BoardModel play(){
        // First Pass
        // Try to execute attack on Human KeyPiece
        if (keyPieceAttack("C", false)) {
            //cout << "Computer has attacked your Key Piece" << endl;
        }
        // Check to see if the Human has any moves that
        // will overtake the AI's keypiece
        // Initiate a block if true
        else if (keyPieceAttack("H", false) && protectKeyPiece("C", false)) {
        }
        // Check to see if the Human has any moves that
        // will overtake the AI's keyspace
        // Initiate a block if true
        else if (keyPieceAttack("H", false) && protectKeyPiece("C", true))
        {
        }
        else if (checkOvertake("C"))
        {
            //cout << "Computer executed a move to overtake a human piece";
        }
        else
        {
            // No winning or blocking moves
            // Execute best move available
            executeBestMove();
        }
        return boardObj;
    }
}
