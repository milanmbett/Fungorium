package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Insect_Buglet extends Insect_Class
{
    private static final Logger INSECT_BUGLET_LOGGER = LogManager.getLogger(Insect_Buglet.class);
    public Insect_Buglet(Tecton_Class targetTecton, Player p)
    {
        hp = 100;
        attackDamage = 100;
        availableSteps = 2;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_Buglet" + Integer.toString(owner.getGame().getPlane().InsectCollection.size());
        cost = 10;
        INSECT_BUGLET_LOGGER.log(Level.forName("CREATE",401),"Insect_Buglet Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().InsectCollection.add(this);
        INSECT_BUGLET_LOGGER.log(Level.forName("ADD", 403), "Insect_Buglet: "+ID+ " added to InsectCollection! InsectCollection size: " + owner.getGame().getPlane().InsectCollection.size());

    }
    public Insect_Buglet(Tecton_Class targetTecton, int hp, int ad, int as, Player p)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        ID = "Insect_Buglet" + Integer.toString(owner.getGame().getPlane().InsectCollection.size());
        owner = p;
        tecton.get_InsectsOnTecton().add(this);
        INSECT_BUGLET_LOGGER.log(Level.forName("CREATE",401),"Insect_Buglet Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().InsectCollection.add(this);
        INSECT_BUGLET_LOGGER.log(Level.forName("ADD", 403), "Insect_Buglet: "+ID+ " added to InsectCollection! InsectCollection size: " + owner.getGame().getPlane().InsectCollection.size());
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_Buglet duplicated = new Insect_Buglet(tecton, hp, attackDamage, availableSteps, owner);
    }
}
