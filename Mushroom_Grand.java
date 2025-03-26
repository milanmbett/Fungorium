public class Mushroom_Grand extends Mushroom_Class 
{
    public Mushroom_Grand(Tecton_Class targetTecton)
    {
        hp = 250; //TODO: Értékét még meg kell beszélni
        power = 25; //TODO: Értékét még meg kell beszélni
        sporeCount = 0;
        tecton = targetTecton;
        Plane.MushroomCollection.add(this);
    }
}
