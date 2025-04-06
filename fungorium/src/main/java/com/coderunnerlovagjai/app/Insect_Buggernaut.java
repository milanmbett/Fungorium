package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Insect_Buggernaut extends Insect_Class
{
    private static final Logger INSECT_BUGGERNAUT_LOGGER = LogManager.getLogger(Insect_Buggernaut.class);

    public Insect_Buggernaut(Tecton_Class targetTecton, Player p)
    {
        hp = 150;
        attackDamage = 50;
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_Buggernaut" + Integer.toString(Plane.InsectCollection.size());
        INSECT_BUGGERNAUT_LOGGER.log(Level.forName("CREATE",401),"Insect_Buggernaut Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_BUGGERNAUT_LOGGER.log(Level.forName("ADD", 403), "Insect_Buggernaut: "+ID+ " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());

    }
    public Insect_Buggernaut(Tecton_Class targetTecton, int hp, int ad, int as, Player p)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_Buggernaut" + Integer.toString(Plane.InsectCollection.size());
        INSECT_BUGGERNAUT_LOGGER.log(Level.forName("CREATE",401),"Insect_Buggernaut Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_BUGGERNAUT_LOGGER.log(Level.forName("ADD", 403), "Insect_Buggernaut: "+ID+ " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_Buggernaut duplicated = new Insect_Buggernaut(tecton, hp, attackDamage, availableSteps, owner);
    }
}
