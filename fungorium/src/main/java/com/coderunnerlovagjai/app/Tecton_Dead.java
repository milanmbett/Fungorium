package com.coderunnerlovagjai.app;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Tecton_Dead extends Tecton_Class
{
    private static final Logger TECTON_DEAD_LOGGER = LogManager.getLogger(Tecton_Dead.class);
    public Tecton_Dead()
    {
        mushroom = null;
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        ID = "Tecton_Dead";
        TECTON_DEAD_LOGGER.log(Level.forName("CREATE",401),"Tecton_Dead Created! ID: " + ID);
    }
    
    @Override
    public boolean isDead() {
        return true; // A Tecton_Dead mindig halott
    }

    public Tecton_Dead(Tecton_Class liveTecton) {
        // Copy neighbours from the living tecton.
        this.set_TectonNeighbours(new ArrayList<>(liveTecton.get_TectonNeighbours()));
        // Set fields appropriate for a dead tecton.
        this.mushroom = null;
        this.insectsOnTecton = new ArrayList<>();
        this.spore = null;
        this.thread = null;
        this.ID = "Tecton_Dead"; //Integer.toString(Plane.TectonCollection.size()
        // Add the dead tecton to the collection.
        TECTON_DEAD_LOGGER.log(Level.forName("TRANSFORM", 404), "Tecton transformed to Dead! New ID: " + this.ID);
    }
    
    @Override
    public void set_Thread(Thread_Class t)
    {
        throw new UnsupportedOperationException("Cannot set thread on dead tecton!");
    }
    
    @Override
    public void set_Mushroom(Mushroom_Class mush)
    {
        throw new UnsupportedOperationException("Cannot set mushroom on dead tecton!");
    }
    
    @Override
    public void set_Spore(Basic_Spore s)
    {
        throw new UnsupportedOperationException("Cannot set spore on dead tecton!");
    }
    
    @Override
    public void set_InsectsOnTecton(List<Insect_Class> insectList)
    {
        throw new UnsupportedOperationException("Cannot set insects on dead tecton!");
    }
    
    @Override
    public boolean canBeCracked() {
        return false; // Dead tectons cannot be cracked
    }

}
