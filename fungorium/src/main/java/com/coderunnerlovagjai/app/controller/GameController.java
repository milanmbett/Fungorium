// filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/main/java/com/coderunnerlovagjai/app/controller/GameController.java
package com.coderunnerlovagjai.app.controller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.GameCanvasFrame;
import com.coderunnerlovagjai.app.Insect_Buggernaut;
import com.coderunnerlovagjai.app.Insect_Class;
import com.coderunnerlovagjai.app.Insect_ShroomReaper;
import com.coderunnerlovagjai.app.Mushroom_Class;
import com.coderunnerlovagjai.app.Mushroom_Maximus;
import com.coderunnerlovagjai.app.Mushroom_Shroomlet;
import com.coderunnerlovagjai.app.Mushroom_Slender;
import com.coderunnerlovagjai.app.Plane;
import com.coderunnerlovagjai.app.Player;
import com.coderunnerlovagjai.app.RoleType;
import com.coderunnerlovagjai.app.Tecton_Class;
import com.coderunnerlovagjai.app.view.ViewFactory;

/**
 * Controller class that handles the game logic and connects the model and view.
 * This implements the Controller component in the MVC pattern.
 */
public class GameController {
    private static final Logger CONTROLLER_LOGGER = LogManager.getLogger(GameController.class);
    private static final String INSECT_ROLE_ERROR = "Current player doesn't have Insect role!";
    private static final String MUSHROOM_ROLE_ERROR = "Current player doesn't have Mushroom role!";
    
    private final Game gameModel;
    
    public GameController(Game game) {
        this.gameModel = game;
    }
    
    /**
     * Initialize and display the game view.
     */
    public void startGame() {
        // Initialize the view using player names from the game model
        Player p1 = gameModel.getPlayer1();
        Player p2 = gameModel.getPlayer2();
        
        // Create the view - this will be displayed automatically
        new GameCanvasFrame(p1.getName(), p2.getName());
        
        // Initialize game views
        initializeViews();
        
        // Start the actual game
        gameModel.startGame();
    }
    
    /**
     * Initialize all entity views in the game.
     */
    private void initializeViews() {
        Plane plane = gameModel.getPlane();
        ViewFactory.createViewsForPlane(plane);
    }
    
    /**
     * Get the current player based on turn number.
     * @return The player whose turn it is
     */
    private Player getCurrentPlayer() {
        int currentPlayerId = gameModel.currentTurnsPlayer();
        return gameModel.getPlayer(currentPlayerId);
    }
    
    /**
     * Place a mushroom on the game board.
     * @param mushroomType The type of mushroom to place
     * @param targetTecton The tecton to place the mushroom on
     */
    public void placeMushroom(String mushroomType, Tecton_Class targetTecton) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getRole() != RoleType.MUSHROOM) {
            CONTROLLER_LOGGER.log(Level.INFO, MUSHROOM_ROLE_ERROR);
            return;
        }
        
        // Check if player has enough action points
        if (currentPlayer.getAction() <= 0) {
            CONTROLLER_LOGGER.log(Level.INFO, "No action points left!");
            return;
        }
        
        Mushroom_Class mushroom = null;
        
        // Create the appropriate mushroom type
        switch (mushroomType) {
            case "Shroomlet" -> mushroom = new Mushroom_Shroomlet(targetTecton, currentPlayer);
            case "Slender" -> mushroom = new Mushroom_Slender(targetTecton, currentPlayer);
            case "Maximus" -> mushroom = new Mushroom_Maximus(targetTecton, currentPlayer);
            default -> {
                CONTROLLER_LOGGER.log(Level.INFO, "Invalid mushroom type: " + mushroomType);
                return;
            }
        }
        
        // Place the mushroom using the plane
        gameModel.getPlane().place_Mushroom(mushroom, targetTecton);
        
        // Reduce action points
        currentPlayer.setAction(currentPlayer.getAction() - 1);
        CONTROLLER_LOGGER.log(Level.INFO, "Action points reduced to: " + currentPlayer.getAction());
        
        // Request a refresh of the views
        refreshViews();
    }
    
    /**
     * Place an insect on the game board.
     * @param insectType The type of insect to place
     * @param targetTecton The tecton to place the insect on
     */
    public void placeInsect(String insectType, Tecton_Class targetTecton) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getRole() != RoleType.INSECT) {
            CONTROLLER_LOGGER.log(Level.INFO, INSECT_ROLE_ERROR);
            return;
        }
        
        // Check if player has enough action points
        if (currentPlayer.getAction() <= 0) {
            CONTROLLER_LOGGER.log(Level.INFO, "No action points left!");
            return;
        }
        
        Insect_Class insect = null;
        
        // Create the appropriate insect type
        switch (insectType) {
            case "Buggernaut" -> insect = new Insect_Buggernaut(targetTecton, currentPlayer);
            case "ShroomReaper" -> insect = new Insect_ShroomReaper(targetTecton, currentPlayer);
            default -> {
                CONTROLLER_LOGGER.log(Level.INFO, "Invalid insect type: " + insectType);
                return;
            }
        }
        
        // Place the insect using the plane
        gameModel.getPlane().placeInsect(insect, targetTecton);
        
        // Reduce action points
        currentPlayer.setAction(currentPlayer.getAction() - 1);
        CONTROLLER_LOGGER.log(Level.INFO, "Action points reduced to: " + currentPlayer.getAction());
        
        // Request a refresh of the views
        refreshViews();
    }
    
    /**
     * Move an insect on the game board.
     * @param insect The insect to move
     * @param targetTecton The tecton to move the insect to
     */
    public void moveInsect(Insect_Class insect, Tecton_Class targetTecton) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getRole() != RoleType.INSECT) {
            CONTROLLER_LOGGER.log(Level.INFO, INSECT_ROLE_ERROR);
            return;
        }
        
        // Check if player has enough action points
        if (currentPlayer.getAction() <= 0) {
            CONTROLLER_LOGGER.log(Level.INFO, "No action points left!");
            return;
        }
        
        // Store initial location to detect if move is successful
        Tecton_Class initialTecton = insect.get_Tecton();
        
        // Move the insect using the plane
        gameModel.getPlane().move_Insect(currentPlayer, insect, targetTecton);
        
        // Check if the insect actually moved by comparing locations
        if (insect.get_Tecton() != initialTecton && insect.get_Tecton() == targetTecton) {
            // Reduce action points only if the move was successful
            currentPlayer.setAction(currentPlayer.getAction() - 1);
            CONTROLLER_LOGGER.log(Level.INFO, "Action points reduced to: " + currentPlayer.getAction());
        }
        
        // Request a refresh of the views
        refreshViews();
    }
    
    /**
     * Attack a mushroom with an insect.
     * @param insect The attacking insect
     * @param mushroom The mushroom to attack
     */
    public void attackMushroom(Insect_Class insect, Mushroom_Class mushroom) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getRole() != RoleType.INSECT) {
            CONTROLLER_LOGGER.log(Level.INFO, INSECT_ROLE_ERROR);
            return;
        }
        
        // Check if player has enough action points
        if (currentPlayer.getAction() <= 0) {
            CONTROLLER_LOGGER.log(Level.INFO, "No action points left!");
            return;
        }
        
        // Track the initial HP of the mushroom
        int initialHP = mushroom.get_hp();
        
        // Attack the mushroom directly with the insect
        insect.attack_Mushroom(mushroom);
        
        // Check if the mushroom was actually damaged
        if (mushroom.get_hp() < initialHP) {
            // Reduce action points only if attack was successful
            currentPlayer.setAction(currentPlayer.getAction() - 1);
            CONTROLLER_LOGGER.log(Level.INFO, "Action points reduced to: " + currentPlayer.getAction());
        }
        
        // Request a refresh of the views
        refreshViews();
    }
    
    /**
     * End the current player's turn and switch roles automatically.
     */
    public void endTurn() {
        // Log the current state of the game for debugging
        CONTROLLER_LOGGER.log(Level.INFO, "TURN END - Turn number: {}", gameModel.getTurnNumber());
        CONTROLLER_LOGGER.log(Level.INFO, "TectonCollection size BEFORE gameModel.turn(): {}", gameModel.getPlane().TectonCollection.size());

        // Get both players before doing anything
        Player player1 = gameModel.getPlayer1();
        Player player2 = gameModel.getPlayer2();
        
        // Store current player ID before ending turn
        int currentPlayerID = gameModel.currentTurnsPlayer();
        Player currentPlayer = gameModel.getPlayer(currentPlayerID);
        
        // Determine the next player who will take a turn after this
        Player nextPlayer = (currentPlayer == player1) ? player2 : player1;
        
        // Store both players' roles BEFORE doing anything
        RoleType role1Before = player1.getRole();
        RoleType role2Before = player2.getRole();
        
        CONTROLLER_LOGGER.log(Level.INFO, 
            "DETAILED STATE - Current player ID: {}, Player 1 ID: {}, Player 2 ID: {}", 
            currentPlayerID, player1.getId(), player2.getId());
        
        // Log the turn end with player info
        CONTROLLER_LOGGER.log(Level.INFO, 
            "Ending turn for Player {} with role {}", 
            currentPlayer.getId(), currentPlayer.getRole().getRoleName());
        
        // Log initial roles for debugging
        CONTROLLER_LOGGER.log(Level.INFO, "BEFORE TURN END - Player 1 role: {}, Player 2 role: {}", 
            role1Before.getRoleName(), role2Before.getRoleName());
        
        // Make sure we have valid roles before swapping
        if (role1Before == RoleType.NONE || role2Before == RoleType.NONE) {
            CONTROLLER_LOGGER.log(Level.WARN, "Invalid roles detected, setting defaults before swap");
            if (currentPlayer == player1) {
                // If player 1 is current, assume they're Mushroom and player 2 is Insect
                role1Before = RoleType.MUSHROOM;
                role2Before = RoleType.INSECT;
            } else {
                // If player 2 is current, assume they're Mushroom and player 1 is Insect
                role1Before = RoleType.INSECT;
                role2Before = RoleType.MUSHROOM;
            }
        }
        
        // Advance the turn (this increments turnNumber and calls Player.endTurn())
        // Player.endTurn() just sets action points to 0 but doesn't change roles
        gameModel.turn();
        
        CONTROLLER_LOGGER.log(Level.INFO, "TectonCollection size AFTER gameModel.turn(): {}", gameModel.getPlane().TectonCollection.size());

        // Log the turn number after advancing
        CONTROLLER_LOGGER.log(Level.INFO, "Turn number after advancing: {}", gameModel.getTurnNumber());
        
        // Check if the current player changed
        int newCurrentPlayerID = gameModel.currentTurnsPlayer();
        CONTROLLER_LOGGER.log(Level.INFO, "New current player ID: {}", newCurrentPlayerID);
        
        // Log player roles after advancing turn but before manual swap
        CONTROLLER_LOGGER.log(Level.INFO, "After turn() but before manual swap - Player 1 role: {}, Player 2 role: {}", 
            player1.getRole().getRoleName(), player2.getRole().getRoleName());
        
        // Now DO THE ACTUAL ROLE SWAP directly between players
        // This is the critical part: explicitly set the new roles based on previous roles
        if (role1Before == RoleType.MUSHROOM && role2Before == RoleType.INSECT) {
            player1.setRoleInsect();
            player2.setRoleMushroom();
            CONTROLLER_LOGGER.log(Level.INFO, "DIRECT ROLE SWAP: Player 1 now Insect, Player 2 now Mushroom");
        } else if (role1Before == RoleType.INSECT && role2Before == RoleType.MUSHROOM) {
            player1.setRoleMushroom();
            player2.setRoleInsect();
            CONTROLLER_LOGGER.log(Level.INFO, "DIRECT ROLE SWAP: Player 1 now Mushroom, Player 2 now Insect");
        } else {
            // Fallback in case of errors
            CONTROLLER_LOGGER.log(Level.ERROR, "ROLE SWAP FAILED! Using fallback roles");
            player1.setRoleMushroom();
            player2.setRoleInsect();
        }
        
        // Verify the new roles after swap
        CONTROLLER_LOGGER.log(Level.INFO, "AFTER TURN END - Player 1 role: {}, Player 2 role: {}", 
            player1.getRole().getRoleName(), player2.getRole().getRoleName());
        
        // Verify the new current player has the right number of action points
        Player newCurrentPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        CONTROLLER_LOGGER.log(Level.INFO, "New current player: {} with role: {} and {} action points", 
            newCurrentPlayer.getId(), 
            newCurrentPlayer.getRole().getRoleName(),
            newCurrentPlayer.getAction());
        
        // Request a refresh of the views to update UI
        refreshViews();
        
        CONTROLLER_LOGGER.log(Level.INFO, "TectonCollection size BEFORE refreshViews(): {}", gameModel.getPlane().TectonCollection.size());
        CONTROLLER_LOGGER.log(Level.INFO, "Turn ended successfully and roles switched");
    }
    
    /**
     * Refresh all views to reflect current game state.
     */
    private void refreshViews() {
        // Notify ViewModels that models have changed
        com.coderunnerlovagjai.app.viewmodel.ViewModelFactory.refreshAllViewModels();
        
        // Create entity views (if needed)
        ViewFactory.createViewsForPlane(gameModel.getPlane());
        
        // Request repaint of the game canvas
        GameCanvas.getInstance().repaint();
    }
}
