public abstract class Insect_Class 
{
    /* protected int hp;
    protected int attackDamage;
    protected int availableSteps; 
    protected Tecton_Class tecton;*/
    
    public Insect_Class()
    {
        System.out.println("    [Called] Insect_Class constructor");
    }

    public void move_Insect(Tecton_Class targettecton)
    {
        //availableSteps--;
        System.out.println("    - Avaible steps reduced!");
        //targettecton.set_InsectsOnTecton(this);
        System.out.println("    [Set] targettecton.set_InsectsOnTecton(this)");
        //tecton.remove_InsectsOnTecton(); 
        System.out.println("    [Set] tecton.remove_InsectsOnTecton()");
        System.out.println("    [Action] Insect moved!");
    }

    public void attack_Mushroom(Mushroom_Class m)
    {   
        System.out.println("    [Action] Insect attacked mushroom!");     
        m.reduceHP(get_attackDamage());
    }
    public void die_Insect()
    {
        //_SkeletonUtil._tectonClass.get_InsectsOnTecton().remove(this);
        System.out.println("    [Action] Insect died!");
        //Plane.InsectCollection.remove(this);
    }
    public void eat_Spore(Basic_Spore sp)
    {
        //sp.consumed_by(this);
        System.out.println("    [Action] Spore consumed!");
    }
    public void eat_Thread(Thread_Class th)
    {        
        //th.die_Thread();
        System.out.println("    [Action] Thread eaten!");        
    }
    public void reduceHP(int ad)
    {
        System.out.println("    [Action] Insect HP reduced!");
        System.out.println("    [Action] If hp <= 0 Insect dies! (die_Insect())");
    }
    public int get_hp()
    {
        //return hp;
        return 0;
    }
    public int get_attackDamage()
    {
        //return attackDamage;
        System.out.println("    [Get] Insect attackDamage");
        return 0;
    }
    public void set_attackDamage(int damage)
    {
        //attackDamage = damage;
    }
    public int get_availableSteps()
    {
        //return availableSteps;
        return 0;
    }
    public void set_availableSteps(int steps)
    {
        //availableSteps = steps;
    }
    


    ///Lehet kéne egy buffer függvény mely eltárolja a spóra effektjét hogy ne tartson örökké 
    /// pl.: Spore_Slowing az csak következő támadásig tart csak kéne jegyezni hogy megtörtén-t e már a támadás vagy akkor hasonló módon a Spore_Paralysing is 
    /// mert az csak következő turn-be lesz érvényes
}
