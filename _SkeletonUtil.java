//Main.scanner-t lehet használni majd

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
    public static Mushroom_Grand _mushroomGrand;
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
    }
    public static void skeleton_5()
    {
        System.out.println("    SZKELETON 5: Játék incializálása");
    }
    public static void skeleton_6()
    {
        System.out.println("    SZKELETON 6: Játék vége és kiértékelés");
    }
    public static void skeleton_7()
    {
        System.out.println("    SZKELETON 7: Rovar támad gombát");
    }
    public static void skeleton_8()
    {
        System.out.println("    SZKELETON 8: Gomba támad rovart");
    }
    public static void skeleton_9()
    {
        System.out.println("    SZKELETON 9: Spóra generálása");
    }
    public static void skeleton_10()
    {
        System.out.println("    SZKELETON 10: Spóra megevése");
    }
    public static void skeleton_11()
    {
        System.out.println("    SZKELETON 11: Gombafonal megevése");
    }
    public static void skeleton_12()
    {
        System.out.println("    SZKELETON 12: Tekton kettétörése");
    }
    public static void skeleton_13()
    {
        System.out.println("    SZKELETON 13: Tekton elpuszítása");
    }
    public static void skeleton_14()
    {
        System.out.println("    SZKELETON 14: Gombafonal terjedése");
    }
    public static void skeleton_15()
    {
        System.out.println("    SZKELETON 15: Fonál terjedése");
    }


}
