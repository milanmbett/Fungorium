package com.coderunnerlovagjai.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coderunnerlovagjai.app.util.LeaderboardManager;

public class Game { // --- Pálya létrehozás , pontok kiosztása, kiértékelés , játék kezdete, Spóra kiosztás, játék vége ---
    private static final Logger GAME_LOGGER = LogManager.getLogger(Game.class);

    private final Player player1;
    private final Player player2;
    private int turnNumber;
    private final Plane plane; // A játékhoz tartozó pálya 
    private boolean gameOver; // Játék vége állapot
    private final int maxTurns = 50; //TODO Maximum körök száma DEBUG miatt 3
    private boolean baseDestroyed = false;

    public Game(String player1Name, String player2Name) {
        this.player1 = new Player(1, player1Name);
        this.player2 = new Player(2, player2Name);
        this.turnNumber = 0;        
        // Inicializáljuk a Plane-t, a bázisok inicializálása átkerült a külön initGame() metódusba,
        // hogy elkerüljük az "this" szivárgását a konstruktorban.
        this.plane = new Plane();

        GAME_LOGGER.log(Level.forName("INIT", 402), "Game initialized with two players.");
    }

    public Plane getPlane() {
        return plane;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    public void initGame() {
        player1.setGame(this);  
        player2.setGame(this); 
        player1.setRoleMushroom();
        player2.setRoleInsect();
        // A bázisok inicializálása külön metódusban történik
        plane.initBases(player1, player2, this);
        GAME_LOGGER.log(Level.forName("INIT", 402), "Game initialized with two players and their bases.");
        //Tectonok inicializálása
        plane.init_Plane(this);
        //Thread die_thread
        //Thread init
        //die_Spore
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
        //this.turn();
        turnNumber++;
    }

    public Player getPlayer(int id) {
        if (id == player1.getId()) return player1;
        if (id == player2.getId()) return player2;
        return null;
    }

    public Player getPlayer1() {
        return player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public int getCurrentTurn() {
        return turnNumber;
    }

    public int getMaxTurns() {
        return maxTurns;
    }
    
    /* 
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }*/
    

    public void turn() { //Ez majd void lesz, csak meg _Tests miatt int
        
        if(turnNumber >= maxTurns) 
        {
            GAME_LOGGER.log(Level.forName("MAX_TURNS", 401), "Maximum turns reached. Ending game.");
            endGame();
            return;
        }
        if(plane.getBase1().mushroom.isDead() || plane.getBase2().mushroom.isDead()) 
        {
            GAME_LOGGER.log(Level.forName("BASE_DEAD", 401), "One of the bases is dead. Ending game.");
            endGame();
            return;
        }
        
        turnNumber++; // Move this to the start!
        Player currentPlayer;
        if(turnNumber % 2 == 1) {
            GAME_LOGGER.log(Level.forName("TURN", 401), "Player 1's turn: " + turnNumber);
            player1.setRoleMushroom();
            player2.setRoleNull();
        } else {
            GAME_LOGGER.log(Level.forName("TURN", 401), "Player 2's turn: " + turnNumber);
            player2.setRoleInsect();
            player1.setRoleNull();
        }
        currentPlayer = getPlayer(currentTurnsPlayer());
        // Implement game logic for each turn here
        
        // Process player turns
        
        if (plane.getBase1().isDead() || plane.getBase2().isDead()) {
            endGame();
        }
    
        
        // Insect attacks on mushrooms
        for(Insect_Class ins : plane.InsectCollection) {
            ins.attack_Mushroom(ins.get_Tecton().get_Mushroom());
        }
        
        // Mushroom attacks insects and generates income and spawns spores
        for(Mushroom_Class mush : plane.MushroomCollection) {
            if(mush.get_Owner() == currentPlayer) {
                mush.generate_Income();
                mush.attack_Insects();
                mush.spawn_Spores(new Basic_Spore(mush.get_Tecton(), currentPlayer)); // Spawn spores on the mushroom's tecton
            }
        }
        // Reset available steps for insects
        for(Insect_Class ins: plane.InsectCollection) {
            if(ins.get_availableSteps() <= 0) {
                ins.set_availableSteps(ins.get_maxSteps());
            }
        }
        

        
        // Thread expansion and eating
        for(Thread_Class th : new java.util.ArrayList<>(player1.getGame().getPlane().ThreadCollection)) {
            th.expand_Thread();
            th.tryToEat_Insect();
        }

        // Insects eat spores
        for(Insect_Class ins : new java.util.ArrayList<>(player1.getGame().getPlane().InsectCollection)) {
            for(Basic_Spore sp : new java.util.ArrayList<>(player1.getGame().getPlane().SporeCollection)) {
                if(ins.get_Tecton().equals(sp.get_Tecton()) && ins.get_Owner() == currentPlayer) {
                    ins.eat_Spore(sp);
                }
            }
        }

            player1.getRole().onTurn(player1);
    player2.getRole().onTurn(player2);
        turnSimulation(); 
        currentPlayer.endTurn();
        

        // Log all existing threads' IDs individually
        for (Thread_Class th : plane.ThreadCollection) {
            GAME_LOGGER.log(Level.INFO, "Existing thread id: " + th.get_ID());
            GAME_LOGGER.log(Level.INFO, "Existing thread id: " + th.getTectonID());
        }
    }

   private void turnSimulation() 
   {
        

            List<Thread_Class> threads = new ArrayList<>(plane.ThreadCollection);
            for (Thread_Class t : threads) 
            {
                t.expand_Thread();
            }
        GAME_LOGGER.log(Level.forName("AUTO_TURN", 401), "Automated turn logic executed.");
             
    }

public void endGame() {
    // Prevent duplicate calls
    if (gameOver) {
        return;  // Game is already over, don't process again
    }
    
    GAME_LOGGER.log(Level.forName("END_GAME", 402), "Game ended.");
    setGameOver(true);
    
    // Check if enemy bases were destroyed (not own bases)
    boolean player1DestroyedEnemyBase = player2.getGame().getPlane().getBase2().isDead();
    boolean player2DestroyedEnemyBase = player1.getGame().getPlane().getBase1().isDead();
    
    // Save winner's score to leaderboard
    if (player1.getScore() > player2.getScore()) {
        GAME_LOGGER.log(Level.forName("WINNER", 401), "Player 1 wins!");
        LeaderboardManager.save(
            player1.getName(), 
            player1.getScore(), 
            player1DestroyedEnemyBase  // Fixed: Check if player1 destroyed player2's base
        );
    } else if (player2.getScore() > player1.getScore()) {
        GAME_LOGGER.log(Level.forName("WINNER", 401), "Player 2 wins!");
        LeaderboardManager.save(
            player2.getName(), 
            player2.getScore(), 
            player2DestroyedEnemyBase  // Fixed: Check if player2 destroyed player1's base
        );
    } else {
        GAME_LOGGER.log(Level.forName("WINNER", 401), "It's a draw!");
        // In case of a draw, save both players' scores
        // Neither player destroyed the enemy base in a draw
        LeaderboardManager.save(player1.getName(), player1.getScore(), player1DestroyedEnemyBase);
        LeaderboardManager.save(player2.getName(), player2.getScore(), player2DestroyedEnemyBase);
    }
    getPlane().clearAllCollections(); // Clear all collections
}
    
}
