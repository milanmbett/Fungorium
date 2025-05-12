package com.coderunnerlovagjai.app;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class Graphics_Spore extends GraphicsObject<Basic_Spore> {
    public Graphics_Spore(Basic_Spore model) {
        super(model);
    }

    @Override
    public void render(Graphics2D g) {
        AffineTransform old = g.getTransform();
        g.translate(x, y);
        g.rotate(Math.toRadians(model.getRotation()), width/2.0, height/2.0);

        if (img != null) {
            g.drawImage(img, 0, 0, width, height, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillOval(0, 0, width, height);
        }

        g.setTransform(old);
    }
}
