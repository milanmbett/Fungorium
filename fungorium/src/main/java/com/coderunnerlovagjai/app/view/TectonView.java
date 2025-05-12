package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.*;
import com.coderunnerlovagjai.app.view.MouseHandlerForTectons;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Visual representation of a Tecton in the game.
 * Each TectonView displays a Tecton_Class and its contents (insects, mushrooms, etc.)
 */
public class TectonView extends GraphicsObject<Tecton_Class> {
    private static final int MARGIN = 5;
    private static final Color TECTON_COLOR = new Color(102, 51, 0);
    private static final Color TECTON_BORDER = new Color(51, 25, 0);
    private static final Color TECTON_DEAD_COLOR = new Color(80, 80, 80);
    private static final Color TECTON_BASE_COLOR = new Color(255, 215, 0);
    private static final Color SLOT_BACKGROUND = new Color(235, 235, 225, 150);
    
    private List<Rectangle> insectSlots = new ArrayList<>();
    private boolean hasThread = false;
    private boolean hasSpore = false;
    private boolean hasMushroom = false;
    
    // To detect and handle mouse clicks
    private static final MouseHandlerForTectons MOUSE_HANDLER = new MouseHandlerForTectons();
    
    public TectonView(Tecton_Class model) {
        super(model);
        setupInsectSlots();
        
        // Register this tecton with the mouse handler
        MOUSE_HANDLER.registerTecton(this);
    }
    
    private void setupInsectSlots() {
        // Create 5 slots for insects (maximum allowed)
        if (insectSlots == null) {
            insectSlots = new ArrayList<>();
        } else {
            insectSlots.clear();
        }
        int slotSize = 20;
        int slotSpacing = 5;
        
        // Position the slots in a more organized layout at the bottom of the tecton
        // This prevents overlapping with other elements and makes them more accessible
        int startX = x + (width - (slotSize * 5 + slotSpacing * 4)) / 2; // Center horizontally
        int startY = y + height - MARGIN - slotSize - 5; // At the bottom with margin
        
        for (int i = 0; i < 5; i++) {
            insectSlots.add(new Rectangle(
                startX + i * (slotSize + slotSpacing),
                startY,
                slotSize,
                slotSize
            ));
        }
    }
    
    public void modelChanged() {
        // Update state from model
        hasThread = model.get_Thread() != null;
        hasSpore = model.get_Spore() != null;
        hasMushroom = model.get_Mushroom() != null;
        
        // Update slot positions based on the model's current position and size
        Point pos = model.getPosition();
        int width = model.getWidth();
        int height = model.getHeight();
        
        // Recalculate insect slots with better positioning
        if (insectSlots == null) {
            insectSlots = new ArrayList<>();
        } else {
            insectSlots.clear();
        }
        
        int slotSize = 20;
        int slotSpacing = 5;
        
        // Position the slots in a more organized layout at the bottom of the tecton
        int totalWidth = slotSize * 5 + slotSpacing * 4;
        int startX = pos.x + (width - totalWidth) / 2; // Center horizontally
        int startY = pos.y + height - MARGIN - slotSize - 5; // At the bottom
        
        for (int i = 0; i < 5; i++) {
            insectSlots.add(new Rectangle(
                startX + i * (slotSize + slotSpacing),
                startY,
                slotSize,
                slotSize
            ));
        }
    }
    
    /**
     * Called when this tecton is clicked
     */
    public void handleClick() {
        System.out.println("Tecton clicked: " + model.get_ID());
        
        // Visual feedback for click
        highlightTemporarily();
        
        // Find the GameBoardPanel to notify about the selection
        try {
            Component parent = GameCanvas.getInstance().getParent();
            while (parent != null) {
                if (parent instanceof GameBoardPanel) {
                    // Found the GameBoardPanel directly
                    GameBoardPanel gameBoardPanel = (GameBoardPanel) parent;
                    gameBoardPanel.setSelectedTecton(this.model);
                    System.out.println("Notified GameBoardPanel of selected tecton: " + model.get_ID());
                    
                    // Force a repaint to ensure selection is visible
                    GameCanvas.getInstance().repaint();
                    break;
                } else if (parent instanceof Container) {
                    // Try to find GameBoardPanel within this container
                    findGameBoardPanelAndNotify((Container) parent);
                }
                parent = parent.getParent();
            }
        } catch (Exception e) {
            System.err.println("Error notifying controller: " + e.getMessage());
        }
    }
    
    /**
     * Recursively searches for GameBoardPanel in container hierarchy
     */
    private void findGameBoardPanelAndNotify(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            if (component instanceof GameBoardPanel) {
                GameBoardPanel gameBoardPanel = (GameBoardPanel) component;
                gameBoardPanel.setSelectedTecton(this.model);
                System.out.println("Notified GameBoardPanel of selected tecton: " + model.get_ID());
                return;
            } else if (component instanceof Container) {
                findGameBoardPanelAndNotify((Container) component);
            }
        }
    }
    
    private void highlightTemporarily() {
        // Create a separate thread for temporary highlight effect
        new Thread(() -> {
            try {
                // Temporarily change color to bright yellow for highlight
                Color highlightColor = new Color(255, 255, 0, 180); // Yellow with alpha
                
                // Set the highlight color
                setTempColor(highlightColor);
                GameCanvas.getInstance().repaint();
                
                // Wait for 400ms (longer duration for better visibility)
                Thread.sleep(400);
                
                // Revert to original color
                setTempColor(null); // null means use default coloring logic
                GameCanvas.getInstance().repaint();
            } catch (InterruptedException ignored) {
                // Restore original appearance if interrupted
                setTempColor(null);
                GameCanvas.getInstance().repaint();
            }
        }).start();
    }
    
    // Temporary color for highlight effects
    private Color tempColor = null;
    
    private void setTempColor(Color color) {
        tempColor = color;
    }
    
    /**
     * Check if a point is within this tecton's bounds
     * Enhanced with a larger hit area to make clicking easier
     */
    public boolean contains(Point p) {
        Point pos = model.getPosition();
        int width = model.getWidth();
        int height = model.getHeight();
        
        // Expand hit area by 10 pixels in all directions
        return p.x >= pos.x - 10 && p.x <= pos.x + width + 10 &&
               p.y >= pos.y - 10 && p.y <= pos.y + height + 10;
    }
    
    @Override
    public void render(Graphics2D g) {
        // Save original transform
        AffineTransform oldTransform = g.getTransform();
        
        // Get position from model
        Point pos = model.getPosition();
        int width = model.getWidth();
        int height = model.getHeight();
        
        // Check if this tecton is currently selected in the GameBoardPanel
        boolean isSelected = false;
        try {
            Component parent = GameCanvas.getInstance().getParent();
            while (parent != null) {
                if (parent instanceof GameBoardPanel) {
                    GameBoardPanel panel = (GameBoardPanel) parent;
                    isSelected = (panel.getSelectedTecton() == model);
                    break;
                }
                parent = parent.getParent();
            }
        } catch (Exception e) {
            // Ignore any errors when checking selection state
        }
        
        // Draw tecton background with more appealing visual appearance
        if (tempColor != null) {
            // Use the highlight color if set
            g.setColor(tempColor);
        } else if (isSelected) {
            // Use a bright selection color that's very visible
            g.setColor(new Color(255, 255, 0, 150)); // Yellow with transparency
        } else if (model instanceof Tecton_Base) {
            g.setColor(TECTON_BASE_COLOR);
        } else if (model.isDead()) {
            g.setColor(TECTON_DEAD_COLOR);
        } else {
            g.setColor(TECTON_COLOR);
        }
        
        // Main tecton shape - make it hexagonal for better visual appearance
        int[] xPoints = {
            pos.x + width/4, 
            pos.x + width*3/4, 
            pos.x + width, 
            pos.x + width*3/4, 
            pos.x + width/4, 
            pos.x
        };
        int[] yPoints = {
            pos.y, 
            pos.y, 
            pos.y + height/2, 
            pos.y + height, 
            pos.y + height, 
            pos.y + height/2
        };
        g.fillPolygon(xPoints, yPoints, 6);
        
        // Draw border
        g.setColor(TECTON_BORDER);
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(xPoints, yPoints, 6);
        
        // Draw ID with improved visibility
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        String id = model.get_ID();
        if (id != null) {
            // Simplify the displayed ID for better readability
            String displayId = id;
            if (id.contains("_")) {
                String[] parts = id.split("_");
                displayId = parts[parts.length - 1];
            }
            
            FontMetrics fm = g.getFontMetrics();
            int textX = pos.x + (width - fm.stringWidth(displayId)) / 2;
            int textY = pos.y + fm.getAscent() + 5; // Position near the top for better visibility
            g.drawString(displayId, textX, textY);
        }
        
        // Draw insect slots with improved layout
        renderInsectSlots(g);
        
        // Draw thread indicator in an improved position
        if (hasThread) {
            g.setColor(Color.WHITE);
            int threadX = pos.x + 10;
            int threadY = pos.y + height - 25;
            g.fillOval(threadX, threadY, 18, 18);
            g.setColor(Color.BLACK);
            g.drawOval(threadX, threadY, 18, 18);
            g.drawString("T", threadX + 6, threadY + 13);
        }
        
        // Draw spore indicator in an improved position
        if (hasSpore) {
            g.setColor(Color.GREEN);
            int sporeX = pos.x + width - 30;
            int sporeY = pos.y + height - 25;
            g.fillOval(sporeX, sporeY, 18, 18);
            g.setColor(Color.BLACK);
            g.drawOval(sporeX, sporeY, 18, 18);
            g.drawString("S", sporeX + 6, sporeY + 13);
        }
        
        // Draw mushroom indicator (a larger visual element) in center
        if (hasMushroom) {
            g.setColor(new Color(255, 100, 100));
            int mushroomX = pos.x + width / 2 - 15;
            int mushroomY = pos.y + height / 2 - 15;
            g.fillOval(mushroomX, mushroomY, 30, 30);
            g.setColor(Color.BLACK);
            g.drawOval(mushroomX, mushroomY, 30, 30);
            g.drawString("M", mushroomX + 10, mushroomY + 20);
        }
        
        // Restore original transform
        g.setTransform(oldTransform);
    }
    
    private void renderInsectSlots(Graphics2D g) {
        List<Insect_Class> insects = model.get_InsectsOnTecton();
        
        for (int i = 0; i < insectSlots.size(); i++) {
            Rectangle slot = insectSlots.get(i);
            
            // Draw slot background
            g.setColor(SLOT_BACKGROUND);
            g.fillRect(slot.x, slot.y, slot.width, slot.height);
            
            // Draw slot border
            g.setColor(Color.DARK_GRAY);
            g.drawRect(slot.x, slot.y, slot.width, slot.height);
            
            // If there's an insect in this position, indicate it
            if (insects != null && i < insects.size()) {
                g.setColor(Color.RED);
                g.fillOval(slot.x + 2, slot.y + 2, slot.width - 4, slot.height - 4);
                
                // Draw insect's owner indicator (top-left corner)
                if (insects.get(i).get_Owner() != null) {
                    Player owner = insects.get(i).get_Owner();
                    int playerID = owner.getId();
                    if (playerID == 0) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.fillRect(slot.x, slot.y, 5, 5);
                }
            }
        }
    }
}
