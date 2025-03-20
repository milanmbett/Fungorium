public class Insect_ShroomReaper extends Insect_Class
{
    public Insect_ShroomReaper(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Insect_ShroomReaper konstruktor");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        TargetTecton set!");
        _SkeletonUtil._tectonClass.get_InsectsOnTecton().add(this);
        System.out.println("        Insect added to targeted tecton!");
    }
    public void destroy_Tecton()
    {
        //lehetne param-nélküli is hogyha csak azt a tektont tudja megölni amin áll ebben az esetben:
        //keveslem egy kicsit.
        _SkeletonUtil._tectonClass.die_Tecton();
        // ehhez hasonló kell: _SkeletonUtil._tectonClass. = _SkeletonUtil()._tectonDead
    }
    
}
