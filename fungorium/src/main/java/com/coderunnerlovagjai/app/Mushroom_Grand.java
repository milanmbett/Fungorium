package com.coderunnerlovagjai.app;

import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Mushroom_Grand extends Mushroom_Class 
{
    private static final Logger MUSHROOM_GRAND_LOGGER = LogManager.getLogger(Mushroom_Grand.class);
    
    private Game game;
    
    public Mushroom_Grand(Tecton_Class targetTecton,Player p)
    {
        if (!(targetTecton instanceof Tecton_Base)) 
        {
            MUSHROOM_GRAND_LOGGER.log(Level.forName("ERROR", 404), "Target Cannot place on Tecton_Base");
            return;
        }
        hp = 500;
        power = 50;
        sporeCount = 0;
        tecton = targetTecton;
        //tecton.set_Mushroom(this);
        owner = p;
        ID = "Mushroom_Grand" + p.getGame().getPlane().MushroomCollection.size();
        MUSHROOM_GRAND_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Grand Created! ID: " + ID + " on Tecton: " + targetTecton.get_ID());
        addScore(5);

       
    }

    @Override
    public int getIncomeMultiplier() {
        return 15; // Például a Mushroom_Grand 15-szörös szorzót ad
    }

    public void die() {
        MUSHROOM_GRAND_LOGGER.log(Level.forName("DEAD", 401), "Mushroom_Grand is dead! ID: " + ID);
        owner.getGame().getPlane().MushroomCollection.remove(this);

        // Értesítjük a Game osztályt a halálról
        if (game != null) {
            game.endGame();
        }
    }
    @Override
    public void spawn_Spores(Basic_Spore spore)
    {
        Random rnd = new Random();
        
        for (Tecton_Class t : tecton.get_TectonNeighbours()) {
            if (rnd.nextInt(100)<20&&!t.isDead()) { // 20% chance
                owner.getGame().getPlane().SporeCollection.add(spore);
            }
        }
    
        // Also possibly spawn on the same tile
        if (rnd.nextInt(100)<20&&!tecton.isDead()) { // 20% chance
            owner.getGame().getPlane().SporeCollection.add(spore);
        }
    } 
}
