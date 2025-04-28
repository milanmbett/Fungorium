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
    private String ID;
    private Game game; // új mező
    
    public Thread_Class(Tecton_Class targetTecton, Game game) 
    {
        this.game = game; // eltároljuk a game referenciát
        ID = "Thread" + Integer.toString(game.getPlane().ThreadCollection.size());
        
        try {
            // This will throw an exception if the tecton is dead
            targetTecton.set_Thread(this);
            tecton = targetTecton;
            THREAD_LOGGER.log(Level.forName("CREATE",401),"Thread Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
            game.getPlane().ThreadCollection.add(this);
            THREAD_LOGGER.log(Level.forName("ADD", 403), "Thread: "+ID+ " added to ThreadCollection! ThreadCollection size: " + game.getPlane().ThreadCollection.size());
        } catch (UnsupportedOperationException e) {
            // The tecton is dead, it threw an exception when we tried to set the thread
            tecton = null;
            THREAD_LOGGER.log(Level.forName("DEAD", 404), "Cannot create thread on dead tecton!");
        }
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
        THREAD_LOGGER.log(Level.forName("EXPAND", 401), "Thread: " + ID + " is trying to expand!");

        if (tecton.get_TectonNeighbours().size() == 0) {
            THREAD_LOGGER.log(Level.forName("WARN", 401), "Thread: " + ID + " has no neighbours!");
            return;
        }
        List<Tecton_Class> threadlessTectonNeighbours = new ArrayList<>();
        for (Tecton_Class t : tecton.get_TectonNeighbours()) {
            if (t.get_Thread() == null) {
                threadlessTectonNeighbours.add(t);
            }
        }
        if (threadlessTectonNeighbours.isEmpty()) {
            THREAD_LOGGER.log(Level.forName("WARN", 401), "Thread: " + ID + " has no threadless neighbours to expand to!");
            return;
        }
        // Shuffle the list to try neighbours in random order
        java.util.Collections.shuffle(threadlessTectonNeighbours, new Random());
        boolean expanded = false;
        for (Tecton_Class neighbour : threadlessTectonNeighbours) {
            THREAD_LOGGER.log(Level.forName("EXPAND", 401), "Thread: " + ID + " is attempting to expand to tecton: " + neighbour.get_ID());
            try {
                neighbour.set_Thread(new Thread_Class(neighbour, game));
                THREAD_LOGGER.log(Level.forName("EXPAND", 401), "Thread: " + ID + " successfully expanded to tecton: " + neighbour.get_ID());
                expanded = true;
                break;
            } catch (Exception e) {
                THREAD_LOGGER.log(Level.forName("WARN", 401), "Thread: " + ID + " could not expand to tecton: " + neighbour.get_ID() + " because of: " + e.getMessage());
                // Try next neighbour
            }
        }
        if (!expanded) {
            THREAD_LOGGER.log(Level.forName("WARN", 401), "Thread: " + ID + " could not expand to any neighbour.");
        }
    }
    public void die_Thread()
    {
        tecton.set_Thread(null);
        game.getPlane().ThreadCollection.remove(this);
        
    }
    //Végig megyünk a fonál tectonján lévő összes bogaron és megnézzük hogy van-e olyan amelyik paralizálva van ha van megesszük, 
    //és növesztünk egy gombát(Shroomlet) ha még nincsen 
    public void tryToEat_Insect() 
    {
        
        THREAD_LOGGER.log(Level.forName("INFO", 400), "Trying to eat insect on tecton: " + tecton.get_ID() + " with thread: " + ID);
        // Iterate over a copy to avoid ConcurrentModificationException
        for (Insect_Class ins : new ArrayList<>(tecton.get_InsectsOnTecton())) 
        {
            if(ins.get_isParalysed())
            {
                ins.die_Insect();
                THREAD_LOGGER.log(Level.forName("EAT", 401), "Thread: " + ID + " is eating insect: " + ins.get_ID() + " on tecton: " + tecton.get_ID());
                if(tecton.get_Mushroom() == null)
                {
                    THREAD_LOGGER.log(Level.forName("EAT", 401), "Thread: " + ID + " is growing mushroom on tecton: " + tecton.get_ID());
                    Mushroom_Shroomlet ms = new Mushroom_Shroomlet(tecton,null); //TODO: Beállítani ,hogy ugyanaz a gombász legyen? Vagy lehet unclaimed gomba is?
                }
            }    
        }
    }
    public String get_ID()
    {
        return ID;
    }
}

