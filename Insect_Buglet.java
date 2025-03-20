public class Insect_Buglet extends Insect_Class
{
    public Insect_Buglet(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_Buglet constructor");

        _SkeletonUtil._insectBuglet.hp = 100; 
        _SkeletonUtil._insectBuglet.attackDamage = 100; 
        _SkeletonUtil._insectBuglet.availableSteps = 1;

        System.out.println("        [Insect_Buglet] Insect stats set!");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Insect_Buglet] Targeted Tecton set!");
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        [Insect_Buglet] Insect added to targeted tecton!");
    }
}
