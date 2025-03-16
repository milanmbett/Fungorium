public class Spore_Speed extends Basic_Spore
{
    public Spore_Speed(Tecton_Class targetTecton)
    {
        timeToLive = 3;
        tecton = targetTecton;
        Plane.SporeCollection.add(this);
    }
}
