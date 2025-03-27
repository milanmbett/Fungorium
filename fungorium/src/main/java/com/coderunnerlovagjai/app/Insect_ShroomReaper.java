package com.coderunnerlovagjai.app;
import java.util.List;
public class Insect_ShroomReaper extends Insect_Class
{
    public Insect_ShroomReaper(Tecton_Class targetTecton)
    {
        hp = 25; //TODO: Értékét még meg kell beszélni
        attackDamage = 25; //TODO: Értékét még meg kell beszélni
        availableSteps = 1;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
    public Insect_ShroomReaper(Tecton_Class targetTecton, int hp, int ad, int as)
    {
        this.hp = hp;
        attackDamage = ad;
        availableSteps = as;
        tecton = targetTecton;
        tecton.get_InsectsOnTecton().add(this);
        Plane.InsectCollection.add(this);
    }
    public void destroy_Tecton(Tecton_Class t)
    {
        List<Tecton_Class> tmp_tectonList = t.get_TectonNeighbours();
        tecton.die_Tecton();
        tecton = new Tecton_Dead();
        tecton.set_TectonNeighbours(tmp_tectonList);
    }
    @Override
    public void duplicate_Insect()
    {
        Insect_ShroomReaper duplicated = new Insect_ShroomReaper(tecton);
        tecton.get_InsectsOnTecton().add(duplicated);
        Plane.InsectCollection.add(duplicated);
    }
    
}
