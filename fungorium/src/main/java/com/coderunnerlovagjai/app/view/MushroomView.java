package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Visual representation of a Mushroom in the game.
 */
public class MushroomView extends GraphicsObject<Mushroom_Class> {
    private static final int HEALTH_BAR_HEIGHT = 3;
    private static final Color HEALTH_COLOR = Color.GREEN;
    private static final Color DAMAGE_COLOR = Color.RED;
    
    private Color mushroomColor;
    
    public MushroomView(Mushroom_Class model) {
        super(model);
        updateMushroomColor();
    }
    
    private void updateMushroomColor() {
        // Different colors for different mushroom types
        if (model instanceof Mushroom_Grand) {
            mushroomColor = new Color(255, 100, 100); // Red-ish for Grand
        } else if (model instanceof Mushroom_Shroomlet) {
            mushroomColor = new Color(150, 255, 150); // Green-ish for Shroomlet
        } else if (model instanceof Mushroom_Slender) {
            mushroomColor = new Color(150, 150, 255); // Blue-ish for Slender
        } else if (model instanceof Mushroom_Maximus) {
            mushroomColor = new Color(255, 200, 100); // Orange-ish for Maximus
        } else {
            mushroomColor = new Color(255, 180, 180); // Default mushroom color
        }
        
        // Apply player-specific tinting
        if (model.get_Owner() != null) {
            Player owner = model.get_Owner();
            if (owner.getId() == 0) { // Player 1
                mushroomColor = new Color(
                    Math.min(255, mushroomColor.getRed()),
                    mushroomColor.getGreen(),
                    Math.min(255, mushroomColor.getBlue() + 50)
                );
            } else { // Player 2
                mushroomColor = new Color(
                    Math.min(255, mushroomColor.getRed() + 50),
                    Math.min(255, mushroomColor.getGreen() + 50),
                    mushroomColor.getBlue()
                );
            }
        }
    }
    
    @Override
    protected void updateFromModel() {
        super.updateFromModel();
        updateMushroomColor();
    }
    
    @Override
    public void render(Graphics2D g) {
        // Save original transform
        AffineTransform oldTransform = g.getTransform();
        
        // Draw mushroom cap
        int capWidth = width;
        int capHeight = height / 2;
        g.setColor(mushroomColor);
        g.fillOval(x, y, capWidth, capHeight);
        
        // Draw mushroom stem
        int stemWidth = width / 3;
        int stemHeight = height / 2;
        int stemX = x + (width - stemWidth) / 2;
        int stemY = y + capHeight - 3; // Slightly overlap with cap
        g.setColor(new Color(240, 240, 240)); // Whitish stem
        g.fillRect(stemX, stemY, stemWidth, stemHeight);
        
        // Draw borders
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawOval(x, y, capWidth, capHeight);
        g.drawRect(stemX, stemY, stemWidth, stemHeight);
        
        // Draw power indicator
        String powerStr = String.valueOf(model.get_power());
        g.setFont(new Font("Arial", Font.BOLD, 10));
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (width - fm.stringWidth(powerStr)) / 2;
        int textY = y + height / 2 + fm.getAscent() / 2;
        g.setColor(Color.WHITE);
        g.drawString(powerStr, textX, textY);
        
        // Draw level if applicable
        /*if (model.get_() > 1) {
            String levelStr = "Lvl " + model.getLevel();
            int levelX = x + width - fm.stringWidth(levelStr) - 2;
            int levelY = y + height - 5;
            g.setColor(Color.YELLOW);
            g.drawString(levelStr, levelX, levelY);
        }*/
        
        // Draw health bar
        drawHealthBar(g);
        
        // Draw spore count if not zero
        if (model.get_sporeCount() > 0) {
            String sporeStr = "S:" + model.get_sporeCount();
            int sporeX = x + 2;
            int sporeY = y + height - 5;
            g.setColor(Color.GREEN);
            g.drawString(sporeStr, sporeX, sporeY);
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
