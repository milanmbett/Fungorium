public abstract class Insect_Class 
{
    private int hp;
    private int attackDamage;
    private int availableSteps;
    private Tecton_Class tecton;

    public void move_Insect()
    {

    }
    public void attack_Mushroom(Mushroom_Class m)
    {

    }
    public void die_Insect()
    {

    }
    public void eat_Spore()
    {

    }
    public void eat_Thread(Thread_Class th)
    {

    }
    public void reduceHP(int ad)
    {

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
}
