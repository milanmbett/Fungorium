package com.coderunnerlovagjai.app;


import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public abstract class _Tests 
{
    private static final Logger TESTS_LOGGER = LogManager.getLogger(_Tests.class);
    public static void test1() //Tekton létrehozása
    {
        TESTS_LOGGER.log(Level.forName("TEST", 401), "Testing: creating Tecton_Basic");
        Tecton_Class t1 = new Tecton_Basic();
        if(t1.get_Mushroom() != null)
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Basic mushroom is not null!");
            return;
        }
        if(t1.get_Spore() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Basic spore is not null!");
            return;
        }
        if(t1.get_Thread() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Basic thread is not null!");
            return;
        }
        if(t1.get_InsectsOnTecton() == null)
        {
            TESTS_LOGGER.log(Level.forName(null, 0), "Tecton_Basic insectsOnTecton is null!");
            return;
        }
        if(t1.get_TectonNeighbours() == null) 
        {
            TESTS_LOGGER.log(Level.forName(null, 0), "Tecton_Basic TectonNeighbours is null!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test2() //Fő tekton létrehozása
    {
        TESTS_LOGGER.log(Level.forName("TEST", 401), "Testing: creating Tecton_Base");
        Player p1 = new Player();
        Tecton_Base t1 = new Tecton_Base(p1);
        if(t1.get_Owner() == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Base owner is null!");
            return;
        } 
        if(t1.get_Mushroom() == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Base mushroom is null!");
            return;
        } 
        if(t1.get_Spore() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Base spore is not null!");
            return;
        }
        else
        {
            TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton_Base spore: " + t1.get_Spore());
        }
        if(t1.get_Thread() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Base thread is not null!");
            return;
        }
        if(t1.get_InsectsOnTecton() == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Base insectsOnTecton is null!");
            return;
        }
        if(t1.get_TectonNeighbours() == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Base TectonNeighbours is null!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS",400), "Test ran successfully!");

    }
    public static void test3() //Tekton halála
    {
        TESTS_LOGGER.log(Level.forName("TEST", 401), "Testing: Tecton death");
        Tecton_Class t1 = new Tecton_Basic();
        Thread_Class th1 = new Thread_Class(t1);
        // Store the result of die_Tecton for testing
        t1 = t1.die_Tecton();
        
        // Check if thread is removed from the original tecton
        if(t1.get_Thread() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Basic thread is not null!");
            return;
        }
        
        // Verify that creating a thread on the original tecton reference doesn't work
        Thread_Class th2 = new Thread_Class(t1);
        if(th2.get_Tecton() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Thread_Class tecton is not null!");
            return;
        }
        
        // Also verify that creating a thread directly on the dead tecton doesn't work
        Thread_Class th3 = new Thread_Class(t1);
        if(th3.get_Tecton() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Thread_Class on explicit dead tecton is not null!");
            return;
        }
        
        // Verify that other operations on dead tecton are rejected
        try {
            t1.set_Mushroom(new Mushroom_Shroomlet(t1, null));
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Dead tecton allowed setting a mushroom!");
            return;
        } catch (UnsupportedOperationException e) {
            // This is expected
            TESTS_LOGGER.log(Level.forName("GET", 400), "Dead tecton correctly rejected mushroom!");
        }
        
        try {
            t1.set_Spore(new Basic_Spore(t1));
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Dead tecton allowed setting a spore!");
            return;
        } catch (UnsupportedOperationException e) {
            // This is expected
            TESTS_LOGGER.log(Level.forName("GET", 400), "Dead tecton correctly rejected spore!");
        }
        
        TESTS_LOGGER.log(Level.forName("SUCCESS",400), "Test ran successfully!");
    }
    public static void test4() //Tekton szomszédsági lista feltöltése
    { //Mindenki szomszédja mindenkinek
        Tecton_Class t1 = new Tecton_Basic();
        Tecton_Class t2 = new Tecton_Basic();
        Tecton_Class t3 = new Tecton_Basic();
        t1.add_TectonNeighbour(t2);
        t1.add_TectonNeighbour(t3);
        t2.add_TectonNeighbour(t1);
        t2.add_TectonNeighbour(t3);
        t3.add_TectonNeighbour(t1);
        t3.add_TectonNeighbour(t2);
        int tmp = 0;
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton0's Neighbours:");
        for (Tecton_Class tc : t1.get_TectonNeighbours()) 
        {
            ++tmp;
            TESTS_LOGGER.log(Level.forName("GET", 400),tc.get_ID());
        }
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton1's Neighbours:");
        for (Tecton_Class tc : t2.get_TectonNeighbours()) 
        {
            ++tmp;
            TESTS_LOGGER.log(Level.forName("GET", 400), tc.get_ID());
        }
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton2's Neighbours:");
        for (Tecton_Class tc : t3.get_TectonNeighbours()) 
        {
            ++tmp;
            TESTS_LOGGER.log(Level.forName("GET", 400), tc.get_ID());
        }
        if(tmp != 6) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Basic neighbours list is not filled correctly!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
public static void test5() // Tekton kettétörése
{
    TESTS_LOGGER.log(Level.forName("TEST", 401), "Testing: Tecton cracking");
    Player p1 = new Player();
    Tecton_Basic t1 = new Tecton_Basic();
    
    // Create some neighbor tectons
    Tecton_Basic t2 = new Tecton_Basic();
    Tecton_Basic t3 = new Tecton_Basic();

    // Set up neighbor relationships
    t1.add_TectonNeighbour(t2);
    t1.add_TectonNeighbour(t3);
    t2.add_TectonNeighbour(t1);
    t3.add_TectonNeighbour(t1);

    Insect_Tektonizator it1 = new Insect_Tektonizator(t1, p1);

    if (!t1.get_InsectsOnTecton().contains(it1)) {
        TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tektonizator is not on the tecton!");
        return;
    }

    // Get initial neighbours
    List<Tecton_Class> initialNeighbours = t1.get_TectonNeighbours();
    TESTS_LOGGER.log(Level.forName("GET", 400), "Initial neighbours: " + initialNeighbours);

    it1.tectonCrack();

    // After tectonCrack, t1 should be replaced in Plane.TectonCollection
    // We need to find the dead tecton in the collection

    Tecton_Class deadTecton = null;
    for (Tecton_Class tecton : Plane.TectonCollection) {
        if (tecton.get_ID().equals(t1.get_ID()) && tecton instanceof Tecton_Dead) {
            deadTecton = tecton;
            break;
        }
    }

    if (deadTecton == null) {
        TESTS_LOGGER.log(Level.forName("ERROR", 404), "Original Tecton not found or not dead!");
        return;
    }
    
    // Check if the tecton is an instance of Tecton_Dead
    if (!(deadTecton instanceof Tecton_Dead)) {
        TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton is not dead after cracking!");
        return;
    }

    // Check if neighbours are updated (this part is tricky without knowing the exact logic)
    List<Tecton_Class> finalNeighbours = deadTecton.get_TectonNeighbours();
    TESTS_LOGGER.log(Level.forName("GET", 400), "Final neighbours: " + finalNeighbours);

    // Basic check: neighbour list should not be the same
    if (initialNeighbours == finalNeighbours) {
        TESTS_LOGGER.log(Level.forName("ERROR", 404), "Neighbour list is not updated after cracking!");
        return;
    }

    TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
}
    public static void test6() //Fő tekton kettétörése
    {
        Player p1 = new Player();
        Tecton_Base t1 = new Tecton_Base(p1);
        Insect_Tektonizator it1 = new Insect_Tektonizator(t1, p1);
        it1.tectonCrack();
        if(Plane.TectonCollection.size() == 1) 
        {
            TESTS_LOGGER.log(Level.forName("SUCCESS", 404), "Tecton_Base did not split!");
        }
        else
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 400), "Tecton_Base has split!");
        }
    }
    public static void test7() //Fonal létrehozása
    {
        Tecton_Basic t1 = new Tecton_Basic();
        t1.set_Thread(new Thread_Class(t1));
        String tmp = t1.get_Thread().get_ID();
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Thread: " + tmp);
        if(tmp == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Basic thread is null!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test8() //Fonal terjedése
    {
        Tecton_Basic t1 = new Tecton_Basic();
        Tecton_Basic t2 = new Tecton_Basic();
        t1.add_TectonNeighbour(t2);
        t2.add_TectonNeighbour(t1);
        t1.set_Thread(new Thread_Class(t1));
        t1.get_Thread().expand_Thread();
        String tmp = t2.get_Thread().get_ID();
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton2's Thread: " + tmp);
        if(tmp == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton_Basic2 thread is null!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test9() //Rovar létrehozása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        if(Plane.InsectCollection.size() != 1 || t1.get_InsectsOnTecton().size() != 1) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect_Buglet is not created!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test10() //Rovar mozgatása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Tecton_Basic t2 = new Tecton_Basic();
        Thread_Class th1 = new Thread_Class(t1);
        Thread_Class th2 = new Thread_Class(t2);

        t1.add_TectonNeighbour(t2); 
        t2.add_TectonNeighbour(t1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        ib1.move_Insect(t2);
        TESTS_LOGGER.log(Level.forName("GET", 400), "Insect's Tecton: " + ib1.get_Tecton().get_ID());
        if(t2.insectsOnTecton.size() != 1) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect_Buglet is not moved!");
            return;
        }
        if(t1.insectsOnTecton.size() != 0) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect_Buglet is not removed from old Tecton!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");

    }
    public static void test11() //Gomba létrehozása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Mushroom_Shroomlet ms1 = new Mushroom_Shroomlet(t1, p1);
        if(Plane.MushroomCollection.size() != 1 || t1.get_Mushroom() == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Mushroom_Shroomlet is not created!");
            return;
        }
        String tmp = ms1.get_Tecton().get_ID();
        TESTS_LOGGER.log(Level.forName("GET", 400), "Mushroom's Tecton: " + tmp);
        if(tmp == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Mushroom_Shroomlet Tecton is null!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test12() //Rovar támad gombát
    {
        Player p1 = new Player();
        Player p2 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Mushroom_Shroomlet ms1 = new Mushroom_Shroomlet(t1, p2);
        int tmpHP = ms1.get_hp();
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        int tmpATTACK = ib1.get_attackDamage();
        ib1.attack_Mushroom(ms1);
        if(ms1.get_hp() != tmpHP - tmpATTACK) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Mushroom_Shroomlet HP is not correct!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
        
    }
    public static void test13() //Gomba támad rovart
    {
        Player p1 = new Player();
        Player p2 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Tecton_Basic t2 = new Tecton_Basic();
        t1.add_TectonNeighbour(t2);
        t2.add_TectonNeighbour(t1);
        Mushroom_Shroomlet ms1 = new Mushroom_Shroomlet(t1, p2);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        Insect_Buglet ib2 = new Insect_Buglet(t2, p1);
        int tmpHP1 = ib1.get_hp();
        int tmpHP2 = ib2.get_hp();
        int tmpATTACK = ms1.get_power();
        ms1.attack_Insects();
        if(ib1.get_hp() != tmpHP1 - tmpATTACK) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect_Buglet0 HP is not correct!");
            return;
        }
        if(ib2.get_hp() != tmpHP2 - tmpATTACK) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect_Buglet1 HP is not correct!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test14() //Basic_Spore elfogyasztása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Basic_Spore s1 = new Basic_Spore(t1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        ib1.eat_Spore(s1);

        if(t1.get_Spore() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Basic_Spore is not removed from Tecton!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
        
    }
    public static void test15() //Spore_Duplicator elfogyasztása 
    {   //TODO javítani duplicate_insect() metódus
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Spore_Duplicator sd1 = new Spore_Duplicator(t1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        ib1.eat_Spore(sd1);
        int tmp = 0; 

        TESTS_LOGGER.log(Level.forName("GET", 400), "Listing Insects on tecton");
        for (Insect_Class ic : t1.get_InsectsOnTecton()) 
        {
            ++tmp;
            TESTS_LOGGER.log(Level.forName("GET", 400), "Insect ID: " + ic.get_ID());
        }
        if(tmp != 2) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect count is not correct!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");

    }
    public static void test16() //Spore_Paralyzing elfogyasztása
    {
        //Turn t1 = new Turn(); 
        //TODO Itt kéne talán szimulálni köröket, hogy lehessen tesztelni a paralyzed/unparalyzed rovarokat
        //->Turn osztály megalkotása
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Basic_Spore s1 = new Spore_Paralysing(t1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        ib1.eat_Spore(s1);

        if(t1.get_Spore() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Basic_Spore is not removed from Tecton!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test17() //Spore_Slowing elfogyasztása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Basic_Spore s1 = new Spore_Slowing(t1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        ib1.eat_Spore(s1);

        if(t1.get_Spore() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Basic_Spore is not removed from Tecton!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test18() //Spore_Speed elfogyasztása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Basic_Spore s1 = new Spore_Speed(t1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        ib1.eat_Spore(s1);

        if(t1.get_Spore() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Basic_Spore is not removed from Tecton!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
        public static void test19() // Megevett spóra elfogyasztása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Basic_Spore s1 = new Basic_Spore(t1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        Insect_Buglet ib2 = new Insect_Buglet(t1, p1);

        ib1.eat_Spore(s1);

        try {
            ib2.eat_Spore(s1);
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect ate already eaten spore!");
            return;
        } catch (NullPointerException e) {
            TESTS_LOGGER.log(Level.forName("GET", 400), "Correctly threw exception when eating already eaten spore!");
        }

        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test20() //Halott gomba megtámadása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Mushroom_Shroomlet ms1 = new Mushroom_Shroomlet(t1, p1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);

        // Kill the mushroom
        ms1.die_Mushroom();

        try {
            ib1.attack_Mushroom(ms1);
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect attacked already dead mushroom!");
            return;
        } catch (IllegalArgumentException e) {
            TESTS_LOGGER.log(Level.forName("GET", 400), "Correctly threw exception when attacking already dead mushroom!");
        }

        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
     public static void test21() //Halott rovar megtámadása
    {
        Player p1 = new Player();
        Tecton_Basic t1 = new Tecton_Basic();
        Mushroom_Shroomlet ms1 = new Mushroom_Shroomlet(t1, p1);
        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);

        // Kill the insect
        ib1.die_Insect();

        try {
            ms1.attack_Insects();
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Mushroom attacked already dead insect!");
            return;
        } catch (IllegalArgumentException e) {
            TESTS_LOGGER.log(Level.forName("GET", 400), "Correctly threw exception when attacking already dead insect!");
        }

        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void test22() //Paralyzed rovar elfogyasztása
    {
        //Nincs paralyzed rovar --> Nem eszi meg
        //Van paralyzed rovar --> Megeszi  HA nincsen gomba -->> Növeszt egy gombát
        //Van paralyzed rovar --> Megeszi  HA van gomba -->>Nem növeszt gombát
        Player p1 = new Player();
        Tecton_Class t1 = new Tecton_Basic(); //Nincsen paralyzed rovar
        Tecton_Class t2 = new Tecton_Basic(); //Van paralyzed rovar de nincsen gomba
        Tecton_Class t3 = new Tecton_Basic(); //Van paralyzed rovar és gomba is


        Thread_Class th1 = new Thread_Class(t1);
        Thread_Class th2 = new Thread_Class(t2);
        Thread_Class th3 = new Thread_Class(t3);

        Insect_Buglet ib1 = new Insect_Buglet(t1, p1);
        Insect_Buglet ib2 = new Insect_Buglet(t2, p1);
        Insect_Buglet ib3 = new Insect_Buglet(t3, p1);

        Mushroom_Shroomlet ms1 = new Mushroom_Shroomlet(t3, p1);

        Spore_Paralysing sp2 = new Spore_Paralysing(t2);
        Spore_Paralysing sp3 = new Spore_Paralysing(t3);

        ib2.eat_Spore(sp2);
        ib3.eat_Spore(sp3);

        //Egy turn végének szimulálása
        for (Thread_Class iter : Plane.ThreadCollection) 
        {
            iter.tryToEat_Insect();    
        }

        //Lennie kell 1 rovarnak, 2 gombának
        if(Plane.InsectCollection.size() != 1) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect count is not correct!");
            return;
        }
        if(Plane.MushroomCollection.size() != 2) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Mushroom count is not correct!");
            return;
        }
        if(!t2.get_InsectsOnTecton().isEmpty() || !t3.get_InsectsOnTecton().isEmpty()) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Insect count on either tecton is not correct!");
            return;
        }
        if(t2.get_Mushroom() == null || t3.get_Mushroom() == null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Mushroom count on either tecton is not correct!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");





    }
    public static void test23() //Fonal terjedése halott tektonra
    {
        Tecton_Class t1 = new Tecton_Basic();
        Tecton_Class t2 = new Tecton_Dead();
        t1.add_TectonNeighbour(t2);
        t2.add_TectonNeighbour(t1);
        Thread_Class th1 = new Thread_Class(t1);
        th1.expand_Thread();

        if(t2.get_Thread() != null) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Thread expanded to dead tecton!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");

    }
    public static void test24() //Tectonon kettő gomba
    {
        Tecton_Class t1 = new Tecton_Basic();
        Mushroom_Shroomlet m1 = new Mushroom_Shroomlet(t1, null);
        Mushroom_Shroomlet m2 = new Mushroom_Shroomlet(t1, null);
        t1.set_Mushroom(m1);
        t1.set_Mushroom(m2);

        if(!t1.get_Mushroom().equals(m1)) 
        {
            TESTS_LOGGER.log(Level.forName("ERROR", 404), "Tecton has overwritten it's own mushroom!");
            return;
        }
        TESTS_LOGGER.log(Level.forName("SUCCESS", 400), "Test ran successfully!");
    }
    public static void empty()
    {
        Plane.InsectCollection.clear();
        Plane.MushroomCollection.clear();
        Plane.TectonCollection.clear();
        Plane.ThreadCollection.clear();
        Plane.SporeCollection.clear();

    }
    public static void listTests() //rewrite this to use sysmout instead of logger
    {
        System.out.println("----------------------------------------------------------------");
        System.out.println("Tests: ");
        System.out.println("1.  Create Tecton_Basic");
        System.out.println("2.  Create Tecton_Base");
        System.out.println("3.  Tecton_Basic death");
        System.out.println("4.  Tecton_Basic neighbours list fill");
        System.out.println("5.  Tecton_Basic split");
        System.out.println("6.  Tecton_Base split (Negative test)");
        System.out.println("7.  Create Thread");
        System.out.println("8.  Expand Thread");
        System.out.println("9.  Create Insect");
        System.out.println("10. Move Insect");
        System.out.println("11. Create Mushroom");
        System.out.println("12. Insect attack Mushroom");
        System.out.println("13. Mushroom attack Insect");
        System.out.println("14. Insect eats Basic_Spore");
        System.out.println("15. Insect eats Spore_Duplicator");
        System.out.println("16. Insect eats Spore_Paralyzing");
        System.out.println("17. Insect eats Spore_Slowing");
        System.out.println("18. Insect eats Spore_Speed");
        System.out.println("19. Insect eats already eaten spore (Negative test)");
        System.out.println("20. Insect attacks already dead mushroom (Negative test)");
        System.out.println("21. Mushroom attacks already dead insect (Negative test)");
        System.out.println("22. Thread eats Paralyzed insect");
        System.out.println("23. Thread expanding to dead tecton (Negative test)");
        System.out.println("24. Tecton with two mushrooms (Negative test)");
        System.out.println("----------------------------------------------------------------");
    }
}
