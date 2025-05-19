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
import javax.swing.SwingUtilities;

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
    private JLabel currencyLabel;
    private JLabel currencyValueLabel;
    private JLabel actionsLabel;
    private JLabel actionsValueLabel;
    private JPanel bottomPanel;
    private JPanel entityPanel;
    private javax.swing.JLayeredPane layeredPane;
    private int selectedEntityIndex = -1;
    private String[] mushroomTypes = {"Shroomlet", "Maximus", "Slender"};
    private String[] insectTypes = {"Buglet", "Buggernaut", "Stinger", "Tektonizator", "ShroomReaper"};

    private JButton moveInsectButton;
        private enum InteractionState { NORMAL, SELECTING_INSECT, SELECTING_DESTINATION }
        private InteractionState currentState = InteractionState.NORMAL;
        private Insect_Class selectedInsect = null;

    /**
     * Constructs a new game window for two players.
     * @param player1 Name of player 1
     * @param player2 Name of player 2
     */
    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        this.gameModel = new Game(player1, player2);
        initGame();
    }

    private void initGame() {
        // Initialize game state here if needed
        gameModel.initGame();
        gameModel.startGame();
        buildUI();
        
        updateInventoryVisibility();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        new Timer(40, e -> GameCanvas.getInstance().repaint()).start();
        
        // Remove the premature RoleChoose call:
        // RoleChoose(); 
        
        // Instead, do a single role selection when the game is fully loaded and visible
        // This ensures the game UI is ready before showing dialogs
        SwingUtilities.invokeLater(() -> {
            Player firstPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
            showRoleSelectionDialog(firstPlayer);
            updateInventoryVisibility();
            GameCanvas.getInstance().repaint();
            currentPlayerLabel.setText(getTopInfoText());
        });
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
        currencyLabel = new JLabel("CURRENCY:");
        currencyLabel.setForeground(java.awt.Color.WHITE);
        currencyLabel.setBounds(400, 5, 80, 30);
        topInfoPanel.add(currencyLabel);
        currencyValueLabel = new JLabel("80");
        currencyValueLabel.setForeground(java.awt.Color.YELLOW);
        topInfoPanel.add(currencyValueLabel);
        currencyValueLabel.setBounds(480, 5, 50, 30);
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

        // Move Insect button: positioned at the left side of the bottom panel
        moveInsectButton = new JButton();
        moveInsectButton.setFocusable(false);
        moveInsectButton.setBorderPainted(false);
        moveInsectButton.setContentAreaFilled(false);
        moveInsectButton.setFocusPainted(false);
        moveInsectButton.setMargin(new java.awt.Insets(0,0,0,0));
        moveInsectButton.setOpaque(false);
        moveInsectButton.setToolTipText("Move an insect");
        try {
            Image img = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/moveInsectButton.png"));
            if (img != null) {
                moveInsectButton.setIcon(new ImageIcon(img.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
            } else {
                // Fallback if image not found
                moveInsectButton.setText("Move");
            }
        } catch (IOException | IllegalArgumentException e) {
            moveInsectButton.setText("Move");
        }
moveInsectButton.addActionListener(e -> startInsectMovement());
moveInsectButton.setBounds(10, 10, 80, 80); // New position to avoid overlap
bottomPanel.add(moveInsectButton);
        
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
    switch (currentState) {
        case SELECTING_INSECT:
            handleInsectSelection(x, y);
            break;
            
case SELECTING_DESTINATION:
    // Find which tecton was clicked
    for (var t : gameModel.getPlane().TectonCollection) {
        int tx = t.getPosition().x, ty = t.getPosition().y;
        double dist = Math.hypot(x - tx, y - ty);
        if (dist < 40) { // hex radius
            handleDestinationSelection(t);
            break; // Now properly inside the if block
        }
    }
    break;
            
        case NORMAL:
        default:
            // Normal behavior - for placing entities
            for (var t : gameModel.getPlane().TectonCollection) {
                int tx = t.getPosition().x, ty = t.getPosition().y;
                double dist = Math.hypot(x - tx, y - ty);
                if (dist < 40) { // hex radius
                    onTectonClicked(t);
                    break;
                }
            }
            break;
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
                //case "Grand": mush = new Mushroom_Grand(tecton, player); break;
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
                player.setAction(player.getAction() - 1); // Reduce action points
                updateInfoPanels();
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
        // Example: If there are 5 or more insects on the tecton, don't allow placement
        if(tecton.get_InsectsOnTecton().size() <5)
        {
            return true;
        }
        return false;
    }

    private void endTurn() {
        gameModel.turn();


        RoleChoose();

        currentPlayerLabel.setText("Current player: " + getCurrentPlayerName() + " (" + getCurrentPlayerRole() + ")");
        updateInventoryVisibility();
        GameCanvas.getInstance().repaint();
    }

    private void RoleChoose() {
        Player nextPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        showRoleSelectionDialog(nextPlayer);

        currentPlayerLabel.setText("Current player: " + getCurrentPlayerName() + " (" + getCurrentPlayerRole() + ")");
        updateInventoryVisibility();
        GameCanvas.getInstance().repaint();
    }

    /**
 * Shows the role selection dialog for the specified player
 * @param player The player who will select their role
 */
private void showRoleSelectionDialog(Player player) {
    String[] roles = { "Gombász", "Rovarász" };
    int choice = JOptionPane.showOptionDialog(
        this,
        player.getName() + ", válassz szerepet a következő körre:",
        "Szerepválasztás",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        roles,
        roles[0]
    );
    if (choice == 0) {
        player.setRoleMushroom();
    } else if (choice == 1) {
        player.setRoleInsect();
    }
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

        Player player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        moveInsectButton.setVisible(player.getRole() == RoleType.INSECT);
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
        int length  = 0;
        if (isMushroom) {
            types = mushroomTypes;
            imagePrefix = "Mushroom_";
            length = types.length;
        } else {
            types = insectTypes;
            imagePrefix = "Insect_";
            length = types.length;
        }
        for (int i = 0; i < length; i++) {
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

    private void startInsectMovement() {
    Player player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
    
    // Check if current player has insect role
    if (player.getRole() != RoleType.INSECT) {
        JOptionPane.showMessageDialog(this, "You must have the Insect role to move insects.", 
                                      "Invalid Role", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Check if player has remaining actions
    if (player.getAction() <= 0) {
        JOptionPane.showMessageDialog(this, "No action points left!", 
                                      "No Actions", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Change to insect selection state
    currentState = InteractionState.SELECTING_INSECT;
    updateMoveInsectButtonAppearance(); // Add this line
    JOptionPane.showMessageDialog(this, "Click on an insect to move", 
                               "Select Insect", JOptionPane.INFORMATION_MESSAGE);
}

/**
 * Handles insect selection when in SELECTING_INSECT state
 * @param x Click X coordinate
 * @param y Click Y coordinate
 * @return true if an insect was selected
 */
private boolean handleInsectSelection(int x, int y) {
    Player player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
    
    // Check all tectons for insects
    for (var tecton : gameModel.getPlane().TectonCollection) {
        // Calculate distance from click to tecton center
        int tx = tecton.getPosition().x, ty = tecton.getPosition().y;
        double dist = Math.hypot(x - tx, y - ty);
        
        // If clicked near this tecton, check its insects
        if (dist < 60) { // Approximate hex radius
            // Check if the tecton has insects
            if (tecton.get_InsectsOnTecton() == null || tecton.get_InsectsOnTecton().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No insects on this tecton.", 
                                             "No Insects", JOptionPane.WARNING_MESSAGE);
                currentState = InteractionState.NORMAL; // Reset state
                updateMoveInsectButtonAppearance(); // Add this line
                return false;
            }
            
            // For now, just select the first insect owned by current player
            for (Insect_Class insect : tecton.get_InsectsOnTecton()) {
                if (insect.get_Owner() != null && insect.get_Owner().getId() == player.getId()) {
                    // Check if insect is paralyzed
                    if (insect.get_isParalysed()) {
                        JOptionPane.showMessageDialog(this, "This insect is paralyzed and cannot move.", 
                                                    "Insect Paralyzed", JOptionPane.WARNING_MESSAGE);
                        currentState = InteractionState.NORMAL; // Reset state
                        updateMoveInsectButtonAppearance(); // Add this line
                        return false;
                    }
                    
                    // Check if insect has steps left
                    if (insect.get_availableSteps() <= 0) {
                        JOptionPane.showMessageDialog(this, "This insect has no more steps available.", 
                                                    "No Steps", JOptionPane.WARNING_MESSAGE);
                        currentState = InteractionState.NORMAL; // Reset state
                        updateMoveInsectButtonAppearance(); // Add this line
                        return false;
                    }
                    
                    // Select this insect
                    selectedInsect = insect;
                    currentState = InteractionState.SELECTING_DESTINATION;
                    updateMoveInsectButtonAppearance(); // Add this line
                    JOptionPane.showMessageDialog(this, "Now click on a destination tecton.", 
                                                "Select Destination", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }
            }
            
            JOptionPane.showMessageDialog(this, "No insects owned by you on this tecton.", 
                                         "No Valid Insects", JOptionPane.WARNING_MESSAGE);
            currentState = InteractionState.NORMAL; // Reset state
            updateMoveInsectButtonAppearance(); // Add this line
            return false;
        }
    }
    
    JOptionPane.showMessageDialog(this, "No tecton selected. Click closer to a tecton with insects.", 
                                 "No Selection", JOptionPane.WARNING_MESSAGE);
    currentState = InteractionState.NORMAL; // Reset state
    updateMoveInsectButtonAppearance(); // Add this line
    return false;
}

/**
 * Handles destination selection when in SELECTING_DESTINATION state
 * @param destinationTecton The target tecton
 * @return true if the move was successful
 */
private boolean handleDestinationSelection(Tecton_Class destinationTecton) {
    Player player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
    
    // Make sure we have a selected insect
    if (selectedInsect == null) {
        JOptionPane.showMessageDialog(this, "No insect selected. Please try again.", 
                                     "Error", JOptionPane.ERROR_MESSAGE);
        currentState = InteractionState.NORMAL; // Reset state
        updateMoveInsectButtonAppearance(); // Add this line
        return false;
    }
    
    // Attempt to move the insect
    try {
        // Save the original tecton to check if the move was successful
        Tecton_Class originalTecton = selectedInsect.get_Tecton();
        
        // Call the move_Insect method from Plane
        gameModel.getPlane().move_Insect(player, selectedInsect, destinationTecton);
        
        // Check if the insect actually moved (its tecton reference changed)
        if (selectedInsect.get_Tecton() != originalTecton) {
            // Success!
            JOptionPane.showMessageDialog(this, "Insect moved successfully!", 
                                         "Success", JOptionPane.INFORMATION_MESSAGE);
            player.setAction(player.getAction() - 1); // Decrease action points
            updateInfoPanels(); // Update UI
            GameCanvas.getInstance().repaint(); // Redraw
            return true;
        } else {
            // Move didn't happen - the plane.move_Insect method should have shown an error already
            return false;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error moving insect: " + e.getMessage(), 
                                     "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    } finally {
        // Reset state
        selectedInsect = null;
        currentState = InteractionState.NORMAL;
        updateMoveInsectButtonAppearance(); // Add this line
    }
}

// Add this method to update the Move Insect button appearance based on current state
private void updateMoveInsectButtonAppearance() {
    boolean isMovingState = (currentState == InteractionState.SELECTING_INSECT || 
                            currentState == InteractionState.SELECTING_DESTINATION);
    
    if (isMovingState) {
        // Highlighted state - blue border like selected entities
        moveInsectButton.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60,180,255), 4, true),
            javax.swing.BorderFactory.createEmptyBorder(6,6,6,6)
        ));
    } else {
        // Normal state - no border
        moveInsectButton.setBorder(null);
    }
}
}
