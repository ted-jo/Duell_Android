/****************************************************
 * Name:  Ted Johansmeyer                            *
 * Project:  Duell Android Project                   *
 * Class:  OPL CMPS 366                              *
 * Date:  December 3rd 2016                          *
 *****************************************************/
package com.opl.tedjohansmeyer.duell_android;

import java.util.ArrayList;

/**
 * Created by Ted Johansmeyer on 11/5/2016.
 */

public class DieModel {
    // Class variables
    private int[] die = new int[6];
    private String player = new String("");
    private Boolean keyPiece = false;

    // Getters
    final public int[] getDie(){return die;}
    final public String getPlayer(){return player;}
    final public Boolean getKeyPiece(){return keyPiece;}


    // Setters
    public void setPlayer(String p){player = p;}
    public void setKeyPiece(Boolean kp){keyPiece = kp;}
    public void setDie(int[] newDie){
        if(newDie.length == 6) {
            die = newDie;
        }
    }
    /**
     Check if die is empty
     @return true if die is empty
     */
    public Boolean isEmpty(){
        if(die[0] == 0)
            return true;
        else
            return false;
    }
    /**
     Display the die
     @return String, the die output ie. "H23"
     */
    public String displayDie(){
        int top = die[0];
        int right = die[3];
        String topStr = Integer.toString(top);
        String rightStr = Integer.toString(right);

        return player + topStr + rightStr;
    }
    /**
     Move the numbers on the die for a frontal move
     */
    public void frontalMove(){
        // Die[top, bottom, left, right, front, back]
        // newDie[back, front, left, right, top, bottom]
        int newDie[] = new int[6];

        newDie[0] = die[5];
        newDie[1] = die[4];
        newDie[2] = die[2];
        newDie[3] = die[3];
        newDie[4] = die[0];
        newDie[5] = die[1];

        setDie(newDie);
    }
    /**
     Move the numbers on the die for a backward move
     */
    public void backwardMove(){
        // Die[top, bottom, left, right, front, back]
        // newDie[front, back, left, right, bottom, top]
        int newDie[] = new int[6];

        newDie[0] = die[4];
        newDie[1] = die[5];
        newDie[2] = die[2];
        newDie[3] = die[3];
        newDie[4] = die[1];
        newDie[5] = die[0];

        setDie(newDie);
    }
    /**
     Move the numbers on the die for a left move
     */
    public void lateralLeftMove(){
        // Die[top, bottom, left, right, front, back]
        // newDie[right, left, top, bottom, front, back]
        int newDie[] = new int[6];

        newDie[0] = die[3];
        newDie[1] = die[2];
        newDie[2] = die[0];
        newDie[3] = die[1];
        newDie[4] = die[4];
        newDie[5] = die[5];

        setDie(newDie);
    }
    /**
     Move the numbers on the die for a right move
     */
    public void lateralRightMove(){
        // Die[top, bottom, left, right, front, back]
        // newDie[left, right, bottom, top, front, back]
        int newDie[] = new int[6];

        newDie[0] = die[2];
        newDie[1] = die[3];
        newDie[2] = die[1];
        newDie[3] = die[0];
        newDie[4] = die[4];
        newDie[5] = die[5];

        setDie(newDie);
    }


}
