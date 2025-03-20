import java.util.ArrayList;
import java.util.List;

public class Tecton_Dead extends Tecton_Class
{
    public Tecton_Dead()
    {
        mushroom = null;
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        //Plane.TectonCollection.add(this);
    }
    @Override
    public void set_InsectsOnTecton(List<Insect_Class> insectList)
    {
       throw new UnsupportedOperationException("Tecton is dead, insects cannot be on it!");
    }
    @Override
    public void set_Mushroom(Mushroom_Class m)
    {
        throw new UnsupportedOperationException("Tecton is dead, mushroom cannot be on it!");
    }
    @Override
    public void set_Spore(Basic_Spore s)
    {
        throw new UnsupportedOperationException("Tecton is dead, spore cannot be on it!");
    }
    @Override
    public void set_Thread(Thread_Class t)
    {
        throw new UnsupportedOperationException("Tecton is dead, thread cannot be on it!");
    }
}
