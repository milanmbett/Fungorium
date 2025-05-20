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
        if (base1 != null) {
            TectonCollection.add(base1);
            base1.setID(0);
            // Set base1 position (leftmost)
            base1.setPosition(100, 300);
            PLANE_LOGGER.log(Level.INFO, "Base1 added to TectonCollection: " + base1.get_ID() + "Tectoncollection: " + TectonCollection.size());
        } else {
            PLANE_LOGGER.log(Level.ERROR, "Base1 is null and cannot be added!");
        }

        this.base2 = new Tecton_Base(player2, game);
        if (base2 != null) {
            TectonCollection.add(base2);
            base2.setID(1);
            // Set base2 position (rightmost)
            base2.setPosition(700, 300);
            PLANE_LOGGER.log(Level.INFO, "Base2 added to TectonCollection: " + base2.get_ID());
        } else {
            PLANE_LOGGER.log(Level.ERROR, "Base2 is null and cannot be added!");
        }
    }

    public Tecton_Base getBase1() {
        return base1;
    }

    public Tecton_Base getBase2() {
        return base2;
    }

    public void init_Plane(Game game) {
        TectonCollection.clear();
        initBases(game.getPlayer1(), game.getPlayer2(), game);

        // Oszlopok: 2-3-4-4-3-2 -> lehet opcionálisan változtatni -> Map választás
        int[] colHeights = {2, 3, 4, 4, 3, 2};
        int hexRadius = 60;
        int x0 = 165, y0 = 120; // Center offsets
        int xStep = (int)(hexRadius * Math.sqrt(3))-18;
        int yStep = (int)(hexRadius * 1.5)+8;

        // Grid: oszlopokban tároljuk
        List<List<Tecton_Basic>> grid = new ArrayList<>();
        int id = 2; // IDs start after bases

        // Először létrehozzuk az oszlopokat
        for (int col = 0; col < colHeights.length; col++) {
            List<Tecton_Basic> colList = new ArrayList<>();
            int rows = colHeights[col];
            // Függőleges középre igazítás
            int colHeight = (rows - 1) * yStep;
            int yStart = y0 + ((4 * yStep - colHeight) / 2);

            int x = x0 + col * xStep;
            for (int row = 0; row < rows; row++) {
                Tecton_Basic t = new Tecton_Basic();
                t.setID(id++);
                // Példa: szálak kiosztása, ha kell
                if (t.get_ID().equals("Tecton_Basic_3") || t.get_ID().equals("Tecton_Basic_2") ||
                    t.get_ID().equals("Tecton_Basic_18") || t.get_ID().equals("Tecton_Basic_19") ||
                    t.get_ID().equals("Tecton_Base_1") || t.get_ID().equals("Tecton_Base_2")) {
                    t.set_Thread(new Thread_Class(t, game));
                }
                int y = yStart + row * (yStep);
                t.setPosition(x, y);
                colList.add(t);
                TectonCollection.add(t);
            }
            grid.add(colList);
        }

        // Szomszédok összekötése (hatszög logika, oszlop-alapú)
        for (int col = 0; col < grid.size(); col++) {
            for (int row = 0; row < grid.get(col).size(); row++) {
                Tecton_Basic t = grid.get(col).get(row);
                // Lehetséges szomszéd irányok (col, row) eltolások
                int[][] directions = {
                    {-1,  0}, // balra
                    {-1,  1}, // balra-le
                    { 0, -1}, // fel
                    { 0,  1}, // le
                    { 1,  0}, // jobbra
                    { 1, -1}  // jobbra-fel
                };
                for (int[] d : directions) {
                    int nc = col + d[0];
                    int nr = row + d[1];
                    if (nc >= 0 && nc < grid.size() && nr >= 0 && nr < grid.get(nc).size()) {
                        t.add_TectonNeighbour(grid.get(nc).get(nr));
                    }
                }
            }
        }

        // Bázisok pozícionálása (bal és jobb oldal)
        // Bal bázis a bal szélső oszlop közepéhez
        int leftCol = 0;
        int leftBaseY = y0-8 + ((4 * yStep - ((colHeights[leftCol] - 1) * yStep)) / 2) + ((colHeights[leftCol] - 1) * yStep) / 2;
        base1.setPosition(x0 - 100, leftBaseY);

        // Jobb bázis a jobb szélső oszlop közepéhez
        int rightCol = colHeights.length - 1;
        int rightBaseX = x0 + rightCol * xStep + 100;
        int rightBaseY = y0-8 + ((4 * yStep - ((colHeights[rightCol] - 1) * yStep)) / 2) + ((colHeights[rightCol] - 1) * yStep) / 2;
        base2.setPosition(rightBaseX, rightBaseY);

        // Bázis1 kapcsolása bal szélső oszlop minden eleméhez
        for (Tecton_Basic t : grid.get(0)) {
            base1.add_TectonNeighbour(t);
            t.add_TectonNeighbour(base1);
        }
        // Bázis2 kapcsolása jobb szélső oszlop minden eleméhez
        for (Tecton_Basic t : grid.get(grid.size() - 1)) {
            base2.add_TectonNeighbour(t);
            t.add_TectonNeighbour(base2);
        }
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
    if(m.get_Owner().getRole() != RoleType.MUSHROOM) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Player does not have Mushroom role!");
        return;
    }
    if (targetTecton == null) {
        PLANE_LOGGER.log(Level.forName("NULL", 201), "Target tecton is null!");
        return;
    }
    
    // Do validation BEFORE modifying anything
    if (targetTecton.get_Mushroom() != null || targetTecton.isDead()) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is already occupied or dead!");
        return;
    }
    if(targetTecton.thread == null) {
        PLANE_LOGGER.log(Level.forName("NULL", 201), "Thread is null!");
        return;
    }
    
    // Check currency
    int cost = m.getCost();
    if (m.get_Owner().getIncome() < cost) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Not enough currency to place mushroom!");
        return;
    }
    
    // Only after all validation passes, we modify state
    m.get_Owner().decreaseIncome(cost);
    targetTecton.set_Mushroom(m);
    m.set_Tecton(targetTecton);
    MushroomCollection.add(m);
    
    PLANE_LOGGER.log(Level.forName("PLACE", 401), "Mushroom: " + m.get_ID() + 
        " placed on Tecton: " + targetTecton.get_ID() + ". Cost: " + cost + 
        ", remaining resources: " + m.get_Owner().getIncome());
}
    public void move_Insect(Player player,Insect_Class ins, Tecton_Class targetTecton)
    {
        if(player.getRole()!= RoleType.INSECT) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Player does not have Insect role!");
            return;
        }
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
            throw new IllegalArgumentException("Insect: " + ins.get_ID() + " has no available steps!");
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
        if(targetTecton.get_InsectsOnTecton().size()>=5) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is full! Cannot move insect there.");
            return;
        }

        ins.get_Tecton().get_InsectsOnTecton().remove(ins);
        ins.set_Tecton(targetTecton); // Set the insect's tecton
        ins.set_availableSteps(ins.get_availableSteps() - 1); // Decrease available steps
        targetTecton.get_InsectsOnTecton().add(ins);
        PLANE_LOGGER.log(Level.forName("MOVE", 401), "Insect: " + ins.get_ID() + " moved to Tecton: " + targetTecton.get_ID() + ". Available steps: " + ins.get_availableSteps());
    }
   public boolean placeInsect(Insect_Class insect, Tecton_Class target) {
    // Check if the target tecton is valid
    if (target == null) {
        PLANE_LOGGER.log(Level.forName("NULL", 201), "Target tecton is not valid.");
        return false;
    }
    if (target.isDead()) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is dead. Cannot place insect there.");
        return false;
    }
    if(target.get_Thread() == null) {
        PLANE_LOGGER.log(Level.forName("NULL", 201), "You can only place insects on tectons with a thread.");
        return false;
    }
    // Check if the insect is valid and not already placed
    if (insect == null) {
        PLANE_LOGGER.log(Level.forName("NULL", 201), "Insect is not valid.");
        return false;
    }
    if(insect.get_Owner().getRole() != RoleType.INSECT) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Player does not have Insect role!");
        return false;
    }
    if(target.get_InsectsOnTecton().size() >= 5) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is full! Cannot place insect there.");
        return false;
    }
    if(target.get_InsectsOnTecton().contains(insect)) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Insect already exists on this tecton!");
        return false;
    }
    if (target.get_Mushroom() == null) {
        PLANE_LOGGER.log(Level.forName("NULL", 201), "You can only place insects on tectons with a mushroom.");
        return false;
    }
    if(!target.get_ID().startsWith("Tecton_Base")){
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "You can only place insects on your own base!");
        return false;
    }
    if(target.get_Mushroom().get_Owner().getId()!=insect.get_Owner().getId()){
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "You can only place insects on your own mushroom!");
        return false;
    }
    
    // Check if the player has enough resources to place the insect
    int cost = insect.getCost();
    if (insect.get_Owner().getIncome() < cost) {
        PLANE_LOGGER.log(Level.forName("ERROR", 401), "Not enough resources to place the insect.");
        return false;
    }

    // Only after all validation passes - deduct the cost and modify collections
    insect.get_Owner().decreaseIncome(cost);
    insect.set_Tecton(target);
    target.get_InsectsOnTecton().add(insect);
    InsectCollection.add(insect);
    
    // Log the successful placement
    PLANE_LOGGER.log(Level.forName("PLACE", 401), 
        "Insect: " + insect.get_ID() + 
        " placed on Tecton: " + target.get_ID() + 
        ". Cost: " + cost + 
        ", remaining resources: " + insect.get_Owner().getIncome() + ".");
    
    return true;
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

        if(targetMushroom.get_Owner().getRole() != RoleType.MUSHROOM) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Player does not have Mushroom role!");
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
