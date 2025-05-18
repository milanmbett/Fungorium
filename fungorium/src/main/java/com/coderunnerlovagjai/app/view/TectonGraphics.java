package com.coderunnerlovagjai.app.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import com.coderunnerlovagjai.app.GraphicsObject;
import com.coderunnerlovagjai.app.Tecton_Class;

/**
 * Draws a hex tile and its contents based on the underlying Tecton model.
 */
public class TectonGraphics extends GraphicsObject<Tecton_Class> {
    // model and registration handled by super
    private final Polygon hexShape;
    private final int[] xs, ys;

    public TectonGraphics(Tecton_Class model) {
        super(model);
        // Precompute a regular hexagon centered at (0,0) radius 40
        xs = new int[6]; ys = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i - 30);
            xs[i] = (int) (40 * Math.cos(angle));
            ys[i] = (int) (40 * Math.sin(angle));
        }
        hexShape = new Polygon(xs, ys, 6);
    }

    @Override public void render(Graphics2D g) {
        // translate to model position (fields x,y updated by super)
        g.translate(x, y);

        // draw shadow for depth
        g.setColor(new Color(40, 40, 40, 120));
        Polygon shadow = new Polygon();
        for (int i = 0; i < 6; i++) shadow.addPoint(xs[i]+4, ys[i]+6);
        g.fill(shadow);

        // fill base with brighter color
        g.setColor(new Color(180, 140, 80));
        g.fill(hexShape);
        // thick white border
        g.setColor(Color.WHITE);
        g.setStroke(new java.awt.BasicStroke(3f));
        g.draw(hexShape);
        g.setStroke(new java.awt.BasicStroke(1f));

        // draw mushroom if present
        if (model.get_Mushroom() != null) {
            g.setColor(Color.GREEN);
            g.fillOval(-10, -10, 20, 20);
        }
        // draw insect count
        int count = model.get_InsectsOnTecton().size();
        if (count > 0) {
            g.setColor(Color.RED);
            g.drawString(String.valueOf(count), -5, 5);
        }

        // reset transform
        g.translate(-x, -y);
    }
}
