import java.util.*;

public class Plane 
{
    public static List<Basic_Spore> SporeCollection = new ArrayList<>();
    public static List<Tecton_Class> TectonCollection = new ArrayList<>();
    public static List<Insect_Class> InsectCollection = new ArrayList<>();
    public static List<Thread_Class> ThreadCollection = new ArrayList<>();
    public static List<Mushroom_Class> MushroomCollection = new ArrayList<>();

    public void init_Plane()
    {

    }
    public void place_Insect(Insect_Class ins, Tecton_Class targetTecton)
    {
        targetTecton.get_InsectsOnTecton().add(ins);
        InsectCollection.add(ins);
    }
    public void place_Spore(Basic_Spore spore, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Spore().equals(null))
        {
            targetTecton.set_Spore(spore);
            SporeCollection.add(spore);
        }
    }
    public void place_Thread(Thread_Class t, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Thread().equals(null))
        {
            targetTecton.set_Thread(t);
            ThreadCollection.add(t);
        }
    }
    public void place_Mushroom(Mushroom_Class m, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Mushroom().equals(null))
        {
            targetTecton.set_Mushroom(m);
            MushroomCollection.add(m);
        }
    }
    
}
