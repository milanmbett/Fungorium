public class Insect_Buglet extends Insect_Class
{
    public Insect_Buglet(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_Buglet konstruktor");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        TargetTecton set!");
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        Insect added to targeted tecton!");
    }
}
