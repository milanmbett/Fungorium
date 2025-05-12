package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * Main game board view that handles rendering of the game map (tectons) and interaction.
 * This is a specialized canvas that manages the layout and user interaction with game elements.
 */
public class GameBoardView extends JPanel implements MouseListener, MouseMotionListener {
    private static final Color BG_COLOR = new Color(30, 30, 40);
    private static final int TECTON_WIDTH = 100;
    private static final int TECTON_HEIGHT = 80;
    private static final int GRID_SPACING_X = 110;
    private static final int GRID_SPACING_Y = 90;
    
    private final Plane gameModel;
    private final Map<Tecton_Class, Point> tectonPositions = new HashMap<>();
    private Tecton_Class selectedTecton = null;
    private Tecton_Class hoverTecton = null;
    
    public GameBoardView(Plane model) {
        this.gameModel = model;
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(800, 600));
        
        // Register for mouse events
        addMouseListener(this);
        addMouseMotionListener(this);
        
        // Map tectons to grid layout positions
        setupTectonLayout();
        
        // Create views for existing entities
        initializeEntityViews();
    }
    
    private void setupTectonLayout() {
        // First create a layout grid for all tectons
        // This is a simplified positioning system - you might want to use actual tecton
        // neighbor information for a more game-appropriate layout
        
        // Get all tectons from the model
        List<Tecton_Class> tectons = gameModel.TectonCollection;
        if (tectons.isEmpty()) return;
        
        // Create a simple grid layout based on ID numbers
        int maxCols = 4; // Adjust based on desired grid width
        
        for (int i = 0; i < tectons.size(); i++) {
            Tecton_Class tecton = tectons.get(i);
            
            // Calculate grid position
            int row = i / maxCols;
            int col = i % maxCols;
            
            // Special positioning for base tectons
            if (tecton instanceof Tecton_Base) {
                if (tecton == gameModel.getBase1()) {
                    // Position Base1 at top
                    tectonPositions.put(tecton, new Point(GRID_SPACING_X * 2, 40));
                } else if (tecton == gameModel.getBase2()) {
                    // Position Base2 at bottom
                    tectonPositions.put(tecton, new Point(GRID_SPACING_X * 2, 500));
                }
            } else {
                // Regular grid positioning with center offset
                int centerOffsetX = (getWidth() - (maxCols * GRID_SPACING_X)) / 2 + 50;
                int centerOffsetY = 120; // Top margin
                
                tectonPositions.put(tecton, new Point(
                    centerOffsetX + col * GRID_SPACING_X, 
                    centerOffsetY + row * GRID_SPACING_Y
                ));
            }
            
            // Update entity position
            tecton.setPosition(
                tectonPositions.get(tecton).x,
                tectonPositions.get(tecton).y
            );
            tecton.setSize(TECTON_WIDTH, TECTON_HEIGHT);
        }
    }
    
    private void initializeEntityViews() {
        // Create TectonViews for all tectons
        for (Tecton_Class tecton : gameModel.TectonCollection) {
            new TectonView(tecton); // View adds itself to GameCanvas
        }
        
        // Create ThreadViews for all threads
        for (Thread_Class thread : gameModel.ThreadCollection) {
            new ThreadView(thread);
        }
        
        // Create MushroomViews for all mushrooms
        for (Mushroom_Class mushroom : gameModel.MushroomCollection) {
            new MushroomView(mushroom);
        }
        
        // Create InsectViews for all insects
        for (Insect_Class insect : gameModel.InsectCollection) {
            new InsectView(insect);
        }
        
        // Create SporeViews for all spores
        for (Basic_Spore spore : gameModel.SporeCollection) {
            new SporeView(spore);
        }
    }
    
    // Find the tecton at a specific screen position
    private Tecton_Class getTectonAtPosition(Point p) {
        for (Map.Entry<Tecton_Class, Point> entry : tectonPositions.entrySet()) {
            Tecton_Class tecton = entry.getKey();
            Point pos = entry.getValue();
            
            // Check if point is within the tecton's bounds
            if (p.x >= pos.x && p.x < pos.x + TECTON_WIDTH &&
                p.y >= pos.y && p.y < pos.y + TECTON_HEIGHT) {
                return tecton;
            }
        }
        return null;
    }
    
    // MouseListener interface methods
    @Override
    public void mouseClicked(MouseEvent e) {
        Tecton_Class clickedTecton = getTectonAtPosition(e.getPoint());
        
        if (clickedTecton != null) {
            // If we already had a selected tecton, handle possible actions
            if (selectedTecton != null) {
                // Check if this is a valid action based on game rules
                // (like moving an insect from selectedTecton to clickedTecton)
                
                // TODO: Pass this to controller/game logic
                System.out.println("Action from " + selectedTecton.get_ID() + 
                                  " to " + clickedTecton.get_ID());
                
                // Reset selection after action
                selectedTecton = null;
            } else {
                // Select the tecton
                selectedTecton = clickedTecton;
                System.out.println("Selected " + selectedTecton.get_ID());
            }
            repaint();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // Implementation if needed
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        // Implementation if needed
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // Implementation if needed
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
        hoverTecton = null;
        repaint();
    }
    
    // MouseMotionListener interface methods
    @Override
    public void mouseDragged(MouseEvent e) {
        // Implementation if needed
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        Tecton_Class tectonUnderMouse = getTectonAtPosition(e.getPoint());
        
        if (tectonUnderMouse != hoverTecton) {
            hoverTecton = tectonUnderMouse;
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        
        // Enable antialiasing for smoother rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw grid connections between tectons
        drawTectonConnections(g2d);
        
        // Draw selection/hover highlights
        if (selectedTecton != null) {
            drawSelectionHighlight(g2d, selectedTecton, new Color(255, 255, 0, 100));
        }
        
        if (hoverTecton != null) {
            drawSelectionHighlight(g2d, hoverTecton, new Color(255, 255, 255, 80));
        }
        
        // The actual entities will be rendered by the GameCanvas system with GraphicsObjects
    }
    
    private void drawTectonConnections(Graphics2D g) {
        g.setColor(new Color(180, 180, 180, 120));
        g.setStroke(new BasicStroke(2.0f));
        
        // Draw lines between neighboring tectons
        for (Tecton_Class tecton : gameModel.TectonCollection) {
            Point p1 = tectonPositions.get(tecton);
            if (p1 == null) continue;
            
            // Center point of first tecton
            int x1 = p1.x + TECTON_WIDTH / 2;
            int y1 = p1.y + TECTON_HEIGHT / 2;
            
            // Draw line to each neighbor
            for (Tecton_Class neighbor : tecton.get_TectonNeighbours()) {
                Point p2 = tectonPositions.get(neighbor);
                if (p2 == null) continue;
                
                // Center point of neighbor
                int x2 = p2.x + TECTON_WIDTH / 2;
                int y2 = p2.y + TECTON_HEIGHT / 2;
                
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
    
    private void drawSelectionHighlight(Graphics2D g, Tecton_Class tecton, Color color) {
        Point pos = tectonPositions.get(tecton);
        if (pos == null) return;
        
        g.setColor(color);
        g.fillRoundRect(
            pos.x - 5, pos.y - 5,
            TECTON_WIDTH + 10, TECTON_HEIGHT + 10,
            25, 25
        );
    }
}
