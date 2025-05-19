package com.coderunnerlovagjai.app;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tecton_Base extends Tecton_Class //A főbázis ahol a játékosok kezdenek.
{
    private Player owner;
    private boolean isDead = false; 
    private Game game;

    private static final Logger TECTON_BASE_LOGGER = LogManager.getLogger(Tecton_Base.class);

    public Tecton_Base(Player p, Game game)
    {
        this.game = game;
        mushroom = new Mushroom_Grand(this,p);
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = new Thread_Class(this, game);
        owner = p;
        // Use the game instance's plane collection instead of static reference
        ID = "Tecton_Base";
        TECTON_BASE_LOGGER.log(Level.forName("CREATE",401),"Tecton_Base Created! ID: " + ID);
    }

    // Override the canBeCracked method to prevent Tecton_Base from being cracked
    @Override
    public boolean canBeCracked() {
        return false; // Base tectons cannot be cracked
    }

    public Player get_Owner()
    {
        return owner;
    }
    public void set_Owner(Player p)
    {
        if (p == null) {
            TECTON_BASE_LOGGER.log(Level.forName("NULL", 201), "Player is null!");
            return;
        }
        if (!p.equals(game.getPlayer1()) && !p.equals(game.getPlayer2())) { // Validate via game instance
            TECTON_BASE_LOGGER.log(Level.forName("ERROR", 404), "Invalid player!");
            return;
        }
        owner = p;
    }

    @Override
    public boolean isDead() {
        return this.isDead; 
    }

    public void setDeadTrue()
    {
        isDead = true;
        TECTON_BASE_LOGGER.log(Level.forName("DEAD", 401), "Tecton_Base is dead! ID: " + ID);
        
        if (game != null) {
            game.endGame();
        }
    }

}