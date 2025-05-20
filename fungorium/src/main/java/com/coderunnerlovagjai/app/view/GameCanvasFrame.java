package com.coderunnerlovagjai.app.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.GameCanvas;
import com.coderunnerlovagjai.app.Tecton_Class;
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
    private final List<TectonGraphics> tectonGraphicsList = new ArrayList<>();

    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        this.gameModel = new Game(player1, player2);
        initGame();
    }

    private void initGame() {
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
        
        tectonGraphicsList.clear();
        for (var t : gameModel.getPlane().TectonCollection) {
            TectonGraphics tg = new TectonGraphics(t);
            tectonGraphicsList.add(tg);
        }
        content.add(layeredPane, BorderLayout.CENTER);

        // Top and bottom panels
        topInfoPanel = new TopInfoPanel(gameModel);
        bottomActionPanel = new BottomActionPanel(gameModel, interactionManager);
        content.add(topInfoPanel, BorderLayout.NORTH);
        content.add(bottomActionPanel, BorderLayout.SOUTH);

        // Mouse to controller
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
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

        List<Tecton_Class> currentTectonModels = gameModel.getPlane().TectonCollection;
        List<TectonGraphics> viewsToRemove = new ArrayList<>();
        List<Tecton_Class> modelsCurrentlyWithViews = new ArrayList<>();

        for (TectonGraphics tgView : tectonGraphicsList) {
            if (currentTectonModels.contains(tgView.getModel())) {
                modelsCurrentlyWithViews.add(tgView.getModel());
            } else {
                viewsToRemove.add(tgView);
            }
        }
        tectonGraphicsList.removeAll(viewsToRemove);

        for (Tecton_Class modelInPlane : currentTectonModels) {
            if (!modelsCurrentlyWithViews.contains(modelInPlane)) {
                TectonGraphics newTgView = new TectonGraphics(modelInPlane);
                tectonGraphicsList.add(newTgView);
            }
        }
        
        GameCanvas.getInstance().repaint();
    }

    // Delegates end-of-turn
    public void endTurn() {
        gameModel.turn();
        interactionManager.onEndTurn();
    }

    // Delegate game-over handling
    public void showGameOverDialog(String message, String[] options, Runnable onBack, Runnable onExit) {
        try { SoundManager.stopMusic(); }
        catch(Exception ignored) {}
        int choice = showStyledOptionDialog(message, "Game Over", options);
        if (choice == 0) onBack.run(); else onExit.run();
    }
}

