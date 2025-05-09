package com.coderunnerlovagjai.app;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Basic_Spore extends Entity 
{
    private static final Logger BASIC_SPORE_LOGGER = LogManager.getLogger(Basic_Spore.class);
    protected int timeToLive;
    protected Tecton_Class tecton;
    protected String ID;
    protected Player owner;

    public Basic_Spore()
    {
        BASIC_SPORE_LOGGER.log(Level.forName("INIT",402),"Basic_Spore Constructor called!"); 
    }
    public Basic_Spore(Tecton_Class targetTecton, Player player)
    {
        timeToLive = 3; 
        tecton = targetTecton;
        tecton.set_Spore(this);
        Player owner = player;
        ID = "Spore_Basic" + Integer.toString(owner.getGame().getPlane().SporeCollection.size());
        BASIC_SPORE_LOGGER.log(Level.forName("CREATE",401),"Basic_Spore Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().SporeCollection.add(this);
        BASIC_SPORE_LOGGER.log(Level.forName("ADD", 403), "Basic_Spore: "+ID+ " added to SporeCollection! SporeCollection size: " + owner.getGame().getPlane().SporeCollection.size());
    }

    public void consumed_by(Insect_Class insect)
    {
        die_Spore();
    }
    public void die_Spore()
    {
        if(tecton.get_Spore() == null)
        {
            BASIC_SPORE_LOGGER.log(Level.forName("ERROR", 401), "Spore: " + ID + " is already dead!");
            return;
        }
        tecton.set_Spore(null);
        BASIC_SPORE_LOGGER.log(Level.forName("DIE", 401), "Spore: " + ID + " is dead!");
        owner.getGame().getPlane().SporeCollection.remove(this);
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
    public String get_ID()
    {
        return ID;
    }
    
}
