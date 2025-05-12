package com.coderunnerlovagjai.app;

import javax.swing.*;
import java.awt.*;

/**
 * GameCanvasFrame is the main game window, styled like FrameStyle.
 * It hosts the game drawing surface and runs the game loop.
 */
public class GameCanvasFrame extends FrameStyle {
    private GamePanel gamePanel;

    /**
     * Constructs a new game window for two players.
     * @param player1 Name of player 1
     * @param player2 Name of player 2
     */
    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        buildUI();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        startGameLoop();
    }

    /**
     * Builds the game canvas UI within the styled frame.
     */
    @Override
    protected void buildUI() {
        gamePanel = new GamePanel();
        content.setLayout(new BorderLayout());
        content.add(gamePanel, BorderLayout.CENTER);
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
     * Inner panel that performs all game drawing and logic updates.
     */
    private static class GamePanel extends JPanel {
        private boolean running = true;
        // TODO: reference your Game model/controller here

        public GamePanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(BG_COLOR);
            // initialize your game here, e.g. load entities
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            // TODO: render your game entities, e.g. via GraphicsObject instances
        }

        /** Update game state (positions, collisions, etc.) */
        public void updateGame() {
            // TODO: advance your Game model
        }

        public boolean isRunning() {
            return running;
        }
    }
}
