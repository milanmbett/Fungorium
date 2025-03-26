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
}
