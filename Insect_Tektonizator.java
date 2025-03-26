public class Insect_Tektonizator extends Insect_Class
{
    public Insect_Tektonizator(Tecton_Class targetTecton)
    {
        hp = 25; //_TMP value
        attackDamage = 25; //_TMP value
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
    public void tectonCrack(Tecton_Class t)
    {
        
    }
}
