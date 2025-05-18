package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game { // --- Pálya létrehozás , pontok kiosztása, kiértékelés , játék kezdete, Spóra kiosztás, játék vége ---
    private static final Logger GAME_LOGGER = LogManager.getLogger(Game.class);

    private final Player player1;
    private final Player player2;
    private int turnNumber;
    private final Plane plane; // A játékhoz tartozó pálya 
    private boolean gameOver; // Játék vége állapot

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
        
        // Randomly assign roles to players at the start of the game
        assignRandomRoles();
        
        // Start the first turn
        turnNumber++;
    }
    
    /**
     * Randomly assigns Mushroom and Insect roles to the two players.
     * This ensures that one player gets the Mushroom role and the other gets the Insect role.
     */
    private void assignRandomRoles() {
        // Use Math.random() to decide which player gets which role
        if (Math.random() < 0.5) {
            // Player 1 gets Mushroom, Player 2 gets Insect
            player1.setRoleMushroom();
            player2.setRoleInsect();
            GAME_LOGGER.log(Level.forName("ROLE", 401), 
                "Random roles assigned: Player 1 ({}) is Mushroom, Player 2 ({}) is Insect", 
                player1.getName(), player2.getName());
        } else {
            // Player 1 gets Insect, Player 2 gets Mushroom
            player1.setRoleInsect();
            player2.setRoleMushroom();
            GAME_LOGGER.log(Level.forName("ROLE", 401), 
                "Random roles assigned: Player 1 ({}) is Insect, Player 2 ({}) is Mushroom", 
                player1.getName(), player2.getName());
        }
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
    /* 
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }*/

    public void turn() { //Ez majd void lesz, csak meg _Tests miatt int
        Player currentPlayer;
        if(turnNumber % 2 == 1) {
            GAME_LOGGER.log(Level.forName("TURN", 401), "Player 1's turn: " + turnNumber);
        } else {
            GAME_LOGGER.log(Level.forName("TURN", 401), "Player 2's turn: " + turnNumber);
        }
        currentPlayer = getPlayer(currentTurnsPlayer());
        
        // Put all tectons in batch mode to reduce event spam during the full turn simulation
        for (Tecton_Class tecton : plane.TectonCollection) {
            tecton.beginBatch();
        }
        
        // Process player turns
        
        if (plane.getBase1().isDead() || plane.getBase2().isDead()) {
            endGame();
        }
    
        
        // Insect attacks on mushrooms
        for(Insect_Class ins : new java.util.ArrayList<>(player1.getGame().getPlane().InsectCollection)) {
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

        turnSimulation(); 
        turnNumber++;
        currentPlayer.endTurn();
        
        // End batch mode for all tectons and let their final states propagate events
        for (Tecton_Class tecton : plane.TectonCollection) {
            tecton.endBatch();
        }

        // Log all existing threads' IDs individually
        for (Thread_Class th : plane.ThreadCollection) {
            GAME_LOGGER.log(Level.INFO, "Existing thread id: " + th.get_ID());
            GAME_LOGGER.log(Level.INFO, "Existing thread id: " + th.getTectonID());
        }
    
    }

    private void turnSimulation() {
        // Implement automated turn logic here
        GAME_LOGGER.log(Level.forName("AUTO_TURN", 401), "Automated turn logic executed.");
        
        
       
    }

    public void endGame() { //függvény meghívódik a Tecton_Base isDeadTrue() meghívódik
        GAME_LOGGER.log(Level.forName("END_GAME", 402), "Game ended.");
        setGameOver(true);
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
