package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Spore_Duplicator extends Basic_Spore
{
    private static final Logger SPORE_DUPLICATOR_LOGGER = LogManager.getLogger(Spore_Duplicator.class);
    public Spore_Duplicator(Tecton_Class targetTecton)
    {
        timeToLive = 3; //TODO: Értékét még meg kell beszélni
        tecton = targetTecton;
        tecton.set_Spore(this);
        ID = "Spore_Duplicator" + Integer.toString(Plane.SporeCollection.size());
        Plane.SporeCollection.add(this);
    }

    public void consumed_by(Insect_Class insect)
    {
        die_Spore();
        insect.duplicate_Insect();
    }    
}
