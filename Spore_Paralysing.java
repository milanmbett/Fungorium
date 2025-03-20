public class Spore_Paralysing extends Basic_Spore
{
    public Spore_Paralysing(Tecton_Class targetTecton)
    {
        timeToLive = 3; //_TMP value
        tecton = targetTecton;
        //Plane.SporeCollection.add(this);
    }
    @Override
    public void consumed_by(Insect_Class insect)
    {   //jelezni kéne hogy a következő körben avaliableSteps = 0 valahogy
        die_Spore();
    }
}
