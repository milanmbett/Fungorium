package com.coderunnerlovagjai.app.view;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.Image;

import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.controller.InteractionManager;
import com.coderunnerlovagjai.app.util.SoundManager;

/**
 * Main game window, now slimmed down to orchestration only.
 */
public class GameCanvasFrame extends FrameStyle{
    private final Game gameModel;
    private TopInfoPanel topInfoPanel;
    private BottomActionPanel bottomActionPanel;
    private InteractionManager interactionManager;
    private JLayeredPane layeredPane;

    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        this.gameModel = new Game(player1, player2);
        initGame();
    }

    private void initGame() {
        gameModel.getPlane().clearAllCollections();
        gameModel.initGame();
        gameModel.startGame();
        interactionManager = new InteractionManager(gameModel, this);

        buildUI();
        pack(); setResizable(false);
        setLocationRelativeTo(null); setVisible(true);
        new Timer(40, e -> GameCanvas.getInstance().repaint()).start();

        SwingUtilities.invokeLater(() -> interactionManager.onStartTurn());
    }

    @Override
    protected void buildUI() {
        // central canvas + tectons
        GameCanvas canvas = GameCanvas.getInstance();
        canvas.setPreferredSize(new Dimension(800, 600));
        content.setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        canvas.setBounds(0, 0, 800, 600);
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);
        for (var t : gameModel.getPlane().TectonCollection)
            canvas.addGraphics(new TectonGraphics(t));
        content.add(layeredPane, BorderLayout.CENTER);

        // Top and bottom panels
        topInfoPanel = new TopInfoPanel(gameModel);
        bottomActionPanel = new BottomActionPanel(gameModel, interactionManager);
        content.add(topInfoPanel, BorderLayout.NORTH);
        content.add(bottomActionPanel, BorderLayout.SOUTH);

        // Mouse to controller
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                interactionManager.handleCanvasClick(e.getX(), e.getY());
            }
        });

        // Music swap
        try { SoundManager.stopMusic(); SoundManager.playMusic("sounds/gameMusic.mp3", true); }
        catch(Exception ignored) {}
    }

    // Called by InteractionManager when UI needs update
    public void refreshInfo() {
        topInfoPanel.updateInfo();
        bottomActionPanel.updateEntities();
        GameCanvas.getInstance().repaint();
    }

    // Delegates end-of-turn
    public void endTurn() {
        gameModel.turn();
        interactionManager.onEndTurn();
    }

    // Delegate game-over handling
    public void showGameOverDialog(String message, String[] options, Runnable onBack, Runnable onExit) {
        int choice = showStyledOptionDialog(message, "Game Over", options);
        if (choice == 0) onBack.run(); else onExit.run();
    }
}

