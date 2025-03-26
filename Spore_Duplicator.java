public class Spore_Duplicator extends Basic_Spore
{
    public Spore_Duplicator(Tecton_Class targetTecton)
    {
        timeToLive = 3; //_TMP value
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
