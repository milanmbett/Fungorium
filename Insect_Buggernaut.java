public class Insect_Buggernaut extends Insect_Class
{

    public Insect_Buggernaut(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_Buggernaut constructor");

        _SkeletonUtil._insectBuggernaut.hp = 100; 
        _SkeletonUtil._insectBuggernaut.attackDamage = 100; 
        _SkeletonUtil._insectBuggernaut.availableSteps = 1;

        System.out.println("        [Insect_Buggernaut] Insect stats set!");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Insect_Buggernaut] Targeted Tecton set!");
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        [Insect_Buggernaut] Insect added to targeted tecton!");
    }
}
