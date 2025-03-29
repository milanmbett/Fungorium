package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Slender extends Mushroom_Class 
{
    private static final Logger MUSHROOM_SLENDER_LOGGER = LogManager.getLogger(Mushroom_Slender.class);
    public Mushroom_Slender(Tecton_Class targetTecton, Player p)
    {
        hp = 250; //TODO: Értékét még meg kell beszélni
        power = 25; //TODO: Értékét még meg kell beszélni
        sporeCount = 0;
        tecton = targetTecton;
        owner = p;
        ID = "Mushroom_Slender" + Integer.toString(Plane.MushroomCollection.size());
        MUSHROOM_SLENDER_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Slender Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.MushroomCollection.add(this);
        MUSHROOM_SLENDER_LOGGER.log(Level.forName("ADD", 403), "Mushroom_Slender: "+ID+ " added to MushroomCollection! MushroomCollection size: " + Plane.MushroomCollection.size());

    }
}
