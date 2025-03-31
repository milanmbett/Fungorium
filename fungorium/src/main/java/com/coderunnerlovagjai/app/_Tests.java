package com.coderunnerlovagjai.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public abstract class _Tests 
{
    private static final Logger TESTS_LOGGER = LogManager.getLogger(_Tests.class);
    public static void test1() //Tekton létrehozása
    {
        Tecton_Basic t1 = new Tecton_Basic();
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Mushroom: " + t1.get_Mushroom());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Spore: " + t1.get_Spore());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Insects: " + t1.get_InsectsOnTecton());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Neighbours: " + t1.get_TectonNeighbours());
    }
    public static void test2() //Fő tekton létrehozása
    {
        Player p1 = new Player();
        Tecton_Base t1 = new Tecton_Base(p1);
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Mushroom: " + t1.get_Mushroom().get_ID());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Spore: " + t1.get_Spore());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Insects: " + t1.get_InsectsOnTecton());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Neighbours: " + t1.get_TectonNeighbours());
    }
    public static void test3() //Tekton halála
    {
        Tecton_Basic t1 = new Tecton_Basic();
        Tecton_Basic t2 = new Tecton_Basic();
        t1.add_TectonNeighbour(t2);
        t2.add_TectonNeighbour(t1);
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Mushroom: " + t1.get_Mushroom());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Spore: " + t1.get_Spore());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Insects: " + t1.get_InsectsOnTecton());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Neighbours: " + t1.get_TectonNeighbours());
        t1.die_Tecton();
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Mushroom: " + t1.get_Mushroom());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Spore: " + t1.get_Spore());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Insects: " + t1.get_InsectsOnTecton());
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton's Neighbours: " + t1.get_TectonNeighbours());

        TESTS_LOGGER.log(Level.forName("GET", 400),"TectonCollection size: " + Plane.TectonCollection.size());
        for (Tecton_Class tc : Plane.TectonCollection) 
        {
            TESTS_LOGGER.log(Level.forName("GET", 400), "TectonCollection: " + tc.get_ID());    
        }
    }
    public static void test4() //Tekton szomszédsági lista feltöltése
    { //Mindenki szomszédja mindenkinek
        Tecton_Basic t1 = new Tecton_Basic();
        Tecton_Basic t2 = new Tecton_Basic();
        Tecton_Basic t3 = new Tecton_Basic();
        t1.add_TectonNeighbour(t2);
        t1.add_TectonNeighbour(t3);
        t2.add_TectonNeighbour(t1);
        t2.add_TectonNeighbour(t3);
        t3.add_TectonNeighbour(t1);
        t3.add_TectonNeighbour(t2);
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton0's Neighbours:");
        for (Tecton_Class tc : t1.get_TectonNeighbours()) 
        {
            TESTS_LOGGER.log(Level.forName("GET", 400),tc.get_ID());
        }
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton1's Neighbours:");
        for (Tecton_Class tc : t2.get_TectonNeighbours()) 
        {
            TESTS_LOGGER.log(Level.forName("GET", 400), tc.get_ID());
        }
        TESTS_LOGGER.log(Level.forName("GET", 400), "Tecton2's Neighbours:");
        for (Tecton_Class tc : t3.get_TectonNeighbours()) 
        {
            TESTS_LOGGER.log(Level.forName("GET", 400), tc.get_ID());
        }
    }
    public static void test5() //Tekton kettétörése
    {

    }
    public static void test6() //Fő tekton kettétörése
    {

    }
    public static void test7() //Fonal létrehozása
    {

    }
    public static void test8() //Fonal terjedése
    {

    }
    public static void test9() //Rovar létrehozása
    {

    }
    public static void test10() //Rovar mozgatása
    {

    }
    public static void test11() //Gomba létrehozása
    {

    }
    public static void test12() //Rovar támad gombát
    {

    }
    public static void test13() //Gomba támad rovart
    {

    }
    public static void test14() //Basic_Spore elfogyasztása
    {

    }
    public static void test15() //Spore_Duplicator elfogyasztása
    {

    }
    public static void test16() //Spore_Paralyzing elfogyasztása
    {

    }
    public static void test17() //Spore_Slowing elfogyasztása
    {

    }
    public static void test18() //Spore_Speed elfogyasztása
    {

    }
    public static void test19() //Megevett spóra elfogyasztása
    {

    }
    public static void test20() //Halott gomba megtámadása
    {

    }
    public static void test21() //Halott rovar megtámadása
    {

    }
    public static void test22() //Paralyzed rovar elfogyasztása
    {

    }
    public static void test23() //Fonal terjedése halott tektonra
    {

    }
    public static void test24() //Tectonon kettő gomba
    {

    }
}
