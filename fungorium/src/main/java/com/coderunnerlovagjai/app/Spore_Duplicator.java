package fungorium.src.main.java.com.coderunnerlovagjai.app;
public class Spore_Duplicator extends Basic_Spore
{
    public Spore_Duplicator(Tecton_Class targetTecton)
    {
        timeToLive = 3; //TODO: Értékét még meg kell beszélni
        tecton = targetTecton;
        tecton.set_Spore(this);
        Plane.SporeCollection.add(this);
    }

    public void consumed_by(Insect_Class insect)
    {
        die_Spore();
        insect.duplicate_Insect();
    }    
}
