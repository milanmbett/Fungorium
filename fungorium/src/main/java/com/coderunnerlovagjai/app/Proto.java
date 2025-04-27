package com.coderunnerlovagjai.app;

import java.time.temporal.Temporal;
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
    private static List<Tecton_Class> allTectons = new ArrayList<>();
    private static Game game = new Game();

    

    public Proto(){
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
                    System.out.println("3 - Upgrade Spore (Spóra fejlesztése)");
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
                            game.getPlane().place_Mushroom(mushroom, selectTecton());
                            System.out.println("Mushroom placed successfully on tecton: " + selectTecton().get_ID() + ".");
                            break;
                        case 2:
                            selectExistingMushroom().upgrade_Mushroom(game.getPlayer(game.currentTurnsPlayer()));
                            break;
                        case 3:
                            Mushroom_Class selectedMushroom = selectExistingMushroom();
                            game.getPlane().upgradeSpore(selectSpore(selectedMushroom),selectedMushroom);
                            break;
                        default:
                            System.out.println("Invalid choice for Fungus Player.");
                    }
                    break;
                case 2:
                    // Insect player actions
                    game.getPlayer(game.currentTurnsPlayer()).setRoleInsect();
                    String insectID=null;
                    System.out.println("Insect Player selected. Choose an action:");
                    System.out.println("1 - Move Insect");
                    System.out.println("2 - Place Insect");
                    System.out.println("3 - Crack Tecton (Tektontörés)");
                    System.out.println("4 - Eat Thread (Fonal megevése)");
                    System.out.println("5 - Destroy Tecton (Tektont megölni)");
                    System.out.println("0 - Back to main menu");
                    try {
                        choice2 = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input, please enter a number.");
                        continue;
                    }
                    switch (choice2) {
                        case 0:
                            System.out.println("Going back.");
                            break;
                        case 1:
                            game.getPlane().move_Insect(game.getPlayer(game.currentTurnsPlayer()),selectInsect(), selectTecton());
                            break;
                        case 2:
                            game.getPlane().placeInsect(selectNewInsect(), selectTecton());
                            break;
                        case 3:
                            insectID = selectInsect().get_ID();
                            if(insectID == null) {
                                System.out.println("No insect selected.");
                                break;
                            }
                            if (insectID.contains("Insect_Tektonizator")) {
                                ((Insect_Tektonizator) game.getPlane().getInsectByID(insectID)).tectonCrack();
                                System.out.println("Tecton cracked successfully!");
                            } else {
                                System.out.println("You can only crack tectons with a Tektonizator insect.");
                            }
                            break;
                        case 4:
                            selectInsect().eat_Thread();
                            break;
                        case 5:
                            insectID = selectInsect().get_ID();
                            if(insectID == null) {
                                System.out.println("No insect selected.");
                                break;
                            }
                            if (insectID.contains("Insect_ShroomReaper")) {
                                ((Insect_ShroomReaper) game.getPlane().getInsectByID(insectID)).destroy_Tecton();
                                System.out.println("Tecton destroyed successfully!");
                            } else {
                                System.out.println("You can only destroy tectons with a ShroomReaper insect.");
                            }
                            break;
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
        Insect_Class selectedInsect = game.getPlane().getInsectByID(choice);

        return selectedInsect;
    }

    private static Tecton_Class selectTecton() {
        // Placeholder for selecting a tecton
        System.out.println("Select a tecton:");
        for (Tecton_Class t : game.getPlane().TectonCollection) {
            if (t.get_Thread()!=null) {
                System.out.println("Tecton: " + t.get_ID());
            }
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

    private static Mushroom_Class selectExistingMushroom(){
        // Placeholder for selecting an existing mushroom
        System.out.println("Select a mushroom from the list: ");
        for (Mushroom_Class m : game.getPlane().MushroomCollection) {
            if (m.get_Owner().getId() == game.currentTurnsPlayer())
                System.out.println("Mushroom: " + m.get_ID());
        }
        String choice;
        choice = new Scanner(System.in).nextLine();
        Mushroom_Class selectedMushroom = game.getPlane().getMushroomByID(choice);

        return selectedMushroom;
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

    private static Basic_Spore selectSpore(Mushroom_Class mushroom) {
        System.out.println("Select a spore from the list: ");
        System.err.println("1 - Spore Speed (Sebesség spóra)");
        System.err.println("2 - Spore Slowing (Lassító spóra)");
        System.err.println("3 - Spore Paralyzing (Bénító spóra)");
        System.err.println("4 - Spore Duplicator (Duplázó spóra)");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, please enter a number.");
            return null;
        }
        switch (choice) {
            case 1:
                return new Spore_Speed(mushroom.get_Tecton(),game.getPlayer(game.currentTurnsPlayer()));
            case 2:
                return new Spore_Slowing(mushroom.get_Tecton(),game.getPlayer(game.currentTurnsPlayer()));
            case 3:
                return new Spore_Paralysing(mushroom.get_Tecton(),game.getPlayer(game.currentTurnsPlayer()));
            case 4:
                return new Spore_Duplicator(mushroom.get_Tecton(),game.getPlayer(game.currentTurnsPlayer()));
            default:
                System.out.println("Invalid spore type selection.");
                return null;
        }
    }
}
