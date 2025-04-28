package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enum that represents the possible roles a player can have in the game.
 */
public enum RoleType {
    MUSHROOM("Mushroom"),
    INSECT("Insect"),
    NONE("None");

    private static final Logger ROLE_LOGGER = LogManager.getLogger(RoleType.class);
    private final String roleName;

    RoleType(String roleName) {
        this.roleName = roleName;
    }

    /**
     * Returns the name of the role.
     * @return the name of the role
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Executes actions at the beginning of a turn.
     * @param player the player who has this role
     */
    public void onTurn(Player player) {
        switch (this) {
            case MUSHROOM:
                ROLE_LOGGER.log(Level.forName("ON_TURN", 402), "Mushroom role's turn started.");
                // Mushroom-specific turn actions
                break;
            case INSECT:
                ROLE_LOGGER.log(Level.forName("ON_TURN", 402), "Insect role's turn started.");
                // Insect-specific turn actions
                break;
            case NONE:
                ROLE_LOGGER.log(Level.forName("ON_TURN", 402), "No role's turn started.");
                break;
        }
    }

    /**
     * Validates if a mushroom can be placed on a tecton.
     * @param player the player attempting to place the mushroom
     * @param mushroom the mushroom to place
     * @param tecton the target tecton
     * @return true if the mushroom can be placed, false otherwise
     */
    public boolean validateAndPlaceMushroom(Player player, Mushroom_Class mushroom, Tecton_Class tecton) {
        if (this != MUSHROOM) {
            ROLE_LOGGER.log(Level.forName("ERROR", 404), "Player does not have Mushroom role!");
            return false;
        }
        
        // Mushroom placement validation logic
        if (mushroom == null || tecton == null) {
            ROLE_LOGGER.log(Level.forName("NULL", 201), "Mushroom or target tecton is null!");
            return false;
        }
        
        // More validation logic could be added here
        
        ROLE_LOGGER.log(Level.forName("PLACE_MUSHROOM", 402), "Mushroom placed.");
        return true;
    }

    /**
     * Validates if an insect can move to a tecton.
     * @param player the player attempting to move the insect
     * @param insect the insect to move
     * @param tecton the target tecton
     * @return true if the insect can move, false otherwise
     */
    public boolean validateAndMoveInsect(Player player, Insect_Class insect, Tecton_Class tecton) {
        if (this != INSECT) {
            ROLE_LOGGER.log(Level.forName("ERROR", 404), "Player does not have Insect role!");
            return false;
        }
        
        // Insect movement validation logic
        if (insect == null || tecton == null) {
            ROLE_LOGGER.log(Level.forName("NULL", 201), "Insect or target tecton is null!");
            return false;
        }
        
        // Check if the player owns the insect
        if (insect.get_Owner() != player) {
            ROLE_LOGGER.log(Level.forName("ERROR", 404), "Player does not own this insect!");
            return false;
        }
        
        // Check if the target tecton is a neighbor
        if (!insect.get_Tecton().get_TectonNeighbours().contains(tecton)) {
            ROLE_LOGGER.log(Level.forName("ERROR", 404), "Target tecton is not a neighbor!");
            return false;
        }
        
        // Check if the target tecton is not dead
        if (tecton.isDead()) {
            ROLE_LOGGER.log(Level.forName("ERROR", 404), "Cannot move to dead tecton!");
            return false;
        }
        
        // Execute the move
        insect.move_Insect(tecton);
        ROLE_LOGGER.log(Level.forName("MOVE", 401), "Insect moved successfully.");
        return true;
    }
}