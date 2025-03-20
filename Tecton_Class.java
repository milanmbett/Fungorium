import java.util.*;

public abstract class Tecton_Class 
{
    protected List<Tecton_Class>Neighbours = new ArrayList<>();

    protected Mushroom_Class mushroom;
    protected List<Insect_Class> insectsOnTecton; 
    protected Basic_Spore spore;
    protected Thread_Class thread;

    public Tecton_Class()
    {
        mushroom = null;
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        //Plane.TectonCollection.add(this);
    }

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
        //remove_TectonNeighbours();?
        //Plane.TectonCollection.remove(this);

    }
    public void set_InsectsOnTecton(List<Insect_Class> insectList)
    {
        insectsOnTecton = insectList;
    }
    public void remove_InsectsOnTecton()
    {
        for (Insect_Class ins : insectsOnTecton) 
        {
            ins.die_Insect();
        }
        insectsOnTecton.clear();
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
        mushroom.die_Mushroom();
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
        spore.die_Spore();
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
        thread.die_Thread();
    }
    public Thread_Class get_Thread()
    {
        return thread;
    }
}
