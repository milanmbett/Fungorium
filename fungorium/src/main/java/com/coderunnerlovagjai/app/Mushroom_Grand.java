package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Grand extends Mushroom_Class 
{
    private static final Logger MUSHROOM_GRAND_LOGGER = LogManager.getLogger(Mushroom_Grand.class);
    public Mushroom_Grand(Tecton_Class targetTecton,Player p)
    {
        hp = 500;
        power = 50;
        sporeCount = 0;
        tecton = targetTecton;
        tecton.set_Mushroom(this);
        owner = p;
        ID = "Mushroom_Grand" + Integer.toString(Plane.MushroomCollection.size());
        MUSHROOM_GRAND_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Grand Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.MushroomCollection.add(this);
        MUSHROOM_GRAND_LOGGER.log(Level.forName("ADD", 403), "Mushroom_Grand: "+ID+ " added to MushroomCollection! MushroomCollection size: " + Plane.MushroomCollection.size());
       
    }
}
