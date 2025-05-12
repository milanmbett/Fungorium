// filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/main/java/com/coderunnerlovagjai/app/controller/GameController.java
package com.coderunnerlovagjai.app.controller;

import com.coderunnerlovagjai.app.*;
import com.coderunnerlovagjai.app.view.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        
        // Move the insect using the plane
        gameModel.getPlane().move_Insect(currentPlayer, insect, targetTecton);
        
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
        
        // Attack the mushroom directly with the insect
        insect.attack_Mushroom(mushroom);
        
        // Request a refresh of the views
        refreshViews();
    }
    
    /**
     * End the current player's turn.
     */
    public void endTurn() {
        // Call the game's turn method to process end-of-turn events
        gameModel.turn();
        
        // Request a refresh of the views
        refreshViews();
    }
    
    /**
     * Refresh all views to reflect current game state.
     */
    private void refreshViews() {
        // Clear and recreate all entity views
        ViewFactory.createViewsForPlane(gameModel.getPlane());
        
        // Request repaint of the game canvas
        GameCanvas.getInstance().repaint();
    }
}
