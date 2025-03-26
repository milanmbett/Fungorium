public class Mushroom_Slender extends Mushroom_Class 
{
    public Mushroom_Slender(Tecton_Class targetTecton)
    {
        hp = 250; //TODO: Értékét még meg kell beszélni
        power = 25; //TODO: Értékét még meg kell beszélni
        sporeCount = 0;
        tecton = targetTecton;
        Plane.MushroomCollection.add(this);
    }
}
