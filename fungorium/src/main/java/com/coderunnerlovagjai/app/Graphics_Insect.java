package com.coderunnerlovagjai.app;

import java.awt.Graphics2D;

public class Graphics_Insect extends Graphics_Object
{
    public Graphics_Insect(Insect_Class m)
    {
        this.model = m;
        model.addListener(this::onModelEvent);
    }
    private void onModelEvent(ModelEvent e) {
        // szükség esetén logika: 
        //   ha e.type == REMOVED -> unregister és eltávolítjuk magunkat a render listából
        //   ha e.type == UPDATED -> repaint kérése
       // GameCanvas.getInstance().repaint();
    }
    public void render(Graphics2D g) {
        // 1. koordináta‑transzformáció a model.getPosition(), model.getRotation() alapján
        // 2. alak kirajzolása (shape, kép, szín)
    }
    private void unregister() 
    {
        model.removeListener(this::onModelEvent);
    }
}
