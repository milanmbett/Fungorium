package com.coderunnerlovagjai.app;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Plane 
{
    private static final Logger PLANE_LOGGER = LogManager.getLogger(Plane.class);
    public static final List<Basic_Spore> SporeCollection = new ArrayList<>();
    public static final List<Tecton_Class> TectonCollection = new ArrayList<>();
    public static final List<Insect_Class> InsectCollection = new ArrayList<>();
    public static final List<Thread_Class> ThreadCollection = new ArrayList<>();
    public static final List<Mushroom_Class> MushroomCollection = new ArrayList<>();
    //TODO: ID-k jobban kell hogy működjenek ,mert jelenleg nem biztos ,hogy egyediek lesznek!
    //TODO: Páya kialakítása, elrendezése
    private Tecton_Base base1;
    private Tecton_Base base2;

    public void initBases(Player player1, Player player2, Game game) {
        this.base1 = new Tecton_Base(player1, game);
        this.base2 = new Tecton_Base(player2, game);
        PLANE_LOGGER.log(Level.forName("INIT", 402), "Bases initialized for players.");
    }

    public Tecton_Base getBase1() {
        return base1;
    }

    public Tecton_Base getBase2() {
        return base2;
    }

    public void init_Plane() //Lehet nem fog kelleni Skeletonba
    {
        
    }
    public static void place_Insect(Insect_Class ins, Tecton_Class targetTecton)
    {
        targetTecton.get_InsectsOnTecton().add(ins);
    }
    public static void place_Spore(Basic_Spore spore, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Spore() == null)
        {
            targetTecton.set_Spore(spore);
        }
    }
    public static void place_Thread(Thread_Class t, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Thread() == null)
        {
            targetTecton.set_Thread(t);
        }
    }
    public static void place_Mushroom(Mushroom_Class m, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Mushroom() == null)
        {
            targetTecton.set_Mushroom(m);
        }
    }
    public static void move_Insect(Insect_Class ins, Tecton_Class targetTecton)
    {
        ins.get_Tecton().get_InsectsOnTecton().remove(ins);
        targetTecton.get_InsectsOnTecton().add(ins);
    }
    
}
