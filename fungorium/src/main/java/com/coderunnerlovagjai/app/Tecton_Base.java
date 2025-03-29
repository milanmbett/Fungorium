package com.coderunnerlovagjai.app;


import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Tecton_Base extends Tecton_Basic
{
    private Player owner;

    private static final Logger TECTON_BASE_LOGGER = LogManager.getLogger(Tecton_Base.class);

    public Tecton_Base(Player p)
    {
        mushroom = new Mushroom_Grand(this,p);
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        owner = p;
        Plane.TectonCollection.add(this);
        TECTON_BASE_LOGGER.log(Level.forName("CREATE",401),"Tecton_Base Constructor called!");
        TECTON_BASE_LOGGER.log(Level.forName("ADD", 403), "Tecton_Base added to TectonCollection! TectonCollection size: " + Plane.TectonCollection.size());
    }
    public Player get_Owner()
    {
        return owner;
    }
    public void set_Owner(Player p)
    {
        owner = p;
    }
}