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
 * GameCanvasFrame: A fő játékképernyő ablak, amely tartalmazza a pályát, a felső információs sávot,
 * az alsó akciósávot, és kezeli a játék fő eseményeit, új játék indítását, UI frissítést.
 * Felelős a játékosok közötti váltásért, a pálya újrarajzolásáért, és a vezérlő (InteractionManager) példányáért.
 */
public class GameCanvasFrame extends FrameStyle {
    // A játék logikai modellje
    private final Game gameModel;
    // Felső információs panel (játékosok, pontok, stb.)
    private TopInfoPanel topInfoPanel;
    // Alsó akciósáv panel (entitásválasztó, akciógombok)
    private BottomActionPanel bottomActionPanel;
    // Interakciókezelő (vezérlő), amely a felhasználói eseményeket kezeli
    private InteractionManager interactionManager;
    // Rétegezett panel, amelyen a pálya és egyéb rétegek helyezkednek el
    private JLayeredPane layeredPane;
    // A pályán lévő TectonGraphics nézetek listája (grafikus hatszögek)
    private final List<TectonGraphics> tectonGraphicsList = new ArrayList<>();

    /**
     * Konstruktor: új játékot indít a megadott játékos nevekkel.
     * @param player1 Az első játékos neve
     * @param player2 A második játékos neve
     */
    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        this.gameModel = new Game(player1, player2);
        initGame();
    }


    /**
     * Inicializálja az új játékot: törli a régi pályát, újraépíti a UI-t, elindítja a játékot.
     * Ezt hívja a konstruktor és új játék indításakor is.
     */
    private void initGame() {
        // Canvas (pálya) grafikai elemeinek törlése
        GameCanvas.getInstance().clearAll();
        // Játék logikai modelljének teljes törlése
        gameModel.getPlane().clearAllCollections();
        // Játék logikai inicializálása (pálya, bázisok, stb.)
        gameModel.initGame();
        gameModel.startGame();
        // Új InteractionManager példány
        interactionManager = new InteractionManager(gameModel, this);

        buildUI();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        // Folyamatos újrarajzolás (animáció)
        new Timer(40, e -> GameCanvas.getInstance().repaint()).start();

        // Az első kör szerepválasztásának elindítása
        SwingUtilities.invokeLater(() -> interactionManager.onStartTurn());
    }


    /**
     * Felépíti a teljes UI-t: pálya, felső és alsó panel, eseménykezelők, zene.
     * Minden új játék indításakor újra lefut.
     */
    @Override
    protected void buildUI() {
        // Központi canvas (pálya hatszögekkel)
        GameCanvas canvas = GameCanvas.getInstance();
        // Régi egér-eseménykezelők törlése
        for (java.awt.event.MouseListener listener : canvas.getMouseListeners()) {
            canvas.removeMouseListener(listener);
        }
        canvas.setPreferredSize(new Dimension(800, 600));
        content.setLayout(new BorderLayout());

        // Rétegezett panel (canvas + egyéb rétegek)
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));
        canvas.setBounds(0, 0, 800, 600);
        layeredPane.add(canvas, JLayeredPane.DEFAULT_LAYER);

        // TectonGraphics nézetek újragenerálása
        tectonGraphicsList.clear();
        for (var t : gameModel.getPlane().TectonCollection) {
            TectonGraphics tg = new TectonGraphics(t);
            tectonGraphicsList.add(tg);
        }
        content.add(layeredPane, BorderLayout.CENTER);

        // Felső és alsó panelek hozzáadása
        topInfoPanel = new TopInfoPanel(gameModel);
        bottomActionPanel = new BottomActionPanel(gameModel, interactionManager);
        content.add(topInfoPanel, BorderLayout.NORTH);
        content.add(bottomActionPanel, BorderLayout.SOUTH);

        // Egérkattintás átirányítása a vezérlőhöz
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                interactionManager.handleCanvasClick(e.getX(), e.getY());
            }
        });

        // Zene leállítása és újraindítása
        try {
            SoundManager.stopMusic();
            SoundManager.playMusic("sounds/gameMusic.mp3", true);
        } catch(Exception ignored) {}
    }


    /**
     * Frissíti a teljes UI-t (felső/alsó panel, pálya), ha a modell változik.
     * Ezt hívja az InteractionManager, ha változás történik.
     */
    public void refreshInfo() {
        topInfoPanel.updateInfo();
        bottomActionPanel.updateEntities();

        // TectonGraphics nézetek szinkronizálása a modellhez
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


    /**
     * Egy kör végét kezeli: továbbadja a logikának és a vezérlőnek.
     */
    public void endTurn() {
        gameModel.turn();
        interactionManager.onEndTurn();
    }

    /**
     * Játék vége dialógus: leállítja a zenét, felugró ablakot mutat, visszalép vagy kilép.
     * @param message  A megjelenítendő üzenet
     * @param options  A választható opciók
     * @param onBack   Visszalépés callback
     * @param onExit   Kilépés callback
     */
    public void showGameOverDialog(String message, String[] options, Runnable onBack, Runnable onExit) {
        try { SoundManager.stopMusic(); }
        catch(Exception ignored) {}
        int choice = showStyledOptionDialog(message, "Game Over", options);
        if (choice == 0) onBack.run(); else onExit.run();
    }
}

