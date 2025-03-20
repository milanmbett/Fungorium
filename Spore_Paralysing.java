public class Spore_Paralysing extends Basic_Spore
{
    public Spore_Paralysing(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Spore_Paralysing constructor");

        _SkeletonUtil._sporeParalysing.timeToLive = 3; 

        System.out.println("        [Spore_Paralysing] Spore stats set!");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Spore_Paralysing] Targeted Tecton set!");
    }
}
