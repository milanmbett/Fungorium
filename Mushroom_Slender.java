public class Mushroom_Slender extends Mushroom_Class 
{
    public Mushroom_Slender(Tecton_Class targetTecton)
    {
        hp = 250; 
        power = 25;
        sporeCount = 0;
        tecton = targetTecton;
        Plane.MushroomCollection.add(this);
    }
}
