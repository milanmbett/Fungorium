package com.coderunnerlovagjai.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Tecton_Class {
    private static final Logger TECTON_CLASS_LOGGER = LogManager.getLogger(Tecton_Class.class);
    protected List<Tecton_Class> Neighbours = new ArrayList<>();

    protected Mushroom_Class mushroom;
    protected List<Insect_Class> insectsOnTecton = new ArrayList<>();
    protected Basic_Spore spore;
    protected Thread_Class thread;
    protected String ID;

    public Tecton_Class() {
        TECTON_CLASS_LOGGER.log(Level.forName("INIT", 402), "Tecton_Class Constructor called!");

    }

    public abstract boolean isDead();

    // Method to determine if this tecton can be cracked
    public boolean canBeCracked() {
        return true; // Default implementation - most tectons can be cracked
    }

    public List<Tecton_Class> get_TectonNeighbours() {
        if (Neighbours == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "Tecton Neighbours is null!");
            return null;
        }

        TECTON_CLASS_LOGGER.log(Level.forName("GET", 400), "Tecton's Neighbours: " + Neighbours);
        return Neighbours;
    }

    public void add_TectonNeighbour(Tecton_Class t) {
        Neighbours.add(t);
    }

    public void del_TectonNeighbour(Tecton_Class t) {
        Neighbours.remove(t);
    }

    public void remove_TectonNeighbours() {
        for (Tecton_Class t : Neighbours) {
            t.del_TectonNeighbour(this);
        }
    }

    public void set_TectonNeighbours(List<Tecton_Class> t) {
        Neighbours = t;
    }

    // Tectonon lévő összes dolog halálát okozó függvény
    public Tecton_Dead die_Tecton() {
        remove_InsectsOnTecton();
        remove_Mushroom();
        remove_Spore();
        remove_Thread();
        Plane.TectonCollection.remove(this);

        // Create a new dead tecton from this live instance.
        Tecton_Dead dead = new Tecton_Dead(this);

        // Replace this tecton in all neighbours.
        List<Tecton_Class> neighbours = new ArrayList<>(get_TectonNeighbours());
        for (Tecton_Class neighbour : neighbours) {
            neighbour.del_TectonNeighbour(this);
        }

        return dead;
    }

    public void set_InsectsOnTecton(List<Insect_Class> insectList) {
        insectsOnTecton = insectList;
    }

    public void remove_InsectsOnTecton() {
        List<Insect_Class> insectsToRemove = new ArrayList<>(insectsOnTecton);
        for (Insect_Class ins : insectsToRemove) {
            ins.die_Insect();
        }
        insectsOnTecton.clear();
    }

    public List<Insect_Class> get_InsectsOnTecton() {
        if (insectsOnTecton == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "Insects on Tecton is null!");
            return null;
        }
        TECTON_CLASS_LOGGER.log(Level.forName("GET", 400), "Tecton's Insects: " + insectsOnTecton);
        return insectsOnTecton;
    }

    public void set_Mushroom(Mushroom_Class mush) {
        if (mush == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "Mushroom is set to null!");
            mushroom = null;
        } else if (mushroom != null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "There is already a mushroom on this tecton!");
        } else {
            mushroom = mush;
            TECTON_CLASS_LOGGER.log(Level.forName("SET", 400), "Tecton's Mushroom: " + mushroom);
        }
    }

    public void remove_Mushroom() {
        if (mushroom == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("REMOVE", 404), "Mushroom is already null, nothing to remove.");
            return;
        }
        mushroom.die_Mushroom();
    }

    public Mushroom_Class get_Mushroom() {
        if (mushroom == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "Mushroom is null!");
            return null;
        }
        TECTON_CLASS_LOGGER.log(Level.forName("GET", 400), "Tecton's Mushroom: " + mushroom);
        return mushroom;
    }

    public void set_Spore(Basic_Spore s) {
        spore = s;
    }

    public void remove_Spore() {
        if (spore == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("REMOVE", 404), "Spore is already null, nothing to remove.");
            return;
        }
        spore.die_Spore();
    }

    public Basic_Spore get_Spore() {
        if (spore == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "Spore is null!");
            return null;
        }
        TECTON_CLASS_LOGGER.log(Level.forName("GET", 400), "Tecton's Spore: " + spore);
        return spore;
    }

    public void set_Thread(Thread_Class t) {
        thread = t;
    }

    public void remove_Thread() {
        if (thread == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("REMOVE", 404), "Thread is already null, nothing to remove.");
            return;
        }
        thread.die_Thread();
    }

    public Thread_Class get_Thread() {
        if (thread == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "Thread is null!");
            return null;
        }
        TECTON_CLASS_LOGGER.log(Level.forName("GET", 400), "Tecton's Thread: " + thread);
        return thread;
    }

    public String get_ID() {
        if (ID == null) {
            TECTON_CLASS_LOGGER.log(Level.forName("NULL", 201), "Tecton ID is null!");
            return null;
        }
        TECTON_CLASS_LOGGER.log(Level.forName("GET", 400), "Tecton's ID: " + ID);
        return ID;
    }
}