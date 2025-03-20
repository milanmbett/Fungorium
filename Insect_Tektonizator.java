public class Insect_Tektonizator extends Insect_Class
{
    public Insect_Tektonizator(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_Tektonizator constructor");

        _SkeletonUtil._insectBuglet.hp = 25; 
        _SkeletonUtil._insectBuglet.attackDamage = 25; 
        _SkeletonUtil._insectBuglet.availableSteps = 1;

        System.out.println("        [Insect_Tektonizator] Insect stats set!");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Insect_Tektonizator] Targeted Tecton set!");
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        [Insect_Tektonizator] Insect added to targeted tecton!");    
    }
    public void tectonCrack(Tecton_Class t)
    {
        System.out.println("        [Insect_Tektonizator] Insect cracked tecton!");    
    }
}
