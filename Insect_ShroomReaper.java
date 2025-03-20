public class Insect_ShroomReaper extends Insect_Class
{
    public Insect_ShroomReaper(Tecton_Class targetTecton)
    {
        hp = 25; //_TMP value
        attackDamage = 25; //_TMP value
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        //Plane.InsectCollection.add(this);
    }
    public void destroy_Tecton(Tecton_Class t)
    {
        
    }
    
}
