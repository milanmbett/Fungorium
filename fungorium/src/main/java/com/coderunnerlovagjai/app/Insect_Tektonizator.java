package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Insect_Tektonizator extends Insect_Class
{
    private static final Logger INSECT_TEKTONIZATOR_LOGGER = LogManager.getLogger(Insect_Tektonizator.class);
    public Insect_Tektonizator(Tecton_Class targetTecton, Player p)
    {
        hp = 50;
        attackDamage = 10; 
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        ID = "Insect_Tektonizator" + Integer.toString(Plane.InsectCollection.size());
        owner = p;
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CREATE",401),"Insect_Tektonizator Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("ADD", 403), "Insect_Tektonizator: "+ID+ " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());
    }
    public Insect_Tektonizator(Tecton_Class targetTecton, int hp, int ad, int as, Player p)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        ID = "Insect_Tektonizator" + Integer.toString(Plane.InsectCollection.size());
        owner = p;
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CREATE",401),"Insect_Tektonizator Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("ADD", 403), "Insect_Tektonizator: "+ID+ " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());
    }
    public void tectonCrack() //TODO: Megírás
    {
        if(!tecton.canBeCracked())
        {
            INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("WARN", 404), "Tecton:" +tecton.get_ID() + " cannot be cracked!");
            return;
        }

    }
    @Override
    public void duplicate_Insect()
    {
        Insect_Tektonizator duplicated = new Insect_Tektonizator(tecton, hp, attackDamage, availableSteps, owner);
    }
}
