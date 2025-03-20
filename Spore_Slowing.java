public class Spore_Slowing extends Basic_Spore
{
    public Spore_Slowing(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Spore_Slowing constructor");

        _SkeletonUtil._sporeSlowing.timeToLive = 3; 

        System.out.println("        [Spore_Slowing] Spore stats set!");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Spore_Slowing] Targeted Tecton set!");
    }

    @Override
    public void consumed_by(Insect_Class insect)
    {
        _SkeletonUtil._insectClass.eat_Spore(this);
        _SkeletonUtil._insectClass.set_attackDamage(_SkeletonUtil._insectClass.get_attackDamage()/2); //Felezzük az attackDamage-et
        die_Spore();
        System.out.println("        [Spore_Slowing] Spore got consumed, attackDamage got halved!");
    }
}
