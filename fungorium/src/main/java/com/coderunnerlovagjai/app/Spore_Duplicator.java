package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Spore_Duplicator extends Basic_Spore
{
    private static final Logger SPORE_DUPLICATOR_LOGGER = LogManager.getLogger(Spore_Duplicator.class);

    public Spore_Duplicator()
    {
        SPORE_DUPLICATOR_LOGGER.log(Level.forName("INIT",402),"Spore_Duplicator Constructor called!"); 
    }

    public Spore_Duplicator(Tecton_Class targetTecton,Player player)
    {
        timeToLive = 3;
        tecton = targetTecton;
        tecton.set_Spore(this);
        owner = player;
        ID = "Spore_Duplicator" + Integer.toString(owner.getGame().getPlane().SporeCollection.size());
        SPORE_DUPLICATOR_LOGGER.log(Level.forName("CREATE",401),"Spore_Duplicator Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().SporeCollection.add(this);
        SPORE_DUPLICATOR_LOGGER.log(Level.forName("ADD", 403), "Spore_Duplicator: "+ID+ " added to SporeCollection! SporeCollection size: " + owner.getGame().getPlane().SporeCollection.size());
    }

    public void consumed_by(Insect_Class insect)
    {
        die_Spore();
        insect.duplicate_Insect();
        SPORE_DUPLICATOR_LOGGER.log(Level.forName("DUPLICATE", 401), "Insect: " + insect.get_ID() + " duplicated by Spore_Duplicator: " + ID);
        for (Insect_Class ins : tecton.get_InsectsOnTecton()) 
        {
            //get insects on tecton and print out their IDs
            SPORE_DUPLICATOR_LOGGER.log(Level.forName("DUPLICATE", 401), "Insect: " + ins.get_ID() + " on Tecton: " + tecton.get_ID());    
        }
    }    
}
