package com.coderunnerlovagjai.app;
import java.util.ArrayList;

public class Tecton_Basic extends Tecton_Class
{
    public Tecton_Basic()
    {
        mushroom = null;
        insectsOnTecton = new ArrayList<>();
        spore = null;
        thread = null;
        Plane.TectonCollection.add(this);
    }
}
