public class Thread_Class 
{
    private Tecton_Class tecton;

    public Thread_Class(Tecton_Class targetTecton)
    {
        tecton = targetTecton;
        tecton.set_Thread(this);
        //Plane.ThreadCollection.add(this);
    }
    public Tecton_Class get_Tecton()
    {
        return tecton;
    }
    public void set_Tecton(Tecton_Class t)
    {
        tecton = t;
    }
    public void expand_Thread()
    {

    }
    public void die_Thread()
    {
        tecton.set_Thread(null);
        //Plane.ThreadCollection.remove(this);
    }
}

