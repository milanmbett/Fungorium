public class Insect_Stinger extends Insect_Class
{
    public Insect_Stinger(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_Stinger constructor");

        _SkeletonUtil._insectBuglet.hp = 50; 
        _SkeletonUtil._insectBuglet.attackDamage = 150; 
        _SkeletonUtil._insectBuglet.availableSteps = 1;

        System.out.println("        [Insect_Stinger] Insect stats set!");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Insect_Stinger] Targeted Tecton set!");
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        [Insect_Stinger] Insect added to targeted tecton!");       
    }
}
