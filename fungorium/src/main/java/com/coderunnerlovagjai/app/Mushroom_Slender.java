package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Slender extends Mushroom_Class 
{
    private static final Logger MUSHROOM_SLENDER_LOGGER = LogManager.getLogger(Mushroom_Slender.class);
    public Mushroom_Slender(Tecton_Class targetTecton, Player p)
    {
        if((targetTecton.get_Thread()== null))
        {
            MUSHROOM_SLENDER_LOGGER.log(Level.forName("ERROR", 404), "Target Cannot has no Thread");
            return;
        }
        hp = 320;
        power = 32;
        sporeCount = 0;
        tecton = targetTecton;
        //tecton.set_Mushroom(this);
        owner = p;
        ID = "Mushroom_Slender" + p.getGame().getPlane().MushroomCollection.size();
        cost=30;
        addScore(5);
        MUSHROOM_SLENDER_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Slender Created! ID: " + ID + " on Tecton: " + targetTecton.get_ID());
        
    }

    @Override
    public int getIncomeMultiplier() {
        return 10; // Például a Mushroom_Shroomlet 10-szeres szorzót ad
    }
}
