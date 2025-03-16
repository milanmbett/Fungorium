public class Spore_Paralysing extends Basic_Spore
{
    public Spore_Paralysing(Tecton_Class targetTecton)
    {
        timeToLive = 3;
        tecton = targetTecton;
        Plane.SporeCollection.add(this);
    }
}
