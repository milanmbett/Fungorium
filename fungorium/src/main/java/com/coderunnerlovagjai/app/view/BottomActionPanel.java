// BottomActionPanel.java
package com.coderunnerlovagjai.app.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.RoleType;
import com.coderunnerlovagjai.app.controller.InteractionManager;

public class BottomActionPanel extends JPanel {
    private final Game gameModel;
    private final InteractionManager controller;
    private final JPanel entityPanel;
    private final JButton endTurnBtn, moveBtn, crackBtn;

    public BottomActionPanel(Game model, InteractionManager ctrl) {
        this.gameModel = model; this.controller = ctrl;
        setLayout(null); setOpaque(false); setPreferredSize(new Dimension(800, 120));
        entityPanel = new JPanel(null); entityPanel.setOpaque(false);
        entityPanel.setBounds(100,10,550,90); add(entityPanel);

        endTurnBtn = createButton(670,10,80,80, "/images/endTurnButton.png", e->controller.onEndTurn());
        moveBtn    = createButton(15,5,40,40,  "/images/moveInsectButton.png", e->controller.startInsectMovement());
        crackBtn   = createButton(15,55,40,40, "/images/TectonCrackButton.png", e->controller.startTectonCrack());
        add(endTurnBtn); add(moveBtn); add(crackBtn);
        
        updateButtonVisibility(); // Initial update of button visibility
        updateEntities();
    }

    private JButton createButton(int x,int y,int w,int h,String icon, java.awt.event.ActionListener al) {
        JButton b = new JButton(); b.setBounds(x,y,w,h);
        b.setFocusable(false); b.setContentAreaFilled(false);
        b.setBorderPainted(false); b.addActionListener(al);
        try { Image img = ImageIO.read(getClass().getResourceAsStream(icon));
            b.setIcon(new ImageIcon(img.getScaledInstance(w,h,Image.SCALE_SMOOTH)));
        } catch(IOException|NullPointerException ex) {}
        return b;
    }

    public void updateEntities() {
        entityPanel.removeAll();
        controller.populateEntityBoxes(entityPanel);
        updateButtonVisibility(); // Update button visibility with each entity update
        entityPanel.revalidate(); entityPanel.repaint();
    }
    
    /**
     * Updates the visibility of action buttons based on current player's role
     */
    public void updateButtonVisibility() {
        // Get the current player's role
        RoleType currentRole = gameModel.getPlayer(gameModel.currentTurnsPlayer()).getRole();
        
        // Only show move and crack buttons if current player has INSECT role
        boolean isInsect = (currentRole == RoleType.INSECT);
        moveBtn.setVisible(isInsect);
        crackBtn.setVisible(isInsect);
    }
}