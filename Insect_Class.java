public abstract class Insect_Class 
{
    protected Tecton_Class tecton;

    public Insect_Class()
    {
        System.out.println("    [Call] Insect_class konstruktor");
    }

    public void move_Insect(Tecton_Class targetTecton)
    {
        // ide mit? _SkeletonUtil._tectonClass
    }
    public void attack_Mushroom(Mushroom_Class m)
    {        
        _SkeletonUtil._mushroomClass.reduceHP(1);
    }
    public void die_Insect()
    {
        _SkeletonUtil._.get_InsectsOnTecton().remove(this);
        //Plane.InsectCollection.remove(this);
    }
    public void eat_Spore(Basic_Spore sp)
    {
        sp.consumed_by(this);
    }
    public void eat_Thread(Thread_Class th)
    {
        th.die_Thread();
        
    }
    public void reduceHP(int ad)
    {
        hp -= ad;
        if(hp <= 0)
        {
            die_Insect();
        }
    }
    public int get_hp()
    {
        return hp;
    }
    public int get_attackDamage()
    {
        return attackDamage;
    }
    public void set_attackDagame(int damage)
    {
        attackDamage = damage;
    }
    public int get_availableSteps()
    {
        return availableSteps;
    }
    public void set_availableSteps(int steps)
    {
        availableSteps = steps;
    }
    public Tecton_Class get_Tecton()
    {
        return tecton;
    }
    public void set_Tecton(Tecton_Class t)
    {
        tecton = t;
    }


    ///Lehet kéne egy buffer függvény mely eltárolja a spóra effektjét hogy ne tartson örökké 
    /// pl.: Spore_Slowing az csak következő támadásig tart csak kéne jegyezni hogy megtörtén-t e már a támadás vagy akkor hasonló módon a Spore_Paralysing is 
    /// mert az csak következő turn-be lesz érvényes
}
