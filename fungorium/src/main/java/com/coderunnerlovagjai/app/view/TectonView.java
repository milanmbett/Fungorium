package com.coderunnerlovagjai.app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Image;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coderunnerlovagjai.app.Basic_Spore;
import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.GraphicsObject;
import com.coderunnerlovagjai.app.Insect_Class;
import com.coderunnerlovagjai.app.Player;
import com.coderunnerlovagjai.app.Tecton_Base;
import com.coderunnerlovagjai.app.Tecton_Class;
import com.coderunnerlovagjai.app.Tecton_Dead;
import com.coderunnerlovagjai.app.Mushroom_Shroomlet;
import com.coderunnerlovagjai.app.Mushroom_Class;
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

    private static final int STATUS_ICON_SIZE = 16;
    private List<Rectangle> insectSlots = new ArrayList<>();
    private boolean modelHasThread = false;
    private boolean modelHasSpore = false;
    private boolean modelHasMushroom = false;
    private boolean modelHasInsect = false;

    
    private Color tempColor = null;
    
    //Gombák ikonja
    private Image mushroomShroomletIcon;
    private Image mushroomGrandIcon;
    private Image mushroomMaximusIcon;
    private Image musrhoomSlenderIcon;

    //Rovarok ikonja
    private Image insectShroomreaperIcon;
    private Image insectBuggernautIcon;
    private Image insectBugletIcon;
    private Image insectStingerIcon;
    private Image insectTektonizatorIcon;
    //Spórák ikonja
    private Image sporeBasicIcon;
    private Image sporeSlowingIcon;
    private Image sporeParalysingIcon;
    private Image sporeDuplicatorIcon;
    private Image sporeSpeedIcon;
    //Fonál ikonja
    private Image threadIcon;

    public TectonView(Tecton_Class model) {
        super(model);
        VIEW_LOGGER.debug("TectonView created for model ID: {}", model.get_ID());

        try { //TODO: ez igy ok nem?
            if (model instanceof Tecton_Base) {
                this.img = ImageIO.read(getClass().getClassLoader().getResource("images/Tecton_BaseWithSpace.png"));
            } else if (model instanceof Tecton_Dead) {
                this.img = ImageIO.read(getClass().getClassLoader().getResource("images/Tecton_Dead.png"));
            } else {
                this.img = ImageIO.read(getClass().getClassLoader().getResource("images/Tecton_Basic.png"));
            }
        } catch (IOException | IllegalArgumentException e) {
            this.img = null;
        }
        // load Icons
        try 
        {
            mushroomShroomletIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Mushroom_Shroomlet.png"));
        } catch (IOException | IllegalArgumentException e) 
        {
            VIEW_LOGGER.warn("Failed to load Shroomlet icon", e);
            mushroomShroomletIcon = null;
        }
        try {
            mushroomGrandIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Mushroom_Grand.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Grand mushroom icon", e);
            mushroomGrandIcon = null;
        }
        try {
            mushroomMaximusIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Mushroom_Maximus.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Maximus mushroom icon", e);
            mushroomMaximusIcon = null;
        }
        try {
            musrhoomSlenderIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Mushroom_Slender.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Slender mushroom icon", e);
            musrhoomSlenderIcon = null;
        }
        try {
            insectShroomreaperIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Insect_Shroomreaper.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Shroomreaper insect icon", e);
            insectShroomreaperIcon = null;
        }
        try {
            insectBuggernautIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Insect_Buggernaut.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Buggernaut insect icon", e);
            insectBuggernautIcon = null;
        }
        try {
            insectBugletIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Insect_Buglet.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Buglet insect icon", e);
            insectBugletIcon = null;
        }
        try {
            insectStingerIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Insect_Stinger.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Stinger insect icon", e);
            insectStingerIcon = null;
        }
        try {
            insectTektonizatorIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Insect_Tektonizator.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Tektonizator insect icon", e);
            insectTektonizatorIcon = null;
        }
        try {
            sporeBasicIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Spore_Basic.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Basic spore icon", e);
            sporeBasicIcon = null;
        }
        try {
            sporeSlowingIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Spore_Slowing.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Slowing spore icon", e);
            sporeSlowingIcon = null;
        }
        try {
            sporeParalysingIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Spore_Paralysing.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Paralysing spore icon", e);
            sporeParalysingIcon = null;
        }
        try {
            sporeDuplicatorIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Spore_Duplicator.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Duplicator spore icon", e);
            sporeDuplicatorIcon = null;
        }
        try {
            sporeSpeedIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Spore_Speed.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Speed spore icon", e);
            sporeSpeedIcon = null;
        }
        try {
            threadIcon = ImageIO.read(getClass().getClassLoader()
                .getResource("images/Thread.png"));
        } catch (IOException | IllegalArgumentException e) {
            VIEW_LOGGER.warn("Failed to load Thread icon", e);
            threadIcon = null;
        }

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
        this.modelHasInsect = this.model.get_InsectsOnTecton() != null && !this.model.get_InsectsOnTecton().isEmpty();
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

        // ...existing code for image or fallback color...

        if (img != null) {
            g.drawImage(img, 0, 0, width, height, null);
        } else {
            Color baseColor = model.isDead() ? TECTON_DEAD_COLOR : TECTON_COLOR;
            if (model instanceof Tecton_Base) {
                baseColor = TECTON_BASE_COLOR;
            }
            if (tempColor != null) {
                baseColor = tempColor;
            }
            g.setColor(baseColor);
            g.fillRect(0, 0, width, height);
        }

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
            if (threadIcon != null) {
                g.drawImage(threadIcon,
                            MARGIN,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            } else {
                drawIcon(g, "T", Color.BLUE, MARGIN, iconY);
            }
        }
        if (modelHasSpore) {
            Basic_Spore spore = model.get_Spore();
            if (sporeBasicIcon != null && spore instanceof Basic_Spore) 
            {
                g.drawImage(sporeBasicIcon,
                            MARGIN + 15,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            }
            else if (sporeSlowingIcon != null && spore instanceof Basic_Spore) 
            {
                g.drawImage(sporeSlowingIcon,
                            MARGIN + 15,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            }
            else if (sporeParalysingIcon != null && spore instanceof Basic_Spore) 
            {
                g.drawImage(sporeParalysingIcon,
                            MARGIN + 15,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            }
            else if (sporeDuplicatorIcon != null && spore instanceof Basic_Spore) 
            {
                g.drawImage(sporeDuplicatorIcon,
                            MARGIN + 15,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            }
            else if (sporeSpeedIcon != null && spore instanceof Basic_Spore) 
            {
                g.drawImage(sporeSpeedIcon,
                            MARGIN + 15,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            } 
            else 
            {
                drawIcon(g, "S", Color.YELLOW, MARGIN + 15, iconY);
            }
        }
        if (modelHasMushroom) {
            // show specific mushroom icon if Shroomlet
            Mushroom_Class mush = model.get_Mushroom();
            if (mush instanceof Mushroom_Shroomlet && mushroomShroomletIcon != null) 
            {
                g.drawImage(mushroomShroomletIcon,
                            MARGIN + 30,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            }
            else if (mushroomGrandIcon != null && mush instanceof Mushroom_Class) 
            {
                g.drawImage(mushroomGrandIcon,
                            MARGIN + 30,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            }
            else if (mushroomMaximusIcon != null && mush instanceof Mushroom_Class) 
            {
                g.drawImage(mushroomMaximusIcon,
                            MARGIN + 30,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            }
            else if (musrhoomSlenderIcon != null && mush instanceof Mushroom_Class) 
            {
                g.drawImage(musrhoomSlenderIcon,
                            MARGIN + 30,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
            } 
            else 
            {
                drawIcon(g, "M", Color.GREEN, MARGIN + 30, iconY);
            }
        }
        if(modelHasInsect)
        {
            Insect_Class insect = model.get_InsectsOnTecton().get(0); // Assuming first insect for demo
            if (insect != null) {
                if (insectShroomreaperIcon != null && insect instanceof Insect_Class) 
                {
                    g.drawImage(insectShroomreaperIcon,
                                MARGIN + 45,
                                iconY,
                                STATUS_ICON_SIZE,
                                STATUS_ICON_SIZE,
                                null);
                }
                else if (insectBuggernautIcon != null && insect instanceof Insect_Class) 
                {
                    g.drawImage(insectBuggernautIcon,
                                MARGIN + 45,
                                iconY,
                                STATUS_ICON_SIZE,
                                STATUS_ICON_SIZE,
                                null);
                }
                else if (insectBugletIcon != null && insect instanceof Insect_Class) 
                {
                    g.drawImage(insectBugletIcon,
                                MARGIN + 45,
                                iconY,
                                STATUS_ICON_SIZE,
                                STATUS_ICON_SIZE,
                                null);
                }
                else if (insectStingerIcon != null && insect instanceof Insect_Class) 
                {
                    g.drawImage(insectStingerIcon,
                                MARGIN + 45,
                                iconY,
                                STATUS_ICON_SIZE,
                                STATUS_ICON_SIZE,
                                null);
                }
                else if (insectTektonizatorIcon != null && insect instanceof Insect_Class) 
                {
                    g.drawImage(insectTektonizatorIcon,
                            MARGIN + 45,
                            iconY,
                            STATUS_ICON_SIZE,
                            STATUS_ICON_SIZE,
                            null);
                } 
            }
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
