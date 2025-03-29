package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public abstract class Mushroom_Class 
{
    private static final Logger MUSHROOM_CLASS_LOGGER = LogManager.getLogger(Mushroom_Class.class);
    protected int hp;
    protected int power;
    protected int sporeCount;
    protected Tecton_Class tecton;
    protected Player owner;
    
    public Mushroom_Class()
    {

    }
    public Mushroom_Class(Tecton_Class targetTecton)
    {

    }

    public void die_Mushroom()
    {
        tecton.remove_Mushroom();
        Plane.MushroomCollection.remove(this);
    }

    public void spawn_Spores() //TODO: Meg kell írni
    {

    } 
    public void generate_Income() //TODO: Meg kell írni
    {

    } 
    public void attack_Insect(Insect_Class i)
    {
        //Körbemegy a tektonja szomszédsági listáján, és megtámadja az összes rovart.
        for (Tecton_Class t : tecton.get_TectonNeighbours()) 
        {
            for (Insect_Class insect : t.get_InsectsOnTecton()) 
            {
                insect.reduceHP(power);
            }    
        }
    }
    public void upgrade_Mushroom(Mushroom_Class type) //TODO: Meg kell írni
    {
        //úgy emlékszem ez az volt hogy egy másik gomba lesz belőle
        //valahogy megkéne nézni hogy ugyanaz-e típus e?
        //van-e pénz (még ez nem kell szerintem)
        
    }
    public void reduceHP(int ad)
    {
        hp -= ad;
        if(hp <= 0)
        {
            die_Mushroom();
        }
    }
    public int get_hp()
    {
        return hp;
    }
    public void set_hp(int h)
    {
        hp = h;
    }
    public int get_power()
    {
        return power;
    }
    public void set_power(int p)
    {
        power = p;
    }
    public int get_sporeCount()
    {
        return sporeCount;
    }
    public void set_sporeCount(int sc)
    {
        sporeCount = sc;
    }
    public Tecton_Class get_Tecton()
    {
        return tecton;
    }
    public void set_Tecton(Tecton_Class t)
    {
        tecton = t;
    }
    public Player get_Owner()
    {
        return owner;
    }
    public void set_Owner(Player p)
    {
        owner = p;
    }

}
