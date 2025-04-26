package com.coderunnerlovagjai.app;

import java.util.*;

/** 
 * Console-based prototype for the Fungorium game implementing all 14 use cases.
 * Each use case is implemented as a separate method that prints a simulation of that scenario.
 * The program uses placeholder logic and outputs to represent game state changes.
 */
public class Proto {
    // Game state fields
    private static boolean gameInitialized = false;
    private static int nextTectonId = 1;
    private static List<Tecton> allTectons = new ArrayList<>();
    private static int fungusCurrency;
    private static int insectCurrency;

    // Domain classes (static inner classes for simplicity)
    static class Tecton {
        int id;
        List<Tecton> neighbors = new ArrayList<>();
        Mushroom mushroom = null;
        List<Insect> insects = new ArrayList<>();
        Spore spore = null;
        ThreadObj thread = null;
        boolean isDead = false;
        Tecton() {
            this.id = nextTectonId++;
        }
        public String toString() {
            return "Tecton" + id;
        }
    }
    static class Mushroom {
        String type;
        int hp;
        int power;
        Tecton location;
        Mushroom(String type, int hp, int power, Tecton loc) {
            this.type = type;
            this.hp = hp;
            this.power = power;
            this.location = loc;
        }
        public String toString() {
            return type + " Mushroom";
        }
    }
    static class Insect {
        String type;
        int hp;
        int attackDamage;
        Tecton location;
        Insect(String type, int hp, int attackDamage, Tecton loc) {
            this.type = type;
            this.hp = hp;
            this.attackDamage = attackDamage;
            this.location = loc;
        }
        public String toString() {
            return type + " Insect";
        }
    }
    static class Spore {
        String type;
        Tecton location;
        Spore(String type, Tecton loc) {
            this.type = type;
            this.location = loc;
        }
        public String toString() {
            return type + " Spore";
        }
    }
    static class ThreadObj {
        Tecton location;
        ThreadObj(Tecton loc) {
            this.location = loc;
        }
        public String toString() {
            return "Thread";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Fungorium Console Prototype - Use Case Simulation");
        while (true) {
            // Display menu
            System.out.println("\nSelect a role (1 or 2), or 0 to exit:");
            System.out.println("1 - Fungus Player (Gomba játékos)");
            System.out.println("2 - Insect Player (Rovar játékos)");
            System.out.println("3 - Initialize Game (Játék inicializálása)");
            System.out.println("0 - Exit");
        /*  System.out.println("\nChoose an option (1-14) to simulate a use case, or 0 to exit:");
            System.out.println("1  - Gomba lerakása (Place Mushroom)");
            System.out.println("2  - Rovar mozgatása (Move Insect)");
            System.out.println("3  - Rovar lerakása (Place Insect)");
            System.out.println("4  - Gomba fejlesztése (Upgrade Mushroom)");
            System.out.println("5  - Játék inicializálása (Initialize Game)");
            System.out.println("6  - Játék vége és kiértékelés (End Game & Evaluate)");
            System.out.println("7  - Rovar támad gombát (Insect Attacks Mushroom)");
            System.out.println("8  - Gomba támad rovart (Mushroom Attacks Insect)");
            System.out.println("9  - Spóra generálása (Generate Spore)");
            System.out.println("10 - Spóra megevése (Consume Spore)");
            System.out.println("11 - Rovar kettétör tektont (Crack Tecton)");
            System.out.println("12 - Gombafonal megevése (Eat Fungal Thread)");
            System.out.println("13 - Rovar elpusztít tektont (Destroy Tecton)");
            System.out.println("14 - Fonal önálló terjedése (Thread Spreads)");
            System.out.println("0  - Kilépés (Exit)");*/
            // Read user choice
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
                            placeMushroom();
                            break;
                        case 2:
                            upgradeMushroom();
                            break;
                        default:
                            System.out.println("Invalid choice for Fungus Player.");
                    }
                    break;
                case 2:
                    // Insect player actions
                    System.out.println("Insect Player selected. Choose an action:");
                    System.out.println("1 - Move Insect");
                    System.out.println("2 - Place Insect");
                    System.out.println("3 - Insect Attacks Mushroom");
                    System.out.println("0 - Back to main menu");
                    try {
                        choice2 = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, please enter a number.");
                        continue;
                    }
                    switch (choice2) {
                        case 1:
                            moveInsect();
                            break;
                        case 2:
                            placeInsect();
                            break;
                        case 3:
                            insectAttacksMushroom();
                            break;
                        default:
                            System.out.println("Invalid choice for Insect Player.");
                            break;
                    }
                    break;
                case 3:
                    // Initialize game
                    System.out.println("Initializing game...");
                    initializeGame();
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    

                }

            /*switch (choice) {
                case 1:
                    placeMushroom();
                    break;
                case 2:
                    moveInsect();
                    break;
                case 3:
                    placeInsect();
                    break;
                case 4:
                    upgradeMushroom();
                    break;
                case 5:
                    initializeGame();
                    break;
                case 6:
                    endGame();
                    break;
                case 7:
                    insectAttacksMushroom();
                    break;
                case 8:
                    mushroomAttacksInsect();
                    break;
                case 9:
                    generateSpore();
                    break;
                case 10:
                    consumeSpore();
                    break;
                case 11:
                    crackTecton();
                    break;
                case 12:
                    eatThread();
                    break;
                case 13:
                    destroyTecton();
                    break;
                case 14:
                    spreadThread();
                    break;
                default:
                    System.out.println("Invalid option. Please choose a number from 0 to 14.");
            }*/
        }
    }

    // Use Case 5: Játék inicializálása (Initialize Game)
    private static void initializeGame() {
        // Reset game state
        gameInitialized = true;
        nextTectonId = 1;
        allTectons.clear();
        fungusCurrency = 100;
        insectCurrency = 100;
        // Create initial map (tectons and neighbors)
        Tecton t1 = new Tecton();
        Tecton t2 = new Tecton();
        Tecton t3 = new Tecton();
        Tecton t4 = new Tecton();
        Tecton t5 = new Tecton();
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
    private static void placeMushroom() {
        // Simulate choosing a target tecton to place a new mushroom
        Tecton target = null;
        for (Tecton t : allTectons) {
            if (t.mushroom == null && !t.isDead) {
                target = t;
                break;
            }
        }
        if (target == null) {
            System.out.println("No available tecton to place a mushroom.");
            return;
        }
        // Check currency
        int cost = 20;
        if (fungusCurrency < cost) {
            System.out.println("Not enough resources to place a mushroom.");
            return;
        }
        fungusCurrency -= cost;
        // Place the mushroom
        Mushroom newMushroom = new Mushroom("Basic", 30, 10, target);
        target.mushroom = newMushroom;
        System.out.println("Placing a new mushroom on " + target + "...");
        System.out.println("Mushroom placed successfully on " + target + ". (Cost " + cost + ", remaining fungus currency: " + fungusCurrency + ")");
    }

    // Use Case 2: Rovar mozgatása (Move Insect)
    private static void moveInsect() {
        // Simulate moving an insect to a neighboring tecton
        Insect insect = getAnyInsect();
        if (insect == null) {
            System.out.println("No insect available to move.");
            return;
        }
        Tecton current = insect.location;
        Tecton destination = null;
        for (Tecton neigh : current.neighbors) {
            // Choose first valid neighbor (not dead, no enemy fungus)
            if (!neigh.isDead && neigh.mushroom == null) {
                destination = neigh;
                break;
            }
        }
        if (destination == null) {
            System.out.println("No valid neighboring tecton for the insect to move.");
            return;
        }
        // Perform move
        current.insects.remove(insect);
        destination.insects.add(insect);
        insect.location = destination;
        System.out.println("Moving " + insect.type + " from " + current + " to " + destination + "...");
        System.out.println("Insect moved successfully to " + destination + ".");
    }

    // Use Case 3: Rovar lerakása (Place Insect)
    private static void placeInsect() {
        // Simulate choosing an insect type and placing it at the insect starting point (Tecton3)
        Tecton start = null;
        for (Tecton t : allTectons) {
            if (t.id == 3) { // designate Tecton3 as starting point
                start = t;
                break;
            }
        }
        if (start == null) {
            System.out.println("Starting position not found.");
            return;
        }
        if (!start.insects.isEmpty()) {
            System.out.println("Starting position is already occupied by an insect.");
            return;
        }
        // Simulate selecting insect type (default to "Buglet")
        String chosenType = "Buglet";
        // Check currency
        int cost = 20;
        if (insectCurrency < cost) {
            System.out.println("Not enough resources to place an insect.");
            return;
        }
        insectCurrency -= cost;
        // Place the insect
        Insect newInsect = new Insect(chosenType, 30, 10, start);
        start.insects.add(newInsect);
        System.out.println("Placing a new insect (" + chosenType + ") at starting point " + start + "...");
        System.out.println("Insect placed successfully on " + start + ". (Cost " + cost + ", remaining insect currency: " + insectCurrency + ")");
    }

    // Use Case 4: Gomba fejlesztése (Upgrade Mushroom)
    private static void upgradeMushroom() {
        // Simulate selecting a mushroom to upgrade (pick first available)
        Mushroom targetMushroom = null;
        for (Tecton t : allTectons) {
            if (t.mushroom != null) {
                targetMushroom = t.mushroom;
                break;
            }
        }
        if (targetMushroom == null) {
            System.out.println("No mushroom available to upgrade.");
            return;
        }
        // Check currency
        int cost = 30;
        if (fungusCurrency < cost) {
            System.out.println("Not enough resources to upgrade the mushroom.");
            return;
        }
        fungusCurrency -= cost;
        // Apply upgrade (increase stats and change type)
        targetMushroom.type = "Upgraded";
        targetMushroom.hp += 20;
        targetMushroom.power += 5;
        System.out.println("Upgrading mushroom at " + targetMushroom.location + "...");
        System.out.println("Mushroom upgraded successfully! New type: " + targetMushroom.type +
                ", HP: " + targetMushroom.hp + ", Power: " + targetMushroom.power +
                ". (Cost " + cost + ", remaining fungus currency: " + fungusCurrency + ")");
    }

    // Use Case 6: Játék vége és kiértékelés (End Game & Evaluate)
    private static void endGame() {
        // Simulate checking win conditions and ending the game
        boolean fungusBaseDestroyed = false;
        // Check if base mushroom is destroyed (HP <= 0 or removed)
        for (Tecton t : allTectons) {
            if (t.mushroom != null && t.mushroom.type.equals("Base") && t.mushroom.hp <= 0) {
                fungusBaseDestroyed = true;
                break;
            }
        }
        if (fungusBaseDestroyed) {
            System.out.println("The insect player has destroyed the main fungus! Insect player wins!");
        } else {
            // (Turn limit or other conditions not explicitly simulated; just show final counts)
            int fungusCount = 0;
            int insectCount = 0;
            for (Tecton t : allTectons) {
                if (t.mushroom != null) fungusCount++;
                insectCount += t.insects.size();
            }
            System.out.println("Game over. Final count -> Mushrooms: " + fungusCount + ", Insects: " + insectCount + ".");
            if (fungusCount > insectCount) {
                System.out.println("Fungus player has the advantage.");
            } else if (insectCount > fungusCount) {
                System.out.println("Insect player has the advantage.");
            } else {
                System.out.println("The game is a tie.");
            }
        }
        System.out.println("Game ended. You may start a new game (option 5) or exit.");
        gameInitialized = false;
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
