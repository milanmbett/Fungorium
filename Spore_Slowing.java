public class Spore_Slowing extends Basic_Spore
{
    public Spore_Slowing(Tecton_Class targetTecton)
    {
        timeToLive = 3;
        tecton = targetTecton;
        //Plane.SporeCollection.add(this);
    }
}
