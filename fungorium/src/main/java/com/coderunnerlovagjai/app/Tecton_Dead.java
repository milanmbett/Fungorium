package com.coderunnerlovagjai.app;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Tecton_Dead extends Tecton_Class
{
    private static final Logger TECTON_DEAD_LOGGER = LogManager.getLogger(Tecton_Dead.class);
    private int deadVariant; // 1 for first half, 2 for second half

    // Default constructor - might be used by deserialization or specific cases
    public Tecton_Dead()
    {
        this(1); // Default to variant 1 if not specified
    }

    // Constructor to specify the variant
    public Tecton_Dead(int variant) {
        this.mushroom = null;
        this.insectsOnTecton = new ArrayList<>();
        this.spore = null;
        this.thread = null;
        this.ID = String.format("Tecton_Dead_Variant_%d", variant); // Make ID more specific
        this.deadVariant = variant;
        TECTON_DEAD_LOGGER.log(Level.forName("CREATE",401),"Tecton_Dead (Variant: {}) Created! ID: {}", variant, ID);
    }
    
    // Getter for the dead variant
    public int getDeadVariant() {
        return deadVariant;
    }

    @Override
    public boolean isDead() {
        return true; // A Tecton_Dead mindig halott
    }

    // The constructor public Tecton_Dead(Tecton_Class liveTecton) has been removed as per previous step.
    // If it's needed, it should be updated to handle the deadVariant.

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
