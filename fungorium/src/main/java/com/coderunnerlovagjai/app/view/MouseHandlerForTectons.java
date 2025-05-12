package com.coderunnerlovagjai.app.view;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.Tecton_Base;

/**
 * Static class that handles mouse clicks for all tectons with enhanced click detection
 */
class MouseHandlerForTectons extends MouseAdapter {
    private static final List<TectonView> registeredTectons = new ArrayList<>();
    private static boolean initialized = false;
    
    public MouseHandlerForTectons() {
        if (!initialized) {
            // Add this mouse listener to the game canvas for both click and press
            GameCanvas.getInstance().addMouseListener(this);
            initialized = true;
            System.out.println("MouseHandlerForTectons initialized");
        }
    }
    
    public void registerTecton(TectonView tecton) {
        if (!registeredTectons.contains(tecton)) {
            registeredTectons.add(tecton);
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        handleMouseEvent(e);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        handleMouseEvent(e);
    }
    
    private void handleMouseEvent(MouseEvent e) {
        Point clickPoint = e.getPoint();
        
        // First check for Tecton_Base instances as they are the most important
        for (TectonView tecton : registeredTectons) {
            if (tecton.getModel() instanceof Tecton_Base && tecton.contains(clickPoint)) {
                tecton.handleClick();
                return;
            }
        }
        
        // Then check other tectons
        for (TectonView tecton : registeredTectons) {
            if (tecton.contains(clickPoint)) {
                tecton.handleClick();
                // Important: only handle the top-most tecton to avoid multiple notifications
                return;
            }
        }
    }
}
