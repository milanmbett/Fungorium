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
    }
}
