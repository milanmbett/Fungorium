// filepath: /home/borisz/projlab-jva/Fungorium/fungorium/src/main/java/com/coderunnerlovagjai/app/GameCanvasFrame.java
package com.coderunnerlovagjai.app;

import com.coderunnerlovagjai.app.view.GameBoardPanel;
import com.coderunnerlovagjai.app.view.GameBoardView;
import com.coderunnerlovagjai.app.view.TectonSelectionListener;
import com.coderunnerlovagjai.app.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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
        gamePanel = new GamePanel();
        content.setLayout(new BorderLayout());
        content.add(gamePanel, BorderLayout.CENTER);
        
        // Add Civilization 6-style end turn button on the left side
        JPanel leftPanel = createEndTurnSidePanel();
        content.add(leftPanel, BorderLayout.WEST);
        
        // Add game controls panel to the bottom
        JPanel controlPanel = createControlPanel();
        content.add(controlPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates a Civilization 6-style end turn panel on the left side
     */
    private JPanel createEndTurnSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(80, 600));
        panel.setBackground(new Color(30, 30, 40));
        
        // Create the round end turn button at the center-left
        JButton endTurnBtn = new JButton("<html><center>END<br>TURN</center></html>");
        endTurnBtn.setFont(new Font("Arial", Font.BOLD, 14));
        endTurnBtn.setBackground(new Color(180, 20, 20));
        endTurnBtn.setForeground(Color.WHITE);
        endTurnBtn.setFocusPainted(false);
        endTurnBtn.setBorderPainted(false);
        endTurnBtn.setPreferredSize(new Dimension(70, 70));
        
        // Make the button round
        endTurnBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                AbstractButton b = (AbstractButton) c;
                ButtonModel model = b.getModel();
                
                // Choose button color based on state
                if (model.isPressed()) {
                    g2.setColor(new Color(140, 10, 10)); // Darker when pressed
                } else if (model.isRollover()) {
                    g2.setColor(new Color(220, 30, 30)); // Brighter on hover
                } else {
                    g2.setColor(b.getBackground());
                }
                
                // Draw the circular button
                g2.fillOval(0, 0, c.getWidth(), c.getHeight());
                
                // Draw border
                g2.setColor(new Color(240, 240, 240));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(0, 0, c.getWidth() - 1, c.getHeight() - 1);
                
                // Reset graphics context
                g2.dispose();
                
                // Paint the text
                super.paint(g, c);
            }
        });
        
        // Action when end turn button is clicked
        endTurnBtn.addActionListener(e -> {
            gameController.endTurn();
            refreshUI(); // Use the new method to refresh everything
        });
        
        // Add button in a panel to center it
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setOpaque(false);
        buttonContainer.add(endTurnBtn);
        
        // Add inventory panel for the current player
        playerInventoryPanel = new JPanel();
        playerInventoryPanel.setLayout(new BoxLayout(playerInventoryPanel, BoxLayout.Y_AXIS));
        playerInventoryPanel.setBackground(new Color(40, 40, 50));
        playerInventoryPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        updateCurrentPlayerInventory(); // Initialize inventory display
        
        // Add components to the main panel
        panel.add(buttonContainer, BorderLayout.CENTER);
        panel.add(playerInventoryPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    /**
     * Creates a panel with game control buttons.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        
        // End Turn Button
        JButton endTurnBtn = new JButton("End Turn");
        endTurnBtn.addActionListener(e -> {
            gameController.endTurn();
            refreshUI();
        });
        
        // Mushroom Role Button
        JButton mushroomRoleBtn = new JButton("Mushroom Role");
        mushroomRoleBtn.addActionListener(e -> {
            gameModel.getPlayer(gameModel.currentTurnsPlayer()).setRoleMushroom();
            refreshUI();
        });
        
        // Insect Role Button
        JButton insectRoleBtn = new JButton("Insect Role");
        insectRoleBtn.addActionListener(e -> {
            gameModel.getPlayer(gameModel.currentTurnsPlayer()).setRoleInsect();
            refreshUI();
        });
        
        panel.add(mushroomRoleBtn);
        panel.add(insectRoleBtn);
        panel.add(endTurnBtn);
        
        return panel;
    }

    /** Starts the continuous game loop on a separate thread. */
    private void startGameLoop() {
        Thread loop = new Thread(() -> {
            while (gamePanel.isRunning()) {
                gamePanel.updateGame();
                gamePanel.repaint();
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
            float gain = (range * 0.2f) + gainControl.getMinimum();
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
                
                JOptionPane.showMessageDialog(this, 
                    selectedMushroomType + " mushroom placed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset selection
                selectedMushroomType = null;
                
                // Refresh view
                gamePanel.refreshView();
                
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
                
                JOptionPane.showMessageDialog(this, 
                    selectedInsectType + " insect placed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset selection
                selectedInsectType = null;
                
                // Refresh view
                gamePanel.refreshView();
                
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
        // Refresh the game board
        gamePanel.refreshView();
        
        // Update the inventory panel
        updateCurrentPlayerInventory();
        
        // Repaint the frame
        revalidate();
        repaint();
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
            
            // Turn indicator in the center
            JPanel turnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            turnPanel.setOpaque(false);
            JLabel turnLabel = new JLabel("Turn: " + gameModel.getTurnNumber());
            turnLabel.setForeground(Color.WHITE);
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
