public class Insect_Buglet extends Insect_Class
{
    public Insect_Buglet(Tecton_Class targetTecton)
    {
        hp = 100;
        attackDamage = 100;
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        //Plane.InsectCollection.add(this);
    }
}
