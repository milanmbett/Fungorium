// Graphics_Insect.java
package com.coderunnerlovagjai.app;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Graphics_Insect extends GraphicsObject<Insect_Class> {
    public Graphics_Insect(Insect_Class model) {
        super(model);
    }

    @Override
    public void render(Graphics2D g) {
        // transform for rotation/position
        AffineTransform old = g.getTransform();
        g.translate(x, y);
        g.rotate(Math.toRadians(model.getRotation()), width/2.0, height/2.0);

        // draw the insect image or simple shape
        if (img != null) {
            g.drawImage(img, 0, 0, width, height, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
        }

        g.setTransform(old);
    }
}
