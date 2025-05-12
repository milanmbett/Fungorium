package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Visual representation of a Spore in the game.
 */
public class SporeView extends GraphicsObject<Basic_Spore> {
    private static final Color SPORE_COLOR = new Color(100, 255, 100);
    private static final Color SPORE_BORDER = new Color(0, 100, 0);
    
    public SporeView(Basic_Spore model) {
        super(model);
    }
    
    @Override
    public void render(Graphics2D g) {
        // Save original transform
        AffineTransform oldTransform = g.getTransform();
        
        // Apply rotation effect based on time-to-live for visual interest
        int ttl = model.get_timeToLive();
        double pulseScale = 0.9 + Math.sin(System.currentTimeMillis() / 300.0) * 0.1;
        
        AffineTransform pulse = new AffineTransform();
        pulse.translate(x + width / 2, y + height / 2);
        pulse.scale(pulseScale, pulseScale);
        pulse.translate(-(x + width / 2), -(y + height / 2));
        g.transform(pulse);
        
        // Draw spore body
        g.setColor(SPORE_COLOR);
        g.fillOval(x, y, width, height);
        
        // Draw border
        g.setColor(SPORE_BORDER);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(x, y, width, height);
        
        // Draw time-to-live counter
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        String ttlStr = String.valueOf(ttl);
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (width - fm.stringWidth(ttlStr)) / 2;
        int textY = y + height / 2 + fm.getAscent() / 2;
        g.drawString(ttlStr, textX, textY);
        
        // Draw owner indicator (small colored dot)
        /*if (model.get() != null) {
            Player owner = model.get_Owner();
            int playerID = owner.getId();
            if (playerID == 0) { // Player 1
                g.setColor(Color.BLUE);
            } else { // Player 2
                g.setColor(Color.ORANGE);
            }
            g.fillOval(x + width - 8, y + 2, 6, 6);
        }*/
        
        // Restore original transform
        g.setTransform(oldTransform);
    }
}
