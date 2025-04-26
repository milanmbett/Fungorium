package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Role_Insect implements Role //TODO: Megírás
{
    private static final Logger ROLE_INSECT_LOGGER = LogManager.getLogger(Role_Insect.class);
    private Player owner;
    private int action;

    public Role_Insect(Player player) {
        this.owner = player;
        action = 3;
        ROLE_INSECT_LOGGER.log(Level.forName("CREATE", 401), "Insect role created.");
    }
    
    @Override
    public void on_turn() {
        ROLE_INSECT_LOGGER.log(Level.forName("ON_TURN", 402), "Insect role's turn started.");
        //income meg ezeket itt kellene beállítani
    }

    public String getRoleName() {
        return "Insect";
    }

    public void choose_Insect(Insect_Class i) {
        if(i == null) {
            ROLE_INSECT_LOGGER.log(Level.forName("NULL", 201), "Insect is null!");
            return;
        }
        ROLE_INSECT_LOGGER.log(Level.forName("CHOOSE_INSECT", 402), "Insect chosen.");
    }

    public void place_Insect(Insect_Class i, Tecton_Class targetTecton) {
        if(i == null || targetTecton == null) {
            ROLE_INSECT_LOGGER.log(Level.forName("NULL", 201), "Insect or target tecton is null!");
            return;
        }
        if(action <= 0) {
            ROLE_INSECT_LOGGER.log(Level.forName("ERROR", 404), "No action points left!");
            return;
        }
        ROLE_INSECT_LOGGER.log(Level.forName("PLACE_INSECT", 402), "Insect placed.");
    }
    
    public boolean validateAndMoveInsect(Insect_Class insect, Tecton_Class targetTecton) {
        // ha az owner megegyezik
        if (insect.get_Owner() != owner) {
            ROLE_INSECT_LOGGER.log(Level.forName("ERROR", 404), "Player does not own this insect!");
            return false;
        }
        // ha szomszédos a targettecton
        if (!insect.get_Tecton().get_TectonNeighbours().contains(targetTecton)) {
            ROLE_INSECT_LOGGER.log(Level.forName("ERROR", 404), "Target tecton is not a neighbor!");
            return false;
        }
        // Ha nem halott tecton
        if (targetTecton instanceof Tecton_Dead) { //TODO: hiba
            ROLE_INSECT_LOGGER.log(Level.forName("ERROR", 404), "Cannot move to dead tecton!");
            return false;
        }

        // ha gomba van ott
        if (targetTecton.get_Mushroom() != null) {
            ROLE_INSECT_LOGGER.log(Level.forName("ERROR", 404), "Target tecton already has a mushroom!");
            return false;
        }
        
        // minden rendben -> mozgás végrehajtása
        insect.move_Insect(targetTecton);
        return true;
    }

    public void resetRole(Player player) {
        if (player != owner) {
            ROLE_INSECT_LOGGER.log(Level.forName("ERROR", 404), "Player does not own this role!");
            return;
        }
        if(player == null) {
            ROLE_INSECT_LOGGER.log(Level.forName("NULL", 201), "Player is null!");
            return;
        }
        player.setRoleNull();
        ROLE_INSECT_LOGGER.log(Level.forName("RESET", 401), "Insect role reset.");

    }



}
