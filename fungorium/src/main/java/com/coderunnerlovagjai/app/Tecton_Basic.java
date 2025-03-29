package com.coderunnerlovagjai.app;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tecton_Basic extends Tecton_Class
{

    private static final Logger TECTON_BASIC_LOGGER = LogManager.getLogger(Tecton_Basic.class);

    public Tecton_Basic()
    {
        mushroom = null;
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        Plane.TectonCollection.add(this);
        TECTON_BASIC_LOGGER.log(Level.forName("CREATE",401),"Tecton_Basic Constructor called!");
        TECTON_BASIC_LOGGER.log(Level.forName("ADD", 403), "Tecton_Basic added to TectonCollection! TectonCollection size: " + Plane.TectonCollection.size());
    }
}
