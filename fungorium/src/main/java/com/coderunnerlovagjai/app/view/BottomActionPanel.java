
// BottomActionPanel.java
// Az alsó akciósáv, ahol a játékos entitásokat választhat és akciógombokat használhat (kör vége, mozgás, törés)
package com.coderunnerlovagjai.app.view;

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.coderunnerlovagjai.app.Game;
import com.coderunnerlovagjai.app.RoleType;
import com.coderunnerlovagjai.app.controller.InteractionManager;


/**
 * Az alsó akciósáv panelje, amely tartalmazza az entitásválasztó panelt és az akciógombokat.
 * Itt tud a játékos gombát/rovarokat választani, illetve kör vége, mozgás, törés gombokat használni.
 */
public class BottomActionPanel extends JPanel {
    // A játék modell példánya
    private final Game gameModel;
    // A vezérlő (InteractionManager), amely a felhasználói interakciókat kezeli
    private final InteractionManager controller;
    // Az entitásválasztó panel (gombák/rovarok)
    private final JPanel entityPanel;
    // Akciógombok: kör vége, mozgás, törés
    private final JButton endTurnBtn, moveBtn, crackBtn;


    /**
     * Konstruktor: inicializálja a panelt, létrehozza az entitásválasztó panelt és az akciógombokat.
     * @param model A játék modell
     * @param ctrl  Az interakciókezelő
     */
    public BottomActionPanel(Game model, InteractionManager ctrl) {
        this.gameModel = model;
        this.controller = ctrl;
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(800, 120));

        // Entitásválasztó panel (gombák/rovarok)
        entityPanel = new JPanel(null);
        entityPanel.setOpaque(false);
        entityPanel.setBounds(100, 10, 550, 90);
        add(entityPanel);

        // Akciógombok létrehozása
        endTurnBtn = createButton(670, 10, 80, 80, "/images/endTurnButton.png", e -> controller.onEndTurn());
        moveBtn    = createButton(15, 5, 40, 40,  "/images/moveInsectButton.png", e -> controller.startInsectMovement());
        crackBtn   = createButton(15, 55, 40, 40, "/images/TectonCrackButton.png", e -> controller.startTectonCrack());
        add(endTurnBtn);
        add(moveBtn);
        add(crackBtn);

        updateButtonVisibility(); // Gombok láthatóságának frissítése
        updateEntities();         // Entitásválasztó panel frissítése
    }


    /**
     * Segédfüggvény egy képes, átlátszó, eseménykezelős gomb létrehozásához.
     * @param x X pozíció
     * @param y Y pozíció
     * @param w szélesség
     * @param h magasság
     * @param icon ikon elérési útja
     * @param al eseménykezelő
     * @return a létrehozott JButton
     */
    private JButton createButton(int x, int y, int w, int h, String icon, java.awt.event.ActionListener al) {
        JButton b = new JButton();
        b.setBounds(x, y, w, h);
        b.setFocusable(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.addActionListener(al);
        try {
            Image img = ImageIO.read(getClass().getResourceAsStream(icon));
            b.setIcon(new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
        } catch (IOException | NullPointerException ex) {
            // Ha nincs kép, nem állítunk ikont
        }
        return b;
    }


    /**
     * Frissíti az entitásválasztó panelt (gombák/rovarok), és a gombok láthatóságát is.
     */
    public void updateEntities() {
        entityPanel.removeAll();
        controller.populateEntityBoxes(entityPanel);
        updateButtonVisibility(); // Gombok láthatóságának frissítése
        entityPanel.revalidate();
        entityPanel.repaint();
    }
    

    /**
     * Frissíti az akciógombok láthatóságát az aktuális játékos szerepe alapján.
     * Csak akkor látható a mozgás és törés gomb, ha a játékos rovarász.
     */
    public void updateButtonVisibility() {
        // Lekérjük az aktuális játékos szerepét
        RoleType currentRole = gameModel.getPlayer(gameModel.currentTurnsPlayer()).getRole();
        // Csak akkor látható a mozgás és törés gomb, ha rovarász a játékos
        boolean isInsect = (currentRole == RoleType.INSECT);
        moveBtn.setVisible(isInsect);
        crackBtn.setVisible(isInsect);
    }
}