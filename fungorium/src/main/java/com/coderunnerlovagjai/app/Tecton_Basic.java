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
        ID = "Tecton_Basic";
        TECTON_BASIC_LOGGER.log(Level.forName("CREATE",401),"Tecton_Basic Created! ID: " + ID);
    }

    @Override
    public boolean isDead() {
        return false; // A Tecton_Dead mindig halott
    }

}
