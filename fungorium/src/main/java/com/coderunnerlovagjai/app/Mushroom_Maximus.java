package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Maximus extends Mushroom_Class 
{
    private static final Logger MUSHROOM_MAXIMUS_LOGGER = LogManager.getLogger(Mushroom_Maximus.class);
    public Mushroom_Maximus(Tecton_Class targetTecton,Player p)
    {
        hp = 250; //TODO: Értékét még meg kell beszélni
        power = 25; //TODO: Értékét még meg kell beszélni
        sporeCount = 0;
        tecton = targetTecton;
        owner = p;
        ID = "Mushroom_Maximus" + Integer.toString(Plane.MushroomCollection.size());
        MUSHROOM_MAXIMUS_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Maximus Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.MushroomCollection.add(this);
        MUSHROOM_MAXIMUS_LOGGER.log(Level.forName("ADD", 403), "Mushroom_Maximus: "+ID+ " added to MushroomCollection! MushroomCollection size: " + Plane.MushroomCollection.size());

    }
}
