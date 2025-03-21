public class Insect_ShroomReaper extends Insect_Class
{
    public Insect_ShroomReaper(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_ShroomReaper constructor");

        /* _SkeletonUtil._insectBuglet.hp = 25; 
        _SkeletonUtil._insectBuglet.attackDamage = 25; 
        _SkeletonUtil._insectBuglet.availableSteps = 1; */

        System.out.println("        [Insect_ShroomReaper] Insect stats set!");
        //_SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Insect_ShroomReaper] Targeted Tecton set!");
        //_SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        [Insect_ShroomReaper] Insect added to targeted tecton!");       
    }
    public void destroy_Tecton()
    {
        System.out.println("        [Insect_ShroomReaper] Insect destroyed the targeted tecton!");
    }
    
}
