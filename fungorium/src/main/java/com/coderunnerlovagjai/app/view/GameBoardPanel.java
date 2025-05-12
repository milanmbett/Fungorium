package com.coderunnerlovagjai.app.view;

import java.awt.BorderLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.coderunnerlovagjai.app.Basic_Spore;
import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.Insect_Class;
import com.coderunnerlovagjai.app.Mushroom_Class;
import com.coderunnerlovagjai.app.Plane;
import com.coderunnerlovagjai.app.Tecton_Base;
import com.coderunnerlovagjai.app.Tecton_Class;
import com.coderunnerlovagjai.app.Thread_Class;

/**
 * A simplified version of the game board for integrating with the existing GameCanvasFrame.
 * This panel just delegates drawing to the GameCanvas and provides interaction handling.
 */
public class GameBoardPanel extends JPanel {
    private final Plane gameModel;
    private final Map<Tecton_Class, Point> tectonPositions = new HashMap<>();
    private Tecton_Class selectedTecton = null;
    private static final int TECTON_WIDTH = 120; // Increased from 100
    private static final int TECTON_HEIGHT = 100; // Increased from 80
    
    // List of listeners for tecton selection events
    private final List<TectonSelectionListener> selectionListeners = new ArrayList<>();
    
    public GameBoardPanel(Plane model) {
        this.gameModel = model;
        setLayout(new BorderLayout());
        
        // Add the GameCanvas instance to this panel
        add(GameCanvas.getInstance(), BorderLayout.CENTER);
        
        // Position the tectons
        layoutTectons();
        
        // Initialize views for entities
        createEntityViews();
    }
    
    /**
     * Positions all tectons in a layout that makes sense for the game.
     */
    private void layoutTectons() {
        List<Tecton_Class> tectons = gameModel.TectonCollection;
        if (tectons.isEmpty()) return;
        
        // Set up board dimensions
        final int BOARD_WIDTH = 800;
        final int BOARD_HEIGHT = 600;
        final int CENTER_X = BOARD_WIDTH / 2;
        
        // Constants for improved layout structure
        final int ROWS = 4; // Further reduce rows for more spacing
        final int COLUMNS_PER_SIDE = 2; // Fewer columns on each side to reduce clutter
        final int CENTER_COLUMN = COLUMNS_PER_SIDE; // Middle column index remains in center
        
        // Calculate spacing with hexagonal offset - increased spacing
        final int HORIZONTAL_SPACING = (BOARD_WIDTH - TECTON_WIDTH) / (COLUMNS_PER_SIDE * 2 + 1);
        final int VERTICAL_SPACING = (BOARD_HEIGHT - TECTON_HEIGHT) / (ROWS + 1);
        
        // Calculate hexagonal offset (every other row is offset by half spacing)
        final int OFFSET_X = HORIZONTAL_SPACING / 2;
        
        // First position the base tectons in the center column
        Tecton_Base base1 = gameModel.getBase1();
        Tecton_Base base2 = gameModel.getBase2();
        
        if (base1 != null) {
            Point base1Pos = new Point(CENTER_X, VERTICAL_SPACING);
            tectonPositions.put(base1, base1Pos);
            base1.setPosition(base1Pos.x, base1Pos.y);
            base1.setSize(TECTON_WIDTH, TECTON_HEIGHT);
        }
        
        if (base2 != null) {
            Point base2Pos = new Point(CENTER_X, BOARD_HEIGHT - VERTICAL_SPACING - TECTON_HEIGHT);
            tectonPositions.put(base2, base2Pos);
            base2.setPosition(base2Pos.x, base2Pos.y);
            base2.setSize(TECTON_WIDTH, TECTON_HEIGHT);
        }
        
        // Now position all regular tectons in an organized hexagonal grid
        int regularTectonIndex = 0;
        
        // Get all non-base tectons
        List<Tecton_Class> regularTectons = new ArrayList<>();
        for (Tecton_Class tecton : tectons) {
            if (!(tecton instanceof Tecton_Base)) {
                regularTectons.add(tecton);
            }
        }
        
        // Distribute tectons across the rows and columns with proper hex grid offsets
        for (int row = 0; row < ROWS; row++) {
            boolean isOffsetRow = row % 2 == 1; // Odd rows are offset in hex grid
            
            for (int col = 0; col < COLUMNS_PER_SIDE * 2 + 1; col++) {
                // Skip the center column - reserved for bases
                if (col == CENTER_COLUMN) continue;
                
                // If we've used all tectons, stop placing
                if (regularTectonIndex >= regularTectons.size()) break;
                
                Tecton_Class tecton = regularTectons.get(regularTectonIndex);
                
                // Calculate x position 
                int x = (col + 1) * HORIZONTAL_SPACING;
                if (isOffsetRow) x += OFFSET_X; // Offset for hex grid
                
                // Calculate y position with spacing between rows
                int y = (row + 1) * VERTICAL_SPACING;
                
                // Add small random variation for natural look (±5 pixels max)
                java.util.Random rand = new java.util.Random(tecton.get_ID() != null ? tecton.get_ID().hashCode() : tecton.hashCode());
                int randomOffsetX = rand.nextInt(11) - 5; // -5 to +5
                int randomOffsetY = rand.nextInt(11) - 5; // -5 to +5
                
                // Apply position and ensure it stays within bounds
                x = Math.max(TECTON_WIDTH/2, Math.min(BOARD_WIDTH - TECTON_WIDTH/2, x + randomOffsetX));
                y = Math.max(TECTON_HEIGHT/2, Math.min(BOARD_HEIGHT - TECTON_HEIGHT/2, y + randomOffsetY));
                
                Point pos = new Point(x, y);
                tectonPositions.put(tecton, pos);
                tecton.setPosition(pos.x, pos.y);
                tecton.setSize(TECTON_WIDTH, TECTON_HEIGHT);
                
                regularTectonIndex++;
            }
        }
    }
    
    /**
     * Creates view objects for all game entities
     */
    private void createEntityViews() {
        // Clear existing graphics first
        GameCanvas.getInstance().clearAll();
        
        // Create views for all tectons
        for (Tecton_Class tecton : gameModel.TectonCollection) {
            new TectonView(tecton);
        }
        
        // Create views for all threads
        for (Thread_Class thread : gameModel.ThreadCollection) {
            new ThreadView(thread);
        }
        
        // Create views for all mushrooms
        for (Mushroom_Class mushroom : gameModel.MushroomCollection) {
            new MushroomView(mushroom);
        }
        
        // Create views for all insects
        for (Insect_Class insect : gameModel.InsectCollection) {
            new InsectView(insect);
        }
        
        // Create views for all spores
        for (Basic_Spore spore : gameModel.SporeCollection) {
            new SporeView(spore);
        }
    }
    
    /**
     * Refreshes all entity views when the game state changes
     */
    public void refreshViews() {
        createEntityViews();
        repaint();
    }
    
    /**
     * Get the currently selected tecton
     */
    public Tecton_Class getSelectedTecton() {
        return selectedTecton;
    }
    
    /**
     * Set the currently selected tecton
     */
    public void setSelectedTecton(Tecton_Class tecton) {
        this.selectedTecton = tecton;
        
        // Notify all listeners of the selection
        notifyTectonSelected(tecton);
        
        repaint();
    }
    
    /**
     * Add a listener to be notified of tecton selections
     */
    public void addTectonSelectionListener(TectonSelectionListener listener) {
        if (!selectionListeners.contains(listener)) {
            selectionListeners.add(listener);
        }
    }
    
    /**
     * Remove a tecton selection listener
     */
    public void removeTectonSelectionListener(TectonSelectionListener listener) {
        selectionListeners.remove(listener);
    }
    
    /**
     * Notify all listeners that a tecton has been selected
     */
    private void notifyTectonSelected(Tecton_Class tecton) {
        for (TectonSelectionListener listener : selectionListeners) {
            listener.onTectonSelected(tecton);
        }
    }
}
