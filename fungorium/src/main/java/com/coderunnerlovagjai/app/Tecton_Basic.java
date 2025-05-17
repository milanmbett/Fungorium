package com.coderunnerlovagjai.app;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; // Added import
// import org.apache.logging.log4j.Level; // Keep commented if not directly used

public class Tecton_Basic extends Tecton_Class
{

    private static final Logger TECTON_BASIC_LOGGER = LogManager.getLogger(Tecton_Basic.class);
    public static final int DEFAULT_WIDTH = 140;
    public static final int DEFAULT_HEIGHT = 120;

    public Tecton_Basic()
    {
        super(); // Call superclass constructor
        mushroom = null;
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        ID = "Tecton_Basic"; // ID is inherited from Tecton_Class, which inherits from Entity
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT); // setSize is inherited from Entity
        TECTON_BASIC_LOGGER.debug("Tecton_Basic Created! ID: {}", ID); // Corrected logging
    }

    @Override
    public boolean isDead() {
        return false; 
    }

}
