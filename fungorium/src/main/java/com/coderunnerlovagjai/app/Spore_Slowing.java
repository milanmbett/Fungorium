package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Spore_Slowing extends Basic_Spore
{
    private static final Logger SLOWING_SPORE_LOGGER = LogManager.getLogger(Spore_Slowing.class);
    
    public Spore_Slowing()
    {
        SLOWING_SPORE_LOGGER.log(Level.forName("INIT",402),"Spore_Slowing Constructor called!"); 
    }

    public Spore_Slowing(Tecton_Class targetTecton, Player owner)
    {
        timeToLive = 3;
        tecton = targetTecton;
        tecton.set_Spore(this);
        Player player = owner;
        ID = "Spore_Slowing" + Integer.toString(owner.getGame().getPlane().SporeCollection.size());
        SLOWING_SPORE_LOGGER.log(Level.forName("CREATE",401),"Spore_Slowing Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().SporeCollection.add(this);
        SLOWING_SPORE_LOGGER.log(Level.forName("ADD", 403), "Spore_Slowing: "+ID+ " added to SporeCollection! SporeCollection size: " + owner.getGame().getPlane().SporeCollection.size());
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        insect.set_attackDamage(insect.get_attackDamage()/2); //Felezzük az attackDamage-et
        insect.set_availableSteps(insect.get_availableSteps()-1); //Csökkentjük az elérhető lépéseket 1-el
        die_Spore();
    }
}
