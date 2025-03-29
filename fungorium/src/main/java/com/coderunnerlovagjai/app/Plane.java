package com.coderunnerlovagjai.app;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Plane 
{
    public static List<Basic_Spore> SporeCollection = new ArrayList<>();
    public static List<Tecton_Class> TectonCollection = new ArrayList<>();
    public static List<Insect_Class> InsectCollection = new ArrayList<>();
    public static List<Thread_Class> ThreadCollection = new ArrayList<>();
    public static List<Mushroom_Class> MushroomCollection = new ArrayList<>();

    public void init_Plane() //Lehet nem fog kelleni Skeletonba
    {
        
    }
    public void place_Insect(Insect_Class ins, Tecton_Class targetTecton)
    {
        targetTecton.get_InsectsOnTecton().add(ins);
    }
    public void place_Spore(Basic_Spore spore, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Spore().equals(null))
        {
            targetTecton.set_Spore(spore);
        }
    }
    public void place_Thread(Thread_Class t, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Thread().equals(null))
        {
            targetTecton.set_Thread(t);
        }
    }
    public void place_Mushroom(Mushroom_Class m, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Mushroom().equals(null))
        {
            targetTecton.set_Mushroom(m);
        }
    }
    public void move_Insect(Insect_Class ins, Tecton_Class targetTecton)
    {
        ins.get_Tecton().get_InsectsOnTecton().remove(ins);
        targetTecton.get_InsectsOnTecton().add(ins);
    }
    
}
