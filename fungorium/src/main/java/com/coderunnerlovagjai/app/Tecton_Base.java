package com.coderunnerlovagjai.app;

import java.util.ArrayList;
public class Tecton_Base extends Tecton_Basic
{
    Player owner;


    public Tecton_Base(Player p)
    {
        mushroom = new Mushroom_Grand(this);
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        owner = p;
        Plane.TectonCollection.add(this);
    }
}