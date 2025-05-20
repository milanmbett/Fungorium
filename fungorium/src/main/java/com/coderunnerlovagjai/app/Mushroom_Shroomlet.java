package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Mushroom_Shroomlet extends Mushroom_Class
{
    private static final Logger MUSHROOM_SHROOMLET_LOGGER = LogManager.getLogger(Mushroom_Shroomlet.class);
    public static final int VIEWCOST = 20;
    public Mushroom_Shroomlet(Tecton_Class targetTecton, Player p)
    {
        MUSHROOM_SHROOMLET_LOGGER.log(Level.forName("INIT",402),"TRYING TO CREATE MUSHROOM ON TECTON: " + targetTecton.get_ID());
        if((targetTecton.get_Thread()== null))
        {
            MUSHROOM_SHROOMLET_LOGGER.log(Level.forName("ERROR", 404), "Target has no Thread");
            return;
        }
        hp = 250;
        power = 25; 
        sporeCount = 0;
        tecton = targetTecton;
        //tecton.set_Mushroom(this);
        owner = p;
        ID = "Mushroom_Shroomlet" + p.getGame().getPlane().MushroomCollection.size();
        cost=VIEWCOST;
        addScore(5);
        MUSHROOM_SHROOMLET_LOGGER.log(Level.forName("CREATE",401),"Mushroom_Shroomlet Created! ID: " + ID + " on Tecton: " + targetTecton.get_ID());
    }

    @Override
    public int getIncomeMultiplier() {
        return 10; // Például a Mushroom_Shroomlet 10-szeres szorzót ad
    }
}
