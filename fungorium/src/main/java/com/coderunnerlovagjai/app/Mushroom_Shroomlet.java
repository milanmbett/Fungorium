package fungorium.src.main.java.com.coderunnerlovagjai.app;
public class Mushroom_Shroomlet extends Mushroom_Class
{
    public Mushroom_Shroomlet(Tecton_Class targetTecton)
    {
        hp = 250; //TODO: Értékét még meg kell beszélni
        power = 25; //TODO: Értékét még meg kell beszélni
        sporeCount = 0;
        tecton = targetTecton;
        Plane.MushroomCollection.add(this);
    }
}
