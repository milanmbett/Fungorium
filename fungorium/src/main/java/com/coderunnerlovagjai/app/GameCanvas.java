package com.coderunnerlovagjai.app;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/** Canvas that paints all registered GraphicsObject instances. */
public class GameCanvas extends JPanel {
    private static final GameCanvas INSTANCE = new GameCanvas();
    private final List<GraphicsObject<?>> elements = new ArrayList<>();

    public static GameCanvas getInstance() { return INSTANCE; }

    public void addGraphics(GraphicsObject<?> gfx) {
        elements.add(gfx);
    }
    public void removeGraphics(GraphicsObject<?> gfx) {
        elements.remove(gfx);
        getInstance().repaint();
    }


    public void clearAll() {
        elements.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (var gfx : elements) {
            gfx.render((Graphics2D) g);
        }
    }
}