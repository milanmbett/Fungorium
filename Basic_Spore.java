public class Basic_Spore
{
    public Basic_Spore()
    {
        System.out.println("    [Called] Spóra konstruktor");
    }

    public void consumed_by(Insect_Class insect)
    {
        die_Spore();
        System.out.println("    [Action] Spórát megették");
    }
    public void die_Spore()
    {      
        _SkeletonUtil._tectonClass.remove_Spore();        
        System.out.println("    [Action] Spóra eltünt a tektonról! ");
    }
    public Tecton_Class get_Tecton()
    {
        return _SkeletonUtil._tectonClass;
    }
    public void set_Tecton(Tecton_Class t)
    {
        _SkeletonUtil._tectonClass = t;
    }
    
}
