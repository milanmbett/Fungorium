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
    protected String ID;
    
    public Mushroom_Class()
    {
        MUSHROOM_CLASS_LOGGER.log(Level.forName("INIT",402),"Mushroom_Class Constructor called!"); 
    }
    public Mushroom_Class(Tecton_Class targetTecton)
    {

    }

    public void die_Mushroom()
    {
        tecton.set_Mushroom(null);
        Plane.MushroomCollection.remove(this);
    }

    public void spawn_Spores() //TODO: Meg kell írni
    {

    } 
    public void generate_Income() //TODO: Meg kell írni
    {

    } 
    public void attack_Insects()
    {
        for (Insect_Class insect : tecton.insectsOnTecton) 
        {
            if(insect.get_Owner() != owner)
            {
                MUSHROOM_CLASS_LOGGER.log(Level.forName("ATTACK", 401), "Mushroom: " + ID + " is attacking insect: " + insect.get_ID() + " with power: " + power);
                insect.reduceHP(power);
            }
            
        }
        //Körbemegy a tektonja szomszédsági listáján, és megtámadja az összes rovart.
        for (Tecton_Class t : tecton.get_TectonNeighbours()) 
        {
            for (Insect_Class insect : t.get_InsectsOnTecton()) 
            {
                if(insect.get_Owner() != owner)
                {
                    MUSHROOM_CLASS_LOGGER.log(Level.forName("ATTACK", 401), "Mushroom: " + ID + " is attacking insect: " + insect.get_ID() + " with power: " + power);
                    insect.reduceHP(power);
                }
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
        MUSHROOM_CLASS_LOGGER.log(Level.forName("ATTACK", 401), "Mushroom: " + ID + " is attacked by an insect. Damage done: " + ad +" HP left: " + (hp-ad));
        hp -= ad;
        if(hp <= 0)
        {
            MUSHROOM_CLASS_LOGGER.log(Level.forName("DEAD", 401), "Mushroom: " + ID + " is dead!");
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
        if (p != Game.player1 && p != Game.player2) { // Ellenőrzés, hogy érvényes játékos-e
            MUSHROOM_CLASS_LOGGER.log(Level.forName("ERROR", 404), "Invalid player!");
            return;
        }
        owner = p;
    }
    public String get_ID()
    {
        return ID;
    }

}
