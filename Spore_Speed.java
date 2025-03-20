public class Spore_Speed extends Basic_Spore
{
    public Spore_Speed(Tecton_Class targetTecton)
    {
        System.out.println("    [Called] Spore_Speed constructor");

        _SkeletonUtil._sporeSpeed.timeToLive = 3; 

        System.out.println("        [Spore_Speed] Spore stats set!");
        _SkeletonUtil._tectonClass = targetTecton;
        System.out.println("        [Spore_Speed] Targeted Tecton set!");
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        _SkeletonUtil._insectClass.eat_Spore(this);
        _SkeletonUtil._insectClass.set_availableSteps(_SkeletonUtil._insectClass.get_availableSteps()*2); //Kétszeresére növeljük az availableSteps-et? ezt nem tudom hogy ezt beszéltük-e meg
        die_Spore();
        System.out.println("        [Spore_Speed] Spore got consumed, insect's available steps doubled!");
    }
}
