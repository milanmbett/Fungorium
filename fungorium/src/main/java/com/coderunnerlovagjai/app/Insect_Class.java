package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class Insect_Class 
{
    private static final Logger INSECT_CLASS_LOGGER = LogManager.getLogger(Insect_Class.class);
    protected int hp;
    protected int attackDamage;
    protected int availableSteps;
    protected Tecton_Class tecton;
    protected boolean isParalysed = false;
    protected Player owner;
    protected String ID;
    protected int cost=0; //Grafikahoz lehet nem kell


    public Insect_Class()
    {
        INSECT_CLASS_LOGGER.log(Level.forName("INIT",402),"Insect_Class Constructor called!"); 
    }
    protected Insect_Class(Tecton_Class targetTecton)
    {
        
    }
    public void move_Insect(Tecton_Class targetTecton)
    {
        
        if(targetTecton == null)
        {
            INSECT_CLASS_LOGGER.log(Level.forName("NULL", 201), "Target tecton is null!");
            return;
        }
        INSECT_CLASS_LOGGER.log(Level.forName("MOVE", 401), "Insect: " + ID + " is trying to move to " + targetTecton.get_ID());
        if(availableSteps <= 0)
        {
            INSECT_CLASS_LOGGER.log(Level.forName("ERROR", 401), "Insect: " + ID + " has no available steps!");
            return;
        }
        if(targetTecton.equals(tecton))
        {
            INSECT_CLASS_LOGGER.log(Level.forName("ERROR", 401), "Insect: " + ID + " is already on the target tecton!");
            return;
        }
        if(tecton.get_Thread() == null || targetTecton.get_Thread() == null)
        {
            INSECT_CLASS_LOGGER.log(Level.forName("NULL", 201), "There is no thread on either tecton!");
            return;
        }
        INSECT_CLASS_LOGGER.log(Level.forName("MOVE", 401), "Insect: " + ID + " from " + tecton.get_ID() + " to " + targetTecton.get_ID());
        tecton.get_InsectsOnTecton().remove(this);
        tecton = targetTecton;
        targetTecton.get_InsectsOnTecton().add(this);
        availableSteps--;
        INSECT_CLASS_LOGGER.log(Level.forName("MOVE", 401), "Insect: " + ID + " moved to " + targetTecton.get_ID() + ". Available steps: " + availableSteps);

        // Auto-attack mushroom on arrival if present and not owned
        Mushroom_Class m = targetTecton.get_Mushroom();
        if (m != null && m.get_Owner() != owner) {
            INSECT_CLASS_LOGGER.log(Level.forName("AUTO_ATTACK", 401), "Insect: " + ID + " auto-attacks mushroom: " + m.get_ID());
            attack_Mushroom(m);
        }
    }
    public void attack_Mushroom(Mushroom_Class m)
    {
        if(m == null)
        {
            INSECT_CLASS_LOGGER.log(Level.forName("NULL", 201), "Mushroom is null!");
            return;
        }
        if(this.tecton==null || m.tecton==null)
        {
            INSECT_CLASS_LOGGER.log(Level.forName("NULL", 201), "Tecton is null!");
            return;
        }
        INSECT_CLASS_LOGGER.log(Level.forName("ATTACK", 401), "Insect: " + ID + " is trying to attack mushroom: " + m.get_ID() + " on tecton: " + m.tecton.get_ID());
        if(availableSteps <= 0)
        {
            INSECT_CLASS_LOGGER.log(Level.forName("ERROR", 401), "Insect: " + ID + " has no available steps!");
            return;
        }
        if(this.tecton.equals(m.tecton))
        {
            INSECT_CLASS_LOGGER.log(Level.forName("ATTACK", 401),"Insect: " + ID + " is attacking mushroom: " + m.get_ID() + "HP: "+m.get_hp() +" on tecton: " + m.tecton.get_ID());
            if(m.get_Owner() != owner)
            {
                m.reduceHP(attackDamage);
            }
            else
            {
                INSECT_CLASS_LOGGER.log(Level.forName("ERROR", 401), "Insect: " + ID + " cannot attack its own mushroom!");
                return;
            }
        }
    }
    public void die_Insect()
    {
        tecton.get_InsectsOnTecton().remove(this);
    }

    public void eat_Spore(Basic_Spore sp)
    {
        if(sp == null)
        {
            INSECT_CLASS_LOGGER.log(Level.forName("NULL", 201), "Spore is null!");
            return;
        }
        sp.consumed_by(this);
        owner.increaseIncome(5);
        owner.setScore(owner.getScore() + 5);
    }
    public void eat_Thread(Thread_Class th)
    {
        th.die_Thread();
        
    }
    public void reduceHP(int ad)
    {

        hp -= ad;
        INSECT_CLASS_LOGGER.log(Level.forName("REDUCE", 401), "Insect: " + ID + " HP reduced by: " + ad + ". Current HP: " + hp);
        if(hp <= 0)
        {
            die_Insect();
            INSECT_CLASS_LOGGER.log(Level.forName("DIE", 401), "Insect: " + ID + " died!");
        }
    }
    public int get_hp()
    {
        return hp;
    }
    public int get_attackDamage()
    {
        return attackDamage;
    }
    public void set_attackDamage(int damage)
    {
        attackDamage = damage;
        INSECT_CLASS_LOGGER.log(Level.forName("SET", 401), "Insect: " + ID + " attack damage set to: " + attackDamage);
    }
    public int get_availableSteps()
    {
        return availableSteps;
    }
    public void set_availableSteps(int steps)
    {
        availableSteps = steps;
        INSECT_CLASS_LOGGER.log(Level.forName("SET", 401), "Insect: " + ID + " available steps set to: " + availableSteps);
    }
    public Tecton_Class get_Tecton()
    {
        return tecton;
    }
    public void set_Tecton(Tecton_Class t)
    {
        tecton = t;
    }
    public abstract void duplicate_Insect();
    public void paralyse_Insect()
    {
        isParalysed = true;
        INSECT_CLASS_LOGGER.log(Level.forName("PARALYSE", 401), "Insect: " + ID + " is paralysed!");
    }
    public void unparalyse_Insect()
    {
        isParalysed = false; 
    }
    public boolean get_isParalysed()
    {
        INSECT_CLASS_LOGGER.log(Level.forName("EFFECT", 401), "Insect: " + ID + " is paralysed: " + isParalysed);
        return isParalysed;
    }
    public void set_Owner(Player p)
    {
        if (p == null) { // Ensure non-null player
            INSECT_CLASS_LOGGER.log(Level.forName("NULL", 201), "Player is null!");
            return;
        }
        owner = p;
    }
    public Player get_Owner()
    {
        return owner;
    }
    public String get_ID()
    {
        return ID;
    }
    public int getCost()
    {
        return cost;
    }

    


    ///Lehet kéne egy buffer függvény mely eltárolja a spóra effektjét hogy ne tartson örökké 
    /// pl.: Spore_Slowing az csak következő támadásig tart csak kéne jegyezni hogy megtörtén-t e már a támadás vagy akkor hasonló módon a Spore_Paralysing is 
    /// mert az csak következő turn-be lesz érvényes
}
