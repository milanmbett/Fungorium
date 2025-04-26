package com.coderunnerlovagjai.app;

import java.time.temporal.Temporal;
import java.util.*;

import com.coderunnerlovagjai.app.FungoriumPrototype.Insect;
import com.coderunnerlovagjai.app.FungoriumPrototype.Mushroom;
import com.coderunnerlovagjai.app.FungoriumPrototype.Spore;
import com.coderunnerlovagjai.app.FungoriumPrototype.Tecton;
import com.coderunnerlovagjai.app.FungoriumPrototype.ThreadObj;

/** 
 * Console-based prototype for the Fungorium game implementing all 14 use cases.
 * Each use case is implemented as a separate method that prints a simulation of that scenario.
 * The program uses placeholder logic and outputs to represent game state changes.
 */



public class Proto {
    // Game state fields
    private static boolean gameInitialized = false;
    private static int nextTectonId = 1;
    private static List<Tecton_Class> allTectons = new ArrayList<>();
    private static Game game = new Game();

    

    public static void main(String[] args) {
        game.initGame(); 
        game.startGame(); // Elindítja a játékot
        Scanner scanner = new Scanner(System.in);
        System.out.println("Fungorium Console Prototype - "+game.currentTurnsPlayer()+"'s turn");
        while (!game.isGameOver()) {
            // Display menu
            System.out.println("\nSelect a role (1 or 2), or 0 to exit:");
            System.out.println("1 - Fungus Player (Gomba játékos)");
            System.out.println("2 - Insect Player (Rovar játékos)");
            //System.out.println("3 - Initialize Game (Játék inicializálása)");
            System.out.println("0 - Exit");
            int choice1;
            int choice2;
            try {
                choice1 = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }
            if (choice1 == 0) {
                System.out.println("Exiting... Goodbye!");
                scanner.close();
                break;
            }
            // Require initialization before other actions
            if (!gameInitialized && choice1 != 3) {
                System.out.println("Game is not initialized yet. Please initialize the game first (Option 5).");
                continue;
            }
            // Handle player role selection
            
            switch (choice1) {
                case 1:
                    // Fungus player actions
                    game.getPlayer(game.currentTurnsPlayer()).setRoleMushroom();
                    Role_Mushroom mushroomRole = (Role_Mushroom) game.getPlayer(game.currentTurnsPlayer()).getRole();
                    System.out.println("Fungus Player selected. Choose an action:");
                    System.out.println("1 - Place Mushroom");
                    System.out.println("2 - Upgrade Mushroom");
                    System.out.println("0 - Back to main menu");
                    try {
                        choice2 = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, please enter a number.");
                        continue;
                    }
                    switch (choice2) {
                        case 1:
                            System.out.println("Choose a mushroom type to place:");
                            System.out.println("1 - Shroomlet (Alap gomba)");
                            System.out.println("2 - FungusMaximus (Erősebb gomba)");
                            System.out.println("3 - Slender (Vékony gomba)");
                            System.out.println("0 - Back to main menu");
                            int choice3;
                            Mushroom_Class mushroom = null;
                            try {
                                choice3 = Integer.parseInt(scanner.nextLine().trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input, please enter a number.");
                                continue;
                            }
                            switch (choice3) {
                                case 1:
                                    mushroom = new Mushroom_Shroomlet(selectTecton(),game.getPlayer(game.currentTurnsPlayer())); 
                                    break;
                                case 2:
                                    mushroom = new Mushroom_Maximus(selectTecton(),game.getPlayer(game.currentTurnsPlayer())); 
                                    break;
                                case 3:
                                    mushroom = new Mushroom_Slender(selectTecton(),game.getPlayer(game.currentTurnsPlayer()));
                                    break;
                                case 0:
                                    System.out.println("Going back to main menu.");
                                    break;
                                default:
                                    System.out.println("Invalid choice for mushroom type.");
                                    continue;
                            }
                            placeMushroom(game.getPlayer(game.currentTurnsPlayer()), mushroom);
                            break;
                        case 2:
                            upgradeMushroom(game.getPlayer(game.currentTurnsPlayer()), mushroom);
                            break;
                        default:
                            System.out.println("Invalid choice for Fungus Player.");
                    }
                    break;
                case 2:
                    // Insect player actions
                    game.getPlayer(game.currentTurnsPlayer()).setRoleInsect();
                    System.out.println("Insect Player selected. Choose an action:");
                    System.out.println("1 - Move Insect");
                    System.out.println("2 - Place Insect");
                    //System.out.println("3 - Insect Attacks Mushroom");
                    System.out.println("0 - Back to main menu");
                    try {
                        choice2 = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, please enter a number.");
                        continue;
                    }
                    switch (choice2) {
                        case 1:
                            moveInsect(game.getPlayer(game.currentTurnsPlayer()),selectInsect(), selectTecton());
                            break;
                        case 2:
                            placeInsect(game.getPlayer(game.currentTurnsPlayer()), selectNewInsect(), selectTecton());
                            break;
                        /*case 3:
                            insectAttacksMushroom(game.getPlayer(game.currentTurnsPlayer()), selectInsect(), selectTecton());
                            break;*/
                        default:
                            System.out.println("Invalid choice for Insect Player.");
                            break;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    
                }
                game.turn();

        }
        // End game if turn limit reached
        game.endGame();
    }
    
    private static Insect_Class selectInsect(){
        // Placeholder for selecting an insect
        System.out.println("Select an insect from the list: ");
        for (Insect_Class i : game.getPlane().InsectCollection) {
            if (i.get_Owner().getId() == game.currentTurnsPlayer())
                System.out.println("Insect: " + i.get_ID());
        }
        String choice;
        choice = new Scanner(System.in).nextLine();
        Insect_Class selectedInsect = Game.plane.getInsectByID(choice);

        return selectedInsect;
    }

    private static Tecton_Class selectTecton() {
        // Placeholder for selecting a tecton
        System.out.println("Select a tecton:");
        for (Tecton_Class t : allTectons) {
            System.out.println("Tecton: " + t.get_ID());
        }
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, please enter a number.");
            return null;
        }
        if (choice < 0 || choice > allTectons.size()) {
            System.out.println("Invalid tecton selection.");
            return null;
        }
        return allTectons.get(choice);

        // Logic to select a tecton would go here
    }

    private static Insect_Class selectNewInsect() {
        // Placeholder for selecting a new insect type
        System.out.println("Select a new insect type:");
        System.out.println("1 - Buglet (Alap rovar)");
        System.out.println("2 - Buggernaut (strapabíróbb rovar)");
        System.out.println("3 - ShroomReaper (Gombagyilkos rovar)");
        System.out.println("4 - Stinger (Erősebb rovar)");
        System.out.println("5 - Tektonizator (Tektontörő rovar)");
        System.out.println("0 - Back to main menu");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, please enter a number.");
            return null;
        }
        switch (choice) {
            case 1:
                return new Insect_Buglet(selectTecton(), game.getPlayer(game.currentTurnsPlayer()));
            case 2:
                return new Insect_Buggernaut(selectTecton(), game.getPlayer(game.currentTurnsPlayer()));
            case 3:
                return new Insect_ShroomReaper(selectTecton(), game.getPlayer(game.currentTurnsPlayer()));
            case 4:
                return new Insect_Stinger(selectTecton(), game.getPlayer(game.currentTurnsPlayer()));
            case 5:
                return new Insect_Tektonizator(selectTecton(), game.getPlayer(game.currentTurnsPlayer())); 
            case 0:
                System.out.println("Going back to main menu.");
                return null;
            default:
                System.out.println("Invalid insect type selection.");
                return null;
        }
    }

    // Use Case 5: Játék inicializálása (Initialize Game)
    private static void initializeGame() {
        // Reset game state
        gameInitialized = true;
        nextTectonId = 1;
        allTectons.clear();
        // Create initial map (tectons and neighbors)
        Tecton_Class t1 = new Tecton_Class();
        Tecton_Class t2 = new Tecton_Class();
        Tecton_Class t3 = new Tecton_Class();
        Tecton_Class t4 = new Tecton_Class();
        Tecton_Class t5 = new Tecton_Class();
        // Define neighbor relationships (simple layout)
        t1.neighbors.add(t2);
        t1.neighbors.add(t4);
        t2.neighbors.add(t1);
        t2.neighbors.add(t3);
        t3.neighbors.add(t2);
        t4.neighbors.add(t1);
        t4.neighbors.add(t5);
        t5.neighbors.add(t4);
        // Collect tectons
        allTectons.add(t1);
        allTectons.add(t2);
        allTectons.add(t3);
        allTectons.add(t4);
        allTectons.add(t5);
        // Place base mushroom for fungus player on Tecton1
        Mushroom baseMushroom = new Mushroom("Base", 50, 15, t1);
        t1.mushroom = baseMushroom;
        // Starting state: fungus has one base mushroom, insect player has no insects yet
        System.out.println("Game initialized. Board created with " + allTectons.size() + " tectons.");
        System.out.println("Base Mushroom placed at Tecton1. Both players start with 100 currency.");
    }

    // Use Case 1: Gomba lerakása (Place Mushroom)
    private static void placeMushroom(Player player, Mushroom_Class mushroom) {
        Tecton_Class target = selectTecton();
        if (target.mushroom != null || target.isDead()) { // Check if tecton is empty and not dead   
            System.out.println("Target tecton is already occupied by a mushroom or is dead.");    
            return;
        }
        if (target == null) {
            System.out.println("No available tecton to place a mushroom.");
            return;
        }
        // Check currency
        int cost=mushroom.getCost(); // Placeholder for cost calculation
        if (player.getIncome() < cost) {
            System.out.println("Not enough resources to place a mushroom.");
            return;
        }
        player.decreaseIncome(cost); // Decrease player's currency by cost
        // Place the mushroom
        target.mushroom = mushroom;
        System.out.println("Placing a new mushroom on tecton:" + target.get_ID() + "...");
        System.out.println("Mushroom placed successfully on tecton: " + target.get_ID() + ". (Cost: " + cost + ", remaining fungus currency: " + player.getIncome() + ")");
    }

    // Use Case 2: Rovar mozgatása (Move Insect)
    private static void moveInsect(Player player,Insect_Class insect, Tecton_Class target) {
        // Simulate moving an insect to a neighboring tecton
        if (target == null) {
            System.out.println("No target tecton available for the insect to move to.");
            return;
        }
        if (insect == null) {
            System.out.println("No insect available to move.");
            return;
        }
        if(player.getId() != insect.get_Owner().getId()){
            System.out.println("You cannot move an insect that does not belong to you.");
            return;
        }
        Tecton_Class current = insect.get_Tecton();
        for (Tecton_Class neigh : current.get_TectonNeighbours()) {
            // Choose first valid neighbor (not dead and target is neigbor)
            if (!neigh.isDead()&&target.equals(neigh)) { //TODO fonal van-e
                insect.get_Tecton().get_InsectsOnTecton().remove(insect);
                insect.set_Tecton(target);
                target.insectsOnTecton.add(insect);
                break;
            }
        }
        if (destination == null) {
            System.out.println("No valid neighboring tecton for the insect to move.");
            return;
        }
        // Perform move
        System.out.println("Moving " + insect.get_ID() + " from " + current.get_ID() + " to " + target.get_ID() + "...");
        System.out.println("Insect moved successfully to " + target.get_ID() + ".");
    }

    // Use Case 3: Rovar lerakása (Place Insect)
    private static void placeInsect(Player player, Insect_Class insect, Tecton_Class target) {
        // Check if the target tecton is valid
        if (target == null) {
            System.out.println("Target tecton is not valid.");
            return;
        }

        // Check if the player has enough resources to place the insect
        int cost = insect.getCost(); // Assuming `Insect_Class` has a `getCost` method
        if (player.getIncome() < cost) {
            System.out.println("Not enough resources to place the insect.");
            return;
        }

        // Deduct the cost from the player's resources
        player.decreaseIncome(cost);

        // Place the insect on the target tecton
        insect.set_Tecton(target);
        target.get_InsectsOnTecton().add(insect);

        // Log the successful placement
        System.out.println("Insect (" + insect.get_ID() + ") placed successfully on tecton: " + target.get_ID() + ".");
        System.out.println("Cost: " + cost + ", remaining resources: " + player.getIncome() + ".");
    }

    // Use Case 4: Gomba fejlesztése (Upgrade Mushroom)
    private static void upgradeMushroom(Player player, Mushroom_Class mushroom) {
        // Simulate selecting a mushroom to upgrade (pick first available)
        Tecton_Class target= selectTecton();
        if(target==null){
            System.out.println("No available tecton to upgrade the mushroom.");
            return;
        }
        if (target.mushroom == null) {
            System.out.println("No mushroom available to upgrade.");
            return;
        }

        if (mushroom.level >= 3) {
            System.out.println("Mushroom is already at max level.");
            return;
        }
        // Check currency
        int cost = 50;
        if (player.getIncome() < cost) {
            System.out.println("Not enough resources to upgrade the mushroom.");
            return;
        }
        player.decreaseIncome(cost);
        // Apply upgrade (increase stats and change type)
        mushroom.level++;
        mushroom.hp += 20;
        mushroom.power += 5;
        System.out.println("Upgrading mushroom at tecton: " + target.get_ID() + "...");
        System.out.println("Mushroom upgraded successfully! New level: "+mushroom.level);
    }


    // Use Case 7: Rovar támad gombát (Insect Attacks Mushroom)
    private static void insectAttacksMushroom() {
        // Simulate an insect attacking a mushroom
        Insect attacker = getAnyInsect();
        Mushroom targetMushroom = null;
        if (attacker != null) {
            // Check for a mushroom on a neighboring tecton
            for (Tecton neigh : attacker.location.neighbors) {
                if (neigh.mushroom != null) {
                    targetMushroom = neigh.mushroom;
                    break;
                }
            }
        }
        if (attacker == null || targetMushroom == null) {
            System.out.println("No insect is in position to attack a mushroom.");
            return;
        }
        System.out.println(attacker.type + " on " + attacker.location + " attacks the mushroom on " + targetMushroom.location + "!");
        // Deal damage
        int damage = attacker.attackDamage;
        int oldHP = targetMushroom.hp;
        targetMushroom.hp -= damage;
        if (targetMushroom.hp < 0) targetMushroom.hp = 0;
        System.out.println("Mushroom loses " + damage + " HP (from " + oldHP + " to " + targetMushroom.hp + ").");
        if (targetMushroom.hp == 0) {
            System.out.println("The mushroom has been destroyed by the attack!");
            targetMushroom.location.mushroom = null;
        }
    }

    // Use Case 8: Gomba támad rovart (Mushroom Attacks Insect)
    private static void mushroomAttacksInsect() {
        // Simulate a mushroom attacking nearby insect(s)
        Mushroom attacker = null;
        Insect targetInsect = null;
        for (Tecton t : allTectons) {
            if (t.mushroom != null) {
                for (Tecton neigh : t.neighbors) {
                    if (!neigh.insects.isEmpty()) {
                        attacker = t.mushroom;
                        targetInsect = neigh.insects.get(0);
                        break;
                    }
                }
            }
            if (attacker != null) break;
        }
        if (attacker == null || targetInsect == null) {
            System.out.println("No mushroom has an insect in range to attack.");
            return;
        }
        System.out.println("Mushroom at " + attacker.location + " attacks insect on " + targetInsect.location + "!");
        int damage = attacker.power;
        int oldHP = targetInsect.hp;
        targetInsect.hp -= damage;
        if (targetInsect.hp < 0) targetInsect.hp = 0;
        System.out.println("Insect loses " + damage + " HP (from " + oldHP + " to " + targetInsect.hp + ").");
        if (targetInsect.hp == 0) {
            System.out.println("The insect is killed by the mushroom's attack.");
            targetInsect.location.insects.remove(targetInsect);
        }
    }

    // Use Case 9: Spóra generálása (Generate Spore)
    private static void generateSpore() {
        // Simulate automatic spore generation by all mushrooms
        if (getAnyMushroom() == null) {
            System.out.println("No mushrooms on the board to generate spores.");
            return;
        }
        Random rand = new Random();
        List<String> sporeTypes = Arrays.asList("Speed", "Slowing", "Paralyzing", "Duplicator");
        boolean generatedAny = false;
        for (Tecton t : allTectons) {
            if (t.mushroom != null) {
                List<Tecton> possibleTargets = new ArrayList<>();
                for (Tecton neigh : t.neighbors) {
                    if (neigh.mushroom == null && neigh.spore == null && !neigh.isDead && neigh.thread == null) {
                        possibleTargets.add(neigh);
                    }
                }
                if (!possibleTargets.isEmpty()) {
                    Tecton target = possibleTargets.get(rand.nextInt(possibleTargets.size()));
                    String sporeType = sporeTypes.get(rand.nextInt(sporeTypes.size()));
                    Spore newSpore = new Spore(sporeType, target);
                    target.spore = newSpore;
                    System.out.println("Mushroom at " + t + " generates a " + sporeType + " spore on " + target + ".");
                    generatedAny = true;
                }
            }
        }
        if (!generatedAny) {
            System.out.println("No valid neighbor available for spore generation.");
        }
    }

    // Use Case 10: Spóra megevése (Consume Spore)
    private static void consumeSpore() {
        // Simulate an insect moving onto a spore and consuming it
        Spore targetSpore = null;
        for (Tecton t : allTectons) {
            if (t.spore != null) {
                targetSpore = t.spore;
                break;
            }
        }
        if (targetSpore == null) {
            System.out.println("No spore present to consume.");
            return;
        }
        Insect eater = getAnyInsect();
        if (eater == null) {
            // If no insect, create one on a neighbor of the spore for demonstration
            Tecton spawn = !targetSpore.location.neighbors.isEmpty() 
                           ? targetSpore.location.neighbors.get(0) 
                           : targetSpore.location;
            eater = new Insect("Buglet", 30, 10, spawn);
            spawn.insects.add(eater);
            System.out.println("No insect available, placing a new insect at " + spawn + " to consume the spore.");
        }
        // Move insect to spore's location if not already there
        if (eater.location != targetSpore.location) {
            eater.location.insects.remove(eater);
            targetSpore.location.insects.add(eater);
            eater.location = targetSpore.location;
            System.out.println("Insect moves to " + targetSpore.location + " where a spore is present.");
        }
        System.out.println("Insect at " + targetSpore.location + " consumes the " + targetSpore.type + " spore.");
        // Placeholder effects based on spore type
        switch (targetSpore.type) {
            case "Speed":
                System.out.println("The insect feels a burst of speed!");
                break;
            case "Slowing":
                System.out.println("The insect is slowed by the spore's effect.");
                break;
            case "Paralyzing":
                System.out.println("The insect is paralyzed briefly by the spore.");
                break;
            case "Duplicator":
                System.out.println("Strange... the spore's duplicator effect has no immediate visible outcome.");
                break;
        }
        // Remove spore after consumption
        targetSpore.location.spore = null;
    }

    // Use Case 11: Rovar kettétör tektont (Crack Tecton)
    private static void crackTecton() {
        // Simulate a Tektonizator insect cracking the tecton it stands on
        Insect tektonizator = null;
        for (Tecton t : allTectons) {
            for (Insect ins : t.insects) {
                if (ins.type.equals("Tektonizator")) {
                    tektonizator = ins;
                    break;
                }
            }
            if (tektonizator != null) break;
        }
        if (tektonizator == null) {
            // Create a Tektonizator on an available tecton for demonstration
            Tecton targetTile = null;
            for (Tecton t : allTectons) {
                if (!t.isDead && t.mushroom == null) {
                    targetTile = t;
                    break;
                }
            }
            if (targetTile == null) {
                System.out.println("No available tecton to crack.");
                return;
            }
            tektonizator = new Insect("Tektonizator", 25, 5, targetTile);
            targetTile.insects.add(tektonizator);
            System.out.println("Tektonizator insect placed on " + targetTile + " to perform tecton crack.");
        }
        Tecton tile = tektonizator.location;
        if (tile.isDead) {
            System.out.println("This tecton is already dead and cannot be cracked.");
            return;
        }
        System.out.println("Tektonizator on " + tile + " cracks the tecton!");
        // Destroy any thread or insects on the tile (including the Tektonizator itself)
        if (tile.thread != null) {
            tile.thread = null;
            System.out.println("Any fungal thread on " + tile + " is destroyed.");
        }
        if (!tile.insects.isEmpty()) {
            tile.insects.clear();
            System.out.println("Any insects on " + tile + " are lost in the cracking process.");
        }
        // Split the tecton into two new tectons
        allTectons.remove(tile);
        for (Tecton neighbor : tile.neighbors) {
            neighbor.neighbors.remove(tile);
        }
        Tecton newT1 = new Tecton();
        Tecton newT2 = new Tecton();
        // New tectons become neighbors to each other and former neighbors of the cracked tecton
        newT1.neighbors.add(newT2);
        newT2.neighbors.add(newT1);
        for (Tecton neighbor : tile.neighbors) {
            newT1.neighbors.add(neighbor);
            newT2.neighbors.add(neighbor);
            neighbor.neighbors.add(newT1);
            neighbor.neighbors.add(newT2);
        }
        allTectons.add(newT1);
        allTectons.add(newT2);
        System.out.println(tile + " splits into new tectons " + newT1 + " and " + newT2 + ".");
        System.out.println("Neighbor relationships updated for surrounding tectons.");
    }

    // Use Case 12: Gombafonal megevése (Eat Fungal Thread)
    private static void eatThread() {
        // Simulate an insect eating a fungal thread on its current tecton
        ThreadObj targetThread = null;
        Insect eater = null;
        for (Tecton t : allTectons) {
            if (t.thread != null && !t.insects.isEmpty()) {
                targetThread = t.thread;
                eater = t.insects.get(0);
                break;
            }
        }
        if (targetThread == null) {
            // If no thread and insect on same tile, see if any thread exists on a tile and bring an insect there
            for (Tecton t : allTectons) {
                if (t.thread != null) {
                    targetThread = t.thread;
                    eater = new Insect("Buglet", 30, 10, t);
                    t.insects.add(eater);
                    break;
                }
            }
        }
        if (targetThread == null) {
            System.out.println("No fungal thread present to eat.");
            return;
        }
        if (eater == null) {
            eater = new Insect("Buglet", 30, 10, targetThread.location);
            targetThread.location.insects.add(eater);
        }
        System.out.println("Insect on " + targetThread.location + " eats the fungal thread.");
        targetThread.location.thread = null;
        System.out.println("The fungal thread on " + targetThread.location + " has been destroyed.");
    }

    // Use Case 13: Rovar elpusztít tektont (Destroy Tecton)
    private static void destroyTecton() {
        // Simulate a ShroomReaper insect destroying a tecton (e.g., the main fungus's tecton)
        Tecton targetTile = null;
        for (Tecton t : allTectons) {
            if (t.mushroom != null && t.mushroom.type.equals("Base")) {
                targetTile = t;
                break;
            }
        }
        if (targetTile == null) {
            // if no base, choose any non-dead tecton
            for (Tecton t : allTectons) {
                if (!t.isDead) {
                    targetTile = t;
                    break;
                }
            }
        }
        if (targetTile == null) {
            System.out.println("No tecton available to destroy.");
            return;
        }
        // Place a ShroomReaper on target tile if not already present
        Insect shroomReaper = null;
        for (Insect ins : targetTile.insects) {
            if (ins.type.equals("ShroomReaper")) {
                shroomReaper = ins;
                break;
            }
        }
        if (shroomReaper == null) {
            shroomReaper = new Insect("ShroomReaper", 40, 10, targetTile);
            targetTile.insects.add(shroomReaper);
        }
        System.out.println("ShroomReaper on " + targetTile + " uses its destroy ability!");
        // Destroy any mushroom and insects on the tile
        if (targetTile.mushroom != null) {
            targetTile.mushroom = null;
            System.out.println("Any mushroom on " + targetTile + " is destroyed.");
        }
        if (!targetTile.insects.isEmpty()) {
            targetTile.insects.clear();
            System.out.println("Any insects on " + targetTile + " are killed.");
        }
        // Mark tecton as dead (unbuildable for mushrooms)
        targetTile.isDead = true;
        System.out.println(targetTile + " is now a dead tecton (no new mushrooms can be built here).");
    }

    // Use Case 14: Fonal önálló terjedése (Thread Spreads)
    private static void spreadThread() {
        // Simulate a fungal thread spreading to an adjacent tecton
        Random rand = new Random();
        boolean spread = false;
        // If no thread exists yet, create an initial thread from a mushroom
        if (getAnyThread() == null) {
            Mushroom m = getAnyMushroom();
            if (m != null) {
                for (Tecton neigh : m.location.neighbors) {
                    if (neigh.mushroom == null && !neigh.isDead && neigh.thread == null) {
                        neigh.thread = new ThreadObj(neigh);
                        System.out.println("A new fungal thread appears on " + neigh + " (spread from mushroom at " + m.location + ").");
                        spread = true;
                        break;
                    }
                }
            }
        }
        // Spread each existing thread to one neighboring tecton
        for (Tecton t : allTectons) {
            if (t.thread != null) {
                List<Tecton> possibleTargets = new ArrayList<>();
                for (Tecton neigh : t.neighbors) {
                    if (neigh.thread == null && neigh.mushroom == null && !neigh.isDead) {
                        possibleTargets.add(neigh);
                    }
                }
                if (!possibleTargets.isEmpty()) {
                    Tecton target = possibleTargets.get(rand.nextInt(possibleTargets.size()));
                    target.thread = new ThreadObj(target);
                    System.out.println("Thread on " + t + " spreads to " + target + ".");
                    spread = true;
                }
            }
        }
        if (!spread) {
            System.out.println("No thread could spread (no available neighboring tile).");
        }
    }

    // Helper methods to get any existing mushroom/insect/thread
    private static Insect getAnyInsect() {
        for (Tecton t : allTectons) {
            if (!t.insects.isEmpty()) {
                return t.insects.get(0);
            }
        }
        return null;
    }
    private static Mushroom getAnyMushroom() {
        for (Tecton t : allTectons) {
            if (t.mushroom != null) {
                return t.mushroom;
            }
        }
        return null;
    }
    private static ThreadObj getAnyThread() {
        for (Tecton t : allTectons) {
            if (t.thread != null) {
                return t.thread;
            }
        }
        return null;
    }
}
