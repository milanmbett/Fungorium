package com.coderunnerlovagjai.app;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Insect_ShroomReaper extends Insect_Class
{
    private static final Logger INSECT_SHROOM_REAPER_LOGGER = LogManager.getLogger(Insect_ShroomReaper.class);
    public Insect_ShroomReaper(Tecton_Class targetTecton, Player p)
    {
        hp = 25; //TODO: Értékét még meg kell beszélni
        attackDamage = 25; //TODO: Értékét még meg kell beszélni
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_ShroomReaper" + Integer.toString(Plane.InsectCollection.size());
        INSECT_SHROOM_REAPER_LOGGER.log(Level.forName("CREATE",401),"Insect_ShroomReaper Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_SHROOM_REAPER_LOGGER.log(Level.forName("ADD", 403), "Insect_ShroomReaper: "+ID+ " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());
        
    }
    public Insect_ShroomReaper(Tecton_Class targetTecton, int hp, int ad, int as, Player p)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        owner = p;
        ID = "Insect_ShroomReaper" + Integer.toString(Plane.InsectCollection.size());
        INSECT_SHROOM_REAPER_LOGGER.log(Level.forName("CREATE",401),"Insect_ShroomReaper Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_SHROOM_REAPER_LOGGER.log(Level.forName("ADD", 403), "Insect_ShroomReaper: "+ID+ " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());
    }
    public void destroy_Tecton(Tecton_Class t)
    {
        List<Tecton_Class> tmp_tectonList = t.get_TectonNeighbours();
        tecton.die_Tecton();
        tecton = new Tecton_Dead();
        tecton.set_TectonNeighbours(tmp_tectonList);
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_ShroomReaper duplicated = new Insect_ShroomReaper(tecton, hp, attackDamage, availableSteps, owner);
    }
    
}
