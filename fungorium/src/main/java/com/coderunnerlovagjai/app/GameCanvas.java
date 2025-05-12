package com.coderunnerlovagjai.app;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JPanel;

/** 
 * Canvas that paints all registered GraphicsObject instances.
 * Uses a layered approach to ensure proper drawing order.
 */
public class GameCanvas extends JPanel {
    private static final GameCanvas INSTANCE = new GameCanvas();
    private final List<GraphicsObject<?>> elements = new ArrayList<>();
    
    // Background color for the canvas
    private static final Color BG_COLOR = new Color(30, 30, 40);

    private GameCanvas() {
        setBackground(BG_COLOR);
        setDoubleBuffered(true); // Reduce flickering
    }

    public static GameCanvas getInstance() { return INSTANCE; }

    public void addGraphics(GraphicsObject<?> gfx) {
        elements.add(gfx);
    }
    
    public void removeGraphics(GraphicsObject<?> gfx) {
        elements.remove(gfx);
    }
    
    /**
     * Clear all graphics elements from the canvas.
     */
    public void clearAll() {
        elements.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Cast to Graphics2D for better rendering control
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable antialiasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Sort graphics objects by type to ensure proper rendering order:
        // 1. Tecton backgrounds first
        // 2. Threads connecting tectons
        // 3. Spores
        // 4. Mushrooms
        // 5. Insects
        // 6. UI elements on top
        elements.sort(Comparator.comparing(gfx -> getRenderPriority(gfx)));
        
        // Draw each graphic element
        for (var gfx : elements) {
            gfx.render(g2d);
        }
    }
    
    /**
     * Determine rendering priority based on object type.
     * Lower values are rendered first (background).
     */
    private int getRenderPriority(GraphicsObject<?> gfx) {
        Entity model = gfx.getModel();
        
        if (model instanceof Tecton_Class) return 0;
        if (model instanceof Thread_Class) return 1;
        if (model instanceof Basic_Spore) return 2;
        if (model instanceof Mushroom_Class) return 3;
        if (model instanceof Insect_Class) return 4;
        
        // Default priority for other types
        return 5;
    }
}