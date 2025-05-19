package com.coderunnerlovagjai.app.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException; // Import IOException
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import java.awt.Rectangle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coderunnerlovagjai.app.GraphicsObject;
import com.coderunnerlovagjai.app.Tecton_Base;
import com.coderunnerlovagjai.app.Tecton_Class;

public class TectonGraphics extends GraphicsObject<Tecton_Class> {

    private static final Logger LOGGER = LogManager.getLogger(TectonGraphics.class);
    private static final Map<String, BufferedImage> mushroomImages = new HashMap<>();
    private static final Map<String, BufferedImage> insectImages = new HashMap<>();
    private static final Map<String, BufferedImage> sporeImages = new HashMap<>();
    private static BufferedImage threadImage;
    private static BufferedImage tectonBasicImage;
    private static BufferedImage tectonBaseWithSpaceImage;
    private static BufferedImage tectonBaseImage; // Base image
    private static BufferedImage leftBaseImage;   // Rotated left base
    private static BufferedImage rightBaseImage;  // Rotated right base

    static {
        String[] types = { "Mushroom_Grand", "Mushroom_Maximus", "Mushroom_Shroomlet", "Mushroom_Slender" };
        for (String type : types) {
            try {
                String imagePath = "images/" + type + ".png";
                BufferedImage img = ImageIO.read(
                    TectonGraphics.class.getClassLoader().getResourceAsStream(imagePath)
                );
                if (img != null) {
                    mushroomImages.put(type, img);
                } else {
                    LOGGER.warn("Failed to load mushroom image: {}. Resource stream was null.", imagePath);
                }
            } catch (IOException e) {
                LOGGER.error("IOException while loading mushroom image: images/{}.png", type, e);
            } catch (IllegalArgumentException e) {
                LOGGER.error("IllegalArgumentException (likely null resource) for mushroom image: images/{}.png", type, e);
            }
        }
        String[] sporeTypes = { "Basic_Spore", "Spore_Duplicator", "Spore_Paralysing", "Spore_Slowing", "Spore_Speed" };
            for (String type : sporeTypes) {
                String imagePath = "images/" + type + ".png";
                try {
                    BufferedImage img = ImageIO.read(
                    TectonGraphics.class.getClassLoader().getResourceAsStream(imagePath)
                    );
                    if (img != null) {
                        sporeImages.put(type, img);
                    } else {
                        LOGGER.warn("Failed to load spore image: {}. Resource stream was null.", imagePath);
                    }
                    } catch (IOException|IllegalArgumentException e) {
                        LOGGER.error("Error loading spore image: images/{}.png", type, e);
                    }
        }
        String[] insectTypes = { "Insect_Buggernaut", "Insect_Buglet","Insect_ShroomReaper", "Insect_Stinger", "Insect_Tektonizator"};
        for (String type : insectTypes) {
            String path = "images/" + type + ".png";
            try {
                BufferedImage img = ImageIO.read(
                    TectonGraphics.class.getClassLoader().getResourceAsStream(path)
                );
                if (img != null) {
                    insectImages.put(type, img);
                } else {
                    LOGGER.warn("Failed to load insect image: {} (null stream)", path);
                }
            } catch (IOException|IllegalArgumentException e) {
                LOGGER.error("Error loading insect image: {}", path, e);
            }
        }
        
        try {
            tectonBasicImage = loadImageResource("images/Tecton_Basic.png");
            tectonBaseWithSpaceImage = loadImageResource("images/Tecton_BaseWithSpace.png");
            tectonBaseImage = loadImageResource("images/Tecton_Base.png");
            threadImage = loadImageResource("images/Thread_Class.png");
            
            if (tectonBaseImage != null) {
                // Left base should appear rotated to its right on the screen
                leftBaseImage = rotateImage(tectonBaseImage, 30); 
                // Right base should appear rotated to its left on the screen
                rightBaseImage = rotateImage(tectonBaseImage, -30);
            } else {
                LOGGER.error("tectonBaseImage is null, cannot create rotated versions.");
            }
        } catch (Exception e) { 
            LOGGER.error("Error initializing tecton images in static block.", e);
        }
    }

    private static BufferedImage loadImageResource(String path) {
        try {
            BufferedImage img = ImageIO.read(TectonGraphics.class.getClassLoader().getResourceAsStream(path));
            if (img == null) {
                LOGGER.error("Failed to load image: {}. Resource stream was null.", path);
            }
            return img;
        } catch (IOException e) {
            LOGGER.error("IOException while loading image: {}", path, e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.error("IllegalArgumentException (likely null resource) for image: {}", path, e);
            return null;
        }
    }
    
    private static BufferedImage rotateImage(BufferedImage image, double angle) {
        if (image == null) {
            LOGGER.warn("Attempted to rotate a null image. Angle: {}", angle);
            return null;
        }
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        
        int w = image.getWidth();
        int h = image.getHeight();
        
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);
        
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        
        g2d.translate((newWidth - w) / 2.0, (newHeight - h) / 2.0);
        g2d.rotate(rads, w / 2.0, h / 2.0); 
        g2d.drawRenderedImage(image, null);
        g2d.dispose();
        
        return rotated;
    }

    private final Polygon hexShape;
    private final int[] xs;
    private final int[] ys; 

    public TectonGraphics(Tecton_Class model) {
        super(model);
        xs = new int[6]; 
        ys = new int[6];
        // Hex radius increased to match the one in Plane.java (55)
        for (int i = 0; i < 6; i++) {
            double angleRad = Math.toRadians(60.0 * i);  // Start from 0 degrees for flat-topped hex
            xs[i] = (int) (55 * Math.cos(angleRad)); // Using 55 pixel radius for hexagons
            ys[i] = (int) (55 * Math.sin(angleRad)); // Using 55 pixel radius for hexagons
        }
        hexShape = new Polygon(xs, ys, 6);
    }

    @Override 
    public void render(Graphics2D g) {
        g.translate(x, y); 

        if (model instanceof Tecton_Base baseTectonModel) {
            renderBaseTecton(g, baseTectonModel);
        } else {
            renderRegularTecton(g);
            //DEBUG
            //drawDebugId(g);
        }

        drawEntitiesOnTecton(g);
        
        g.translate(-x, -y); // Correct translation to revert the initial g.translate(x, y)
    }

    private void renderBaseTecton(Graphics2D g, Tecton_Base baseTectonModel) {
        int imgSize = 160; // Larger size for base tectons
        BufferedImage imageToDraw = null;

        if (baseTectonModel.getPosition().x < 400) { 
            imageToDraw = leftBaseImage;
            if (imageToDraw == null) LOGGER.warn("leftBaseImage is null for Tecton_Base ID: {}", baseTectonModel.get_ID());
        } else { 
            imageToDraw = rightBaseImage;
            if (imageToDraw == null) LOGGER.warn("rightBaseImage is null for Tecton_Base ID: {}", baseTectonModel.get_ID());
        }
        
        if (imageToDraw != null) {
            g.drawImage(imageToDraw, -imgSize/2, -imgSize/2, imgSize, imgSize, null);
        } else { 
            LOGGER.warn("Fallback rendering for Base Tecton ID: {}. Rotated image was null.", baseTectonModel.get_ID());
            g.setColor(new Color(190, 150, 80)); 
            g.fillOval(-imgSize/2, -imgSize/2, imgSize, imgSize); 
            g.setColor(new Color(220, 180, 100)); 
            g.fillOval(-imgSize/4, -imgSize/4, imgSize/2, imgSize/2);
        }
    }

    private void renderRegularTecton(Graphics2D g) {
        int imgSize = 120;
        // always draw the same base image
        BufferedImage imageToDraw = tectonBasicImage;
        if (imageToDraw != null) {
            g.drawImage(imageToDraw, -imgSize/2, -imgSize/2, imgSize, imgSize, null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillOval(-imgSize/2, -imgSize/2, imgSize, imgSize);
        }
    }

    private void drawEntitiesOnTecton(Graphics2D g) {
        // 1) Draw Thread (bigger than mushroom)
        if (model.get_Thread() != null) {
            int threadSize = 80;  // slightly larger
            if (threadImage != null) {
                g.drawImage(threadImage, -threadSize/2, -threadSize/2, threadSize, threadSize, null);
            } else {
                LOGGER.warn("Thread image not found. Using fallback.");
                g.setColor(Color.BLUE);
                g.fillOval(-threadSize/2, -threadSize/2, threadSize, threadSize);
            }
        }

        // 2) Draw Mushroom (middle layer)
        if (model.get_Mushroom() != null) {
            String mushroomType = model.get_Mushroom().getClass().getSimpleName();
            BufferedImage mushroomImg = mushroomImages.get(mushroomType);
            int mushroomSize = 50;
            if (mushroomImg != null) {
                g.drawImage(mushroomImg, -mushroomSize/2, -mushroomSize/2, mushroomSize, mushroomSize, null);
            } else {
                LOGGER.warn("Mushroom image not found for type: {}. Using fallback.", mushroomType);
                g.setColor(Color.GREEN);
                g.fillOval(-mushroomSize/2, -mushroomSize/2, mushroomSize, mushroomSize);
            }
        }

        // 3) Draw Spore (top layer, offset slightly to the right)
        if (model.get_Spore() != null) {
            String sporeType = model.get_Spore().getClass().getSimpleName();
            BufferedImage sporeImg = sporeImages.get(sporeType);
            int sporeSize = 40;
            int xOffset = (sporeSize / 2) + 30;  // move right
            if (sporeImg != null) {
                g.drawImage(sporeImg,
                            -sporeSize/2 + xOffset,
                            -sporeSize/2,
                            sporeSize,
                            sporeSize,
                            null);
            } else {
                LOGGER.warn("Spore image not found for type: {}. Using fallback.", sporeType);
                g.setColor(Color.MAGENTA);
                g.fillOval(-sporeSize/2 + xOffset, -sporeSize/2, sporeSize, sporeSize);
            }
        }

        // 4) Draw insect slots
        int slotCount = 5;
        int slotSize  = 20;
        int gap       = 10;
        int totalW    = slotCount * slotSize + (slotCount - 1) * gap;
        int startX    = -totalW / 2;
        int slotY     = 60; // just below the hex

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < slotCount; i++) {
            int x = startX + i * (slotSize + gap);
            g.drawRect(x, slotY, slotSize, slotSize);
        }

        // 5) Draw each insect into its slot
        if (model.get_InsectsOnTecton() != null) {
            int idx = 0;
            for (var insect : model.get_InsectsOnTecton()) {
                if (idx >= slotCount) break;
                String type = insect.getClass().getSimpleName();
                BufferedImage img = insectImages.get(type);
                int x = startX + idx * (slotSize + gap);

                if (img != null) {
                    g.drawImage(img, x, slotY, slotSize, slotSize, null);
                } else {
                    // fallback if image missing
                    g.setColor(Color.RED);
                    g.fillRect(x, slotY, slotSize, slotSize);
                }
                idx++;
            }
        }

        // …remove or comment out the old insect‐count drawString…
    }
        private void drawDebugId(Graphics2D g) {
        String id = String.valueOf(model.get_ID());
        // save original settings
        var origColor = g.getColor();
        var origFont  = g.getFont();

        // configure debug font & color
        g.setFont(origFont.deriveFont(java.awt.Font.BOLD, 14f));
        g.setColor(Color.YELLOW);

        // center text above hex (radius ≈55)
        var fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(id);
        int xPos = -textWidth / 2;
        int yPos = -60;  

        g.drawString(id, xPos, yPos);

        // restore
        g.setFont(origFont);
        g.setColor(origColor);
    }
}
