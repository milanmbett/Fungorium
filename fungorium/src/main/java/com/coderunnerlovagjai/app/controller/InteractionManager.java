package com.coderunnerlovagjai.app.controller;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;

import com.coderunnerlovagjai.app.*;
import com.coderunnerlovagjai.app.view.GameCanvasFrame;
import com.coderunnerlovagjai.app.view.MainMenu;

/**
 * Central controller for all game interactions:
 * role selection, placement, movement, cracking, and turn management.
 */
public class InteractionManager {
    private final Game model;
    private final GameCanvasFrame view;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2); // Or newSingleThreadExecutor()

    // Interaction modes
    private enum InteractionState { NORMAL, SELECTING_ENTITY, SELECTING_DESTINATION, SELECTING_CRACK }
    private InteractionState currentState = InteractionState.NORMAL;

    // Constants for insect click detection, should match view rendering if possible
    private static final int INSECT_ORBIT_RADIUS = 20; // Assumed radius from TectonGraphics
    private static final int INSECT_CLICK_RADIUS = 8;  // Assumed slotSize/2 from TectonGraphics

    // For placement
    private int selectedEntityIndex = -1;
    // For insect/crack actions
    private Insect_Class selectedInsect;

    public InteractionManager(Game model, GameCanvasFrame view) {
        this.model = model;
        this.view  = view;
    }

    /** First role pick on startup and every turn. */
    public void onStartTurn() {
        Player p = model.getPlayer(model.currentTurnsPlayer());
        String[] roles = {"Gombász", "Rovarász"};
        int choice = view.showStyledOptionDialog(
            p.getName() + ", válassz szerepet a következő körre:",
            "Szerepválasztás",
            roles
        );
        if (choice == 0) p.setRoleMushroom(); else p.setRoleInsect();
        selectedEntityIndex = -1;
        currentState = InteractionState.NORMAL;
        view.refreshInfo();
    }

    /** Handles clicks on the main canvas, delegating by state. */
    public void handleCanvasClick(int x, int y) {
        switch (currentState) {
            case SELECTING_ENTITY:
                handleEntitySelection(x, y);
                break;
            case SELECTING_DESTINATION:
                handleDestinationSelection(x, y);
                break;
            case SELECTING_CRACK:
                handleCrackSelection(x, y);
                break;
            case NORMAL:
            default:
                placeEntityOnClick(x, y);
                break;
        }
    }

    /** Ends current turn and triggers next role pick or game over. */
    public void onEndTurn() {
        // Optionally, disable parts of the UI here if needed
        // view.setInteractionEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean gameOverResult;
            private String gameOverMessageResult;

            @Override
            protected Void doInBackground() throws Exception {
                model.turn(); // This is the potentially long-running task
                gameOverResult = model.isGameOver();
                if (gameOverResult) {
                    gameOverMessageResult = buildGameOverMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Call get() to rethrow exceptions from doInBackground, if any
                    if (gameOverResult) {
                        view.showGameOverDialog(
                            gameOverMessageResult,
                            new String[]{"Back to Main Menu", "Exit Game"},
                            InteractionManager.this::returnToMenu,
                            () -> System.exit(0)
                        );
                    } else {
                        onStartTurn(); // This shows a dialog and refreshes info
                    }
                } catch (Exception e) {
                    System.err.println("Error during turn processing: " + e.getMessage());
                    e.printStackTrace();
                    view.showStyledMessageDialog(
                        "An error occurred: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    // Optionally, re-enable UI parts here
                    // view.setInteractionEnabled(true);
                    // view.refreshInfo(); // onStartTurn usually calls this, but if not, ensure it's called.
                }
            }
        };
        executorService.submit(worker);
    }

    /** Initiates insect movement flow. */
    public void startInsectMovement() {
        Player p = model.getPlayer(model.currentTurnsPlayer());
        if (p.getRole() != RoleType.INSECT) {
            view.showStyledMessageDialog(
                "You must have the Insect role to move insects.",
                "Invalid Role", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (p.getAction() <= 0) {
            view.showStyledMessageDialog(
                "No action points left!",
                "No Actions", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        currentState = InteractionState.SELECTING_ENTITY;
        view.showStyledMessageDialog(
            "Click on an insect to move", "Select Insect", JOptionPane.INFORMATION_MESSAGE
        );
    }

    /** Initiates tecton crack flow. */
    public void startTectonCrack() {
        Player p = model.getPlayer(model.currentTurnsPlayer());
        if (p.getRole() != RoleType.INSECT) {
            view.showStyledMessageDialog(
                "You must have the Insect role to crack tectons.",
                "Invalid Role", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (p.getAction() <= 0) {
            view.showStyledMessageDialog(
                "No action points left!", "No Actions", JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        currentState = InteractionState.SELECTING_CRACK;
        view.showStyledMessageDialog(
            "Click on a Tektonizator insect to crack its tecton.",
            "Select Tektonizator", JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Helper class for populateEntityBoxes data transfer
    private static class EntityBoxUIData {
        Image image;
        int cost;
        String typeName;
        int originalIndex;
        boolean isSelected;
        String imagePath; // Store path for potential retry or logging
    }

    /** Fills the bottom panel with mushroom or insect options using a background thread for image loading. */
    public void populateEntityBoxes(JPanel panel) {
        panel.removeAll(); // Clear panel on EDT first
        // It's often good to call revalidate and repaint after removeAll
        // if the panel might not be updated immediately by adding new components.
        // However, process() will also call them.

        Player p = model.getPlayer(model.currentTurnsPlayer());
        boolean isMush = p.getRole() == RoleType.MUSHROOM;
        String[] types = isMush
            ? new String[]{"Shroomlet","Maximus","Slender"}
            : new String[]{"Buglet","Buggernaut","Stinger","Tektonizator","ShroomReaper"};
        String prefix = isMush ? "Mushroom_" : "Insect_";
        final int currentSelectedEntityIndex = this.selectedEntityIndex;

        SwingWorker<Void, EntityBoxUIData> worker = new SwingWorker<Void, EntityBoxUIData>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i < types.length; i++) {
                    EntityBoxUIData data = new EntityBoxUIData();
                    data.originalIndex = i;
                    data.typeName = types[i];
                    data.cost = getEntityCost(types[i], isMush);
                    data.isSelected = (i == currentSelectedEntityIndex);
                    data.imagePath = "images/" + prefix + types[i] + ".png";

                    try {
                        // Attempt to load image using ImageIO directly for background loading.
                        // If you have an ImageCache that can load asynchronously or is very fast,
                        // you might use it here, but ensure it doesn't block this worker unnecessarily.
                        data.image = ImageIO.read(
                            InteractionManager.class.getClassLoader().getResourceAsStream(data.imagePath)
                        );
                    } catch (IOException | IllegalArgumentException e) {
                        System.err.println("Failed to load image " + data.imagePath + ": " + e.getMessage());
                        data.image = null; // Handle missing image in createAndAddBoxToPanel_EDT
                    }
                    publish(data); // Publish data for one box
                }
                return null;
            }

            @Override
            protected void process(List<EntityBoxUIData> chunks) {
                // This runs on the EDT
                for (EntityBoxUIData data : chunks) {
                    createAndAddBoxToPanel_EDT(panel, data);
                }
                // Revalidate and repaint after adding a batch of components
                panel.revalidate();
                panel.repaint();
            }

            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions from doInBackground itself
                } catch (Exception e) {
                    System.err.println("Error in populateEntityBoxes worker: " + e.getMessage());
                    e.printStackTrace();
                    // Potentially show a generic error to the user
                }
                // Final revalidate and repaint can be useful, though process should handle most updates.
                panel.revalidate();
                panel.repaint();
            }
        };
        executorService.submit(worker);
    }
    
    // Helper method to create and add a single entity box to the panel. MUST be called on EDT.
    private void createAndAddBoxToPanel_EDT(JPanel parentPanel, EntityBoxUIData data) {
        final int idx = data.originalIndex;

        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Use data.isSelected for highlighting
                g.setColor(data.isSelected
                    ? new Color(60,180,255,120)
                    : new Color(200,200,200,100)
                );
                g.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
            }
        };
        box.setBounds(idx * 110, 0, 100, 100); // Standard layout
        box.setLayout(null); // Using null layout as in original
        box.setOpaque(false); // Important if paintComponent does custom painting

        // Add cost label
        JLabel costLabel = new JLabel("Cost: " + data.cost);
        costLabel.setForeground(Color.WHITE); // Assuming white text is desired
        costLabel.setFont(new Font("Arial", Font.BOLD, 12));
        costLabel.setBounds(0, 0, 100, 20); // Position at top
        costLabel.setHorizontalAlignment(SwingConstants.CENTER);
        box.add(costLabel);

        // Add image icon
        if (data.image != null) {
            JLabel iconLabel = new JLabel(new ImageIcon(
                data.image.getScaledInstance(64, 64, Image.SCALE_SMOOTH)
            ));
            iconLabel.setBounds(18, 20, 64, 64); // Position below cost
            box.add(iconLabel);
        } else {
            // Handle missing image, e.g., show placeholder text or a default error icon
            JLabel errorLabel = new JLabel("N/A");
            errorLabel.setForeground(Color.RED);
            errorLabel.setBounds(18, 20, 64, 64);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            box.add(errorLabel);
            System.err.println("Placeholder shown for missing image: " + data.imagePath);
        }

        box.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectedEntityIndex = idx; // Update selection
                view.refreshInfo();       // Refresh the view to reflect selection change
            }
        });
        parentPanel.add(box);
    }

    // ───── Internal Helpers ─────────────────────────────────────────────────

    /** Handles mushroom/insect placement clicks. */
    private void placeEntityOnClick(int x, int y) {
        for (Tecton_Class tecton : model.getPlane().TectonCollection) {
            int tx = tecton.getPosition().x, ty = tecton.getPosition().y;
            if (Math.hypot(x - tx, y - ty) < 40) {
                Player player = model.getPlayer(model.currentTurnsPlayer());
                RoleType role = player.getRole();

                    if (player.getAction() <= 0) {
                    view.showStyledMessageDialog(
                        "No action points left to place an entity!",
                        "No Actions",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (selectedEntityIndex < 0) {
                    view.showStyledMessageDialog(
                        "Please select an entity to place first!",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }

                if (role == RoleType.MUSHROOM) {
                    placeMushroom(tecton, player);
                } else if (role == RoleType.INSECT) {
                    placeInsect(tecton, player);
                }
                return;
            }
        }
    }

    private void placeMushroom(Tecton_Class tecton, Player player) {
        String[] types = {"Shroomlet","Maximus","Slender"};
        Mushroom_Class mush;
        switch (types[selectedEntityIndex]) {
            case "Shroomlet": mush = new Mushroom_Shroomlet(tecton, player); break;
            case "Maximus":  mush = new Mushroom_Maximus(tecton, player);  break;
            case "Slender":  mush = new Mushroom_Slender(tecton, player);  break;
            default: return;
        }
        int cost = mush.getCost();
        if (player.getIncome() < cost) {
            view.showStyledMessageDialog(
                "Not enough income!", "Insufficient Income", JOptionPane.WARNING_MESSAGE
            ); return;
        }
        if (tecton.get_Mushroom()!=null || tecton.get_Thread()==null) {
            view.showStyledMessageDialog("Cannot place mushroom here!","Invalid",JOptionPane.WARNING_MESSAGE);
            return;
        }
        model.getPlane().place_Mushroom(mush, tecton);
        player.setAction(player.getAction()-1);
        //player.decreaseIncome(cost);
        view.refreshInfo();
    }

    private void placeInsect(Tecton_Class tecton, Player player) {
        String[] types = {"Buglet","Buggernaut","Stinger","Tektonizator","ShroomReaper"};
        Insect_Class insect;
        switch (types[selectedEntityIndex]) {
            case "Buglet":       insect = new Insect_Buglet(tecton, player);       break;
            case "Buggernaut":   insect = new Insect_Buggernaut(tecton, player);   break;
            case "Stinger":      insect = new Insect_Stinger(tecton, player);      break;
            case "Tektonizator": insect = new Insect_Tektonizator(tecton, player); break;
            case "ShroomReaper": insect = new Insect_ShroomReaper(tecton, player); break;
            default: return;
        }
        int cost = insect.getCost();
        if (player.getIncome() < cost) {
            view.showStyledMessageDialog("Not enough income!","Insufficient Income",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tecton.get_InsectsOnTecton().size()>=5) {
            view.showStyledMessageDialog("Cannot place insect here!","Invalid",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(!tecton.get_ID().startsWith("Tecton_Base")){
            view.showStyledMessageDialog("Cannot place insect here!","Invalid",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(tecton.get_Mushroom().get_Owner().getId()!=insect.get_Owner().getId()){
            view.showStyledMessageDialog("Cannot place insect on the enemy base!","Invalid",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (model.getPlane().placeInsect(insect, tecton)) {
            player.setAction(player.getAction()-1);
            //player.decreaseIncome(cost);
            view.refreshInfo();
        } else {
            view.showStyledMessageDialog("Placement failed!","Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Selects an insect for movement by clicking its slot rectangle. */
private void handleEntitySelection(int x, int y) {
    Player p = model.getPlayer(model.currentTurnsPlayer());
    int slotCount = 5, slotSize = 20, radius = 30;
    double angleStep = 2 * Math.PI / slotCount;

    for (Tecton_Class t : model.getPlane().TectonCollection) {
        int tx = t.getPosition().x, ty = t.getPosition().y;
        // first check we’re roughly on this hex
        if (Math.hypot(x - tx, y - ty) < radius + slotSize/2) {
            List<Insect_Class> insects = t.get_InsectsOnTecton();
            if (insects == null || insects.isEmpty()) {
                view.showStyledMessageDialog("No insects on this tecton.",
                                             "No Insects", JOptionPane.WARNING_MESSAGE);
                currentState = InteractionState.NORMAL;
                view.refreshInfo();
                return;
            }
            // now test each insect’s slot rect
            for (int i = 0, foundIdx = 0; i < insects.size() && foundIdx < slotCount; i++) {
                Insect_Class ins = insects.get(i);
                if (ins.get_Owner() == null || ins.get_Owner().getId() != p.getId())
                    continue;
                double angle = Math.PI/2 + foundIdx * angleStep;
                int cx = tx + (int)(radius * Math.cos(angle));
                int cy = ty + (int)(radius * Math.sin(angle));
                Rectangle slot = new Rectangle(cx - slotSize/2, cy - slotSize/2, slotSize, slotSize);
                if (slot.contains(x, y)) {
                    selectedInsect = ins;
                    currentState = InteractionState.SELECTING_DESTINATION;
                    view.showStyledMessageDialog("Now click on a destination tecton.",
                                                 "Select Destination", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                foundIdx++;
            }
            // clicked on the hex but not on any slot
            view.showStyledMessageDialog("Click directly on an insect slot to select it.",
                                         "No Insect Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
}

    /** Moves the selected insect to a clicked neighboring tecton. */
    private void handleDestinationSelection(int x, int y) {
        Player p = model.getPlayer(model.currentTurnsPlayer());
        if (selectedInsect == null) {
            view.showStyledMessageDialog(
                "No insect selected. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE
            );
            currentState = InteractionState.NORMAL;
            view.refreshInfo();
            return;
        }
        Tecton_Class origin = selectedInsect.get_Tecton();
        for (Tecton_Class t : model.getPlane().TectonCollection) {
            int tx = t.getPosition().x, ty = t.getPosition().y;
            if (Math.hypot(x - tx, y - ty) < 40) {
                if (!origin.get_TectonNeighbours().contains(t)) {
                    view.showStyledMessageDialog(
                        "Not adjacent, cannot move there.",
                        "Invalid Move", JOptionPane.WARNING_MESSAGE
                    );
                } else if (t.get_Thread() == null) {
                    view.showStyledMessageDialog(
                        "No thread on destination.",
                        "Invalid Move", JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    try {
                        model.getPlane().move_Insect(p, selectedInsect, t);
                        p.setAction(p.getAction() - 1);
                    } catch (Exception e) {
                        view.showStyledMessageDialog(
                            e.getMessage(),
                            "Invalid Move", JOptionPane.WARNING_MESSAGE
                        );
                    }
                }
                currentState = InteractionState.NORMAL;
                selectedInsect = null;
                view.refreshInfo();
                return;
            }
        }
    }
  /** Selects a Tektonizator and cracks its current tecton via its slot rectangle. */
private void handleCrackSelection(int x, int y) {
    Player p = model.getPlayer(model.currentTurnsPlayer());
    int slotCount = 5, slotSize = 20, radius = 30;
    double angleStep = 2 * Math.PI / slotCount;

    for (Tecton_Class t : model.getPlane().TectonCollection) {
        int tx = t.getPosition().x, ty = t.getPosition().y;
        if (Math.hypot(x - tx, y - ty) < radius + slotSize/2) {
            List<Insect_Class> insects = t.get_InsectsOnTecton();
            // filter only your Tektonizators
            insects.removeIf(i -> !(i instanceof Insect_Tektonizator)
                                  || i.get_Owner() == null
                                  || i.get_Owner().getId() != p.getId());
            if (insects.isEmpty()) {
                view.showStyledMessageDialog("No Tektonizator here.",
                                             "Invalid", JOptionPane.WARNING_MESSAGE);
                currentState = InteractionState.NORMAL;
                view.refreshInfo();
                return;
            }
            // test each slot
            for (int idx = 0; idx < insects.size() && idx < slotCount; idx++) {
                double angle = Math.PI/2 + idx * angleStep;
                int cx = tx + (int)(radius * Math.cos(angle));
                int cy = ty + (int)(radius * Math.sin(angle));
                Rectangle slot = new Rectangle(cx - slotSize/2, cy - slotSize/2, slotSize, slotSize);
                if (slot.contains(x, y)) {
                    Insect_Tektonizator tek = (Insect_Tektonizator) insects.get(idx);
                    // now crack
                    if (t instanceof Tecton_Base) {
                        int confirm = view.showStyledOptionDialog(
                        "Destroy base and end game?", "Confirm Destruction",
                        new String[]{"Yes","No"});
                    if (confirm == 0) {
                        tek.tectonCrack();
                        model.endGame();    // <— invoke saving logic
                        view.showGameOverDialog(
                        buildGameOverMessage(),
                        new String[]{"Back to Main Menu","Exit Game"},
                        this::returnToMenu,
                        () -> System.exit(0)
                        );
                    } else {
                        view.showStyledMessageDialog(
                            "Crack cancelled.",
                            "Cancelled", JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } else {
                    tek.tectonCrack();
                    view.showStyledMessageDialog(
                        "Cracked tecton successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE
                    );
                }
                // reset state
                selectedInsect = null;
    currentState = InteractionState.NORMAL;
    return;
}

            }
            // clicked on the hex but outside any crack‐slot
            view.showStyledMessageDialog("Click directly on a Tektonizator slot to use it.",
                                         "No Tektonizator Selected", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }
}

    private String buildGameOverMessage() {
        Player p1 = model.getPlayer1();
        Player p2 = model.getPlayer2();
        String msg = "Game Over!\n" + p1.getName()+": "+p1.getScore()+"\n"+p2.getName()+": "+p2.getScore();
        return msg;
    }

    private void returnToMenu() {
        view.dispose();
        new MainMenu();
    }

    /** Shuts down the executor service. Call this when InteractionManager is no longer needed. */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            // Optionally, await termination:
            // try {
            //     if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            //         executorService.shutdownNow();
            //     }
            // } catch (InterruptedException e) {
            //     executorService.shutdownNow();
            //     Thread.currentThread().interrupt();
            // }
        }
    }

    /**
 * Gets the cost for an entity based on its type
 * @param type The entity type string
 * @param isMushroom Whether the entity is a mushroom (true) or insect (false)
 * @return The cost of the entity
 */
private int getEntityCost(String type, boolean isMushroom) {
    // Return appropriate costs for each entity type
    if (isMushroom) {
        switch (type) {
            case "Shroomlet": return Mushroom_Shroomlet.VIEWCOST;
            case "Maximus": return Mushroom_Maximus.VIEWCOST;
            case "Slender": return Mushroom_Slender.VIEWCOST;
            default: return 0;
        }
    } else {
        switch (type) {
            case "Buglet": return Insect_Buglet.VIEWCOST;
            case "Buggernaut": return Insect_Buggernaut.VIEWCOST;
            case "Stinger": return Insect_Stinger.VIEWCOST;
            case "Tektonizator": return Insect_Tektonizator.VIEWCOST;
            case "ShroomReaper": return Insect_ShroomReaper.VIEWCOST;
            default: return 0;
        }
    }
}
}
