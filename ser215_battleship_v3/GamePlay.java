/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ser215_battleship_v3;

import java.util.Random;

/**
 *
 * @author Tim
 */
public class GamePlay {

    String gameStatus;

    public GamePlay() {
        gameStatus = "players turn";
    }

    public void AI_Turn() {

    }

    public void takingTurns() {
        //    if (gameStatus == 2) {

    }

    public void switchTurns(GamePlay turnOrder) {
        if (turnOrder.gameStatus.equals("players turn")) {
            turnOrder.gameStatus = "Opponents turn";

        }
        if (gameStatus.equals("Opponents turn")) {
            gameStatus = "players turn";
        }
    }

    public String getTurnOrder() {
        return gameStatus;
    }

}