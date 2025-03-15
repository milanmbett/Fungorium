import java.util.*;

public abstract class Tecton_Class 
{
    private List<Tecton_Class>Neighbours = new ArrayList<>();

    private Mushroom_Class mushroom = null;
    private List<Insect_Class> insectsOnTecton = new ArrayList<>(); 
    private Basic_Spore spore = null;
    private Thread_Class thread = null;

    public List<Tecton_Class> get_TectonNeighbours()
    {
        return Neighbours;
    }
    public void add_TectonNeighbour(Tecton_Class t)
    {
        Neighbours.add(t);
    }
    public void del_TectonNeighbour(Tecton_Class t)
    {
        Neighbours.remove(t);
    }
    public void remove_TectonNeighbours()
    {
        for (Tecton_Class t : Neighbours) 
        {
            t.del_TectonNeighbour(this);
        }
    }
    public void set_TectonNeighbours(List<Tecton_Class> t)
    {
        Neighbours = t;
    }
    public void die_Tecton()
    {
        remove_InsectsOnTecton();
        remove_Mushroom();
        remove_Spore();
        remove_Thread();

    }
    public void set_InsectsOnTecton(List<Insect_Class> insectList)
    {
        insectsOnTecton = insectList;
    }
    public void remove_InsectsOnTecton()
    {
        for (Insect_Class ins : insectsOnTecton) 
        {
            Plane.InsectCollection.remove(ins);
            insectsOnTecton.remove(ins);
        }
    }
    public List<Insect_Class> get_InsectsOnTecton()
    {
        return insectsOnTecton;
    }
    public void set_Mushroom(Mushroom_Class mush)
    {
        mushroom = mush;
    }
    public void remove_Mushroom()
    {
        Plane.MushroomCollection.remove(mushroom);
        mushroom = null;
    }
    public Mushroom_Class get_Mushroom()
    {
        return mushroom;
    }
    public void set_Spore(Basic_Spore s)
    {
        spore = s;
    }
    public void remove_Spore()
    {
        Plane.SporeCollection.remove(spore);
        spore = null;
    }
    public Basic_Spore get_Spore()
    {
        return spore;
    }
    public void set_Thread(Thread_Class t)
    {
        thread = t;
    }
    public void remove_Thread()
    {
        Plane.ThreadCollection.remove(thread);
        thread = null;
    }
    public Thread_Class get_Thread()
    {
        return thread;
    }
}
