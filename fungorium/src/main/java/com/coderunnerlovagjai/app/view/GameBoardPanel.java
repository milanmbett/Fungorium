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

import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.Plane;
import com.coderunnerlovagjai.app.Tecton_Basic;
import com.coderunnerlovagjai.app.Tecton_Class;
// import com.coderunnerlovagjai.app.view.TectonSelectionListener; // Already in the same package

public class GameBoardPanel extends JPanel {
    private static final Logger BOARD_LOGGER = LogManager.getLogger(GameBoardPanel.class);
    private final transient Game gameModel;
    private final List<TectonView> tectonViews = new ArrayList<>();
    private transient TectonView selectedTectonView = null;
    private final List<TectonSelectionListener> listeners = new ArrayList<>();

    // Constants for hexagonal grid layout
    private static final int HEX_WIDTH = Tecton_Basic.DEFAULT_WIDTH; // nagyobb hexagon
    private static final int HEX_HEIGHT = Tecton_Basic.DEFAULT_HEIGHT; // nagyobb hexagon
    private static final int HEX_COL_SPACING = (int)(HEX_WIDTH * 0.85); // kicsit nagyobb átfedés
    private static final int HEX_ROW_SPACING = (int)(HEX_HEIGHT * 0.75); // honeycomb vertical spacing
    private static final int GRID_START_X = 80; // nagyobb margó
    private static final int GRID_START_Y = 80;
    private static final int DEFAULT_NUM_COLUMNS = 4; // 4 oszlop

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
        int numRows;
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
            this.numRows = 4; // fixen 4 sor
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
            setPreferredSize(new java.awt.Dimension(0,0));
            revalidate();
            repaint();
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
        int panelWidth = DEFAULT_NUM_COLUMNS * HEX_COL_SPACING + GRID_START_X;
        int baseX = panelWidth / 2 - HEX_WIDTH / 2;
        int baseYTop = GRID_START_Y / 2;
        int baseYBottom = GRID_START_Y / 2 + 4 * HEX_ROW_SPACING + HEX_HEIGHT;

        // Felső bázis
        if (base1 != null) {
            base1.setPosition(baseX, baseYTop);
            base1.setSize(HEX_WIDTH, HEX_HEIGHT); //TODO itt megfeleloen
            this.tectonViews.add(new TectonView(base1));
        }

        // Grid középen (16 tecton)
        LayoutState layoutManager = new LayoutState(GRID_START_X, GRID_START_Y + HEX_ROW_SPACING, DEFAULT_NUM_COLUMNS, HEX_COL_SPACING, HEX_ROW_SPACING);
        int maxX = 0;
        int maxY = 0;
        for (Tecton_Class tectonModel : nonBaseTectons) {
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
            }
        }

        // Alsó bázis
        if (base2 != null) {
            base2.setPosition(baseX, baseYBottom);
            base2.setSize(HEX_WIDTH, HEX_HEIGHT);
            this.tectonViews.add(new TectonView(base2));
        }

        // Preferred size beállítása
        if (!this.tectonViews.isEmpty()) {
            java.awt.Dimension preferredDim = new java.awt.Dimension(maxX + GRID_START_X/2, baseYBottom + HEX_HEIGHT + GRID_START_Y/2);
            setPreferredSize(preferredDim);
        } else {
            setPreferredSize(new java.awt.Dimension(GRID_START_X * 2, GRID_START_Y * 2));
        }
        revalidate();
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