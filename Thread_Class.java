import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Thread_Class 
{
    private Tecton_Class tecton;

    public Thread_Class(Tecton_Class targetTecton)
    {
        tecton = targetTecton;
        tecton.set_Thread(this);
        Plane.ThreadCollection.add(this);
    }
    public Tecton_Class get_Tecton()
    {
        return tecton;
    }
    public void set_Tecton(Tecton_Class t)
    {
        tecton = t;
    }
    public void expand_Thread()
    {
        //menjen végig a tektonja szomszédsági listáján és ha van szabad hely, akkor hozzon létre egy új fonalat véletlenszerűen
        //jelenleg nem nézi meg hogy halott tekton-e a szomszéd ,de dobunk egy UnsupportedOperationException vagy valami mást
        //azt elkapjuk akkor majd próbáljuk újra esetleg vagy az is lehetne hogy ha pont azt választja ki akkor nem csinál semmit azt nincs semmi baj
        
        if(tecton.get_TectonNeighbours().size()==0)
        {
            return;
        }
        List<Tecton_Class> threadlessTectonNeighbours = new ArrayList<>();
        for(Tecton_Class t : tecton.get_TectonNeighbours())
        {
            if(t.get_Thread().equals(null))
            {
                threadlessTectonNeighbours.add(t);
            } 
        }
        if(threadlessTectonNeighbours.size()==0)
        {
            return;
        }
        Random random = new Random();
        int rand = random.nextInt(threadlessTectonNeighbours.size());
        threadlessTectonNeighbours.get(rand).set_Thread(new Thread_Class(threadlessTectonNeighbours.get(rand)));
    }
    public void die_Thread()
    {
        tecton.set_Thread(null);
        Plane.ThreadCollection.remove(this);
    }
}

