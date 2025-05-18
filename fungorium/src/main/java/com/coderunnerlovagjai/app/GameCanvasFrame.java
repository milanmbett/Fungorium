// filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/main/java/com/coderunnerlovagjai/app/GameCanvasFrame.java
package com.coderunnerlovagjai.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.coderunnerlovagjai.app.controller.GameController;
import com.coderunnerlovagjai.app.view.GameBoardPanel;
import com.coderunnerlovagjai.app.view.TectonSelectionListener;

/**
 * GameCanvasFrame is the main game window, styled like FrameStyle.
 * It hosts the game drawing surface, player information, and game controls.
 */
public class GameCanvasFrame extends FrameStyle implements TectonSelectionListener {
    private GamePanel gamePanel;
    private Game gameModel;
    private GameController gameController;
    private Clip backgroundMusic;
    private JPanel playerInventoryPanel;
    private JButton endTurnBtn; // Added for easy access
    private Timer endTurnPulseTimer; // Timer for pulsing effect on end turn button
    
    // Selected item types for placement
    private String selectedMushroomType = null;
    private String selectedInsectType = null;

    /**
     * Constructs a new game window for two players.
     * @param player1 Name of player 1
     * @param player2 Name of player 2
     */
    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        
        // Stop main menu music if it's playing
        stopMainMenuMusic();
        
        // Initialize the game model
        gameModel = new Game(player1, player2);
        gameModel.initGame();
        
        // Call startGame to initialize and assign roles randomly
        gameModel.startGame();
        
        // Initialize the controller
        gameController = new GameController(gameModel);
        
        // Build the UI
        buildUI();
        
        // Register as a tecton selection listener
        if (gamePanel != null && gamePanel.boardPanel != null) {
            gamePanel.boardPanel.addTectonSelectionListener(this);
        }
        
        // Configure the window
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        
        // Start the game loop
        startGameLoop();
        // Play background music
        playBackgroundMusic();
    }

    /**
     * Builds the game canvas UI within the styled frame.
     */
    @Override
    protected void buildUI() {
        // Create player turn indicator panel at the top
        JPanel turnIndicatorPanel = createTurnIndicatorPanel();
        content.add(turnIndicatorPanel, BorderLayout.NORTH);

        gamePanel = new GamePanel();
        content.setLayout(new BorderLayout());
        content.add(gamePanel, BorderLayout.CENTER);

        // --- REMOVE the left panel (vertical end turn + inventory) ---
        // JPanel leftPanel = createEndTurnSidePanel();
        // content.add(leftPanel, BorderLayout.WEST);

        // --- Add new horizontal toolbar at the bottom ---
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.setBackground(new Color(40, 40, 50));
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Inventory panel (horizontal)
        playerInventoryPanel = new JPanel();
        playerInventoryPanel.setLayout(new BoxLayout(playerInventoryPanel, BoxLayout.X_AXIS));
        playerInventoryPanel.setOpaque(false);
        updateCurrentPlayerInventory();
        toolbarPanel.add(playerInventoryPanel);

        // Add glue to push the End Turn button to the right
        toolbarPanel.add(Box.createHorizontalGlue());

        // End Turn Button (styled horizontally)
        endTurnBtn = new JButton("<html><b>END TURN</b></html>");
        endTurnBtn.setFont(new Font("Arial", Font.BOLD, 16));
        endTurnBtn.setBackground(new Color(180, 20, 20));
        endTurnBtn.setForeground(Color.WHITE);
        endTurnBtn.setFocusPainted(false);
        endTurnBtn.setBorderPainted(false);
        endTurnBtn.setPreferredSize(new Dimension(120, 48));
        // Attach the correct action listener for ending turn
        endTurnBtn.addActionListener(e -> {
            Player currentPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
            boolean proceed = true;
            if (currentPlayer.getAction() > 0) {
                proceed = JOptionPane.showConfirmDialog(this, 
                    "You still have " + currentPlayer.getAction() + " action points left. End your turn anyway?",
                    "Confirm End Turn", 
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            }
            if (proceed) {
                // Log state before ending turn
                Player player1 = gameModel.getPlayer1();
                Player player2 = gameModel.getPlayer2();
                System.out.println("BEFORE END TURN - Current turn: " + gameModel.getTurnNumber());
                System.out.println("Player 1 role: " + player1.getRole().getRoleName() + 
                                 ", Actions: " + player1.getAction());
                System.out.println("Player 2 role: " + player2.getRole().getRoleName() + 
                                 ", Actions: " + player2.getAction());
                // End the turn - this will advance the turn and swap roles
                gameController.endTurn();
                // Log state after ending turn
                System.out.println("AFTER END TURN - Current turn: " + gameModel.getTurnNumber());
                System.out.println("Player 1 role: " + player1.getRole().getRoleName() + 
                                 ", Actions: " + player1.getAction());
                System.out.println("Player 2 role: " + player2.getRole().getRoleName() + 
                                 ", Actions: " + player2.getAction());
                // Update the UI to reflect the new state
                refreshUI(); 
            }
        });
        toolbarPanel.add(endTurnBtn);

        content.add(toolbarPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates a panel that displays whose turn it is at the top of the screen.
     */
    private JPanel createTurnIndicatorPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 40));
        panel.setPreferredSize(new Dimension(800, 40));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Create turn label with placeholder text - will be updated in refreshUI()
        JLabel turnLabel = new JLabel("Current Turn: Player 1", JLabel.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setName("turnIndicator"); // Set name for easy finding later
        
        panel.add(turnLabel);
        return panel;
    }
    
    /** Starts the continuous game loop on a separate thread. */
    private void startGameLoop() {
        Thread loop = new Thread(() -> {
            while (!gameModel.isGameOver()) {
                // Update and repaint in a loop
                repaint();
                try {
                    Thread.sleep(16); // ~60 FPS
                } catch (InterruptedException ignored) {}
            }
        });
        loop.setDaemon(true);
        loop.start();
    }

    /**
     * Updates the inventory panel with the current player's available items
     */
    private void updateCurrentPlayerInventory() {
        if (playerInventoryPanel == null) return;
        
        // Clear existing components
        playerInventoryPanel.removeAll();
        
        // Get current player
        Player currentPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        
        // Log current player and role info for debugging
        System.out.println("Current player: " + currentPlayer.getId() + 
                          ", Role: " + currentPlayer.getRole().getRoleName() + 
                          ", Actions: " + currentPlayer.getAction());
        
        // Get player roles for debugging
        Player player1 = gameModel.getPlayer1();
        Player player2 = gameModel.getPlayer2();
        System.out.println("Player 1 role: " + player1.getRole().getRoleName() + 
                          ", Player 2 role: " + player2.getRole().getRoleName());
        
        // Update end turn button state based on action points
        if (endTurnBtn != null) {
            // Highlight the button when player has no actions left
            if (currentPlayer.getAction() <= 0) {
                endTurnBtn.setBackground(new Color(220, 50, 50)); // Brighter red to indicate ready to end turn
                endTurnBtn.setForeground(new Color(255, 255, 255));
                endTurnBtn.setToolTipText("Click to end your turn");
                // Make the button pulse to draw attention
                if (endTurnPulseTimer == null) {
                    endTurnPulseTimer = new Timer(500, e -> {
                        if (endTurnBtn.getBackground().getRed() > 200) {
                            endTurnBtn.setBackground(new Color(180, 30, 30));
                        } else {
                            endTurnBtn.setBackground(new Color(220, 50, 50));
                        }
                    });
                    endTurnPulseTimer.start();
                }
            } else {
                // Stop pulsing if it was active
                if (endTurnPulseTimer != null && endTurnPulseTimer.isRunning()) {
                    endTurnPulseTimer.stop();
                    endTurnPulseTimer = null;
                }
                endTurnBtn.setBackground(new Color(180, 20, 20)); // Default color
                endTurnBtn.setForeground(Color.WHITE);
                endTurnBtn.setToolTipText("You still have " + currentPlayer.getAction() + " actions remaining");
            }
        }
        
        // Create title for inventory
        JLabel titleLabel = new JLabel("INVENTORY");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerInventoryPanel.add(titleLabel);
        playerInventoryPanel.add(Box.createVerticalStrut(10));
        
        // Display current role
        JLabel roleLabel = new JLabel("Role: " + currentPlayer.getRole().getRoleName());
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerInventoryPanel.add(roleLabel);
        playerInventoryPanel.add(Box.createVerticalStrut(5));
        
        // Show action points
        JLabel actionLabel = new JLabel("Actions: " + currentPlayer.getAction());
        actionLabel.setForeground(Color.WHITE);
        actionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerInventoryPanel.add(actionLabel);
        playerInventoryPanel.add(Box.createVerticalStrut(10));
        
        // Add items based on role
        if (currentPlayer.getRole() == RoleType.MUSHROOM) {
            // Mushroom types that can be placed
            JButton shroomletBtn = createInventoryButton("Shroomlet");
            shroomletBtn.addActionListener(e -> {
                selectedMushroomType = "Shroomlet";
                selectedInsectType = null; // Clear any insect selection
                JOptionPane.showMessageDialog(this, 
                    "Shroomlet mushroom selected. Click on a tecton to place it.",
                    "Item Selected", JOptionPane.INFORMATION_MESSAGE);
            });
            playerInventoryPanel.add(shroomletBtn);
            playerInventoryPanel.add(Box.createVerticalStrut(5));
            
            JButton slenderBtn = createInventoryButton("Slender");
            slenderBtn.addActionListener(e -> {
                selectedMushroomType = "Slender";
                selectedInsectType = null; // Clear any insect selection
                JOptionPane.showMessageDialog(this, 
                    "Slender mushroom selected. Click on a tecton to place it.",
                    "Item Selected", JOptionPane.INFORMATION_MESSAGE);
            });
            playerInventoryPanel.add(slenderBtn);
            playerInventoryPanel.add(Box.createVerticalStrut(5));
            
            JButton maximusBtn = createInventoryButton("Maximus");
            maximusBtn.addActionListener(e -> {
                selectedMushroomType = "Maximus";
                selectedInsectType = null; // Clear any insect selection
                JOptionPane.showMessageDialog(this, 
                    "Maximus mushroom selected. Click on a tecton to place it.",
                    "Item Selected", JOptionPane.INFORMATION_MESSAGE);
            });
            playerInventoryPanel.add(maximusBtn);
            
        } else if (currentPlayer.getRole() == RoleType.INSECT) {
            // Insect types that can be placed
            JButton bugletBtn = createInventoryButton("Buglet");
            bugletBtn.addActionListener(e -> {
                selectedInsectType = "Buglet";
                selectedMushroomType = null; // Clear any mushroom selection
                JOptionPane.showMessageDialog(this, 
                    "Buglet insect selected. Click on a tecton to place it.",
                    "Item Selected", JOptionPane.INFORMATION_MESSAGE);
            });
            playerInventoryPanel.add(bugletBtn);
            playerInventoryPanel.add(Box.createVerticalStrut(5));
            
            JButton buggernautBtn = createInventoryButton("Buggernaut");
            buggernautBtn.addActionListener(e -> {
                selectedInsectType = "Buggernaut";
                selectedMushroomType = null; // Clear any mushroom selection
                JOptionPane.showMessageDialog(this, 
                    "Buggernaut insect selected. Click on a tecton to place it.",
                    "Item Selected", JOptionPane.INFORMATION_MESSAGE);
            });
            playerInventoryPanel.add(buggernautBtn);
            playerInventoryPanel.add(Box.createVerticalStrut(5));
            
            JButton reaperBtn = createInventoryButton("Shroom Reaper");
            reaperBtn.addActionListener(e -> {
                selectedInsectType = "ShroomReaper";
                selectedMushroomType = null; // Clear any mushroom selection
                JOptionPane.showMessageDialog(this, 
                    "Shroom Reaper insect selected. Click on a tecton to place it.",
                    "Item Selected", JOptionPane.INFORMATION_MESSAGE);
            });
            playerInventoryPanel.add(reaperBtn);
        }
        
        // Refresh the panel
        playerInventoryPanel.revalidate();
        playerInventoryPanel.repaint();
    }
    
    /**
     * Creates a styled button for the inventory panel
     */
    private JButton createInventoryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(60, 60, 70));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 120)));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(150, 30));
        return button;
    }
    
    private void playBackgroundMusic() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/ingame.mp3");
            if (audioSrc == null) {
                System.err.println("Could not find background music file: /sounds/ingame.mp3");
                return;
            }
            
            // We need to buffer the InputStream because mark/reset is not supported by the default stream from getResourceAsStream
            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

            // Set volume to 20%
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range) + gainControl.getMinimum();
            gainControl.setValue(gain);

            // Stop existing main menu music
            stopMainMenuMusic();
            
            // Play new game music
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Game background music started at 20% volume");
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Stops the main menu music if it's currently playing
     */
    private void stopMainMenuMusic() {
        try {
            // This is a hack to access the MainMenu instance and stop its music
            // Find all frames and look for MainMenu
            for (Frame frame : Frame.getFrames()) {
                if (frame instanceof MainMenu) {
                    MainMenu menu = (MainMenu) frame;
                    // Use reflection to access private fields
                    try {
                        Field soundOnField = MainMenu.class.getDeclaredField("soundOn");
                        soundOnField.setAccessible(true);
                        soundOnField.set(menu, false);
                        
                        Field playerField = MainMenu.class.getDeclaredField("player");
                        playerField.setAccessible(true);
                        Object player = playerField.get(menu);
                        if (player != null) {
                            // Call close method using reflection
                            Method closeMethod = player.getClass().getMethod("close");
                            closeMethod.invoke(player);
                        }
                    } catch (Exception e) {
                        System.err.println("Error stopping main menu music: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding MainMenu frame: " + e.getMessage());
        }
    }

    /**
     * Handles tecton selection events from the game board
     */
    @Override
    public void onTectonSelected(Tecton_Class tecton) {
        // Handle the tecton selection for item placement
        handleTectonSelection(tecton);
    }
    
    /**
     * Handles tecton selection and item placement
     */
    public void handleTectonSelection(Tecton_Class tecton) {
        if (tecton == null) return;
        
        Player currentPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        
        // Check if player has enough action points
        if (currentPlayer.getAction() <= 0) {
            JOptionPane.showMessageDialog(this, 
                "You have no action points left! End your turn to continue.",
                "No Actions", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // If a mushroom type is selected and player has mushroom role
        if (selectedMushroomType != null && currentPlayer.getRole() == RoleType.MUSHROOM) {
            try {
                switch(selectedMushroomType) {
                    case "Shroomlet":
                        // Create and place the mushroom
                        new Mushroom_Shroomlet(tecton, currentPlayer);
                        break;
                    case "Slender":
                        // Create and place the slender mushroom
                        new Mushroom_Slender(tecton, currentPlayer);
                        break;
                    case "Maximus":
                        // Create and place the maximus mushroom
                        new Mushroom_Maximus(tecton, currentPlayer);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, 
                            "Unknown mushroom type: " + selectedMushroomType,
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }
                
                // Reduce action points after successful placement
                currentPlayer.setAction(currentPlayer.getAction() - 1);
                
                JOptionPane.showMessageDialog(this, 
                    selectedMushroomType + " mushroom placed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset selection
                selectedMushroomType = null;
                
                // Refresh view
                gamePanel.refreshView();
                
                // Update inventory to show reduced action points
                updateCurrentPlayerInventory();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error placing mushroom: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        // If an insect type is selected and player has insect role
        else if (selectedInsectType != null && currentPlayer.getRole() == RoleType.INSECT) {
            try {
                switch(selectedInsectType) {
                    case "Buglet":
                        // Create and place the buglet
                        new Insect_Buglet(tecton, currentPlayer);
                        break;
                    case "Buggernaut":
                        // Create and place the buggernaut
                        new Insect_Buggernaut(tecton, currentPlayer);
                        break;
                    case "ShroomReaper":
                        // Create and place the shroom reaper
                        new Insect_ShroomReaper(tecton, currentPlayer);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, 
                            "Unknown insect type: " + selectedInsectType,
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }
                
                // Reduce action points after successful placement
                currentPlayer.setAction(currentPlayer.getAction() - 1);
                
                JOptionPane.showMessageDialog(this, 
                    selectedInsectType + " insect placed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset selection
                selectedInsectType = null;
                
                // Refresh view
                gamePanel.refreshView();
                
                // Update inventory to show reduced action points
                updateCurrentPlayerInventory();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error placing insect: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Refreshes the entire UI to reflect latest game state
     */
    private void refreshUI() {
        // Log the current state for debugging
        Player player1 = gameModel.getPlayer1();
        Player player2 = gameModel.getPlayer2();
        Player currentPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        
        System.out.println("REFRESH UI - Current player: " + currentPlayer.getId() + 
                          ", Current turn: " + gameModel.getTurnNumber());
        System.out.println("Player 1 role: " + player1.getRole().getRoleName() + 
                         ", Actions: " + player1.getAction());
        System.out.println("Player 2 role: " + player2.getRole().getRoleName() + 
                         ", Actions: " + player2.getAction());
        
        // Update the inventory panel to reflect current player's role and actions
        updateCurrentPlayerInventory();
        
        // Update the turn indicator to show current player
        updateTurnIndicator();
        
        // Force refresh of the game panel to update entity views
        if (gamePanel != null) {
            gamePanel.refreshView();
        }
        
        // Update the status panel in the game panel
        if (gamePanel != null) {
            gamePanel.updateStatusPanel();
        }
        
        // Repaint the entire frame
        revalidate();
        repaint();
    }

    /**
     * Updates the turn indicator panel to show the current player's turn and role.
     */
    private void updateTurnIndicator() {
        // Find the turn indicator label
        Component[] components = content.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel && comp == content.getComponent(0)) { // The north component is our indicator panel
                JPanel panel = (JPanel) comp;
                for (Component panelComp : panel.getComponents()) {
                    if (panelComp instanceof JLabel && "turnIndicator".equals(panelComp.getName())) {
                        JLabel turnLabel = (JLabel) panelComp;
                        
                        // Get current player and their role
                        Player currentPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
                        String playerName = currentPlayer.getName();
                        String roleName = currentPlayer.getRole().getRoleName();
                        
                        // Update the label
                        turnLabel.setText("Current Turn: " + playerName + " (" + roleName + ")");
                        
                        // Change color based on role
                        if (roleName.equals("Mushroom")) {
                            turnLabel.setForeground(new Color(200, 150, 50)); // Gold color for Mushroom
                        } else if (roleName.equals("Insect")) {
                            turnLabel.setForeground(new Color(50, 200, 100)); // Green color for Insect
                        } else {
                            turnLabel.setForeground(Color.WHITE); // Default color
                        }
                        
                        break;
                    }
                }
                break;
            }
        }
    }

    /**
     * Inner panel that performs all game drawing and logic updates.
     */
    private class GamePanel extends JPanel {
        private boolean running = true;
        public GameBoardPanel boardPanel;

        public GamePanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(new Color(30, 30, 40));
            setLayout(new BorderLayout());
            
            // Initialize the game board panel
            boardPanel = new GameBoardPanel(gameModel.getPlane());
            boardPanel.addTectonSelectionListener(GameCanvasFrame.this); // Register as a listener
            add(boardPanel, BorderLayout.CENTER);
            
            // Create a status panel at the top
            add(createStatusPanel(), BorderLayout.NORTH);
        }
        
        private JPanel createStatusPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            
            // Player 1 info on the left
            JPanel p1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p1Panel.setOpaque(false);
            JLabel p1Label = new JLabel(gameModel.getPlayer1().getName() + ": ");
            p1Label.setForeground(Color.WHITE);
            JLabel p1Score = new JLabel("Score: " + gameModel.getPlayer1().getScore());
            p1Score.setForeground(Color.WHITE);
            p1Panel.add(p1Label);
            p1Panel.add(p1Score);
            
            // Player 2 info on the right
            JPanel p2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            p2Panel.setOpaque(false);
            JLabel p2Label = new JLabel(gameModel.getPlayer2().getName() + ": ");
            p2Label.setForeground(Color.WHITE);
            JLabel p2Score = new JLabel("Score: " + gameModel.getPlayer2().getScore());
            p2Score.setForeground(Color.WHITE);
            p2Panel.add(p2Label);
            p2Panel.add(p2Score);
            
            // Turn indicator in the center with current player and role
            JPanel turnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            turnPanel.setOpaque(false);
            
            // Get current player and role information
            Player currentPlayer = gameModel.getPlayer(gameModel.currentTurnsPlayer());
            String currentPlayerName = currentPlayer.getName();
            String currentRole = currentPlayer.getRole().getRoleName();
            
            // Create label with current player and role
            JLabel turnLabel = new JLabel("Current Turn: " + currentPlayerName + " (" + currentRole + ")");
            turnLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Set color based on role
            if (currentRole.equals("Mushroom")) {
                turnLabel.setForeground(new Color(200, 150, 50)); // Gold for Mushroom
            } else if (currentRole.equals("Insect")) {
                turnLabel.setForeground(new Color(50, 200, 100)); // Green for Insect
            } else {
                turnLabel.setForeground(Color.YELLOW); // Default
            }
            
            turnPanel.add(turnLabel);
            
            panel.add(p1Panel, BorderLayout.WEST);
            panel.add(turnPanel, BorderLayout.CENTER);
            panel.add(p2Panel, BorderLayout.EAST);
            
            return panel;
        }
        
        /** Updates the view to reflect current game state */
        public void refreshView() {
            boardPanel.refreshViews();
            repaint();
        }
        
        /** Updates the status panel with current player info */
        public void updateStatusPanel() {
            Component[] components = getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel && comp == getComponent(0)) { // The north component is our status panel
                    remove(comp);
                    add(createStatusPanel(), BorderLayout.NORTH);
                    break;
                }
            }
            revalidate();
            repaint();
        }

        /** Update game state (positions, collisions, etc.) */
        public void updateGame() {
            // Auto-refresh view for animations or timer-based updates
            // Only call if there are actual updates to visualize
            // boardPanel.refreshViews();
        }

        public boolean isRunning() {
            return running && !gameModel.isGameOver();
        }
    }
}
