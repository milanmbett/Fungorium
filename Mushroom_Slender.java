public class Mushroom_Slender extends Mushroom_Class 
{
    public Mushroom_Slender(Tecton_Class targetTecton)
    {
        hp = 250; //_TMP value
        power = 25; //_TMP value
        sporeCount = 0;
        tecton = targetTecton;
        Plane.MushroomCollection.add(this);
    }
}
