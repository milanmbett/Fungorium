public abstract class Insect_Class 
{
    protected int hp;
    protected int attackDamage;
    protected int availableSteps;

    public Insect_Class()
    {
        System.out.println("    [Called] Insect_Class constructor");
    }

    public void attack_Mushroom(Mushroom_Class m)
    {        
        _SkeletonUtil._mushroomClass.reduceHP(1);
        System.out.println("    [Action] Insect attacked mushroom!");
    }
    public void die_Insect()
    {
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().remove(this);
        System.out.println("    [Action] Insect died!");
        //Plane.InsectCollection.remove(this);
    }
    public void eat_Spore(Basic_Spore sp)
    {
        sp.consumed_by(this);
        System.out.println("    [Action] Spore consumed!");
    }
    public void eat_Thread(Thread_Class th)
    {        
        th.die_Thread();
        System.out.println("    [Action] Thread eaten!");        
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
    public void set_attackDamage(int damage)
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
    


    ///Lehet kéne egy buffer függvény mely eltárolja a spóra effektjét hogy ne tartson örökké 
    /// pl.: Spore_Slowing az csak következő támadásig tart csak kéne jegyezni hogy megtörtén-t e már a támadás vagy akkor hasonló módon a Spore_Paralysing is 
    /// mert az csak következő turn-be lesz érvényes
}
