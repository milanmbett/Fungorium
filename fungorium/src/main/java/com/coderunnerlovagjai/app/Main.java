package com.coderunnerlovagjai.app;

import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



//TODO: Fontos dolgok
/* 
* - Kommentek írása

 * - Proto GUI(CLI) elkészítése (Mirkó, Balu)
 * --- Parancsok létrehozása amik szimulálni tudják a játékot
 * --- Tesztek indításának módosítása, hogy a main indulásakor ne menjenek tesztek?
 
 * - Player megvalósítása (Zoli)
 * --- Pontok tárolása, Role választás
 * - Role   megvalósítása (Zoli)
 * - Turn   megvalósítása  (Zoli)
 * --- Körök számának tárolása, körök közötti váltás pontok számolása
 * - Game   megvalósítása (Zoli)
 * --- Pálya létrehozás , pontok kiosztása, kiértékelés , játék kezdete 

 * - Maradandó funkciók megvalósítása (Mirkó, Balu)
 
 * - Dokumentum megírása (Zoli)
 *    
    Player:
    - A játékos alapadatai (név, stb.)
    - A játékos által választott szerep (Role)
    - Delegálja a játékmechanikákat a Role-nak

    Role:
    - Specifikus viselkedés a szerepekhez
    - Pontszámítás stratégiája szerepenként

    Game:
    - Körök kezelése
    - Játék állapotának fenntartása
*/


public class Main {

    private static final Logger MAIN_LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Support a direct test flag
        if (args.length > 0 && args[0].equals("--test")) {
            if (args.length > 1) {
                runTest(args[1]);
            } else {
                MAIN_LOGGER.log(Level.ERROR, "Please provide a test number after --test flag.");
            }
            scanner.close();
            return;
        }
        boolean exit = false;
        while (!exit) {
            System.out.println("=== Main Menu ===");
            System.out.println("1. Play Game");
            System.out.println("2. Run Tests");
            System.out.println("enter 'exit' to quit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    // Start the game
                    Proto proto = new Proto();
                    break;
                case "2":
                    // Enter test mode
                    boolean doneTests = false;
                    while (!doneTests) {
                        _Tests.listTests();
                        doneTests = processInput(scanner, scanner.nextLine());
                    }
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    MAIN_LOGGER.log(Level.WARN, "Invalid input. Please enter 1, 2, or 'exit'.");
            }
        }
        scanner.close();
    }

    private static boolean processInput(Scanner scanner, String input) {
        switch (input.toLowerCase()) {
            case "1":
                _Tests.test1();
                _Tests.empty();
                break;
            case "2":
                _Tests.test2();
                _Tests.empty();
                break;
            case "3":
                _Tests.test3();
                _Tests.empty();
                break;
            case "4":
                _Tests.test4();
                _Tests.empty();
                break;
            //case "5":
            //    _Tests.test5();
            //    _Tests.empty();
            //    break;
            //case "6":
            //    _Tests.test6();
            //    _Tests.empty();
            //    break;
            case "7":
                _Tests.test7();
                _Tests.empty();
                break;
            case "8":
                _Tests.test8();
                _Tests.empty();
                break;
            case "9":
                _Tests.test9();
                _Tests.empty();
                break;
            case "10":
                _Tests.test10();
                _Tests.empty();
                break;
            case "11":
                _Tests.test11();
                _Tests.empty();
                break;
            case "12":
                _Tests.test12();
                _Tests.empty();
                break;
            case "13":
                _Tests.test13();
                _Tests.empty();
                break;
            case "14":
                _Tests.test14();
                _Tests.empty();
                break;
            case "15":
                _Tests.test15();
                _Tests.empty();
                break;
            case "16":
                _Tests.test16();
                _Tests.empty();
                break;
            case "17":
                _Tests.test17();
                _Tests.empty();
                break;
            case "18":
                _Tests.test18();
                _Tests.empty();
                break;
            case "19":
                _Tests.test19();
                _Tests.empty();
                break;
            case "20":
                _Tests.test20();
                _Tests.empty();
                break;
            case "21":
                _Tests.test21();
                _Tests.empty();
                break;
            case "22":
                _Tests.test22();
                _Tests.empty();
                break;
            case "23":
                _Tests.test23();
                _Tests.empty();
                break;
            case "24":
                _Tests.test24();
                _Tests.empty();
                break;
            case "25":
                _Tests.test25();
                _Tests.empty();
                break;
            case "26":
                _Tests.test26();
                _Tests.empty();
                break;
            case "27":
                _Tests.test27();
                _Tests.empty();
                break;
            case "exit":
                return true;
            default:
                MAIN_LOGGER.log(Level.WARN, "Invalid input. Please enter a number between 1 and 24, or 'exit' to quit.");
                break;
        }
        return false;
    }

    private static void runTest(String testNumber) {
        switch (testNumber) {
            case "1":
                _Tests.test1();
                _Tests.empty();
                break;
            case "2":
                _Tests.test2();
                _Tests.empty();
                break;
            case "3":
                _Tests.test3();
                _Tests.empty();
                break;
            case "4":
                _Tests.test4();
                _Tests.empty();
                break;
            //case "5":
            //    _Tests.test5();
            //    _Tests.empty();
            //    break;
            //case "6":
            //    _Tests.test6();
            //    _Tests.empty();
            //    break;
            case "7":
                _Tests.test7();
                _Tests.empty();
                break;
            case "8":
                _Tests.test8();
                _Tests.empty();
                break;
            case "9":
                _Tests.test9();
                _Tests.empty();
                break;
            case "10":
                _Tests.test10();
                _Tests.empty();
                break;
            case "11":
                _Tests.test11();
                _Tests.empty();
                break;
            case "12":
                _Tests.test12();
                _Tests.empty();
                break;
            case "13":
                _Tests.test13();
                _Tests.empty();
                break;
            case "14":
                _Tests.test14();
                _Tests.empty();
                break;
            case "15":
                _Tests.test15();
                _Tests.empty();
                break;
            case "16":
                _Tests.test16();
                _Tests.empty();
                break;
            case "17":
                _Tests.test17();
                _Tests.empty();
                break;
            case "18":
                _Tests.test18();
                _Tests.empty();
                break;
            case "19":
                _Tests.test19();
                _Tests.empty();
                break;
            case "20":
                _Tests.test20();
                _Tests.empty();
                break;
            case "21":
                _Tests.test21();
                _Tests.empty();
                break;
            case "22":
                _Tests.test22();
                _Tests.empty();
                break;
            case "23":
                _Tests.test23();
                _Tests.empty();
                break;
            case "24":
                _Tests.test24();
                _Tests.empty();
                break;
            case "25":
                _Tests.test25();
                _Tests.empty();
                break;
            case "26":
                _Tests.test26();
                _Tests.empty();
                break;
            case "27":
                _Tests.test27();
                _Tests.empty();
                break;
            default:
                MAIN_LOGGER.log(Level.WARN, "Invalid test number provided.");
                break;
        }
    } 
}