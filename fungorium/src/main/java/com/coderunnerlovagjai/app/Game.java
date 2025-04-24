package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game { // --- Pálya létrehozás , pontok kiosztása, kiértékelés , játék kezdete
    private static final Logger GAME_LOGGER = LogManager.getLogger(Game.class);

    public static Player player1;
    public static Player player2;
    public static int turnNumber;

    public Game() {
        player1 = new Player(); //id = 1
        player2 = new Player(); //id = 2
        this.turnNumber = 1;
        GAME_LOGGER.log(Level.forName("INIT", 402), "Game initialized with two players.");
    }

    public int currentTurnsPlayer() {
        if (turnNumber % 2 == 1) {
            return player1.getId(); // Player 1's turn
        } else {
            return player2.getId(); // Player 2's turn
        }
    }

    public static Player getPlayer(int id) {
        if (id == player1.getId()) return player1;
        if (id == player2.getId()) return player2;
        return null;
    }

    public int turn() {
        turnNumber++;
        GAME_LOGGER.log(Level.forName("TURN", 401), "Turn number: " + turnNumber);
        // Implement game logic for each turn here
        if (player1 != null && player2 != null) {
            // Process player turns
            //player1.processTurn();
            //player2.processTurn();
            
            // Update game state on the plane
            //Plane.updateState();
        }

        return turnNumber;
    }
}
