package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Shroomlet extends Mushroom_Class
{
    private static final Logger MUSHROOM_SHROOMLET_LOGGER = LogManager.getLogger(Mushroom_Shroomlet.class);
    public Mushroom_Shroomlet(Tecton_Class targetTecton, Player p)
    {
        hp = 250;
        power = 25; 
        sporeCount = 0;
        tecton = targetTecton;
        tecton.set_Mushroom(this);
        owner = p;
        ID = "Mushroom_Shroomlet" + Integer.toString(owner.getGame().getPlane().MushroomCollection.size());
        cost=20;
        MUSHROOM_SHROOMLET_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Shroomlet Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        //owner.getGame().getPlane().MushroomCollection.add(this);
        MUSHROOM_SHROOMLET_LOGGER.log(Level.forName("ADD", 403), "Mushroom_Shroomlet: "+ID+ " added to MushroomCollection! MushroomCollection size: " + owner.getGame().getPlane().MushroomCollection.size());
    }

    @Override
    public int getIncomeMultiplier() {
        return 10; // Például a Mushroom_Shroomlet 10-szeres szorzót ad
    }
}
