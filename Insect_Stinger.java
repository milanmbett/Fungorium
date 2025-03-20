public class Insect_Stinger extends Insect_Class
{
    public Insect_Stinger(Tecton_Class targetTecton)
    {
        hp = 50; //_TMP value
        attackDamage = 150; //_TMP value
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        //Plane.InsectCollection.add(this);
    }
}
