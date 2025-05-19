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
    private JButton tectonCrackButton;
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
                moveInsectButton.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            } else {
                // Fallback if image not found
                moveInsectButton.setText("Move");
            }
        } catch (IOException | IllegalArgumentException e) {
            moveInsectButton.setText("Move");
        }
        tectonCrackButton = new JButton();
tectonCrackButton.setFocusable(false);
tectonCrackButton.setBorderPainted(false);
tectonCrackButton.setContentAreaFilled(false);
tectonCrackButton.setFocusPainted(false);
tectonCrackButton.setMargin(new java.awt.Insets(0,0,0,0));
tectonCrackButton.setOpaque(false);
tectonCrackButton.setToolTipText("Crack tecton with Tektonizator");
try {
    Image img = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/TectonCrackButton.png"));
    if (img != null) {
        tectonCrackButton.setIcon(new ImageIcon(img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
    } else {
        // Fallback if image not found
        tectonCrackButton.setText("Crack");
    }
} catch (IOException | IllegalArgumentException e) {
    tectonCrackButton.setText("Crack");
}
tectonCrackButton.addActionListener(e -> startTectonCrack());
moveInsectButton.addActionListener(e -> startInsectMovement());
// Adjust the positions and sizes to prevent overlap
moveInsectButton.setBounds(15, 5, 40, 40);  
tectonCrackButton.setBounds(15, 55, 40, 40);

bottomPanel.add(moveInsectButton);
bottomPanel.add(tectonCrackButton);
        
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
        if (dist < 40) // hex radius
        {
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
    
    // Check if we're in a special interaction state
    if (currentState == InteractionState.SELECTING_INSECT) {
        int tx = tecton.getPosition().x, ty = tecton.getPosition().y;
        handleInsectSelection(tx, ty);
        return;
    } else if (currentState == InteractionState.SELECTING_DESTINATION) {
        if (tectonCrackButton.isSelected()) {
            // Handle tecton cracking
            handleTectonCrack(tecton);
        } else {
            // Handle normal insect movement
            handleDestinationSelection(tecton);
        }
        return;
    }
        
        // Check if an entity is selected
        if (selectedEntityIndex < 0) {
            showStyledMessageDialog( "Please select an entity to place first!", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (isMushroomRole) {
            if (!canPlaceMushroomHere(tecton)) {
                showStyledMessageDialog( "Cannot place mushroom here!", "Invalid", JOptionPane.WARNING_MESSAGE);
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
                showStyledMessageDialog( "Cannot place insect here!", "Invalid", JOptionPane.WARNING_MESSAGE);
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
    boolean placementSuccessful = gameModel.getPlane().placeInsect(insect, tecton);
    if (placementSuccessful) {
        GameCanvas.getInstance().repaint();
        player.setAction(player.getAction() - 1); // Reduce action points only if successful
        updateInfoPanels();
    } else {
        // Show specific error message from plane's validation
        showStyledMessageDialog( 
            "Cannot place insect here. The location may be invalid or you lack resources.", 
            "Placement Failed", JOptionPane.WARNING_MESSAGE);
    }
}
        } else {
            showStyledMessageDialog( "Unknown player role!", "Error", JOptionPane.ERROR_MESSAGE);
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

    // Check if game is over after the turn
    if (gameModel.isGameOver()) {
        handleGameOver();
        return;
    }

    RoleChoose();

    currentPlayerLabel.setText("Current player: " + getCurrentPlayerName() + " (" + getCurrentPlayerRole() + ")");
    updateInventoryVisibility();
    GameCanvas.getInstance().repaint();
}

/**
 * Handles game over state: saves scores to leaderboard and shows end game dialog
 */
/**
 * Handles game over state: saves scores to leaderboard and shows end game dialog
 */
private void handleGameOver() {
    Player player1 = gameModel.getPlayer1();
    Player player2 = gameModel.getPlayer2();
    
    String winnerMessage;
    Player winner = null;
    boolean winnerDestroyedOpponentBase = false; // This flag will be true for the winner ONLY if they won by base destruction

    // 1. Check for base destruction first (automatic victory)
    if (gameModel.getPlane().getBase1().isDead()) { // Player 1's base is dead
        winner = player2; // Player 2 wins
        winnerMessage = player2.getName() + " wins by destroying " + player1.getName() + "'s base!";
        winnerDestroyedOpponentBase = true; // Player 2 (the winner) won by destroying a base
    } else if (gameModel.getPlane().getBase2().isDead()) { // Player 2's base is dead
        winner = player1; // Player 1 wins
        winnerMessage = player1.getName() + " wins by destroying " + player2.getName() + "'s base!";
        winnerDestroyedOpponentBase = true; // Player 1 (the winner) won by destroying a base
    } 
    // 2. If no base was destroyed, determine winner by points
    else if (player1.getScore() > player2.getScore()) {
        winner = player1;
        winnerMessage = player1.getName() + " wins with " + player1.getScore() + " points!";
        // winnerDestroyedOpponentBase remains false because it's a score-based win
    } else if (player2.getScore() > player1.getScore()) {
        winner = player2;
        winnerMessage = player2.getName() + " wins with " + player2.getScore() + " points!";
        // winnerDestroyedOpponentBase remains false because it's a score-based win
    } 
    // 3. If no base destroyed and scores are equal, it's a tie
    else {
        winnerMessage = "It's a tie! Both players have " + player1.getScore() + " points.";
        // winner is null, winnerDestroyedOpponentBase remains false
    }
    
    // Save to leaderboard
    if (winner != null) {
        // For the winner, save their score and whether they won by destroying a base
        saveToLeaderboard(winner.getName(), winner.getScore(), winnerDestroyedOpponentBase);
        // For the loser, they did not win by destroying a base, so the flag is false
        Player loser = (winner == player1) ? player2 : player1;
        saveToLeaderboard(loser.getName(), loser.getScore(), false);
    } else {
        // It's a tie, so for both players, the flag for destroying a base is false
        saveToLeaderboard(player1.getName(), player1.getScore(), false);
        saveToLeaderboard(player2.getName(), player2.getScore(), false);
    }
    
    // Show game over dialog
    String message = "Game Over!\n\n" + winnerMessage + 
                    "\n\nPlayer 1: " + player1.getScore() + " points" +
                    "\nPlayer 2: " + player2.getScore() + " points";
    
    String[] options = {"Back to Main Menu", "Exit Game"};
    int choice = showStyledOptionDialog(
        message,
        "Game Over",
        options
    );
    
    if (choice == 0) {
        // Return to main menu
        dispose();
        try {
            SoundManager.stopMusic();
        } catch (Exception e) {
            // Ignore if sound manager isn't available
        }
        new MainMenu();
    } else {
        // Exit game
        System.exit(0);
    }
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
    int choice = showStyledOptionDialog(
        player.getName() + ", válassz szerepet a következő körre:",
        "Szerepválasztás",
        roles
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
    boolean isInsectRole = player.getRole() == RoleType.INSECT;
    moveInsectButton.setVisible(isInsectRole);
    tectonCrackButton.setVisible(isInsectRole);
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
        showStyledMessageDialog( "You must have the Insect role to move insects.", 
                                      "Invalid Role", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Check if player has remaining actions
    if (player.getAction() <= 0) {
        showStyledMessageDialog( "No action points left!", 
                                      "No Actions", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Change to insect selection state
    currentState = InteractionState.SELECTING_INSECT;
    updateMoveInsectButtonAppearance(); // Add this line
    showStyledMessageDialog( "Click on an insect to move", 
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
        int tx = tecton.getPosition().x, ty = tecton.getPosition().y;
        double dist = Math.hypot(x - tx, y - ty);
        
        if (dist < 60) { // Approximate hex radius
            // Check if the tecton has insects
            if (tecton.get_InsectsOnTecton() == null || tecton.get_InsectsOnTecton().isEmpty()) {
                showStyledMessageDialog("No insects on this tecton.", 
                                       "No Insects", JOptionPane.WARNING_MESSAGE);
                currentState = InteractionState.NORMAL;
                updateMoveInsectButtonAppearance();
                return false;
            }
            
            // Show insect selection dialog if there are multiple insects
            if (tecton.get_InsectsOnTecton().size() > 1) {
                String[] insectNames = new String[tecton.get_InsectsOnTecton().size()];
                Insect_Class[] insects = new Insect_Class[tecton.get_InsectsOnTecton().size()];
                int idx = 0;
                
                for (var insect : tecton.get_InsectsOnTecton()) {
                    String typeName = insect.getClass().getSimpleName().replace("Insect_", "");
                    insectNames[idx] = typeName;
                    insects[idx] = insect;
                    idx++;
                }
                
                int choice = showStyledOptionDialog(
                    "Select an insect to use",
                    "Insect Selection",
                    insectNames
                );
                
                if (choice >= 0 && choice < insects.length) {
                    Insect_Class insect = insects[choice];
                    
                    // Check if the insect belongs to the current player
                    if (insect.get_Owner() != null && insect.get_Owner().getId() == player.getId()) {
                        selectedInsect = insect;
                        
if (currentState == InteractionState.SELECTING_INSECT) {
    // If we're selecting for movement
    currentState = InteractionState.SELECTING_DESTINATION;
    updateMoveInsectButtonAppearance();
    showStyledMessageDialog("Now click on a destination tecton.", 
                         "Select Destination", JOptionPane.INFORMATION_MESSAGE);
} else {
    // Just selecting without any operation
    String typeName = insect.getClass().getSimpleName().replace("Insect_", "");
    showStyledMessageDialog("Selected " + typeName + ".", 
                         "Selected", JOptionPane.INFORMATION_MESSAGE);
    currentState = InteractionState.NORMAL;
}
                        return true;
                    } else {
                        showStyledMessageDialog("You can only select your own insects.", 
                                             "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                } else {
                    // User canceled the selection
                    return false;
                }
            } else {
                // Single insect case
                Insect_Class insect = tecton.get_InsectsOnTecton().get(0);
                
                // Check if the insect belongs to the current player
                if (insect.get_Owner() != null && insect.get_Owner().getId() == player.getId()) {
                    selectedInsect = insect;
                    String typeName = insect.getClass().getSimpleName().replace("Insect_", "");
                    
                    // Special handling for Tektonizator when tectonCrack button is selected
                    if (tectonCrackButton.isSelected() && insect instanceof Insect_Tektonizator) {
                        // Immediately execute crack on the current tecton
                        executeTectonCrack((Insect_Tektonizator)insect, insect.get_Tecton());
                        currentState = InteractionState.NORMAL;
                        tectonCrackButton.setSelected(false);
                        updateMoveInsectButtonAppearance();
                        return true;
                    }
                    
                    if (currentState == InteractionState.SELECTING_INSECT) {
                        // If we're selecting for movement
                        currentState = InteractionState.SELECTING_DESTINATION;
                        updateMoveInsectButtonAppearance();
                        showStyledMessageDialog("Now click on a destination tecton.", 
                                             "Select Destination", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Just selecting without any operation
                        showStyledMessageDialog("Selected " + typeName + ".", 
                                             "Selected", JOptionPane.INFORMATION_MESSAGE);
                        currentState = InteractionState.NORMAL;
                    }
                    return true;
                } else {
                    showStyledMessageDialog("You can only select your own insects.", 
                                         "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }
    }
    
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
        showStyledMessageDialog( "No insect selected. Please try again.", 
                                     "Error", JOptionPane.ERROR_MESSAGE);
        currentState = InteractionState.NORMAL;
        updateMoveInsectButtonAppearance();
        return false;
    }
    
    // Get the original tecton and validate movement conditions before attempting to move
    Tecton_Class originalTecton = selectedInsect.get_Tecton();
    
    // Check if destination is a neighbor of the current tecton
    boolean isNeighbor = false;
    for (Tecton_Class neighbor : originalTecton.get_TectonNeighbours()) {
        if (neighbor == destinationTecton) {
            isNeighbor = true;
            break;
        }
    }
    
    if (!isNeighbor) {
        showStyledMessageDialog( 
            "Cannot move to the selected location. It's not adjacent to the insect's current position.", 
            "Invalid Move", JOptionPane.WARNING_MESSAGE);
        currentState = InteractionState.NORMAL;
        selectedInsect = null;
        updateMoveInsectButtonAppearance();
        return false;
    }
    
    // Check if there's a thread on the destination tecton
    if (destinationTecton.get_Thread() == null) {
        showStyledMessageDialog( 
            "Cannot move to the selected tecton. There is no thread for the insect to walk on.", 
            "Invalid Move", JOptionPane.WARNING_MESSAGE);
        currentState = InteractionState.NORMAL;
        selectedInsect = null;
        updateMoveInsectButtonAppearance();
        return false;
    }
    
    try {
        // Call the move_Insect method from Plane
        gameModel.getPlane().move_Insect(player, selectedInsect, destinationTecton);
        
        // Check if the insect actually moved (its tecton reference changed)
        if (selectedInsect.get_Tecton() != originalTecton) {
            // Success! No message box needed per requirement
            player.setAction(player.getAction() - 1); // Decrease action points
            updateInfoPanels(); // Update UI
            GameCanvas.getInstance().repaint(); // Redraw
            return true;
        } else {
            // Move didn't happen but none of our checks caught it
            // This could be due to other validation in the move_Insect method
            showStyledMessageDialog( 
                "Could not move insect. The move may be invalid for gameplay reasons.", 
                "Move Failed", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    } catch (Exception e) {
        // Handle any exceptions from the move_Insect method
        showStyledMessageDialog( 
            "Error moving insect: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    } finally {
        // Reset state
        selectedInsect = null;
        currentState = InteractionState.NORMAL;
        updateMoveInsectButtonAppearance();
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
    /**
 * Saves a player's score to the leaderboard file
 */
private void saveToLeaderboard(String playerName, int score, boolean destroyedBase) {
    String appDir = System.getProperty("user.home") + java.io.File.separator + ".fungorium";
    String dataFile = appDir + java.io.File.separator + "leaderboard.txt";
    
    // Make sure the directory exists
    new java.io.File(appDir).mkdirs();
    
    try {
        // Append to the file
        java.io.FileWriter fw = new java.io.FileWriter(dataFile, true);
        try (java.io.PrintWriter pw = new java.io.PrintWriter(fw)) {
            pw.printf("%s,%d,%b%n", playerName, score, destroyedBase);
        }
    } catch (java.io.IOException e) {
        showStyledMessageDialog(
            "Could not save score to leaderboard: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
private void startTectonCrack() {
    Player player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
    
    // Check if current player has insect role
    if (player.getRole() != RoleType.INSECT) {
        showStyledMessageDialog("You must have the Insect role to crack tectons.", 
                               "Invalid Role", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Check if player has remaining actions
    if (player.getAction() <= 0) {
        showStyledMessageDialog("No action points left!", 
                               "No Actions", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Set button as selected for visual feedback
    tectonCrackButton.setSelected(true);
    
    // Change to insect selection state to let user pick a Tektonizator
    currentState = InteractionState.SELECTING_INSECT;
    updateMoveInsectButtonAppearance();
    showStyledMessageDialog("Click on a Tektonizator insect to crack its tecton.", 
                           "Select Tektonizator", JOptionPane.INFORMATION_MESSAGE);
}

/**
 * Executes the tecton cracking operation
 * @param tektonizator The Tektonizator insect to use
 * @param targetTecton The tecton to crack
 */
private void executeTectonCrack(Insect_Tektonizator tektonizator, Tecton_Class targetTecton) {
    Player player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
    
    try {
        // Check if the target is a Base tecton
        if (targetTecton instanceof Tecton_Base) {
            // Crack the base and end the game
            tektonizator.tectonCrack();
            
            // Set game over state
            gameModel.setGameOver(true);
            
            // Handle game over
            handleGameOver();
            return;
        } 
        
        // Check if the target can be cracked
        if (!targetTecton.canBeCracked()) {
            String reason = "";
            if (targetTecton instanceof Tecton_Dead) {
                reason = "Dead tectons cannot be cracked.";
            } else {
                reason = "This tecton cannot be cracked.";
            }
            
            showStyledMessageDialog(reason, "Invalid Target", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Execute the crack operation
        tektonizator.tectonCrack();
        
        // Reduce action points
        player.setAction(player.getAction() - 1);
        
        // Clear selection
        selectedInsect = null;
        
        // Update UI
        GameCanvas.getInstance().repaint();
        updateInfoPanels();
        
        // Show success message
        showStyledMessageDialog("Tecton cracked successfully!", 
                             "Success", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (Exception e) {
        showStyledMessageDialog("Failed to crack tecton: " + e.getMessage(),
                             "Error", JOptionPane.ERROR_MESSAGE);
    }
}
/**
 * Handles tecton cracking using a selected Tektonizator
 * @param targetTecton The tecton to crack
 * @return true if cracking was successful
 */
/**
 * Handles tecton cracking using a selected Tektonizator
 * @param targetTecton The tecton to crack
 * @return true if cracking was successful
 */
private boolean handleTectonCrack(Tecton_Class targetTecton) {
    Player player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
    
    // Make sure we have a selected insect
    if (selectedInsect == null || !(selectedInsect instanceof Insect_Tektonizator)) {
        showStyledMessageDialog("No Tektonizator selected. Please try again.", 
                               "Error", JOptionPane.ERROR_MESSAGE);
        currentState = InteractionState.NORMAL;
        tectonCrackButton.setSelected(false);
        updateMoveInsectButtonAppearance();
        return false;
    }
    
    // Check if the target tecton can be cracked
    if (!targetTecton.canBeCracked()) {
        if (targetTecton instanceof Tecton_Base) {
            // Base tectons are a special case - they can be cracked to end the game
            Insect_Tektonizator tektonizator = (Insect_Tektonizator) selectedInsect;
            
            // Move the tektonizator to the target tecton if it's not already there
            if (tektonizator.get_Tecton() != targetTecton) {
                if (!tektonizator.get_Tecton().get_TectonNeighbours().contains(targetTecton)) {
                    showStyledMessageDialog("The tektonizator must be on or adjacent to the target base.",
                                         "Invalid Target", JOptionPane.WARNING_MESSAGE);
                    currentState = InteractionState.NORMAL;
                    tectonCrackButton.setSelected(false);
                    updateMoveInsectButtonAppearance();
                    return false;
                }
                
                // Move the tektonizator to the target tecton
                gameModel.getPlane().move_Insect(player, tektonizator, targetTecton);
            }
            
            // Confirm destruction of base
            int choice = showStyledOptionDialog(
                "Are you sure you want to destroy this base? This will end the game.",
                "Confirm Base Destruction",
                new String[] {"Yes, destroy base", "No, cancel"}
            );
            
            if (choice != 0) {
                // User canceled
                currentState = InteractionState.NORMAL;
                tectonCrackButton.setSelected(false);
                updateMoveInsectButtonAppearance();
                return false;
            }
            
            // Perform the tecton crack on base
            tektonizator.tectonCrack();
            
            // Game over is set in the tectonCrack method when cracking a base
            // Handle the game over state
            handleGameOver();
            return true;
        } else if (targetTecton instanceof Tecton_Dead) {
            showStyledMessageDialog("Dead tectons cannot be cracked.",
                                 "Invalid Target", JOptionPane.WARNING_MESSAGE);
        } else {
            showStyledMessageDialog("This tecton cannot be cracked.",
                                 "Invalid Target", JOptionPane.WARNING_MESSAGE);
        }
        currentState = InteractionState.NORMAL;
        tectonCrackButton.setSelected(false);
        updateMoveInsectButtonAppearance();
        return false;
    }
    
    // Perform the crack operation
    try {
        Insect_Tektonizator tektonizator = (Insect_Tektonizator) selectedInsect;
        
        // Move the tektonizator to the target tecton if it's not already there
        if (tektonizator.get_Tecton() != targetTecton) {
            if (!tektonizator.get_Tecton().get_TectonNeighbours().contains(targetTecton)) {
                showStyledMessageDialog("The tektonizator must be on or adjacent to the target tecton.",
                                     "Invalid Target", JOptionPane.WARNING_MESSAGE);
                currentState = InteractionState.NORMAL;
                tectonCrackButton.setSelected(false);
                updateMoveInsectButtonAppearance();
                return false;
            }
            
            // Move the tektonizator to the target tecton
            gameModel.getPlane().move_Insect(player, tektonizator, targetTecton);
        }
        
        // Perform the tecton crack
        tektonizator.tectonCrack();
        
        // Reduce action points
        player.setAction(player.getAction() - 1);
        
        // Repaint and update UI
        GameCanvas.getInstance().repaint();
        updateInfoPanels();
        
        // Show success message
        showStyledMessageDialog("Tecton cracked successfully!", 
                             "Success", JOptionPane.INFORMATION_MESSAGE);
        
        currentState = InteractionState.NORMAL;
        tectonCrackButton.setSelected(false);
        updateMoveInsectButtonAppearance();
        return true;
    } catch (Exception e) {
        showStyledMessageDialog("Failed to crack tecton: " + e.getMessage(),
                             "Error", JOptionPane.ERROR_MESSAGE);
        currentState = InteractionState.NORMAL;
        tectonCrackButton.setSelected(false);
        updateMoveInsectButtonAppearance();
        return false;
    }
}
}
