package com.coderunnerlovagjai.app;

import java.util.Scanner;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//TODO: Van olyan tekton-fajta, amelyik életben tartja azokat a fonalakat, amelyek nincsenek közvetve vagy közvetlenül gombatesthez kötve.
//TODO: Az elrágott fonalak nem pusztulnak el azonnal, hanem csak egy kis idő elteltével (ez fonaltípustól függő idő).
//TODO: Gomba1 hp: 150 attack: 25; --> *25%, *25% -->
//TODO: Rovar1 hp: 100 attack: 50; --> *25%, *25% -->   
public class Main
{

    private static final Logger MAIN_LOGGER = LogManager.getLogger(Main.class);
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        while (!done) 
        {
            _Tests.listTests();
            switch (scanner.nextLine().toLowerCase()) 
            {
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
            case "5":
                _Tests.test5();
                _Tests.empty();
                break;
            case "6":
                _Tests.test6();
                _Tests.empty();
                break;
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
            case "exit":
                done = true;
                break;
            default:
                MAIN_LOGGER.log(Level.WARN, "Invalid input. Please enter a number between 1 and 24, or 'exit' to quit.");
                break;
            }     
        }
        scanner.close();
    }
}