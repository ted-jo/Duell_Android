/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

/**
 * Created by Ted Johansmeyer on 11/5/2016.
 */

public class TournamentModel {
    // Class Variables
    private int computerWins, humanWins;

    // Getters
    final public int getComputerWins() {return computerWins;}
    final public int getHumanWins() {return humanWins;}
    // Setters
    public void setComputerWins(int compWins) {
        if(compWins >= 0){
            computerWins = compWins;
        }
    }
    public void setHumanWins(int humWins){
        if(humWins >= 0){
            humanWins = humWins;
        }
    }
}
