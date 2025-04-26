package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game { // --- Pálya létrehozás , pontok kiosztása, kiértékelés , játék kezdete, Spóra kiosztás, játék vége ---
    private static final Logger GAME_LOGGER = LogManager.getLogger(Game.class);

    public static Player player1;
    public static Player player2;
    public static int turnNumber;
    public static Plane plane; // A játékhoz tartozó pálya 

    public Game() {
        player1 = new Player(1); //id = 1
        player2 = new Player(2); //id = 2
        this.turnNumber = 0;

        // Inicializáljuk a Plane-t, a bázisok inicializálása átkerült a külön initGame() metódusba,
        // hogy elkerüljük az "this" szivárgását a konstruktorban.
        plane = new Plane();

        GAME_LOGGER.log(Level.forName("INIT", 402), "Game initialized with two players.");
    }

    public void initGame() {
        // A bázisok inicializálása külön metódusban történik
        plane.initBases(player1, player2, this);
        GAME_LOGGER.log(Level.forName("INIT", 402), "Game initialized with two players and their bases.");
    }

    public int currentTurnsPlayer() {
        if (turnNumber % 2 == 1) {
            return player1.getId(); // Player 1's turn
        } else {
            return player2.getId(); // Player 2's turn
        }
    }
    
    public void startGame() { //Proto-> if choice->startGame() egész eddigi utána
        GAME_LOGGER.log(Level.forName("START", 401), "Game started.");
        // Implement game start logic here
        // For example, you can initialize the game state, set up the board, etc.
        this.turn();
    }

    public static Player getPlayer(int id) {
        if (id == player1.getId()) return player1;
        if (id == player2.getId()) return player2;
        return null;
    }

    public int turn() { //Ez majd void lesz, csak meg _Tests miatt int
        Player currentPlayer;
        if(turnNumber == 0)
        {
            GAME_LOGGER.log(Level.forName("TURN", 401), "First Turn! Player1 starts. " + turnNumber);
            turnNumber++;
            return turnNumber;
        } else {
            currentPlayer = getPlayer(currentTurnsPlayer());
            GAME_LOGGER.log(Level.forName("TURN", 401), "Turn number: " + turnNumber);
            // Implement game logic for each turn here
            
            // Process player turns
            //player1.processTurn();
            //player2.processTurn();
            if (plane.getBase1().isDead() || plane.getBase2().isDead()) {
                endGame();
            }
            // Update game state on the plane
            //Plane.updateState();
            for(Mushroom_Class m : plane.MushroomCollection) {
                if(m.get_Owner() == currentPlayer) {
                    m.generate_Income();
                }
            }
            
            turnNumber++;
            return turnNumber;
        }
    }

    public void endGame() { //függvény meghívódik a Tecton_Base isDeadTrue() meghívódik
        GAME_LOGGER.log(Level.forName("END_GAME", 402), "Game ended.");

        // Calculate scores, determine winner, etc.
        
        if(player1.getScore() > player2.getScore()) {
            GAME_LOGGER.log(Level.forName("WINNER", 401), "Player 1 wins!");
        } else if(player2.getScore() > player1.getScore()) {
            GAME_LOGGER.log(Level.forName("WINNER", 401), "Player 2 wins!");
        } else {
            GAME_LOGGER.log(Level.forName("WINNER", 401), "It's a draw!");
        }
    }
}
