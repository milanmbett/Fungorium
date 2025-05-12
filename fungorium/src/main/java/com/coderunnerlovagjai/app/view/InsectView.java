package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Visual representation of an Insect in the game.
 */
public class InsectView extends GraphicsObject<Insect_Class> {
    private static final int HEALTH_BAR_HEIGHT = 3;
    private static final Color HEALTH_COLOR = Color.GREEN;
    private static final Color DAMAGE_COLOR = Color.RED;
    
    private Color insectColor;
    
    public InsectView(Insect_Class model) {
        super(model);
        updateInsectColor();
    }
    
    private void updateInsectColor() {
        // Different colors for different insect types
        if (model instanceof Insect_Buggernaut) {
            insectColor = new Color(150, 0, 0);
        } else if (model instanceof Insect_ShroomReaper) {
            insectColor = new Color(50, 0, 150);
        } else {
            insectColor = Color.MAGENTA; // Default color
        }
        
        // Apply player-specific tinting
        if (model.get_Owner() != null) {
            Player owner = model.get_Owner();
            if (owner.getId() == 0) { // Player 1
                insectColor = new Color(
                    Math.min(255, insectColor.getRed() + 50),
                    insectColor.getGreen(),
                    Math.min(255, insectColor.getBlue() + 100)
                );
            } else { // Player 2
                insectColor = new Color(
                    Math.min(255, insectColor.getRed() + 100),
                    Math.min(255, insectColor.getGreen() + 50),
                    insectColor.getBlue()
                );
            }
        }
    }
    
    @Override
    protected void updateFromModel() {
        super.updateFromModel();
        updateInsectColor();
    }
    
    @Override
    public void render(Graphics2D g) {
        // Save original transform
        AffineTransform oldTransform = g.getTransform();
        
        // Apply rotation if needed (could be used for movement animation)
        if (model.getRotation() != 0) {
            AffineTransform rotation = new AffineTransform();
            rotation.rotate(model.getRotation(), x + width / 2, y + height / 2);
            g.transform(rotation);
        }
        
        // Draw insect body
        g.setColor(insectColor);
        g.fillOval(x, y, width, height);
        
        // Draw border
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(x, y, width, height);
        
        // Draw attack damage indicator
        String attackStr = String.valueOf(model.get_attackDamage());
        g.setFont(new Font("Arial", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (width - fm.stringWidth(attackStr)) / 2;
        int textY = y + height / 2 + fm.getAscent() / 2;
        g.setColor(Color.WHITE);
        g.drawString(attackStr, textX, textY);
        
        // Draw health bar
        drawHealthBar(g);
        
        // Draw paralysis indicator if insect is paralyzed
        if (model.get_isParalysed()) {
            g.setColor(new Color(255, 255, 0, 180)); // Yellow with transparency
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(x, y, x + width, y + height);
            g.drawLine(x + width, y, x, y + height);
        }
        
        // Restore original transform
        g.setTransform(oldTransform);
    }
    
    private void drawHealthBar(Graphics2D g) {
        int maxHP = 100;
        int currentHP = model.get_hp();
        
        if (maxHP > 0) {
            int healthWidth = (int)((float)currentHP / maxHP * width);
            
            // Background (damaged portion)
            g.setColor(DAMAGE_COLOR);
            g.fillRect(x, y - HEALTH_BAR_HEIGHT - 2, width, HEALTH_BAR_HEIGHT);
            
            // Foreground (current health)
            g.setColor(HEALTH_COLOR);
            g.fillRect(x, y - HEALTH_BAR_HEIGHT - 2, healthWidth, HEALTH_BAR_HEIGHT);
            
            // Border
            g.setColor(Color.BLACK);
            g.drawRect(x, y - HEALTH_BAR_HEIGHT - 2, width, HEALTH_BAR_HEIGHT);
        }
    }
}
