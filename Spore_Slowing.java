public class Spore_Slowing extends Basic_Spore
{
    public Spore_Slowing(Tecton_Class targetTecton)
    {
        timeToLive = 3;
        tecton = targetTecton;
        //Plane.SporeCollection.add(this);
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {
        insect.eat_Spore(this);
        insect.set_attackDagame(insect.get_attackDamage()/2); //Felezzük az attackDamage-et
        die_Spore();
    }
}
