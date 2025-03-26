public class Insect_Buggernaut extends Insect_Class
{

    public Insect_Buggernaut(Tecton_Class targetTecton)
    {
        hp = 150; //_TMP value
        attackDamage = 50; //_TMP value
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
    public Insect_Buggernaut(Tecton_Class targetTecton, int hp, int ad, int as)
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
        Insect_Buggernaut duplicated = new Insect_Buggernaut(tecton, hp, attackDamage, availableSteps);
        tecton.get_InsectsOnTecton().add(duplicated);
        Plane.InsectCollection.add(duplicated);
    }
}
