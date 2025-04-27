package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Maximus extends Mushroom_Class 
{
    private static final Logger MUSHROOM_MAXIMUS_LOGGER = LogManager.getLogger(Mushroom_Maximus.class);
    public Mushroom_Maximus(Tecton_Class targetTecton,Player p)
    {
        hp = 400;
        power = 40;
        sporeCount = 0;
        tecton = targetTecton;
        tecton.set_Mushroom(this);
        owner = p;
        ID = "Mushroom_Maximus" + Integer.toString(owner.getGame().getPlane().MushroomCollection.size());
        cost=40;
        MUSHROOM_MAXIMUS_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Maximus Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().MushroomCollection.add(this);
        MUSHROOM_MAXIMUS_LOGGER.log(Level.forName("ADD", 403), "Mushroom_Maximus: "+ID+ " added to MushroomCollection! MushroomCollection size: " + owner.getGame().getPlane().MushroomCollection.size());

    }

    @Override
    public int getIncomeMultiplier() {
        return 10; // Például a Mushroom_Shroomlet 10-szeres szorzót ad
    }
}
