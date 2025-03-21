
public class Thread_Class 
{

    public Thread_Class(Tecton_Class targetTecton)
    {
        /* tecton = targetTecton;
        tecton.set_Thread(this); */
        //Plane.ThreadCollection.add(this);
    }
    public Tecton_Class get_Tecton()
    {
        //return tecton;
        return null;
    }
    public void set_Tecton(Tecton_Class t)
    {
        //tecton = t;
    }
    public void expand_Thread()
    {
        
        /* if(tecton.get_TectonNeighbours().size()==0)
        {
            return;
        }
        List<Tecton_Class> threadlessTectonNeighbours = new ArrayList<>();
        for(Tecton_Class t : tecton.get_TectonNeighbours())
        {
            if(t.get_Thread().equals(null))
            {
                threadlessTectonNeighbours.add(t);
            } 
        }
        if(threadlessTectonNeighbours.size()==0)
        {
            return;
        }
        Random random = new Random();
        int rand = random.nextInt(threadlessTectonNeighbours.size());
        threadlessTectonNeighbours.get(rand).set_Thread(new Thread_Class(threadlessTectonNeighbours.get(rand))); */
    }
    public void die_Thread()
    {
        //tecton.set_Thread(null);
        //Plane.ThreadCollection.remove(this);
    }
}

