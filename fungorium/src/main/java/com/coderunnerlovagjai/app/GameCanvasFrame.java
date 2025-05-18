package com.coderunnerlovagjai.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JPanel toolbar;
    private JButton endTurnButton;
    private JComboBox<String> mushroomSelector;
    private JComboBox<String> insectSelector;
    private JLabel currentPlayerLabel;

    /**
     * Constructs a new game window for two players.
     * @param player1 Name of player 1
     * @param player2 Name of player 2
     */
    public GameCanvasFrame(String player1, String player2) {
        super("Fungorium - " + player1 + " vs " + player2, "/images/fungoriumIcon3.png");
        this.gameModel = new Game(player1, player2);
        gameModel.initGame();
        gameModel.getPlayer1().setRoleMushroom();
        gameModel.getPlayer2().setRoleInsect();
        gameModel.startGame();
        buildUI();
        updateInventoryVisibility();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

        // ↓ remove turn() from the loop here ↓
        // old: new Timer(40, e -> { gameModel.turn(); GameCanvas.getInstance().repaint(); }).start();
        new Timer(40, e -> GameCanvas.getInstance().repaint()).start();
    }

    /**
     * Builds the game canvas UI within the styled frame.
     */
    @Override
    protected void buildUI() {
        // use our new GameCanvas for rendering
        GameCanvas canvas = GameCanvas.getInstance();
        canvas.setPreferredSize(new Dimension(800, 600));
        content.setLayout(new BorderLayout());
        content.add(canvas, BorderLayout.CENTER);
        // register each Tecton for rendering
        for (var t : gameModel.getPlane().TectonCollection) {
            canvas.addGraphics(new TectonGraphics(t));
        }
        // Compact toolbar below the map
        toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        currentPlayerLabel = new JLabel("Current player: " + getCurrentPlayerName());
        toolbar.add(currentPlayerLabel);
        toolbar.add(new JLabel("Mushroom:"));
        mushroomSelector = new JComboBox<>(new String[]{"Shroomlet", "Maximus", "Slender", "Grand", "Maximus"});
        mushroomSelector.setFocusable(false);
        toolbar.add(mushroomSelector);
        toolbar.add(new JLabel("Insect:"));
        insectSelector = new JComboBox<>(new String[]{"Buglet", "Buggernaut", "Stinger", "Tektonizator", "ShroomReaper"});
        insectSelector.setFocusable(false);
        toolbar.add(insectSelector);
        endTurnButton = new JButton("End Turn");
        endTurnButton.setFocusable(false);
        endTurnButton.addActionListener(e -> endTurn());
        toolbar.add(endTurnButton);
        content.add(toolbar, BorderLayout.SOUTH);
        // Mouse input for tecton selection
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleCanvasClick(e.getX(), e.getY());
            }
        });
        // repaint on a timer (~60 FPS)
        new Timer(40, e -> canvas.repaint()).start();
        updateInventoryVisibility();
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
        if (isMushroomRole) {
            if (!canPlaceMushroomHere(tecton)) {
                JOptionPane.showMessageDialog(this, "Cannot place mushroom here!", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String type = (String) mushroomSelector.getSelectedItem();
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
                GameCanvas.getInstance().repaint();
            }
        } else if (isInsectRole) {
            if (!canPlaceInsectHere(tecton)) {
                JOptionPane.showMessageDialog(this, "Cannot place insect here!", "Invalid", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String type = (String) insectSelector.getSelectedItem();
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

    private boolean canPlaceMushroomHere(Tecton_Class tecton) {
        // Example rule: can't place next to insect base (implement real logic)
        for (var neighbor : tecton.get_TectonNeighbours()) {
            if (neighbor.get_Mushroom() != null && neighbor.get_Mushroom().getClass().getSimpleName().contains("Insect")) {
                return false;
            }
        }
        return tecton.get_Mushroom() == null;
    }

    private boolean canPlaceInsectHere(Tecton_Class tecton) {
        // Example: only allow if no insect present
        return tecton.get_InsectsOnTecton().isEmpty();
    }

    private void endTurn() {
        gameModel.turn();
        currentPlayerLabel.setText("Current player: " + getCurrentPlayerName());
        updateInventoryVisibility();
        GameCanvas.getInstance().repaint();
    }

    private String getCurrentPlayerName() {
        int id = gameModel.currentTurnsPlayer();
        var p = gameModel.getPlayer(id);
        return p != null ? p.getName() : "?";
    }

    private void updateInventoryVisibility() {
        // Show only the relevant inventory for the current player
        var player = gameModel.getPlayer(gameModel.currentTurnsPlayer());
        var role = player.getRole();
        boolean isMushroomRole = role == RoleType.MUSHROOM;
        boolean isInsectRole = role == RoleType.INSECT;
        mushroomSelector.setVisible(isMushroomRole);
        // The label before it
        ((JLabel)toolbar.getComponent(toolbar.getComponentZOrder(mushroomSelector)-1)).setVisible(isMushroomRole);
        insectSelector.setVisible(isInsectRole);
        ((JLabel)toolbar.getComponent(toolbar.getComponentZOrder(insectSelector)-1)).setVisible(isInsectRole);
        toolbar.revalidate();
        toolbar.repaint();
    }
}
