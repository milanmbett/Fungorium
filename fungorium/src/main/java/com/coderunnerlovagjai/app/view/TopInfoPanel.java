// TopInfoPanel.java
package com.coderunnerlovagjai.app.view;

import javax.swing.*;
import java.awt.*;
import com.coderunnerlovagjai.app.Game;

public class TopInfoPanel extends JPanel {
    private final Game gameModel;
    private JLabel playerLabel, incomeLabel, pointsLabel, actionsLabel;

    public TopInfoPanel(Game model) {
        this.gameModel = model;
        setLayout(null); setOpaque(false); setPreferredSize(new Dimension(800, 40));
        playerLabel    = createLabel(10,5,400,30);
        incomeLabel    = createLabel(400,5,80,30, "INCOME:");
        pointsLabel    = createLabel(500,5,70,30, "POINTS:");
        actionsLabel   = createLabel(630,5,80,30, "ACTIONS:");
        add(playerLabel); add(incomeLabel); add(pointsLabel); add(actionsLabel);
        updateInfo();
    }

    private JLabel createLabel(int x,int y,int w,int h) {
        JLabel l = new JLabel(); l.setBounds(x,y,w,h); l.setForeground(Color.WHITE); return l;
    }
    private JLabel createLabel(int x,int y,int w,int h, String t) {
        JLabel l = createLabel(x,y,w,h); l.setText(t); return l;
    }

public void updateInfo() {
    var p = gameModel.getPlayer(gameModel.currentTurnsPlayer());
    int playerIndex = gameModel.currentTurnsPlayer();
    
    // Determine color based on player index (1 for player1, 0 for player2)
    String nameColor = (playerIndex == 0) ? "red" : "blue";
    
    // Use HTML formatting to color just the name
    playerLabel.setText("<html>Current player: <font color='" + nameColor + "'>" 
                        + p.getName() + "</font> (" + p.getRole() + ")</html>");
    
    incomeLabel.setText("INCOME: " + p.getIncome());
    pointsLabel.setText("POINTS: " + p.getScore());
    actionsLabel.setText("ACTIONS: " + p.getAction());
}
}