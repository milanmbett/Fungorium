package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Spore_Speed extends Basic_Spore
{
    private static final Logger SPORE_SPEED_LOGGER = LogManager.getLogger(Spore_Speed.class);
    public Spore_Speed(Tecton_Class targetTecton)
    {
        timeToLive = 3; //TODO: Értékét még meg kell beszélni
        tecton = targetTecton;
        tecton.set_Spore(this);
        ID = "Spore_Speed" + Integer.toString(Plane.SporeCollection.size());
        Plane.SporeCollection.add(this);
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        insect.eat_Spore(this);
        insect.set_availableSteps(insect.get_availableSteps()*2); //Kétszeresére növeljük az availableSteps-et? ezt nem tudom hogy ezt beszéltük-e meg
        die_Spore();
    }
}
