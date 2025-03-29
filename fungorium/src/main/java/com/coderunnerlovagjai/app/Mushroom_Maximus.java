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
        ID = "Mushroom_Maximus" + Integer.toString(Plane.MushroomCollection.size());
        Plane.MushroomCollection.add(this);
        owner = p;
    }
}
