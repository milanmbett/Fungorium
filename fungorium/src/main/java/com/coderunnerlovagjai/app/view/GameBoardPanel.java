package com.coderunnerlovagjai.app.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.Mushroom_Class;
import com.coderunnerlovagjai.app.Plane;
import com.coderunnerlovagjai.app.Tecton_Basic;
import com.coderunnerlovagjai.app.Tecton_Class;

public class GameBoardPanel extends JPanel {
    private static final Logger BOARD_LOGGER = LogManager.getLogger(GameBoardPanel.class);
    private final transient Game gameModel;
    private final List<TectonView> tectonViews = new ArrayList<>();
    private transient TectonView selectedTectonView = null;
    private final List<TectonSelectionListener> listeners = new ArrayList<>();

    // Constants for hexagonal grid layout
    // Kisebb sima tecton, sokkal nagyobb bázis, kisebb margó fent
    private static final double BASE_WIDTH = Tecton_Basic.DEFAULT_WIDTH * 4.6;
    private static final double BASE_HEIGHT = Tecton_Basic.DEFAULT_HEIGHT * 1.5;
    private static final double HEX_WIDTH = Tecton_Basic.DEFAULT_WIDTH * 0.85;
    private static final double HEX_HEIGHT = Tecton_Basic.DEFAULT_HEIGHT * 0.85;
    private static final double HEX_COL_SPACING = HEX_WIDTH * 0.85;
    private static final double HEX_ROW_SPACING = HEX_HEIGHT * 0.75;
    private static final int GRID_START_X = 80;
    private static final int GRID_START_Y = 20; // kisebb, hogy fentebb legyen

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
        int tectonsPlaced = 0;
        int numColumns;
        int startX;
        int startY;
        int colSpacing;
        int rowSpacing;

        LayoutState(int startX, int startY, int numColumns, int colSpacing, int rowSpacing) {
            this.startX = startX;
            this.startY = startY;
            this.numColumns = numColumns;
            this.colSpacing = colSpacing;
            this.rowSpacing = rowSpacing;
        }

        public Point getNextPosition() {
            int row = tectonsPlaced / numColumns;
            int col = tectonsPlaced % numColumns;
            int x = startX + col * colSpacing;
            int y = startY + row * rowSpacing;
            // minden páratlan sor legyen eltolva fél hexagon szélességgel
            if (row % 2 == 1) {
                x += colSpacing / 2;
            }
            tectonsPlaced++;
            return new Point(x, y);
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
        if (plane == null) {
            BOARD_LOGGER.error("this.gameModel's Plane is null. Cannot initialize TectonViews.");
            this.tectonViews.clear();
            repaint();
            return;
        }

        List<Tecton_Class> tectons = plane.TectonCollection;
        if (tectons == null || tectons.isEmpty()) {
            this.tectonViews.clear();
            setPreferredSize(new java.awt.Dimension(0,0)); // Explicitly set to 0,0 if no tectons
            revalidate();
            repaint();
            BOARD_LOGGER.warn("TectonCollection is null or empty. GameBoardPanel will be blank.");
            return;
        }

        Tecton_Class base1 = plane.getBase1();
        Tecton_Class base2 = plane.getBase2();
        List<Tecton_Class> nonBaseTectons = new ArrayList<>();
        for (Tecton_Class t : tectons) {
            if (t != base1 && t != base2) {
                nonBaseTectons.add(t);
            }
        }

        this.tectonViews.clear();

        // Bázisok pozíciója (felül és alul középen)

        int panelWidth = getWidth();
        if (panelWidth <= 0) {                              // ha a layout még nem futott le
            panelWidth = ((int)GRID_START_X                     
                        + (int)HEX_COL_SPACING * 4            
                        + (int)BASE_WIDTH);                    
        }
        int baseX = (panelWidth - (int)BASE_WIDTH) / 2 + 100; 
        // Corrected baseX calculation: Center the base, then shift right by 100px.
        // The previous calculation: (panelWidth - BASE_WIDTH) + 624; pushed bases off-screen.
        BOARD_LOGGER.info("Calculated baseX: {} (panelWidth: {}, BASE_WIDTH: {})", baseX, panelWidth, BASE_WIDTH);

        double baseYTop   = 10;
        // Adjusted baseYBottom calculation to be more dynamic based on rows and hex heights
        // Assuming 4 rows of non-base tectons as per LayoutState
        double nonBaseGridHeight = 4 * HEX_ROW_SPACING; 
        double baseYBottom= baseYTop + (int)BASE_HEIGHT + (int)nonBaseGridHeight + 20; // Added some spacing


        // Felső bázis
        if (base1 != null) {
            // 1) felülre kirakjuk a bázist
            base1.setPosition((int)baseX, (int)baseYTop);
            base1.setSize((int)BASE_WIDTH , (int)BASE_HEIGHT);
            TectonView baseView = new TectonView(base1);
            this.tectonViews.add(baseView);

            // 2) ide jönnek a slot-mezők:
            Point[] slotOffsets = new Point[] {
                new Point( 60,  20),
                new Point(120,  20),
                new Point( 30,  80),
                new Point( 90,  80),
                new Point(150,  80),
            };
            List<Tecton_Class> children = base1.get_TectonNeighbours();
            for (int i = 0; i < children.size() && i < slotOffsets.length; i++) {
                Tecton_Class child = children.get(i);
                Point off = slotOffsets[i];

                // a slot középre igazítása a gyermek-hex alá
                int childX = (int)baseX + off.x  - (int)HEX_WIDTH/2;
                int childY = (int)baseYTop + off.y - (int)HEX_HEIGHT/2;

                child.setPosition(childX, childY);
                child.setSize((int)HEX_WIDTH, (int)HEX_HEIGHT);
                this.tectonViews.add(new TectonView(child));
            }
        }

        // Nem bázis tectonok feljebb
        LayoutState layoutManager = new LayoutState(
            (int)GRID_START_X,
            (int)baseYTop + (int)BASE_HEIGHT - 55,
            4, (int)HEX_COL_SPACING, (int)HEX_ROW_SPACING
        );
        int maxX = 0;
        int maxY = 0;

        Set<Tecton_Class> slottedTectons = new HashSet<>();
        if (base1 != null) slottedTectons.addAll(base1.get_TectonNeighbours());
        if (base2 != null) slottedTectons.addAll(base2.get_TectonNeighbours());


        for (Tecton_Class t : nonBaseTectons) {
            if (slottedTectons.contains(t)) continue;
            Point p = layoutManager.getNextPosition();
            t.setPosition(p.x, p.y);
            t.setSize((int)HEX_WIDTH, (int)HEX_HEIGHT); // Ensure size is set
            this.tectonViews.add(new TectonView(t));
            maxX = Math.max(maxX, p.x + (int)HEX_WIDTH);
            maxY = Math.max(maxY, p.y + (int)HEX_HEIGHT);
        }

        // Alsó bázis
        if (base2 != null) {
            // Ensure baseYBottom is below the maxY of non-base tectons if they exist
            double actualBaseYBottom = maxY > 0 ? Math.max(maxY + 20, baseYBottom-80) : baseYBottom-80;
            base2.setPosition((int)baseX+50, (int)actualBaseYBottom);
            base2.setSize((int)BASE_WIDTH, (int)BASE_HEIGHT);
            this.tectonViews.add(new TectonView(base2));
            // Update overall maxY if base2 is lower
            maxY = Math.max(maxY, (int)actualBaseYBottom + (int)BASE_HEIGHT);
        }


        // Preferred size beállítása
        if (!this.tectonViews.isEmpty()) {
            int finalWidth = (int)GRID_START_X; // Start with left margin
            if (base1 != null || base2 != null) {
                 finalWidth = Math.max(finalWidth, (int)baseX + (int)BASE_WIDTH + (int)GRID_START_X/2);
            }
            finalWidth = Math.max(finalWidth, maxX + (int)GRID_START_X/2);
            
            int finalHeight = maxY + (int)GRID_START_Y; // Add top/bottom margin

            // Ensure minimum size for visibility
            finalWidth = Math.max(finalWidth, GRID_START_X * 2);
            finalHeight = Math.max(finalHeight, GRID_START_Y * 2);

            java.awt.Dimension preferredDim = new java.awt.Dimension(finalWidth, finalHeight);
            setPreferredSize(preferredDim);
            BOARD_LOGGER.info("Preferred size set to: {}", preferredDim);
        } else {
            setPreferredSize(new java.awt.Dimension(GRID_START_X * 2, GRID_START_Y * 2)); // Default small size
            BOARD_LOGGER.info("No TectonViews, preferred size set to default small.");
        }
        revalidate(); // Ensure layout is updated with new preferred size
        repaint(); // Repaint after views and size are set
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
        // Preserve current selection before clearing views
        Tecton_Class prevSelected = getSelectedTectonModel();

        // Re-initialize all tecton views (clears tectonViews and repaints)
        initializeTectonViews();

        // Restore selection if still present
        if (prevSelected != null) {
            setSelectedTectonModel(prevSelected);
        }
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
                // Felső bázis (Base1) elforgatása 180 fokkal
                if (view.getModel() == gameModel.getPlane().getBase1()) {
                    Graphics2D g2dRot = (Graphics2D) g2d.create();
                    Point pos = view.getModel().getPosition();
                    int cx = pos.x + view.getModel().getWidth() / 2;
                    int cy = pos.y + view.getModel().getHeight() / 2;
                    g2dRot.rotate(Math.PI, cx, cy);
                    view.render(g2dRot, view == selectedTectonView);
                    g2dRot.dispose();
                } else {
                    view.render(g2d, view == selectedTectonView);
                }
            }
        } finally {
            g2d.dispose(); // Dispose of the graphics copy
        }
    }
    private Class<? extends Mushroom_Class> previewType = null;

    public void setPreviewMushroomType(Class<? extends Mushroom_Class> type) {
    this.previewType = type;
    // keep the current hex selected, so repaint() will draw the ghost
    repaint();
}
}
