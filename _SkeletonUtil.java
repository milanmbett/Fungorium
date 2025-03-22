//Main.scanner-t lehet használni majd

import java.util.Scanner;

public class _SkeletonUtil 
{
    public static Tecton_Class _tectonClass;
    public static Tecton_Class _targetTecton;
    public static Mushroom_Class _mushroomClass;
    public static Insect_Class _insectClass;
    public static Basic_Spore _basicSpore;

    // ========== TECTONS ===========
    public static Tecton_Dead _tectonDead;
    public static Tecton_Basic _tectonBasic1;
    public static Tecton_Basic _tectonBasic2;

    // ========== SHROOMS ===========
    public static Mushroom_Grand _mushroomGrand1;
    public static Mushroom_Grand _mushroomGrand2;
    public static Mushroom_Maximus _mushroomMaximus;
    public static Mushroom_Shroomlet _mushroomShroomlet;
    public static Mushroom_Slender _mushroomSlender;

    // ========= INSECTS ============
    public static Insect_Buggernaut _insectBuggernaut;
    public static Insect_Buglet _insectBuglet;
    public static Insect_ShroomReaper _insectShroomreaper;
    public static Insect_Stinger _insectStinger;
    public static Insect_Tektonizator _insectTectonizator;

    // ========= SPORES ============ 
    public static Spore_Paralysing _sporeParalysing;
    public static Spore_Slowing _sporeSlowing;
    public static Spore_Speed _sporeSpeed;

    // ========= THREADS ============
    public static Thread_Class _thread1;

    
    public static void skeleton_1()
    {
        System.out.println("    SZKELETON 1: Gomba lerakása");

        //inicilizálás
        _tectonBasic1 = new Tecton_Basic();
        System.out.println("    - Felhasználó legálisan kiválasztotta ezt a Tecton_Basic-et");

        _mushroomShroomlet = new Mushroom_Shroomlet(_tectonBasic1);

        _tectonBasic1.set_Mushroom(_mushroomClass);

        
    }
    public static void skeleton_2()
    {
        System.out.println("    SZKELETON 2: Rovar lerakása");

        //inicilizálás
        _tectonBasic1 = new Tecton_Basic();

        System.out.println("    - Felhasználó legálisan kiválasztotta ezt a Tecton_Basic-et");

        _insectBuglet = new Insect_Buglet(_tectonBasic1);   

        System.out.println("    - Felhasználó legálisan lerakhatja ezt az Insect_Buglet-et");


        _tectonBasic1.set_InsectsOnTecton(_insectBuglet);

    }
    public static void skeleton_3()
    {
        System.out.println("    SZKELETON 3: Rovar mozgatása");

        //inicilizálás
        _tectonBasic1 = new Tecton_Basic();
        _tectonBasic2 = new Tecton_Basic();

        _insectBuglet = new Insect_Buglet(_tectonBasic1);

        System.out.println("    - Felhasználó kijelöli ezt az Insect_Buglet-et egy adott Tecton-on");

        _insectBuglet.move_Insect(_tectonBasic2);


    }
    public static void skeleton_4()
    {
        System.out.println("    SZKELETON 4: Gomba fejlesztése");

        //inicilizálás
        _tectonBasic1 = new Tecton_Basic();
        Scanner scanner = new Scanner(System.in);
        int choice1 = -1;
        int choice2 = -1;
        System.out.println("    - Felhasználó kiválaszt egy Gomba-t");
        System.out.println("    - 1. Gomba fejlesztése");
        System.out.println("    - 2. Spóra fejlesztése");
        System.out.println("    - Válaszás: ");
        choice1 = scanner.nextInt();
        if(choice1 == 1)
        {
            _mushroomShroomlet = new Mushroom_Shroomlet(_tectonBasic1);
            _mushroomShroomlet.upgrade_Mushroom(_basicSpore);
            System.out.println("    - Felhasználó választott egy Gomba-t és fejlesztette");
        }
        else if(choice1 == 2)
        {
            System.out.println("    - Felhasználó kiválaszt egy Spórát");
            System.out.println("    - 1. Paralizáló spóra");
            System.out.println("    - 2. Lassító spóra");
            System.out.println("    - 3. Gyorsító spóra");
            System.out.println("    - Válaszás: ");
            choice2 = scanner.nextInt();
            if (choice2 == 1)
            {
                _basicSpore = new Spore_Paralysing(_tectonBasic1);
            }
            else if (choice2 == 2)
            {
                _basicSpore = new Spore_Slowing(_tectonBasic1);
            }
            else if (choice2 == 3)
            {
                _basicSpore = new Spore_Speed(_tectonBasic1);
            }
        }
        else 
            System.out.println("    - Felhasználó nem választott semmit");
        
    }
    public static void skeleton_5()
    {
        System.out.println("    SZKELETON 5: Játék incializálása");

        //incializálás
        _tectonBasic1 = new Tecton_Basic();
        _tectonBasic2 = new Tecton_Basic();
        System.out.println("    - Pálya létrehozva");
        _mushroomGrand1 = new Mushroom_Grand(_tectonBasic1);
        _mushroomGrand2 = new Mushroom_Grand(_tectonBasic2);

    }
    public static void skeleton_6()
    {
        System.out.println("    SZKELETON 6: Játék vége és kiértékelés");
    }
    public static void skeleton_7() //Milán
    {
        System.out.println("    SZKELETON 7: Rovar támad gombát");
        _tectonBasic1 = new Tecton_Basic();
        _insectBuglet = new Insect_Buglet(_tectonBasic1);
        _mushroomShroomlet = new Mushroom_Shroomlet(_tectonBasic1);
        _insectBuglet.attack_Mushroom(_mushroomShroomlet);

    }
    public static void skeleton_8() //Milán
    {
        System.out.println("    SZKELETON 8: Gomba támad rovart");
        _tectonBasic1 = new Tecton_Basic();
        _tectonBasic2 = new Tecton_Basic();
        _mushroomShroomlet = new Mushroom_Shroomlet(_tectonBasic1);
        _insectBuglet = new Insect_Buglet(_tectonBasic2);
        _mushroomShroomlet.attack_Insect(_insectBuglet);

    }
    public static void skeleton_9()
    {
        System.out.println("    SZKELETON 9: Spóra generálása");
        _tectonBasic1 = new Tecton_Basic();
        _tectonBasic2 = new Tecton_Basic();
        _tectonBasic1.add_TectonNeighbour(_tectonBasic2);

        _basicSpore = new Basic_Spore();
        _tectonBasic1.set_Spore(_basicSpore);
        _basicSpore.expand_Spore(_tectonBasic2);




        /*System.out.println("    SZKELETON 15: Fonál terjedése");
        _tectonBasic1 = new Tecton_Basic();
        _tectonBasic2 = new Tecton_Basic();
        _tectonBasic1.add_TectonNeighbour(_tectonBasic2);
        
        _thread1 = new Thread_Class(_tectonBasic1);
        _tectonBasic1.set_Thread(_thread1);

        _thread1.expand_Thread(_tectonBasic2);*/
    }
    public static void skeleton_10()
    {
        System.out.println("    SZKELETON 10: Spóra megevése");
        _tectonBasic1 = new Tecton_Basic();
        _insectBuglet = new Insect_Buglet(_tectonBasic1);
        _basicSpore = new Basic_Spore();
        _insectBuglet.eat_Spore(_basicSpore);
    }
    public static void skeleton_11()
    {
        System.out.println("    SZKELETON 11: Gombafonal megevése");
        _tectonBasic1 = new Tecton_Basic();
        _thread1 = new Thread_Class(_tectonBasic1);
        _insectBuglet = new Insect_Buglet(_tectonBasic1);
        _insectBuglet.eat_Thread(_thread1);

    }
    public static void skeleton_12()
    {
        System.out.println("    SZKELETON 12: Tekton kettétörése");
        _tectonBasic1 = new Tecton_Basic();
        _insectTectonizator = new Insect_Tektonizator(_tectonBasic1);
        _insectTectonizator.tectonCrack(_tectonBasic1);

    }
    public static void skeleton_13()
    {
        System.out.println("    SZKELETON 13: Tekton elpuszítása");
        _tectonBasic1 = new Tecton_Basic();
        _insectShroomreaper = new Insect_ShroomReaper(_tectonBasic1);
        _insectShroomreaper.destroy_Tecton();
    }
    public static void skeleton_14()
    {
        System.out.println("    SZKELETON 14: Gombafonal terjedése");
    }
    public static void skeleton_15() //Milán
    {
        System.out.println("    SZKELETON 15: Fonál terjedése");
        _tectonBasic1 = new Tecton_Basic();
        _tectonBasic2 = new Tecton_Basic();
        _tectonBasic1.add_TectonNeighbour(_tectonBasic2);
        
        _thread1 = new Thread_Class(_tectonBasic1);
        _tectonBasic1.set_Thread(_thread1);

        _thread1.expand_Thread(_tectonBasic2);
    }


}
