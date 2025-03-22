public class Mushroom_Grand extends Mushroom_Class 
{
    private int level;
    public Mushroom_Grand(Tecton_Class targetTecton)
    {
        /* hp = 250; //_TMP value
        power = 25; //_TMP value
        sporeCount = 0;
        tecton = targetTecton;
        level = 1; */
        //Plane.MushroomCollection.add(this);
        System.out.println("    [Called] Grand Fungus constructor");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Grand Fungus] Targeted Tecton set!");
    }
    public int get_level()
    {
        return level;
    }
    public void set_level(int l)
    {
        level = l;
    }
    public void upgrade_Grand()
    {
        /* if(level == 1)
        {
            level = 2;
            hp = 500; //_TMP value
            power = 50; //_TMP value
        }
        else if(level == 2)
        {
            level = 3;
            hp = 750; //_TMP value
            power = 75; //_TMP value
        }
        else
        {
            System.out.println("A gomba már maximális szinten van!");
        } */
    }
}
