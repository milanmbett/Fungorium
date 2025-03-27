package fungorium.src.main.java.com.coderunnerlovagjai.app;
public class Insect_Stinger extends Insect_Class
{
    public Insect_Stinger(Tecton_Class targetTecton)
    {
        hp = 50; //TODO: Értékét még meg kell beszélni
        attackDamage = 150; //TODO: Értékét még meg kell beszélni
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
    public Insect_Stinger(Tecton_Class targetTecton, int hp, int ad, int as)
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
        Insect_Stinger duplicated = new Insect_Stinger(tecton, hp, attackDamage, availableSteps);
        tecton.get_InsectsOnTecton().add(duplicated);
        Plane.InsectCollection.add(duplicated);
    }
}
