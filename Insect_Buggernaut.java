public class Insect_Buggernaut extends Insect_Class
{

    public Insect_Buggernaut(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_Buggernaut konstruktor");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        TargetTecton set!");
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        Insect added to targeted tecton!");
    }
}
