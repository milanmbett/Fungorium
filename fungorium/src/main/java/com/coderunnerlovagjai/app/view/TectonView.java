package com.coderunnerlovagjai.app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.GraphicsObject;
import com.coderunnerlovagjai.app.Insect_Class;
import com.coderunnerlovagjai.app.Player;
import com.coderunnerlovagjai.app.Tecton_Base;
import com.coderunnerlovagjai.app.Tecton_Class;
import com.coderunnerlovagjai.app.viewmodel.TectonViewModel;

/**
 * Visual representation of a Tecton in the game.
 * Each TectonView displays a Tecton_Class and its contents (insects, mushrooms, etc.)
 * Updated to use MVVM pattern for better separation of concerns.
 */
public class TectonView extends GraphicsObject<Tecton_Class> implements Consumer<TectonViewModel.TectonData> {
    private static final Logger VIEW_LOGGER = LogManager.getLogger(TectonView.class.getName());
    private static final int MARGIN = 5;
    private static final Color TECTON_COLOR = new Color(102, 51, 0);
    private static final Color TECTON_BORDER = new Color(51, 25, 0);
    private static final Color TECTON_DEAD_COLOR = new Color(80, 80, 80);
    private static final Color TECTON_BASE_COLOR = new Color(255, 215, 0);
    private static final Color SLOT_BACKGROUND = new Color(235, 235, 225, 150);
    private static final Color SELECTION_HIGHLIGHT_COLOR = new Color(255, 255, 0, 150);

    private List<Rectangle> insectSlots = new ArrayList<>();
    private boolean modelHasThread = false;
    private boolean modelHasSpore = false;
    private boolean modelHasMushroom = false;
    
    private Color tempColor = null;

    public TectonView(Tecton_Class model) {
        super(model);
        VIEW_LOGGER.debug("TectonView created for model ID: {}", model.get_ID());
    }

    public Tecton_Class getModel() { // Added getModel() method
        return this.model;
    }

    @Override
    public void accept(TectonViewModel.TectonData data) {
        VIEW_LOGGER.trace("ViewModel data received (accept method).");
    }
    
    @Override
    protected void updateFromModel() {
        super.updateFromModel();
        this.modelHasThread = this.model.get_Thread() != null;
        this.modelHasSpore = this.model.get_Spore() != null;
        this.modelHasMushroom = this.model.get_Mushroom() != null;
        setupInsectSlots();
        VIEW_LOGGER.trace("TectonView updated from model ID: {}", this.model.get_ID());
    }

    private void setupInsectSlots() {
        if (insectSlots == null) {
            insectSlots = new ArrayList<>();
        } else {
            insectSlots.clear();
        }
        int slotSize = 20;
        int slotSpacing = 5;
        
        int startX = this.x + (this.width - (slotSize * 5 + slotSpacing * 4)) / 2; 
        int startY = this.y + this.height - MARGIN - slotSize - 5; 
        
        for (int i = 0; i < 5; i++) {
            insectSlots.add(new Rectangle(
                startX + i * (slotSize + slotSpacing),
                startY,
                slotSize,
                slotSize
            ));
        }
    }

    public void handleClick() {
        VIEW_LOGGER.info("TectonView clicked: {}", this.model.get_ID());
        highlightTemporarily();
        
        try {
            Component parent = GameCanvas.getInstance().getParent();
            while (parent != null) {
                if (parent instanceof GameBoardPanel) {
                    ((GameBoardPanel) parent).setSelectedTectonModel(this.model);
                    VIEW_LOGGER.debug("Notified GameBoardPanel of selected tecton: {}", this.model.get_ID());
                    break; 
                }
                parent = parent.getParent();
            }
            if (parent == null) {
                VIEW_LOGGER.warn("Could not find GameBoardPanel ancestor to notify selection for tecton: {}", this.model.get_ID());
            }
        } catch (Exception e) {
            VIEW_LOGGER.error("Error notifying GameBoardPanel of selection: {}", e.getMessage(), e);
        }
    }
    
    private void highlightTemporarily() {
        new Thread(() -> {
            try {
                setTempColor(new Color(255, 255, 100, 180));
                if (GameCanvas.getInstance() != null) GameCanvas.getInstance().repaint();
                Thread.sleep(300);
                setTempColor(null);
                if (GameCanvas.getInstance() != null) GameCanvas.getInstance().repaint();
            } catch (InterruptedException ignored) {
                setTempColor(null);
                if (GameCanvas.getInstance() != null) GameCanvas.getInstance().repaint();
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    private void setTempColor(Color color) {
        this.tempColor = color;
    }
    
    public boolean contains(Point p) {
        return p.x >= this.x - 5 && p.x <= this.x + this.width + 5 &&
               p.y >= this.y - 5 && p.y <= this.y + this.height + 5;
    }

    @Override
    public void render(Graphics2D g) {
        render(g, false); // Default to not selected if called without selection state
    }

    /**
     * Renders the Tecton and its contents.
     * @param g The Graphics2D context to draw on.
     * @param isSelected Whether this tecton is currently selected.
     */
    public void render(Graphics2D g, boolean isSelected) {
        // Save original transform and stroke
        AffineTransform oldTransform = g.getTransform();
        BasicStroke oldStroke = (BasicStroke) g.getStroke();

        // Position the graphics context at the Tecton's location
        g.translate(x, y);

        // Determine Tecton color based on its state
        Color baseColor = model.isDead() ? TECTON_DEAD_COLOR : TECTON_COLOR;
        if (model instanceof Tecton_Base) {
            baseColor = TECTON_BASE_COLOR;
        }
        if (tempColor != null) { // For temporary highlight
            baseColor = tempColor;
        }

        // Draw Tecton body
        g.setColor(baseColor);
        g.fillRect(0, 0, width, height);
        g.setColor(TECTON_BORDER);
        g.setStroke(new BasicStroke(2));
        g.drawRect(0, 0, width, height);

        // Draw selection highlight if selected
        if (isSelected) {
            g.setColor(SELECTION_HIGHLIGHT_COLOR);
            g.fillRect(0, 0, width, height);
        }

        // Draw Tecton ID (optional, for debugging)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        String idText = model.get_ID();
        g.drawString(idText, (width - fm.stringWidth(idText)) / 2, fm.getAscent() + MARGIN);

        // Draw player owner indicator (if applicable)
        Player owner = model.getOwner();
        if (owner != null) {
            g.setColor(owner.getColor()); // Assuming Player has a getColor() method
            g.fillRect(width - MARGIN - 10, MARGIN, 10, 10); // Small square indicator
            g.setColor(Color.BLACK);
            g.drawRect(width - MARGIN - 10, MARGIN, 10, 10);
        }

        // Draw status icons (thread, spore, mushroom)
        int iconY = MARGIN + fm.getHeight() + 5;
        if (modelHasThread) {
            drawIcon(g, "T", Color.CYAN, MARGIN, iconY);
        }
        if (modelHasSpore) {
            drawIcon(g, "S", Color.MAGENTA, MARGIN + 15, iconY);
        }
        if (modelHasMushroom) {
            drawIcon(g, "M", Color.GREEN, MARGIN + 30, iconY);
        }

        // Draw insect slots and insects
        drawInsectSlots(g);

        // Restore original transform and stroke
        g.setTransform(oldTransform);
        g.setStroke(oldStroke);
    }

    private void drawIcon(Graphics2D g, String text, Color color, int iconX, int iconY) {
        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(text, iconX, iconY + g.getFontMetrics().getAscent());
    }

    private void drawInsectSlots(Graphics2D g) {
        if (insectSlots == null) return;

        for (int i = 0; i < insectSlots.size(); i++) {
            Rectangle slot = insectSlots.get(i);
            g.setColor(SLOT_BACKGROUND);
            g.fillRect(slot.x - x, slot.y - y, slot.width, slot.height); // Adjust for translated context
            g.setColor(Color.DARK_GRAY);
            g.drawRect(slot.x - x, slot.y - y, slot.width, slot.height);

            // Draw insect if present in this slot
            List<Insect_Class> insects = model.get_InsectsOnTecton();
            if (i < insects.size() && insects.get(i) != null) {
                Insect_Class insect = insects.get(i);
                // Simple representation: a colored circle or letter
                g.setColor(insect.getOwner().getColor()); // Assuming Insect_Class has getOwner() and Player has getColor()
                g.fillOval(slot.x - x + 2, slot.y - y + 2, slot.width - 4, slot.height - 4);
                g.setColor(Color.BLACK);
                g.drawString(insect.get_ID().substring(0, 1), slot.x - x + 7, slot.y - y + 15);
            }
        }
    }

    private static final int player1Id = 1;
    private static final int player2Id = 2;
}
