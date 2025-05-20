package com.coderunnerlovagjai.app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coderunnerlovagjai.app.GraphicsObject;
import com.coderunnerlovagjai.app.Mushroom_Class;
import com.coderunnerlovagjai.app.Player;
import com.coderunnerlovagjai.app.Tecton_Base;
import com.coderunnerlovagjai.app.Tecton_Class;
import com.coderunnerlovagjai.app.Tecton_Dead;

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
    private static BufferedImage tectonDeadImage1; // Image for Tecton_Dead variant 1
    private static BufferedImage tectonDeadImage2; // Image for Tecton_Dead variant 2

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
            tectonDeadImage1 = loadImageResource("images/Tecton_Dead_1.png"); // Load Tecton_Dead variant 1 image
            tectonDeadImage2 = loadImageResource("images/Tecton_Dead_2.png"); // Load Tecton_Dead variant 2 image
            
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
        BufferedImage imageToDraw;
        int drawX; // Represents the top-left X for drawing, relative to the model's translated origin (center)
        int drawY; // Represents the top-left Y for drawing, relative to the model's translated origin (center)

        if (model.isDead()) {
            int targetVisualWidth = 60;    // Dead tecton halves are visually 60px wide
            int targetVisualHeight = 120;  // Dead tecton halves use original image height (120px)
            
            drawY = -targetVisualHeight / 2; // e.g., -60 for a 120px tall image (top-left Y relative to model center)

            if (model instanceof Tecton_Dead deadModel) {
                int variant = deadModel.getDeadVariant();
                imageToDraw = null; 

                switch (variant) {
                    case 1:
                        imageToDraw = tectonDeadImage1;
                        drawX = -47; // (Original.X - 60) - (Original.X - 15)
                        break;
                    case 2:
                        imageToDraw = tectonDeadImage2;
                        drawX = -12; // (Original.X + 0) - (Original.X + 15)
                        break;
                    default:
                        LOGGER.warn("Unknown Tecton_Dead variant: {} for Tecton ID: {}. Defaulting to image 1, 60x120, centered on its model.", variant, model.get_ID());
                        imageToDraw = tectonDeadImage1; 
                        drawX = -targetVisualWidth / 2;    
                        break;
                }

                if (imageToDraw == null) {
                    LOGGER.warn("Tecton_Dead image is null (variant {}, Tecton ID: {}). Falling back to gray 60x120 oval.", 
                                (variant >= 1 && variant <= 2 ? String.valueOf(variant) : "unknown/defaulted"), model.get_ID());
                    g.setColor(Color.GRAY);
                    g.fillOval(drawX, drawY, targetVisualWidth, targetVisualHeight);
                } else {
                    // Draw the selected image, scaled to targetVisualWidth x targetVisualHeight
                    
                    g.drawImage(imageToDraw,
                                drawX, drawY,                               // Destination top-left X, Y
                                drawX + targetVisualWidth, drawY + targetVisualHeight, // Destination bottom-right X, Y
                                0, 0,                                     // Source top-left X, Y (of the original image)
                                imageToDraw.getWidth(), imageToDraw.getHeight(), // Source bottom-right X, Y (full original image dimensions)
                                null);
                }
            } else { // Model isDead but not an instance of Tecton_Dead
                LOGGER.warn("Model isDead but not Tecton_Dead instance for Tecton ID: {}. Defaulting to gray 60x120 oval centered on its model.", model.get_ID());
                g.setColor(Color.GRAY);
                drawX = -targetVisualWidth / 2; 
                g.fillOval(drawX, drawY, targetVisualWidth, targetVisualHeight); 
            }
        } else { // For regular, non-dead (live) tectons
            int liveTectonVisualSize = 120; 
            imageToDraw = tectonBasicImage; 
            drawX = -liveTectonVisualSize / 2; 
            drawY = -liveTectonVisualSize / 2; 

            if (imageToDraw == null) {
                LOGGER.warn("Tecton_Basic.png not loaded for Tecton ID: {}. Falling back to dark gray 120x120 oval.", model.get_ID());
                g.setColor(Color.DARK_GRAY); 
                g.fillOval(drawX, drawY, liveTectonVisualSize, liveTectonVisualSize); 
            } else {
                 g.drawImage(imageToDraw, drawX, drawY, liveTectonVisualSize, liveTectonVisualSize, null);
            }
        }
    } // THIS IS THE CORRECTED END OF renderRegularTecton

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
            Mushroom_Class m = model.get_Mushroom();
            BufferedImage img = mushroomImages.get(m.getClass().getSimpleName());
            int size = 50;
            if (img != null) {
                g.drawImage(img, -size/2, -size/2, size, size, null);
            } else {
                // fallback
                g.setColor(Color.GREEN);
                g.fillOval(-size/2, -size/2, size, size);
            }

            // outline by owner
            Player owner = m.get_Owner();
            Color outline = owner.getId() == 1 ? Color.BLUE : Color.RED;
            Stroke oldStroke = g.getStroke();
            g.setColor(outline);
            g.setStroke(new BasicStroke(3f));
            g.drawOval(-size/2, -size/2, size, size);
            g.setStroke(oldStroke);

            // draw mushroom HP (white text)
            {
                int hp = m.get_hp();
                String txt = String.valueOf(hp);
                Color orig = g.getColor();
                g.setColor(Color.WHITE);
                var fm = g.getFontMetrics();
                int w = fm.stringWidth(txt);
                int y0 = fm.getAscent()/2;
                g.drawString(txt, -w/2, y0);
                g.setColor(orig);
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

        // 4) Draw insect slots in a circle around the center
        int slotCount = 5;
        int slotSize  = 20;
        int radius    = 30; // distance from center
        double angleStep = 2 * Math.PI / slotCount;

        for (int i = 0; i < slotCount; i++) {
            double angle = Math.PI / 2 + i * angleStep; // start at top
            int cx = (int) (radius * Math.cos(angle));
            int cy = (int) (radius * Math.sin(angle));
            //g.setColor(Color.LIGHT_GRAY);
            g.setColor(new Color(0,0,0,0));
            g.drawRect(cx - slotSize/2, cy - slotSize/2, slotSize, slotSize);
        }

        // 5) Draw each insect into its slot (same positions)
        if (model.get_InsectsOnTecton() != null) {
            int idx = 0;
            for (var insect : model.get_InsectsOnTecton()) {
                if (idx >= slotCount) break;
                double angle = Math.PI / 2 + idx * angleStep;
                int cx = (int) (radius * Math.cos(angle));
                int cy = (int) (radius * Math.sin(angle));
                String type = insect.getClass().getSimpleName();
                BufferedImage img = insectImages.get(type);

                if (img != null) {
                    g.drawImage(img, cx - slotSize/2, cy - slotSize/2, slotSize, slotSize, null);
                } else {
                    g.setColor(Color.RED);
                    g.fillRect(cx - slotSize/2, cy - slotSize/2, slotSize, slotSize);
                }
                // outline by owner
                Player owner = insect.get_Owner();
                Color outline;
                if (owner != null) {
                    outline = owner.getId() == 1 ? Color.BLUE : Color.RED;
                } else {
                    outline = Color.GRAY; 
                    LOGGER.warn("Insect owner is null for insect: {}", insect.get_ID());
                }
                Stroke old = g.getStroke();
                g.setColor(outline);
                g.setStroke(new BasicStroke(2f));
                g.drawRect(cx - slotSize/2, cy - slotSize/2, slotSize, slotSize);
                g.setStroke(old);
                // draw insect HP
                {
                    int hp = insect.get_hp();
                    String txt = String.valueOf(hp);
                    var origColor = g.getColor();
                    g.setColor(Color.WHITE);
                    var fm = g.getFontMetrics();
                    int w = fm.stringWidth(txt);
                    int x0 = cx - w/2;
                    int y0 = cy + slotSize/2 - 2;
                    g.drawString(txt, x0, y0);
                    g.setColor(origColor);
                }
                idx++;
            }
        }
    }
}
