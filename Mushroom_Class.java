public abstract class Mushroom_Class 
{
    private int hp;
    private int power;
    private int sporeCount;
    private Tecton_Class tecton;
     
    public void spawn_Spores()
    {

    } 
    public void generate_Income() ///???
    {

    } 
    public void attack_Insect(Insect_Class i)
    {

    }
    public void upgrade_Mushroom()
    {

    }
    public void reduceHP(int ad)
    {

    }
    public int get_hp()
    {
        return hp;
    }
    public void set_hp(int h)
    {
        hp = h;
    }
    public int get_power()
    {
        return power;
    }
    public void set_power(int p)
    {
        power = p;
    }
    public int get_sporeCount()
    {
        return sporeCount;
    }
    public void set_sporeCount(int sc)
    {
        sporeCount = sc;
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
