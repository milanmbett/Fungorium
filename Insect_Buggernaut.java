public class Insect_Buggernaut extends Insect_Class
{

    public Insect_Buggernaut(Tecton_Class targetTecton)
    {
        hp = 150;
        attackDamage = 50;
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        //Plane.InsectCollection.add(this);
    }
}
