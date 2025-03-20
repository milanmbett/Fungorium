public class Spore_Speed extends Basic_Spore
{
    public Spore_Speed(Tecton_Class targetTecton)
    {
        timeToLive = 3; //_TMP value
        tecton = targetTecton;
        //Plane.SporeCollection.add(this);
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        insect.eat_Spore(this);
        insect.set_availableSteps(insect.get_availableSteps()*2); //Kétszeresére növeljük az availableSteps-et? ezt nem tudom hogy ezt beszéltük-e meg
        die_Spore();
    }
}
