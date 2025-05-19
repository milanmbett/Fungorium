package com.coderunnerlovagjai.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.coderunnerlovagjai.app.view.TectonGraphics;

/**
 * GameCanvasFrame is the main game window, styled like FrameStyle.
 * It hosts the game drawing surface and runs the game loop.
 */
public class GameCanvasFrame extends FrameStyle {
    private final Game gameModel;
    private JButton endTurnButton;
    private JLabel currentPlayerLabel;
    private JPanel topInfoPanel;
    private JLabel pointsLabel;
    private JLabel scoreValueLabel;
    private JLabel actionsLabel;
    private JLabel actionsValueLabel;
    private JPanel bottomPanel;
    private JPanel entityPanel;
    private javax.swing.JLayeredPane layeredPane;
    private int selectedEntityIndex = -1;
    private String[] mushroomTypes = {"Shroomlet", "Maximus", "Slender", "Grand", "Maximus"};
    private String[] insectTypes = {"Buglet", "Buggernaut", "Stinger", "Tektonizator", "ShroomReaper"};

    /**
     * Constructs a new game window for two players.
     * @param player1 Name of player 1
     * @param player2 Name of player 2
     */
    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        this.gameModel = new Game(player1, player2);
        gameModel.initGame();
        gameModel.startGame();
        buildUI();
        updateInventoryVisibility();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        new Timer(40, e -> GameCanvas.getInstance().repaint()).start();
    }

    /**
     * Builds the game canvas UI within the styled frame.
     */
    @Override
    protected void buildUI() {
        GameCanvas canvas = GameCanvas.getInstance();
        canvas.setPreferredSize(new Dimension(800, 600));
        content.setLayout(new BorderLayout());
        
        // Create a layered pane to allow for component overlapping
        layeredPane = new javax.swing.JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        content.add(layeredPane, BorderLayout.CENTER);
        
        // Add canvas to the layered pane at the DEFAULT layer
        canvas.setBounds(0, 0, 800, 600);
        layeredPane.add(canvas, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        for (var t : gameModel.getPlane().TectonCollection) {
            canvas.addGraphics(new TectonGraphics(t));
        }
        // Top info bar (player/role left, points/actions right)
        topInfoPanel = new JPanel(null);
        topInfoPanel.setOpaque(false);
        topInfoPanel.setPreferredSize(new Dimension(800, 40));
        currentPlayerLabel = new JLabel(getTopInfoText());
        currentPlayerLabel.setBounds(10, 5, 400, 30);
        currentPlayerLabel.setForeground(java.awt.Color.WHITE);
        topInfoPanel.add(currentPlayerLabel);
        pointsLabel = new JLabel("POINTS:");
        pointsLabel.setForeground(java.awt.Color.WHITE);
        pointsLabel.setBounds(500, 5, 70, 30);
        topInfoPanel.add(pointsLabel);
        scoreValueLabel = new JLabel("0");
        scoreValueLabel.setForeground(java.awt.Color.YELLOW);
        scoreValueLabel.setBounds(570, 5, 50, 30);
        topInfoPanel.add(scoreValueLabel);
        actionsLabel = new JLabel("ACTIONS:");
        actionsLabel.setForeground(java.awt.Color.WHITE);
        actionsLabel.setBounds(630, 5, 80, 30);
        topInfoPanel.add(actionsLabel);
        actionsValueLabel = new JLabel("0");
        actionsValueLabel.setForeground(java.awt.Color.YELLOW);
        actionsValueLabel.setBounds(710, 5, 40, 30);
        topInfoPanel.add(actionsValueLabel);
        content.add(topInfoPanel, BorderLayout.NORTH);
        // Bottom panel for entity selection only
        bottomPanel = new JPanel(null);
        bottomPanel.setPreferredSize(new Dimension(800, 120));
        bottomPanel.setOpaque(false);
        entityPanel = new JPanel(null);
        entityPanel.setOpaque(false);
        entityPanel.setBounds(100, 10, 550, 90); // Make the entity panel a bit smaller to make room for the end turn button
        bottomPanel.add(entityPanel);
        
        // End Turn button: positioned at the right side of the bottom panel 
        endTurnButton = new JButton();
        endTurnButton.setFocusable(false);
        endTurnButton.setBorderPainted(false);
        endTurnButton.setContentAreaFilled(false);
        endTurnButton.setFocusPainted(false);
        endTurnButton.setMargin(new java.awt.Insets(0,0,0,0));
        endTurnButton.setOpaque(false);
        endTurnButton.setToolTipText("End your turn");
        try {
            Image img = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/endTurnButton.png"));
            if (img != null) {
                endTurnButton.setIcon(new ImageIcon(img.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
            }
        } catch (IOException | IllegalArgumentException e) {}
        endTurnButton.addActionListener(e -> endTurn());
        endTurnButton.setBounds(670, 10, 80, 80); // Position in the bottom panel next to entity selection
        bottomPanel.add(endTurnButton); // Add directly to the bottom panel instead of layered pane
        
        content.add(bottomPanel, BorderLayout.SOUTH);
        
        // Mouse input for tecton selection
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleCanvasClick(e.getX(), e.getY());
            }
        });
        new Timer(40, e -> canvas.repaint()).start();
        updateInfoPanels();
        // Stop menu music and start game music
        try {
            SoundManager.stopMusic();
            SoundManager.playMusic("sounds/gameMusic.mp3", true);
        } catch (Exception e) {
            // ignore if SoundManager not present
        }
    }

    private void handleCanvasClick(int x, int y) {
        // Find which tecton was clicked
        for (var t : gameModel.getPlane().TectonCollection) {
            int tx = t.getPosition().x, ty = t.getPosition().y;
            double dist = Math.hypot(x - tx, y - ty);
            if (dist < 40) { // hex radius
                onTectonClicked(t);
                break;
            }
        }
    }

    private void onTectonClicked(Tecton_Class tecton) {
        // Determine current player and role
        var player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        var role = player.getRole();
        boolean isMushroomRole = role == RoleType.MUSHROOM;
        boolean isInsectRole = role == RoleType.INSECT;
                if (player.getAction() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "No actions left. Please use the Skip Turn button!",
                "No Actions Remaining",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Check if an entity is selected
        if (selectedEntityIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select an entity to place first!", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (isMushroomRole) {
            if (!canPlaceMushroomHere(tecton)) {
                JOptionPane.showMessageDialog(this, "Cannot place mushroom here!", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String type = mushroomTypes[selectedEntityIndex];
            Mushroom_Class mush = null;
            switch (type) {
                case "Shroomlet": mush = new Mushroom_Shroomlet(tecton, player); break;
                case "Maximus": mush = new Mushroom_Maximus(tecton, player); break;
                case "Slender": mush = new Mushroom_Slender(tecton, player); break;
                case "Grand": mush = new Mushroom_Grand(tecton, player); break;
                // Fifth type can be another valid mushroom, e.g., Maximus again or a new one if available
                default: break;
            }
            if (mush != null) {
                gameModel.getPlane().place_Mushroom(mush, tecton);
                player.setAction(player.getAction() - 1); // Reduce action points
                GameCanvas.getInstance().repaint();
                updateInfoPanels(); // Update action count display
            }
        } else if (isInsectRole) {
            if (!canPlaceInsectHere(tecton)) {
                JOptionPane.showMessageDialog(this, "Cannot place insect here!", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String type = insectTypes[selectedEntityIndex];
            Insect_Class insect = null;
            switch (type) {
                case "Buglet": insect = new Insect_Buglet(tecton, player); break;
                case "Buggernaut": insect = new Insect_Buggernaut(tecton, player); break;
                case "Stinger": insect = new Insect_Stinger(tecton, player); break;
                case "Tektonizator": insect = new Insect_Tektonizator(tecton, player); break;
                case "ShroomReaper": insect = new Insect_ShroomReaper(tecton, player); break;
                default: break;
            }
            if (insect != null) {
                gameModel.getPlane().placeInsect(insect, tecton);
                GameCanvas.getInstance().repaint();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Unknown player role!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //Dunno mi ez
   /*  private boolean canPlaceMushroomHere(Tecton_Class tecton) {
        // Example rule: can't place next to insect base (implement real logic)
        for (var neighbor : tecton.get_TectonNeighbours()) {
            if (neighbor.get_Mushroom() != null && neighbor.get_Mushroom().getClass().getSimpleName().contains("Insect")) {
                return false;
            }
        }
        return tecton.get_Mushroom() == null;
    }
    */
    private boolean canPlaceMushroomHere(Tecton_Class tecton) {
        // Example: only allow if no mushroom present
        return (tecton.get_Mushroom()==null && tecton.get_Thread() != null);
    }

    private boolean canPlaceInsectHere(Tecton_Class tecton) {
        // Example: only allow if no insect present
        return tecton.get_InsectsOnTecton().isEmpty();
    }

    private void endTurn() {
        gameModel.turn();
        currentPlayerLabel.setText("Current player: " + getCurrentPlayerName() + " (" + getCurrentPlayerRole() + ")");
        updateInventoryVisibility();
        GameCanvas.getInstance().repaint();
    }

    private String getCurrentPlayerRole() {
        var player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        var role = player.getRole();
        if (role == RoleType.MUSHROOM) return "Mushroom";
        if (role == RoleType.INSECT) return "Insect";
        return "None";
    }

    private String getCurrentPlayerName() {
        int id = gameModel.currentTurnsPlayer();
        var p = gameModel.getPlayer(id);
        return p != null ? p.getName() : "?";
    }

    private void updateInventoryVisibility() {
        // Update entity selection panel at the bottom with current player's options
        updateInfoPanels();
    }

    private String getTopInfoText() {
        return "Current player: " + getCurrentPlayerName() + " (" + getCurrentPlayerRole() + ")";
    }

    private void updateInfoPanels() {
        // Update top info
        currentPlayerLabel.setText(getTopInfoText());
        var player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
                boolean enabled = player.getAction() > 0;
        entityPanel.setEnabled(enabled);
        for (var comp : entityPanel.getComponents()) {
            comp.setEnabled(enabled);
        }
        entityPanel.repaint();
        scoreValueLabel.setText(String.valueOf(player.getScore()));
        actionsValueLabel.setText(String.valueOf(player.getAction()));
        // Update entityPanel: show 5 nice boxes with icons for current role
        entityPanel.removeAll();
        String[] types;
        String imagePrefix;
        boolean isMushroom = player.getRole() == RoleType.MUSHROOM;
        if (isMushroom) {
            types = mushroomTypes;
            imagePrefix = "Mushroom_";
        } else {
            types = insectTypes;
            imagePrefix = "Insect_";
        }
        for (int i = 0; i < 5; i++) {
            // Create a final copy of the index to use in the anonymous inner class
            final int index = i;
            JPanel box = new JPanel() {
                @Override
                protected void paintComponent(java.awt.Graphics g) {
                    super.paintComponent(g);
                    g.setColor(index == selectedEntityIndex ? new java.awt.Color(220,240,255,240) : new java.awt.Color(240,240,240,220));
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                }
            };
            box.setLayout(null);
            box.setBounds(i*110, 0, 100, 100);
            box.setOpaque(false);
            box.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(index == selectedEntityIndex ? new java.awt.Color(60,180,255) : new java.awt.Color(180,180,180), index == selectedEntityIndex ? 4 : 2, true),
                javax.swing.BorderFactory.createEmptyBorder(6,6,6,6)
            ));
            try {
                String iconName = imagePrefix + types[i] + ".png";
                java.awt.Image img = javax.imageio.ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/" + iconName));
                if (img != null) {
                    javax.swing.JLabel iconLabel = new javax.swing.JLabel(new javax.swing.ImageIcon(img.getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH)));
                    iconLabel.setBounds(18, 10, 64, 64);
                    box.add(iconLabel);
                }
            } catch (Exception ignored) {}
            int idx = i;
            box.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectedEntityIndex = idx;
                    updateInfoPanels();
                }
            });
            entityPanel.add(box);
        }
        entityPanel.repaint();
    }
}
