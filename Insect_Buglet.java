public class Insect_Buglet extends Insect_Class
{
    public Insect_Buglet(Tecton_Class targetTecton)
    {
        hp = 100; //_TMP value
        attackDamage = 100; //_TMP value
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
    public Insect_Buglet(Tecton_Class targetTecton, int hp, int ad, int as)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_Buglet duplicated = new Insect_Buglet(tecton, hp, attackDamage, availableSteps);
        tecton.get_InsectsOnTecton().add(duplicated);
        Plane.InsectCollection.add(duplicated);
    }
}
