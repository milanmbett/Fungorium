public class Spore_Slowing extends Basic_Spore
{
    public Spore_Slowing(Tecton_Class targetTecton)
    {
        timeToLive = 3; //_TMP value
        tecton = targetTecton;
        //Plane.SporeCollection.add(this);
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        insect.set_attackDamage(insect.get_attackDamage()/2); //Felezzük az attackDamage-et
        die_Spore();
    }
}
