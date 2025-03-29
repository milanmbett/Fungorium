package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Spore_Slowing extends Basic_Spore
{
    private static final Logger SLOWING_SPORE_LOGGER = LogManager.getLogger(Spore_Slowing.class);
    public Spore_Slowing(Tecton_Class targetTecton)
    {
        timeToLive = 3; //TODO: Értékét még meg kell beszélni
        tecton = targetTecton;
        Plane.SporeCollection.add(this);
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        insect.set_attackDamage(insect.get_attackDamage()/2); //Felezzük az attackDamage-et
        die_Spore();
    }
}
