package com.coderunnerlovagjai.app;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Thread_Class 
{
    private static final Logger THREAD_LOGGER = LogManager.getLogger(Thread_Class.class);
    private Tecton_Class tecton;
    
    public Thread_Class(Tecton_Class targetTecton)
    {
        tecton = targetTecton;
        tecton.set_Thread(this);
        Plane.ThreadCollection.add(this);
    }
    public Tecton_Class get_Tecton()
    {
        return tecton;
    }
    public void set_Tecton(Tecton_Class t)
    {
        tecton = t;
    }
    //Jelenleg a fonál egy véletlenszerű szomszédos tectonra ugrik, ha nincs szomszédos tecton akkor nem ugrik sehova, viszont halott tektonra is rak(Ez nem jó)
    //TODO: Ne lehessen halott tecton-ra létrehozni fonalakat   
    //TODO: Megvizsgálni ,hogy van-e gomba? Asszem specifikáció azt mondja hogy csak akkor nőhet? Nem tudom tényleg
    public void expand_Thread()
    {
              
        if(tecton.get_TectonNeighbours().size()==0)
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
        threadlessTectonNeighbours.get(rand).set_Thread(new Thread_Class(threadlessTectonNeighbours.get(rand)));
    }
    public void die_Thread()
    {
        //TODO: következő körben a fonál meghalása
        tecton.set_Thread(null);
        Plane.ThreadCollection.remove(this);
    }
    //Végig megyünk a fonál tectonján lévő összes bogaron és megnézzük hogy van-e olyan amelyik paralizálva van ha van megesszük, 
    //és növesztünk egy gombát(Shroomlet) ha még nincsen 
    public void tryToEat_Insect() //TODO: Turn végén meghívni Thread_Collection összes elemére
    {
        for (Insect_Class ins : tecton.get_InsectsOnTecton()) 
        {
            if(ins.get_isParalysed())
            {
                ins.die_Insect();
                if(tecton.get_Mushroom().equals(null))
                {
                    tecton.set_Mushroom(new Mushroom_Shroomlet(tecton,null)); //TODO: Beállítani ,hogy ugyanaz a gombász legyen? Vagy lehet unclaimed gomba is?
                }
            }    
        }
    }
}

