public abstract class Mushroom_Class 
{
    /* protected int hp;
    protected int power;
    protected int sporeCount;
    protected Tecton_Class tecton;
    protected Basic_Spore spore; */
    
    public Mushroom_Class()
    {
        System.out.println("    [Called] Mushroom_Class constructor");
    }
    public Mushroom_Class(int HP, int p, int sporeC, Tecton_Class targetTecton)
    {
        /* hp = HP;
        power = p;
        sporeCount = sporeC;
        tecton = targetTecton;
        tecton.set_Mushroom(this); */
        //Plane.MushroomCollection.add(this);
    }
    public Mushroom_Class(Tecton_Class targetTecton)
    {
        /* hp = 100; //_TMP value
        power = 10; //_TMP value
        sporeCount = 10; //_TMP value
        tecton = targetTecton;
        tecton.set_Mushroom(this); */
        //Plane.MushroomCollection.add(this);
    }

    public void die_Mushroom()
    {
        //tecton.remove_Mushroom();
        //Plane.MushroomCollection.remove(this);
    }

    public void spawn_Spores() //TODO
    {
        ///???
        
        //spore.set_Tecton(_SkeletonUtil._tectonClass);
    } 
    public void attack_Insect(Insect_Class i)
    {
        System.out.println("    [Action] Mushroom attacked insect!");
        i.reduceHP(get_power());
        //Körbemegy a tektonja szomszédsági listáján, és megtámadja az összes rovart.
        /* for (Tecton_Class t : tecton.get_TectonNeighbours()) 
        {
            for (Insect_Class insect : t.get_InsectsOnTecton()) 
            {
                insect.reduceHP(power);
            }    
        } */
    }
    public void upgrade_Mushroom(Basic_Spore s)
    {
        System.out.println("    [Action] Mushroom spore upgraded!");
        //másik spórát szór
        //spore=s;
    }
    public void reduceHP(int ad)
    {
        System.out.println("    [Action] Mushroom HP reduced!");
        System.out.println("    [Action] If hp <= 0 Mushroom dies! (die_Mushroom())");
        /* hp -= ad;
        if(hp <= 0)
        {
            die_Mushroom();
        } */
    }
    public int get_hp()
    {
        //return hp;
        System.out.println("    [Get] Mushroom hp");
        return 0;
    }
    public void set_hp(int h)
    {
        System.out.println("    [Action] Calculate new Mushroom HP");
        //hp = h;
    }
    public int get_power()
    {
        System.out.println("    [Get] Mushroom power");
        //return power;
        return 0;
    }
    public void set_power(int p)
    {
        //power = p;
    }
    public int get_sporeCount()
    {
        System.out.println("    [Get] How much spore the Mushroom has spread");
        //return sporeCount;
        return 0;
    }
    public void set_sporeCount(int sc)
    {
        //sporeCount = sc;
    }
    public Tecton_Class get_Tecton()
    {
        //return tecton;
        return null;
    }
    public void set_Tecton(Tecton_Class t)
    {
        //tecton = t;
    }

}
