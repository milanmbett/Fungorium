package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Role_Mushroom implements Role //TODO: Megírás
{
    private static final Logger ROLE_MUSHROOM_LOGGER = LogManager.getLogger(Role_Mushroom.class);

    private Player owner;
    private int action;

    public Role_Mushroom(Player player) {
        this.owner = player;
        action = 3;
        ROLE_MUSHROOM_LOGGER.log(Level.forName("CREATE", 401), "Mushroom role created.");
    }

    public void on_turn() {
        ROLE_MUSHROOM_LOGGER.log(Level.forName("ON_TURN", 402), "Mushroom role's turn started.");
    
    }

    public String getRoleName() {
        return "Mushroom";
    }
    public void resetRole(Player player) {
        if (player != owner) {
            ROLE_MUSHROOM_LOGGER.log(Level.forName("ERROR", 404), "Player does not own this role!");
            return;
        }
        if(player == null) {
            ROLE_MUSHROOM_LOGGER.log(Level.forName("NULL", 201), "Player is null!");
            return;
        }
        player.setRoleNull(); // Reset the role for the player
        ROLE_MUSHROOM_LOGGER.log(Level.forName("RESET_ROLE", 402), "Mushroom role has been reset.");
    }

    public void place_Mushroom(Mushroom_Class m, Tecton_Class targetTecton) {
        if(m == null || targetTecton == null) {
            ROLE_MUSHROOM_LOGGER.log(Level.forName("NULL", 201), "Mushroom or target tecton is null!");
            return;
        }
        ROLE_MUSHROOM_LOGGER.log(Level.forName("PLACE_MUSHROOM", 402), "Mushroom placed.");
    }

    public void upgrade_Mushroom(Mushroom_Class m) {
        if(m == null) {
            ROLE_MUSHROOM_LOGGER.log(Level.forName("NULL", 201), "Mushroom is null!");
            return;
        }
        ROLE_MUSHROOM_LOGGER.log(Level.forName("UPGRADE_MUSHROOM", 402), "Mushroom upgraded.");
    }

    public void double_Money() {
        ROLE_MUSHROOM_LOGGER.log(Level.forName("DOUBLE_MONEY", 402), "Money doubled.");
    }


}
