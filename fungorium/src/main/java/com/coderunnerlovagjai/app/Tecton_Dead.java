package com.coderunnerlovagjai.app;
import java.util.ArrayList;

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
        ID = "Tecton_Dead" + Integer.toString(Plane.TectonCollection.size());
        TECTON_DEAD_LOGGER.log(Level.forName("CREATE",401),"Tecton_Basic Created! ID: " + ID);
        Plane.TectonCollection.add(this);
        TECTON_DEAD_LOGGER.log(Level.forName("ADD", 403), "Tecton_Basic: "+ID+ " added to TectonCollection! TectonCollection size: " + Plane.TectonCollection.size());
    }
    @Override
    public void set_Thread(Thread_Class t)
    {
        throw new UnsupportedOperationException("Cannot set thread on dead tecton!");
    }
}
