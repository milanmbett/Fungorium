package com.coderunnerlovagjai.app.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.Tecton_Base;

/**
 * Enhanced mouse handler for the game to improve Tecton selection for all player roles.
 * This class registers itself as a mouse listener with the game canvas and routes
 * click events to the appropriate TectonView objects.
 */
public class MouseHandlerExtension extends MouseAdapter {
    private static final List<TectonView> tectonViews = new ArrayList<>();
    private static boolean initialized = false;
    
    /**
     * Creates a new mouse handler and initializes it if not already done.
     */
    public MouseHandlerExtension() {
        if (!initialized) {
            // Add this mouse listener to the game canvas
            GameCanvas.getInstance().addMouseListener(this);
            System.out.println("MouseHandlerExtension initialized on GameCanvas");
            initialized = true;
        }
    }
    
    /**
     * Registers a TectonView for click handling
     * @param tectonView the TectonView to register
     */
    public void registerTectonView(TectonView tectonView) {
        if (!tectonViews.contains(tectonView)) {
            tectonViews.add(tectonView);
        }
    }
    
    /**
     * Handles mouse pressed events for better responsiveness
     */
    @Override
    public void mousePressed(MouseEvent e) {
        handleMouseEvent(e);
    }
    
    /**
     * Also handle mouse clicked events for compatibility
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        handleMouseEvent(e);
    }
    
    /**
     * Processes mouse events and routes them to the appropriate tecton
     * @param e the mouse event
     */
    private void handleMouseEvent(MouseEvent e) {
        Point clickPoint = e.getPoint();
        
        // First check for base tectons as they are most important
        for (TectonView tecton : tectonViews) {
            if (tecton.getModel() instanceof Tecton_Base && tecton.contains(clickPoint)) {
                tecton.handleClick();
                return; // Early return to prevent multiple tectons from being clicked
            }
        }
        
        // Then check all other tectons
        for (TectonView tecton : tectonViews) {
            if (tecton.contains(clickPoint)) {
                tecton.handleClick();
                return; // Early return to prevent multiple tectons from being clicked
            }
        }
    }
    
    /**
     * Finds the GameBoardPanel in the container hierarchy
     * @param container the container to search
     * @return the GameBoardPanel if found, null otherwise
     */
    public static GameBoardPanel findGameBoardPanel(Container container) {
        for (Component child : container.getComponents()) {
            if (child instanceof GameBoardPanel) {
                return (GameBoardPanel) child;
            } else if (child instanceof Container) {
                GameBoardPanel result = findGameBoardPanel((Container) child);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
