public class Mushroom_Shroomlet extends Mushroom_Class
{
    public Mushroom_Shroomlet(Tecton_Class targetTecton)
    {
        hp = 250;
        power = 25;
        sporeCount = 0;
        tecton = targetTecton;
        Plane.MushroomCollection.add(this);
    }
}
