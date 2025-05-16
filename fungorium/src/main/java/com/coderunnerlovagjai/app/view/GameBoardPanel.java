package com.coderunnerlovagjai.app.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Corrected imports for model classes
import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.Plane;
import com.coderunnerlovagjai.app.Tecton_Class;
import com.coderunnerlovagjai.app.Tecton_Basic;
// import com.coderunnerlovagjai.app.view.TectonSelectionListener; // Already in the same package

public class GameBoardPanel extends JPanel {
    private static final Logger BOARD_LOGGER = LogManager.getLogger(GameBoardPanel.class);
    private final transient Game gameModel;
    private final List<TectonView> tectonViews = new ArrayList<>();
    private transient TectonView selectedTectonView = null;
    private final List<TectonSelectionListener> listeners = new ArrayList<>();

    // Constants for hexagonal grid layout
    private static final int HEX_WIDTH = Tecton_Basic.DEFAULT_WIDTH;
    private static final int HEX_HEIGHT = Tecton_Basic.DEFAULT_HEIGHT;
    private static final int HEX_COL_SPACING = HEX_WIDTH + 10;
    private static final int HEX_ROW_SPACING = (int) (HEX_HEIGHT * 0.75);
    private static final int GRID_START_X = 50;
    private static final int GRID_START_Y = 50;
    private static final int DEFAULT_NUM_COLUMNS = 5; // Added constant

    public GameBoardPanel(Game gameModel) {
        if (gameModel == null) {
            BOARD_LOGGER.error("Game model cannot be null.");
            throw new IllegalArgumentException("Game model cannot be null.");
        }
        this.gameModel = gameModel;
        setBackground(new Color(45, 45, 55)); // Darker background
        initializeTectonViews();
        setupMouseListener();
        BOARD_LOGGER.info("GameBoardPanel initialized with Game model.");
    }

    // Constructor accepting Plane - ensure Plane can provide Game instance or this needs redesign
    public GameBoardPanel(Plane plane) {
        if (plane == null) {
            BOARD_LOGGER.error("Plane cannot be null for GameBoardPanel(Plane) constructor.");
            throw new IllegalArgumentException("Plane cannot be null.");
        }
        // This logic assumes Tecton_Base (part of Plane) can provide the Game instance.
        // This might need a more robust way to get the Game instance.
        if (plane.getBase1() != null && plane.getBase1().getGame() != null) {
            this.gameModel = plane.getBase1().getGame();
        } else if (plane.getBase2() != null && plane.getBase2().getGame() != null) {
            this.gameModel = plane.getBase2().getGame();
        } else {
            BOARD_LOGGER.error("Could not retrieve Game instance from Plane. Plane or its bases might not be fully initialized or don\\'t provide Game access.");
            throw new IllegalStateException("Failed to initialize GameModel from Plane. Ensure Plane and its components are correctly linked to Game.");
        }

        setBackground(new Color(45, 45, 55));
        if (this.gameModel != null) {
            initializeTectonViews();
        } else {
            // This case should ideally be prevented by the exception above.
            BOARD_LOGGER.error("GameModel is null after attempting to retrieve from Plane. Tecton views not initialized.");
        }
        setupMouseListener();
        BOARD_LOGGER.info("GameBoardPanel initialized with Plane model.");
    }

    // Nested class for managing layout state
    private static class LayoutState {
        int currentX;
        int currentY;
        int tectonsInRow;
        boolean oddRow;
        final int numColumns;
        final int startX;
        final int colSpacing;
        final int rowSpacing;

        LayoutState(int startX, int startY, int numColumns, int colSpacing, int rowSpacing) {
            this.startX = startX;
            this.currentX = startX;
            this.currentY = startY;
            this.numColumns = numColumns;
            this.colSpacing = colSpacing;
            this.rowSpacing = rowSpacing;
            this.tectonsInRow = 0;
            this.oddRow = false;
        }

        public Point getNextPosition() {
            int xPos = currentX + (oddRow ? colSpacing / 2 : 0);
            int yPos = currentY;

            tectonsInRow++;
            currentX += colSpacing;
            if (tectonsInRow >= numColumns) {
                tectonsInRow = 0;
                currentX = startX;
                currentY += rowSpacing;
                oddRow = !oddRow;
            }
            return new Point(xPos, yPos);
        }
    }

    private void initializeTectonViews() {
        BOARD_LOGGER.info("initializeTectonViews called. Panel visible: {}, Panel width: {}, Panel height: {}, Panel preferred size: {}",
                          isVisible(), getWidth(), getHeight(), getPreferredSize());

        if (this.gameModel == null) {
            BOARD_LOGGER.error("this.gameModel (hash: {}) is null. Cannot initialize TectonViews.", System.identityHashCode(this.gameModel));
            this.tectonViews.clear();
            repaint();
            return;
        }

        Plane plane = this.gameModel.getPlane();
        BOARD_LOGGER.info("Using Plane instance (hash: {}) from this.gameModel (hash: {})",
                          System.identityHashCode(plane), System.identityHashCode(this.gameModel));

        if (plane == null) {
            BOARD_LOGGER.error("this.gameModel\\'s Plane is null. Cannot initialize TectonViews.");
            this.tectonViews.clear();
            repaint();
            return;
        }

        List<Tecton_Class> tectons = plane.TectonCollection;
        BOARD_LOGGER.info("TectonCollection reference (hash: {}) from Plane (hash: {}).",
                          System.identityHashCode(tectons), System.identityHashCode(plane));

        if (tectons == null) {
            BOARD_LOGGER.error("TectonCollection is null in Plane instance (hash: {}). Cannot initialize TectonViews.",
                              System.identityHashCode(plane));
            this.tectonViews.clear();
            repaint();
            return;
        }

        BOARD_LOGGER.info("TectonCollection size as seen by GameBoardPanel.initializeTectonViews: {}. Current tectonViews count before clear: {}",
                          tectons.size(), this.tectonViews.size());

        this.tectonViews.clear();
        BOARD_LOGGER.info("TectonViews cleared. Current tectonViews count: {}", this.tectonViews.size());

        if (tectons.isEmpty()) {
            BOARD_LOGGER.warn("TectonCollection is empty. No TectonViews will be created. Setting preferred size to 0,0.");
            setPreferredSize(new java.awt.Dimension(0,0)); // Explicitly set to 0,0 if empty
            revalidate(); // Notify layout manager
            repaint();
            return; // Return early as no views to create or layout
        }

        LayoutState layoutManager = new LayoutState(GRID_START_X, GRID_START_Y, DEFAULT_NUM_COLUMNS, HEX_COL_SPACING, HEX_ROW_SPACING);
        int maxX = 0;
        int maxY = 0;

        for (Tecton_Class tectonModel : tectons) {
            if (tectonModel != null) {
                Point position = layoutManager.getNextPosition();
                
                tectonModel.setPosition(position.x, position.y);
                // Ensure width and height are set on the model if not already
                if (tectonModel.getWidth() == 0 || tectonModel.getHeight() == 0) {
                    tectonModel.setSize(HEX_WIDTH, HEX_HEIGHT);
                }

                TectonView tectonView = new TectonView(tectonModel);
                this.tectonViews.add(tectonView);
                
                // Update maxX and maxY based on the tecton's position and dimensions
                // The position from layoutManager already includes GRID_START_X/Y offsets
                maxX = Math.max(maxX, position.x + tectonModel.getWidth());
                maxY = Math.max(maxY, position.y + tectonModel.getHeight());
                
            } else {
                BOARD_LOGGER.warn("Encountered a null Tecton_Class model in TectonCollection during initialization.");
            }
        }
        
        if (!this.tectonViews.isEmpty()) {
            // Add a small margin to the calculated maximums
            java.awt.Dimension preferredDim = new java.awt.Dimension(maxX + GRID_START_X/2, maxY + GRID_START_Y/2); // Using half of grid start as margin
            setPreferredSize(preferredDim);
            BOARD_LOGGER.info("Calculated and set preferred size to: {}. tectonViews count: {}", preferredDim, this.tectonViews.size());
        } else {
            // This case is handled by the tectons.isEmpty() check above, but as a fallback:
            setPreferredSize(new java.awt.Dimension(GRID_START_X * 2, GRID_START_Y * 2)); // Minimal default if somehow empty but not caught
            BOARD_LOGGER.warn("TectonViews list is empty after loop, setting minimal preferred size.");
        }
        
        revalidate(); // Crucial for layout manager to recognize new preferred size

        BOARD_LOGGER.info("Initialized {} TectonViews with hexagonal layout. Panel preferred size: {}. Repainting.",
                          this.tectonViews.size(), getPreferredSize());
        repaint();
    }

    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                BOARD_LOGGER.debug("Mouse clicked at: {}", clickPoint);
                TectonView clickedView = null;
                for (TectonView view : tectonViews) {
                    if (view.contains(clickPoint)) { // Assumes TectonView has a proper contains method
                        clickedView = view;
                        break;
                    }
                }

                if (clickedView != null) {
                    selectedTectonView = clickedView;
                    // TectonView extends GraphicsObject, which has getModel()
                    BOARD_LOGGER.info("Tecton selected: {}", selectedTectonView.getModel().get_ID());
                    notifyListeners(selectedTectonView.getModel());
                } else {
                    selectedTectonView = null;
                    BOARD_LOGGER.info("Background clicked, selection cleared.");
                    notifyListeners(null);
                }
                repaint();
            }
        });
    }

    private void notifyListeners(Tecton_Class tectonModel) {
        for (TectonSelectionListener listener : listeners) {
            listener.onTectonSelected(tectonModel);
        }
    }

    public void addTectonSelectionListener(TectonSelectionListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeTectonSelectionListener(TectonSelectionListener listener) {
        listeners.remove(listener);
    }

    public Tecton_Class getSelectedTectonModel() {
        // TectonView extends GraphicsObject, which has getModel()
        return selectedTectonView != null ? selectedTectonView.getModel() : null;
    }

    public void setSelectedTectonModel(Tecton_Class tectonModel) {
        this.selectedTectonView = null;
        if (tectonModel != null) {
            for (TectonView view : tectonViews) {
                // TectonView extends GraphicsObject, which has getModel()
                if (view.getModel() == tectonModel) {
                    this.selectedTectonView = view;
                    break;
                }
            }
        }
        BOARD_LOGGER.info("Selected tecton model set programmatically to: {}", tectonModel != null ? tectonModel.get_ID() : "null");
        notifyListeners(tectonModel);
        repaint();
    }

    public void refreshViews() {
        BOARD_LOGGER.debug("refreshViews called. Re-initializing TectonViews and repainting.");
        initializeTectonViews();
        // repaint(); // initializeTectonViews already calls repaint
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create(); // Use create() to get a copy for local modifications
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (tectonViews.isEmpty() && gameModel != null && gameModel.getPlane() != null) {
                BOARD_LOGGER.warn("TectonViews list is empty during paint. Attempting to re-initialize.");
                initializeTectonViews();
            }

            for (TectonView view : tectonViews) {
                // Pass selection state to TectonView\'s render method
                view.render(g2d, view == selectedTectonView);
            }
        } finally {
            g2d.dispose(); // Dispose of the graphics copy
        }
    }
}