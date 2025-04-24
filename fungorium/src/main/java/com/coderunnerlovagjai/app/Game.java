package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game {
    private static final Logger GAME_LOGGER = LogManager.getLogger(Game.class);

    public static Player player1;
    public static Player player2;
    private int turnNumber;

    public Game() {
        player1 = new Player(); //id = 1
        player2 = new Player(); //id = 2
        this.turnNumber = 0;
        GAME_LOGGER.log(Level.forName("INIT", 402), "Game initialized with two players.");
    }

    public static Player getPlayer(int id) {
        if (id == player1.getId()) return player1;
        if (id == player2.getId()) return player2;
        return null;
    }

    public void createInsectForPlayer(Player player, Tecton_Class tecton) {
        if (player != player1 && player != player2) {
            GAME_LOGGER.log(Level.forName("ERROR", 404), "Invalid player!");
            return;
        }

        Insect_Class insect = new Insect_Buglet(tecton, player); 
        insect.set_Owner(player);
        Plane.InsectCollection.add(insect);
        GAME_LOGGER.log(Level.forName("CREATE", 401), "Insect created for player: " + player);
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
