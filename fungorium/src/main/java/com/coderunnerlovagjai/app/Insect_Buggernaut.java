package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Insect_Buggernaut extends Insect_Class
{
    private static final Logger INSECT_BUGGERNAUT_LOGGER = LogManager.getLogger(Insect_Buggernaut.class);
    public static final int VIEWCOST = 15;
    public Insect_Buggernaut(Tecton_Class targetTecton, Player p)
    {
        hp = 150;
        attackDamage = 50;
        maxSteps = 1;
        availableSteps = maxSteps;
        tecton = targetTecton;
        //tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_Buggernaut" + Integer.toString(owner.getGame().getPlane().InsectCollection.size());
        cost = VIEWCOST;
        INSECT_BUGGERNAUT_LOGGER.log(Level.forName("CREATE",401),
        "Insect_Buggernaut Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        //owner.getGame().getPlane().InsectCollection.add(this);
        //INSECT_BUGGERNAUT_LOGGER.log(Level.forName("ADD", 403), "Insect_Buggernaut: "+ID+ " added to InsectCollection! InsectCollection size: " + owner.getGame().getPlane().InsectCollection.size());

    }
    public Insect_Buggernaut(Tecton_Class targetTecton, int hp, int ad, int as, Player p)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_Buggernaut" + Integer.toString(owner.getGame().getPlane().InsectCollection.size());
        INSECT_BUGGERNAUT_LOGGER.log(Level.forName("CREATE",401),"Insect_Buggernaut Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().InsectCollection.add(this);
        INSECT_BUGGERNAUT_LOGGER.log(Level.forName("ADD", 403), "Insect_Buggernaut: "+ID+ " added to InsectCollection! InsectCollection size: " + owner.getGame().getPlane().InsectCollection.size());
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_Buggernaut duplicated = new Insect_Buggernaut(tecton, hp, attackDamage, availableSteps, owner);
    }
}
