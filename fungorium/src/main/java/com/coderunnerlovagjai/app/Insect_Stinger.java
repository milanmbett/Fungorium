package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Insect_Stinger extends Insect_Class
{
    private static final Logger INSECT_STINGER_LOGGER = LogManager.getLogger(Insect_Stinger.class);
    public Insect_Stinger(Tecton_Class targetTecton, Player p)
    {
        hp = 50; 
        attackDamage = 150; 
        availableSteps = 1;
        tecton = targetTecton;
        //tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_Stinger" + Integer.toString(owner.getGame().getPlane().InsectCollection.size());
        cost = 20;
        INSECT_STINGER_LOGGER.log(Level.forName("CREATE",401),
        "Insect_Buggernaut Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        //owner.getGame().getPlane().InsectCollection.add(this);
        //INSECT_STINGER_LOGGER.log(Level.forName("ADD", 403), "Insect_Stinger: "+ID+ " added to InsectCollection! InsectCollection size: " + owner.getGame().getPlane().InsectCollection.size());

    }
    public Insect_Stinger(Tecton_Class targetTecton, int hp, int ad, int as, Player p)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        owner.getGame().getPlane().InsectCollection.add(this);
        owner = p;
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_Stinger duplicated = new Insect_Stinger(tecton, hp, attackDamage, availableSteps, owner);
        tecton.get_InsectsOnTecton().add(duplicated);
        owner.getGame().getPlane().InsectCollection.add(duplicated);
    }
}
