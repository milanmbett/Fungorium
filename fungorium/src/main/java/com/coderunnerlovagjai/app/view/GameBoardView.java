package com.coderunnerlovagjai.app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.coderunnerlovagjai.app.Plane;
import com.coderunnerlovagjai.app.Tecton_Base;
import com.coderunnerlovagjai.app.Tecton_Class;

/**
 * Main game board view that handles rendering of the game map (tectons) and interaction.
 * This is a specialized canvas that manages the layout and user interaction with game elements.
 */
public class GameBoardView extends JPanel {
    private static final Color BG_COLOR = new Color(30, 30, 40);
    private static final int TECTON_WIDTH = 100;
    private static final int TECTON_HEIGHT = 80;
    private static final int GRID_SPACING_X = 110;
    private static final int GRID_SPACING_Y = 90;

    private final Plane gameModel;
    private final Map<Tecton_Class, Point> tectonPositions = new HashMap<>();

    public GameBoardView(Plane model) {
        this.gameModel = model;
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(800, 600));
        setupTectonLayout();
    }

    private void setupTectonLayout() {
        List<Tecton_Class> tectons = gameModel.TectonCollection;
        if (tectons.isEmpty()) return;
        int maxCols = 4;
        for (int i = 0; i < tectons.size(); i++) {
            Tecton_Class tecton = tectons.get(i);
            int row = i / maxCols;
            int col = i % maxCols;
            if (tecton instanceof Tecton_Base) {
                if (tecton == gameModel.getBase1()) {
                    tectonPositions.put(tecton, new Point(GRID_SPACING_X * 2, 40));
                } else if (tecton == gameModel.getBase2()) {
                    tectonPositions.put(tecton, new Point(GRID_SPACING_X * 2, 500));
                }
            } else {
                int centerOffsetX = (getWidth() - (maxCols * GRID_SPACING_X)) / 2 + 50;
                int centerOffsetY = 120;
                tectonPositions.put(tecton, new Point(
                    centerOffsetX + col * GRID_SPACING_X, 
                    centerOffsetY + row * GRID_SPACING_Y
                ));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawTectonConnections(g2d);
    }

    private void drawTectonConnections(Graphics2D g) {
        g.setColor(new Color(180, 180, 180, 120));
        g.setStroke(new BasicStroke(2.0f));
        for (Tecton_Class tecton : gameModel.TectonCollection) {
            Point p1 = tectonPositions.get(tecton);
            if (p1 == null) continue;
            int x1 = p1.x + TECTON_WIDTH / 2;
            int y1 = p1.y + TECTON_HEIGHT / 2;
            for (Tecton_Class neighbor : tecton.get_TectonNeighbours()) {
                Point p2 = tectonPositions.get(neighbor);
                if (p2 == null) continue;
                int x2 = p2.x + TECTON_WIDTH / 2;
                int y2 = p2.y + TECTON_HEIGHT / 2;
                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}
