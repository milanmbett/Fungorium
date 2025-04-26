package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Slender extends Mushroom_Class 
{
    private static final Logger MUSHROOM_SLENDER_LOGGER = LogManager.getLogger(Mushroom_Slender.class);
    public Mushroom_Slender(Tecton_Class targetTecton, Player p)
    {
        hp = 320;
        power = 32;
        sporeCount = 0;
        tecton = targetTecton;
        tecton.set_Mushroom(this);
        owner = p;
        ID = "Mushroom_Slender" + Integer.toString(Plane.MushroomCollection.size());
        cost=30;
        MUSHROOM_SLENDER_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Slender Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.MushroomCollection.add(this);
        MUSHROOM_SLENDER_LOGGER.log(Level.forName("ADD", 403), "Mushroom_Slender: "+ID+ " added to MushroomCollection! MushroomCollection size: " + Plane.MushroomCollection.size());

    }

    @Override
    public int getIncomeMultiplier() {
        return 10; // Például a Mushroom_Shroomlet 10-szeres szorzót ad
    }
}
