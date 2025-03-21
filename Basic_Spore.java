public class Basic_Spore
{
    
    public Basic_Spore()
    {
        System.out.println("    [Called] Spore constructor");
    }

    public void consumed_by(Insect_Class insect)
    {
        die_Spore(); //maradhat
        System.out.println("    [Action] Spore eaten by insect");
    }
    public void die_Spore()
    {      
        //_SkeletonUtil._tectonClass.remove_Spore();        
        System.out.println("    [Action] Spore died");
    }
    public Tecton_Class get_Tecton()
    {
        System.out.println("    [Get] Spore get_Tecton");
        //return _SkeletonUtil._tectonClass;
        return null;
    }
    public void set_Tecton(Tecton_Class t)
    {
        //_SkeletonUtil._tectonClass = t;
        System.out.println("    [Set] Spore set_Tecton");
    }
    
}
