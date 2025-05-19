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
        // Clear previous tectons if any
        TectonCollection.clear();
        // Place bases first
        initBases(game.getPlayer1(), game.getPlayer2(), game);
        // Hex grid: 4 rows, 5 columns (excluding bases)
        int rows = 4, cols = 5;
        int hexRadius = 60; // Increased radius for larger, touching hexes
        int x0 = 200, y0 = 170; // Adjusted starting offset to center the smaller grid
        Tecton_Basic[][] grid = new Tecton_Basic[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tecton_Basic t = new Tecton_Basic();
                t.setID(2 + row * cols + col);
                //EZ NAGYON ROSSZ :---) my bad nem akartam ezzel baszkodni
                if(t.get_ID().equals("Tecton_Basic_7") || t.get_ID().equals("Tecton_Basic_12") || t.get_ID().equals("Tecton_Basic_17") 
                || t.get_ID().equals("Tecton_Basic_11") || t.get_ID().equals("Tecton_Basic_16") || t.get_ID().equals("Tecton_Basic_21"))
                {
                    t.set_Thread(new Thread_Class(t, game));
                }
                // Calculate hex position to create perfect honeycomb pattern
                // For flat-topped hexagons that touch at sides:
                // - Horizontal distance between centers = 2 × radius × cos(30°) = radius × √3
                // - Vertical distance between centers = radius × 1.5 for row spacing
                int x = x0 + col * (int)(hexRadius * Math.sqrt(3)); // Exact horizontal spacing for touching
                
                // Offset odd columns to create the honeycomb pattern
                int y = y0 + row * (int)(hexRadius * 1.5);
                if (col % 2 == 1) {
                    y += (int)(hexRadius * 0.75); // Offset for odd columns (half of 1.5)
                }
                
                t.setPosition(x, y);
                grid[row][col] = t;
                TectonCollection.add(t);
            }
        }
        // Connect neighbors (hex grid)
        // For even-q offset coordinates (flat-topped hexes), different directions based on column parity
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Tecton_Basic t = grid[row][col];
                // Different neighbors for even and odd columns
                int[][] directions;
                if (col % 2 == 0) { // Even column
                    directions = new int[][] {
                        {-1, 0}, {-1, 1},  // Northwest, Northeast
                        {0, -1}, {0, 1},    // West, East
                        {1, 0}, {1, 1}      // Southwest, Southeast
                    };
                } else { // Odd column
                    directions = new int[][] {
                        {-1, -1}, {-1, 0},  // Northwest, Northeast
                        {0, -1}, {0, 1},    // West, East
                        {1, -1}, {1, 0}     // Southwest, Southeast
                    };
                }
                
                for (int[] d : directions) {
                    int nr = row + d[0];
                    int nc = col + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        t.add_TectonNeighbour(grid[nr][nc]);
                    }
                }
            }
        }
        
        // Connect base1 to leftmost column (first column)
        // Adjust base1 position to fit the smaller honeycomb grid
        base1.setPosition(100, 300); // Moved to match the smaller grid
        for (int row = 1; row < rows-1; row++) { // Connect to middle rows of the smaller grid
            base1.add_TectonNeighbour(grid[row][0]);
            grid[row][0].add_TectonNeighbour(base1);
        }
        
        // Connect base2 to rightmost column (last column)
        // Adjust base2 position to fit the smaller honeycomb grid
        base2.setPosition(675, 300); // Moved to match the smaller grid
        for (int row = 1; row < rows-1; row++) { // Connect to middle rows of the smaller grid
            base2.add_TectonNeighbour(grid[row][cols-1]);
            grid[row][cols-1].add_TectonNeighbour(base2);
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
        if (targetTecton== null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Target tecton is null!");
            return;
        }

        //TODO: base tekton ellenőrzés

        if (targetTecton.get_Mushroom() != null || targetTecton.isDead()) {  // TODO: valamiért belelép ha nincs rajta gomba akkor is
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
    public void placeInsect(Insect_Class insect, Tecton_Class target) {
        // Check if the target tecton is valid
        if (target == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Target tecton is not valid.");
            return;
        }
        if (target.isDead()) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is dead. Cannot place insect there.");
            return;
        }
        if(target.get_Thread() == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "You can only place insects on tectons with a thread.");
            return;
        }
        // Check if the insect is valid and not already placed
        if (insect == null) {
            PLANE_LOGGER.log(Level.forName("NULL", 201), "Insect is not valid.");
            return;
        }
        if(insect.get_Owner().getRole() != RoleType.INSECT) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Player does not have Insect role!");
            return;
        }
        if(target.get_InsectsOnTecton().size()>=5) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Target tecton is full! Cannot move insect there.");
            return;
        }
        // Check if the player has enough resources to place the insect
        int cost = insect.getCost(); // Assuming `Insect_Class` has a `getCost` method
        if (insect.get_Owner().getIncome() < cost) {
            PLANE_LOGGER.log(Level.forName("ERROR", 401), "Not enough resources to place the insect.");
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

    public boolean placeInsectPossible(Insect_Class insect, Tecton_Class tecton) {
    if (tecton.get_InsectsOnTecton().contains(insect)) {
        // already placed here
        return false;
    }
    // your existing add logic, e.g.:
    InsectCollection.add(insect);
    tecton.get_InsectsOnTecton().add(insect);
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
