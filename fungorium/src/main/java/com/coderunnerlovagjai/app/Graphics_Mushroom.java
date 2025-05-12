// Graphics_Mushroom.java
package com.coderunnerlovagjai.app;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Graphics_Mushroom extends GraphicsObject<Mushroom_Class> {
    public Graphics_Mushroom(Mushroom_Class model) {
        super(model);
    }

    @Override
    public void render(Graphics2D g) {
        // 1. set up transform based on model.getRotation(), etc.
        AffineTransform old = g.getTransform();
        g.translate(x, y);
        g.rotate(Math.toRadians(model.getRotation()), width/2.0, height/2.0);

        // 2. draw the mushroom image (or fallback to a shape)
        if (img != null) {
            g.drawImage(img, 0, 0, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(0, 0, width, height);
        }

        g.setTransform(old);
    }
}
