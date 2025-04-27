package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Spore_Speed extends Basic_Spore
{
    private static final Logger SPORE_SPEED_LOGGER = LogManager.getLogger(Spore_Speed.class);

    public Spore_Speed()
    {
        SPORE_SPEED_LOGGER.log(Level.forName("INIT",402),"Spore_Speed Constructor called!"); 
    }

    public Spore_Speed(Tecton_Class targetTecton)
    {
        timeToLive = 3;
        tecton = targetTecton;
        tecton.set_Spore(this);
        ID = "Spore_Speed" + Integer.toString(Plane.SporeCollection.size());
        SPORE_SPEED_LOGGER.log(Level.forName("CREATE",401),"Spore_Speed Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.SporeCollection.add(this);
        SPORE_SPEED_LOGGER.log(Level.forName("ADD", 403), "Spore_Speed: "+ID+ " added to SporeCollection! SporeCollection size: " + Plane.SporeCollection.size());
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        insect.set_availableSteps(insect.get_availableSteps()*2); //Kétszeresére növeljük az availableSteps-et? ezt nem tudom hogy ezt beszéltük-e meg
        die_Spore();
    }
}
