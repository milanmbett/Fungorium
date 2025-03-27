package fungorium.src.main.java.com.coderunnerlovagjai.app;

public class Basic_Spore
{
    protected int timeToLive;
    protected Tecton_Class tecton;

    public Basic_Spore()
    {

    }
    public Basic_Spore(Tecton_Class targetTecton)
    {
        timeToLive = 3; //TODO: Értékét még meg kell beszélni
        tecton = targetTecton;
        tecton.set_Spore(this);
        Plane.SporeCollection.add(this);
    }

    public void consumed_by(Insect_Class insect)
    {
        die_Spore();
    }
    public void die_Spore()
    {
        tecton.remove_Spore();
        Plane.SporeCollection.remove(this);
    }

    public int get_timeToLive()
    {
        return timeToLive;
    }
    public void set_timeToLive(int ttl)
    {
        timeToLive = ttl;
    }
    public Tecton_Class get_Tecton()
    {
        return tecton;
    }
    public void set_Tecton(Tecton_Class t)
    {
        tecton = t;
    }
    //Maradandó életidő csökkentése
    public void decay()
    {
        timeToLive--;
        if(timeToLive == 0)
        {
            die_Spore();
        }
    }
    
}
