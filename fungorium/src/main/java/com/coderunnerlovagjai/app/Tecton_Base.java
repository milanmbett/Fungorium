package com.coderunnerlovagjai.app;
import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tecton_Base extends Tecton_Class //A főbázis ahol a játékosok kezdenek.
{
    private Player owner;
    private boolean isDead = false; //TODO: Valójában ez nem feltétlen kellhet, mert a setDeadTrue enélkül is hívható
    private Game game;

    private static final Logger TECTON_BASE_LOGGER = LogManager.getLogger(Tecton_Base.class);

    public Tecton_Base(Player p, Game game)
    {
        this.game = game;
        mushroom = new Mushroom_Grand(this,p);
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        owner = p;
        ID = "Tecton_Base" + Integer.toString(Plane.TectonCollection.size());
        TECTON_BASE_LOGGER.log(Level.forName("CREATE",401),"Tecton_Base Created! ID: " + ID);
        Plane.TectonCollection.add(this);
        TECTON_BASE_LOGGER.log(Level.forName("ADD", 403), "Tecton_Base: "+ID+ " added to TectonCollection! TectonCollection size: " + Plane.TectonCollection.size());
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
        if (p != Game.player1 && p != Game.player2) { // Ellenőrzés, hogy érvényes játékos-e
            TECTON_BASE_LOGGER.log(Level.forName("ERROR", 404), "Invalid player!");
            return;
        }
        owner = p;
    }

    @Override
    public boolean isDead() {
        return true; 
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