package com.coderunnerlovagjai.app.controller;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
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
        model.turn();
        if (model.isGameOver()) {
            view.showGameOverDialog(
                buildGameOverMessage(),
                new String[]{"Back to Main Menu", "Exit Game"},
                this::returnToMenu,
                () -> System.exit(0)
            );
        } else {
            onStartTurn();
        }
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

    /** Fills the bottom panel with mushroom or insect options. */
    public void populateEntityBoxes(JPanel panel) {
        panel.removeAll();
        Player p = model.getPlayer(model.currentTurnsPlayer());
        boolean isMush = p.getRole() == RoleType.MUSHROOM;
        String[] types = isMush
            ? new String[]{"Shroomlet","Maximus","Slender"}
            : new String[]{"Buglet","Buggernaut","Stinger","Tektonizator","ShroomReaper"};
        String prefix = isMush ? "Mushroom_" : "Insect_";

        for (int i = 0; i < types.length; i++) {
            final int idx = i;
            JPanel box = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(idx == selectedEntityIndex
                        ? new Color(60,180,255,120)
                        : new Color(200,200,200,100)
                    );
                    g.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                }
            };
            box.setBounds(i*110,0,100,100);
            box.setLayout(null);
            try {
                Image img = ImageIO.read(
                    getClass().getClassLoader().getResourceAsStream("images/"+prefix+types[i]+".png")
                );
                if (img != null) {
                    JLabel icon = new JLabel(new ImageIcon(
                        img.getScaledInstance(64,64,Image.SCALE_SMOOTH)
                    ));
                    icon.setBounds(18,10,64,64);
                    box.add(icon);
                }
            } catch (IOException ignored) {}
            box.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectedEntityIndex = idx;
                    view.refreshInfo();
                }
            });
            panel.add(box);
        }
        panel.revalidate();
        panel.repaint();
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
        player.decreaseIncome(cost);
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
        if (model.getPlane().placeInsect(insect, tecton)) {
            player.setAction(player.getAction()-1);
            player.decreaseIncome(cost);
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
                            "Error moving: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE
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
                            view.showGameOverDialog(buildGameOverMessage(),
                                new String[]{"Back to Main Menu","Exit Game"},
                                this::returnToMenu, ()->System.exit(0));
                        }
                    } else if (!t.canBeCracked()) {
                        view.showStyledMessageDialog("Cannot crack this tecton.",
                                                     "Invalid Target", JOptionPane.WARNING_MESSAGE);
                    } else {
                        tek.tectonCrack();
                        p.setAction(p.getAction() - 1);
                        view.showStyledMessageDialog("Tecton cracked!",
                                                     "Success", JOptionPane.INFORMATION_MESSAGE);
                        view.refreshInfo();
                    }
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
}
