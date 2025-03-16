public class Insect_Stinger extends Insect_Class
{
    public Insect_Stinger(Tecton_Class targetTecton)
    {
        hp = 50;
        attackDamage = 150;
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
}
