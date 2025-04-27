package com.coderunnerlovagjai.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Insect_Tektonizator extends Insect_Class {
    private static final Logger INSECT_TEKTONIZATOR_LOGGER = LogManager.getLogger(Insect_Tektonizator.class);

    public Insect_Tektonizator(Tecton_Class targetTecton, Player p) {
        hp = 50;
        attackDamage = 10;
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        ID = "Insect_Tektonizator" + Integer.toString(Plane.InsectCollection.size());
        cost = 50;
        owner = p;
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CREATE", 401), "Insect_Tektonizator Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("ADD", 403), "Insect_Tektonizator: " + ID + " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());
    }

    public Insect_Tektonizator(Tecton_Class targetTecton, int hp, int ad, int as, Player p) {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        ID = "Insect_Tektonizator" + Integer.toString(Plane.InsectCollection.size());
        owner = p;
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CREATE", 401), "Insect_Tektonizator Created! ID: " + ID + " on Tecton: " + tecton.get_ID());
        Plane.InsectCollection.add(this);
        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("ADD", 403), "Insect_Tektonizator: " + ID + " added to InsectCollection! InsectCollection size: " + Plane.InsectCollection.size());
    }

    public void tectonCrack() {
        if (!tecton.canBeCracked()) {
            INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("WARN", 404), "Tecton:" + tecton.get_ID() + " cannot be cracked!");
            return;
        }

        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CRACK", 401), "Insect_Tektonizator: " + ID + " is cracking tecton: " + tecton.get_ID());

        // Create two new tectons
        Tecton_Basic newTecton1 = new Tecton_Basic();
        Tecton_Basic newTecton2 = new Tecton_Basic();

        // Get the old tecton's neighbours
        List<Tecton_Class> neighbours = new ArrayList<>(tecton.get_TectonNeighbours());

        // Remove the original tecton from the collection
        Plane.TectonCollection.remove(tecton);

        tecton.remove_InsectsOnTecton();
        tecton.remove_Mushroom();
        tecton.remove_Spore();
        tecton.remove_Thread();
        tecton.remove_TectonNeighbours();


        // Set up the new tectons' neighbours
        newTecton1.add_TectonNeighbour(newTecton2);
        newTecton2.add_TectonNeighbour(newTecton1);

        // Distribute the old tecton's neighbours to the new tectons
        for (int i = 0; i < neighbours.size(); i++) {
            if (i % 2 == 0) {
                newTecton1.add_TectonNeighbour(neighbours.get(i));
                neighbours.get(i).add_TectonNeighbour(newTecton1);
            } else {
                newTecton2.add_TectonNeighbour(neighbours.get(i));
                neighbours.get(i).add_TectonNeighbour(newTecton2);
            }
            neighbours.get(i).del_TectonNeighbour(tecton);
        }



        INSECT_TEKTONIZATOR_LOGGER.log(Level.forName("CRACK", 401), "Tecton cracked successfully.  New tectons: " + newTecton1.get_ID() + " and " + newTecton2.get_ID());
    }

    @Override
    public void duplicate_Insect() {
        Insect_Tektonizator duplicated = new Insect_Tektonizator(tecton, hp, attackDamage, availableSteps, owner);
    }
}