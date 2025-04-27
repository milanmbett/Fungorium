package com.coderunnerlovagjai.app;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Plane 
{
    private final Logger PLANE_LOGGER = LogManager.getLogger(Plane.class);
    public final List<Basic_Spore> SporeCollection = new ArrayList<>();
    public final List<Tecton_Class> TectonCollection = new ArrayList<>();
    public final List<Insect_Class> InsectCollection = new ArrayList<>();
    public final List<Thread_Class> ThreadCollection = new ArrayList<>();
    public final List<Mushroom_Class> MushroomCollection = new ArrayList<>();
    //TODO: ID-k jobban kell hogy működjenek ,mert jelenleg nem biztos ,hogy egyediek lesznek!
    //TODO: Páya kialakítása, elrendezése
    private Tecton_Base base1;
    private Tecton_Base base2;

    public void initBases(Player player1, Player player2, Game game) {
        this.base1 = new Tecton_Base(player1, game);
        this.base2 = new Tecton_Base(player2, game);
        PLANE_LOGGER.log(Level.forName("INIT", 402), "Bases initialized for players.");
    }

    public Tecton_Base getBase1() {
        return base1;
    }

    public Tecton_Base getBase2() {
        return base2;
    }

    public void init_Plane(Game game) // now needs Game
    {
        Tecton_Basic t1 = new Tecton_Basic();
        Tecton_Basic t2 = new Tecton_Basic();
        Tecton_Basic t3 = new Tecton_Basic();
        Tecton_Basic t4 = new Tecton_Basic();
        Tecton_Basic t5 = new Tecton_Basic();
        Tecton_Basic t6 = new Tecton_Basic();
        Tecton_Basic t7 = new Tecton_Basic();
        Tecton_Basic t8 = new Tecton_Basic();
        Tecton_Basic t9 = new Tecton_Basic();
        Tecton_Basic t10 = new Tecton_Basic();
        Tecton_Basic t11 = new Tecton_Basic();
        Tecton_Basic t12 = new Tecton_Basic();
        Tecton_Basic t13 = new Tecton_Basic();
        Tecton_Basic t14 = new Tecton_Basic();
        Tecton_Basic t15 = new Tecton_Basic();
        Tecton_Basic t16 = new Tecton_Basic();

        TectonCollection.add(t1);
        TectonCollection.add(t2);
        TectonCollection.add(t3);
        TectonCollection.add(t4);
        TectonCollection.add(t5);
        TectonCollection.add(t6);
        TectonCollection.add(t7);
        TectonCollection.add(t8);
        TectonCollection.add(t9);
        TectonCollection.add(t10);
        TectonCollection.add(t11);
        TectonCollection.add(t12);
        TectonCollection.add(t13);
        TectonCollection.add(t14);
        TectonCollection.add(t15);
        TectonCollection.add(t16);
        

        t1.add_TectonNeighbour(t2);
        t1.add_TectonNeighbour(t5);
        t2.add_TectonNeighbour(t3);
        t2.add_TectonNeighbour(t6);
        t2.add_TectonNeighbour(base1);
        t3.add_TectonNeighbour(t4);
        t3.add_TectonNeighbour(t7);
        t3.add_TectonNeighbour(base1);
        t4.add_TectonNeighbour(t8);
        t5.add_TectonNeighbour(t6);
        t5.add_TectonNeighbour(t9);
        t6.add_TectonNeighbour(t7);
        t6.add_TectonNeighbour(t10);
        t7.add_TectonNeighbour(t8);
        t7.add_TectonNeighbour(t11);
        t8.add_TectonNeighbour(t12);
        t9.add_TectonNeighbour(t10);
        t9.add_TectonNeighbour(t13);
        t10.add_TectonNeighbour(t11);
        t10.add_TectonNeighbour(t14);
        t11.add_TectonNeighbour(t12);
        t11.add_TectonNeighbour(t15);
        t12.add_TectonNeighbour(t16);
        t13.add_TectonNeighbour(t14);
        t14.add_TectonNeighbour(base2);
        t14.add_TectonNeighbour(t15);
        t15.add_TectonNeighbour(base2);
        t15.add_TectonNeighbour(t16);

        // add threads on t2, t3, t14 and t15
        Thread_Class tr2  = new Thread_Class(t2,  game);
        Thread_Class tr3  = new Thread_Class(t3,  game);
        Thread_Class tr14 = new Thread_Class(t14, game);
        Thread_Class tr15 = new Thread_Class(t15, game);

        t2.set_Thread(tr2);
        t3.set_Thread(tr3);
        t14.set_Thread(tr14);
        t15.set_Thread(tr15);
        ThreadCollection.add(tr2);
        ThreadCollection.add(tr3);
        ThreadCollection.add(tr14);
        ThreadCollection.add(tr15);

        
        PLANE_LOGGER.log(Level.forName("INIT_THREAD", 402),
            "Threads initialized on t2, t3, t14, t15");
    }
    public void place_Spore(Basic_Spore spore, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Spore() == null)
        {
            targetTecton.set_Spore(spore);
        }
    }
    public void place_Thread(Thread_Class t, Tecton_Class targetTecton)
    {
        if(targetTecton.get_Thread() == null)
        {
            targetTecton.set_Thread(t);
        }
    }
    public void place_Mushroom(Mushroom_Class m, Tecton_Class targetTecton)
    {
        if (targetTecton== null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Target tecton is null!");
            return;
        }

        //TODO: base tekton ellenőrzés

        if (targetTecton.get_Mushroom() != null || targetTecton.isDead()) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is already occupied or dead!");
            return;
        }
        if(targetTecton.thread == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Thread is null!");
            return;
        }

        
        if(targetTecton.get_Mushroom() == null)
        {
            // Check currency
            int cost=m.getCost(); // Placeholder for cost calculation
            if (m.get_Owner().getIncome() < cost) {
                PLANE_LOGGER.log(Level.forName("ERROR", 401), "Not enough currency to place mushroom!");
                return;
            }
            m.get_Owner().decreaseIncome(cost); // Decrease player's currency by cost
            targetTecton.set_Mushroom(m);
            m.set_Tecton(targetTecton); // Set the mushroom's tecton
            MushroomCollection.add(m); // Add mushroom to the collection
            PLANE_LOGGER.log(Level.forName("PLACE", 401), "Mushroom: " + m.get_ID() + " placed on Tecton: " + targetTecton.get_ID());
        }
    }
    public void move_Insect(Player player,Insect_Class ins, Tecton_Class targetTecton)
    {
        if (targetTecton == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Target tecton is null!");
            return;
        }
        if (ins == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Insect is null!");
            return;
        }
        if (ins.get_Tecton() == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Insect's tecton is null!");
            return;
        }
        if (targetTecton.isDead()) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is dead!");
            return;
        }
        if (targetTecton.get_InsectsOnTecton().contains(ins)) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Insect is already on the target tecton!");
            return;
        }
        if (targetTecton.get_Thread() == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Target tecton has no thread!");
            return;
        }
        if(player.getId() != ins.get_Owner().getId()){
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "You cannot move an insect that does not belong to you.");
            return;
        }
        if(ins.get_availableSteps() <= 0) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Insect: " + ins.get_ID() + " has no available steps!");
            return;
        }
        if (!ins.get_Tecton().get_TectonNeighbours().contains(targetTecton)) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is not a neighbour of the insect's current tecton!");
            return;  
        }
        if (ins.get_Tecton().equals(targetTecton)) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Insect: " + ins.get_ID() + " is already on the target tecton!");
            return;
        }
        if (ins.get_Tecton().get_Thread() == null || targetTecton.get_Thread() == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "There is no thread on either tecton!");
            return;
        }

        ins.get_Tecton().get_InsectsOnTecton().remove(ins);
        ins.set_Tecton(targetTecton); // Set the insect's tecton
        ins.set_availableSteps(ins.get_availableSteps() - 1); // Decrease available steps
        targetTecton.get_InsectsOnTecton().add(ins);
        PLANE_LOGGER.log(Level.forName("MOVE", 401), "Insect: " + ins.get_ID() + " moved to Tecton: " + targetTecton.get_ID() + ". Available steps: " + ins.get_availableSteps());
    }
    public void placeInsect(Insect_Class insect, Tecton_Class target) {
        // Check if the target tecton is valid
        if (target == null) {
            System.out.println("Target tecton is not valid.");
            return;
        }
        if (target.isDead()) {
            System.out.println("Target tecton is dead. Cannot place insect there.");
            return;
        }
        if(target.get_Thread() == null) {
            System.out.println("You can only place insects on tectons with a thread.");
            return;
        }
        // Check if the insect is valid and not already placed
        if (insect == null) {
            System.out.println("Insect is not valid.");
            return;
        }

        // Check if the player has enough resources to place the insect
        int cost = insect.getCost(); // Assuming `Insect_Class` has a `getCost` method
        if (insect.get_Owner().getIncome() < cost) {
            System.out.println("Not enough resources to place the insect.");
            return;
        }

        // Deduct the cost from the player's resources
        insect.get_Owner().decreaseIncome(cost);

        // Place the insect on the target tecton
        insect.set_Tecton(target);
        target.get_InsectsOnTecton().add(insect);

        // Log the successful placement
        
        PLANE_LOGGER.log(Level.forName("PLACE", 401), "Insect: " + insect.get_ID() + " placed on Tecton: " + target.get_ID() + ". Cost: " + cost + ", remaining resources: " + insect.get_Owner().getIncome() + ".");
    }
    
    public void removeInsect(Insect_Class insect) {
        if (InsectCollection.remove(insect)) {
            PLANE_LOGGER.log(Level.forName("REMOVE", 401), "Insect: " + insect.get_ID() + " removed from InsectCollection.");
        } else {
            PLANE_LOGGER.log(Level.forName("ERROR", 404), "Failed to remove Insect: " + insect.get_ID() + " from InsectCollection.");
        }
    }

    public Insect_Class getInsectByID(String id) {
        for (Insect_Class insect : InsectCollection) {
            if (insect.get_ID().equalsIgnoreCase(id)) {
                return insect;
            }
        }

        PLANE_LOGGER.log(Level.forName("ERROR", 404), "Insect with ID {} not found!", id);
        return null;
    }

    public Mushroom_Class getMushroomByID(String id) {
        for (Mushroom_Class mushroom : MushroomCollection) {
            if (mushroom.get_ID().equalsIgnoreCase(id)) {
                return mushroom;
            }
        }

        PLANE_LOGGER.log(Level.forName("ERROR", 404), "Mushroom with ID {} not found!", id);
        return null;
    }
    public Basic_Spore upgradeSpore(Basic_Spore spore, Mushroom_Class targetMushroom) {
        if (targetMushroom == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Target mushroom is null!");
            return null;
        }
        
        // Check currency
        int cost = 50; // Placeholder for cost calculation
        if (targetMushroom.get_Owner().getIncome() < cost) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Not enough currency to upgrade spore!");
            return null;
        }
        targetMushroom.get_Owner().decreaseIncome(cost); // Decrease player's currency by cost
        targetMushroom.spawn_Spores(spore);
        SporeCollection.add(spore);
        return spore;

    }
}
