package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Insect_Buggernaut extends Insect_Class
{
    private static final Logger INSECT_BUGGERNAUT_LOGGER = LogManager.getLogger(Insect_Buggernaut.class);

    public Insect_Buggernaut(Tecton_Class targetTecton, Player p)
    {
        hp = 150; //TODO: Értékét még meg kell beszélni
        attackDamage = 50; //TODO: Értékét még meg kell beszélni
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
        owner = p;
    }
    public Insect_Buggernaut(Tecton_Class targetTecton, int hp, int ad, int as, Player p)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
        owner = p;
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_Buggernaut duplicated = new Insect_Buggernaut(tecton, hp, attackDamage, availableSteps, owner);
        tecton.get_InsectsOnTecton().add(duplicated);
        Plane.InsectCollection.add(duplicated);
    }
}
