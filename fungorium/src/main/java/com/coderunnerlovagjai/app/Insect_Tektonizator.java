package com.coderunnerlovagjai.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Insect_Tektonizator extends Insect_Class {
    private static final Logger INSECT_TEKTONIZATOR_LOGGER = LogManager.getLogger(Insect_Tektonizator.class);
    public static final int VIEWCOST = 500;
public Insect_Tektonizator(Tecton_Class targetTecton, Player p) {
    hp = 50;
    attackDamage = 10;
    availableSteps = 1;
    tecton = targetTecton;
    // Set owner FIRST, before using it
    owner = p;
    ID = "Insect_Tektonizator" + Integer.toString(owner.getGame().getPlane().InsectCollection.size());
    cost = VIEWCOST;
    INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CREATE", 401), 
        "Insect_Tektonizator Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
    // Let placeInsect handle adding to collections
}

    public Insect_Tektonizator(Tecton_Class targetTecton, int hp, int ad, int as, Player p) {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        ID = "Insect_Tektonizator" + Integer.toString(owner.getGame().getPlane().InsectCollection.size());
        owner = p;
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CREATE", 401), "Insect_Tektonizator Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        owner.getGame().getPlane().InsectCollection.add(this);
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("ADD", 403), "Insect_Tektonizator: " + ID + " added to InsectCollection! InsectCollection size: " + owner.getGame().getPlane().InsectCollection.size());
    }


    public void tectonCrack() {
        if (tecton == null) {
            INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("ERROR", 404), "Target tecton for cracking is null!");
            return;
        }

        // First, check if the tecton is already dead.
        if (tecton instanceof Tecton_Dead || tecton.isDead()) { // Added tecton.isDead() for robustness
            INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("WARN", 404), "Tecton:" + tecton.get_ID() + " is already dead!");
            return;
        }

        // Next, specifically handle Tecton_Base destruction.
        // This is different from "cracking" a regular tecton into two.
        if (tecton instanceof Tecton_Base) {
            INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CRACK", 401), "Insect_Tektonizator: " + ID + " is destroying Tecton_Base: " + tecton.get_ID());
            ((Tecton_Base) tecton).setDeadTrue(); // This marks the base as dead and calls game.endGame()
            // owner.getGame().setGameOver(true); // This is now redundant as Tecton_Base.setDeadTrue() handles it.
            return;
        }

        // For other tecton types, check if they are designed to be cracked (split).
        if (!tecton.canBeCracked()) {
            INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("WARN", 404), "Tecton:" + tecton.get_ID() + " cannot be cracked (e.g., specific type that doesn't split).");
            return;
        }

        // Proceed with the original logic for cracking a regular tecton into two Tecton_Dead instances.
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CRACK", 401), "Insect_Tektonizator: " + ID + " is cracking tecton: " + tecton.get_ID());

        Plane plane = owner.getGame().getPlane();
        List<Tecton_Class> neighbours = new ArrayList<>(tecton.get_TectonNeighbours());

        // Create two new dead tectons
        // Ensure they get unique IDs and are added to the plane's collection
        Tecton_Dead newTecton1 = new Tecton_Dead();
        newTecton1.setID(plane.TectonCollection.size()); // Simple way to get a new ID index
        plane.TectonCollection.add(newTecton1);
        newTecton1.setPosition(tecton.getPosition().x - 15, tecton.getPosition().y); // Adjust position slightly

        Tecton_Dead newTecton2 = new Tecton_Dead();
        newTecton2.setID(plane.TectonCollection.size()); // Simple way to get a new ID index
        plane.TectonCollection.add(newTecton2);
        newTecton2.setPosition(tecton.getPosition().x + 15, tecton.getPosition().y); // Adjust position slightly
        
        // Remove the original tecton from the plane's collection
        plane.TectonCollection.remove(tecton);

        // Clear entities from the original tecton model
        tecton.remove_InsectsOnTecton();
        tecton.remove_Mushroom();
        tecton.remove_Spore();
        tecton.remove_Thread();
        
        // Update neighbour relationships
        // 1. Remove the old tecton from its original neighbours' lists
        for (Tecton_Class neighbour : neighbours) {
            neighbour.del_TectonNeighbour(tecton);
        }

        // 2. Make the two new dead tectons neighbours of each other
        newTecton1.add_TectonNeighbour(newTecton2);
        newTecton2.add_TectonNeighbour(newTecton1);

        // 3. Distribute the original neighbours between the two new dead tectons
        for (int i = 0; i < neighbours.size(); i++) {
            Tecton_Class originalNeighbour = neighbours.get(i);
            if (i % 2 == 0) {
                newTecton1.add_TectonNeighbour(originalNeighbour);
                originalNeighbour.add_TectonNeighbour(newTecton1);
            } else {
                newTecton2.add_TectonNeighbour(originalNeighbour);
                originalNeighbour.add_TectonNeighbour(newTecton2);
            }
        }
        // The original tecton's neighbour list is implicitly cleared by it being dereferenced
        // or if tecton.remove_TectonNeighbours() was called (which it was in original code).

        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CRACK", 401), "Tecton cracked successfully. New tectons: " + newTecton1.get_ID() + " and " + newTecton2.get_ID());
    }


    @Override
    public void duplicate_Insect() {
        Insect_Tektonizator duplicated = new Insect_Tektonizator(tecton, hp, attackDamage, availableSteps, owner);
    }
}