package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Spore_Paralysing extends Basic_Spore
{
    private static final Logger SPORE_PARALYSING_LOGGER = LogManager.getLogger(Spore_Paralysing.class);

    public Spore_Paralysing(Player player)
    {
        Player owner = player;
        SPORE_PARALYSING_LOGGER.log(Level.forName("INIT",402),"Spore_Paralysing Constructor called!"); 
    }

    public Spore_Paralysing(Tecton_Class targetTecton,Player player)
    {
        timeToLive = 3; //_TMP value
        tecton = targetTecton;
        tecton.set_Spore(this);
        Player owner = player;
        ID = "Spore_Paralysing" + Integer.toString(owner.getGame().getPlane().SporeCollection.size());
        SPORE_PARALYSING_LOGGER.log(Level.forName("CREATE",401),"Spore_Paralysing Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().SporeCollection.add(this);
        SPORE_PARALYSING_LOGGER.log(Level.forName("ADD", 403), "Spore_Paralysing: "+ID+ " added to SporeCollection! SporeCollection size: " + owner.getGame().getPlane().SporeCollection.size());
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {   //jelezni kéne hogy a következő körben avaliableSteps = 0 valahogy
        SPORE_PARALYSING_LOGGER.log(Level.forName("CONSUME", 401), "Spore_Paralysing: " + ID + " is consumed by Insect: " + insect.get_ID());
        insect.paralyse_Insect();
        die_Spore();
    }

}
