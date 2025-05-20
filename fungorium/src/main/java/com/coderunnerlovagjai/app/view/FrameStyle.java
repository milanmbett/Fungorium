package com.coderunnerlovagjai.app.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public abstract class FrameStyle extends JFrame {
    protected static final Color BG_COLOR      = new Color(0x181818);
    protected static final Color BUTTON_COLOR  = new Color(0x34978D);
    protected static final Color BUTTON_SHADOW = new Color(0x2A7471);
    protected static final Font  TITLE_FONT    = new Font("SansSerif", Font.BOLD, 48);
    protected static final Font  BUTTON_FONT   = new Font("SansSerif", Font.BOLD, 32);

    // ─── globally skin all JOptionPanes ───────────────────────────────────────
    static {
        // global dialog styling
        UIManager.put("OptionPane.background",       new ColorUIResource(BG_COLOR));
        UIManager.put("Panel.background",            new ColorUIResource(BG_COLOR));
        UIManager.put("OptionPane.messageForeground",new ColorUIResource(Color.WHITE));
        UIManager.put("OptionPane.messageFont",      new FontUIResource(new Font("SansSerif", Font.BOLD, 18)));
        UIManager.put("OptionPane.buttonFont",       new FontUIResource(BUTTON_FONT));
        UIManager.put("Button.background",           new ColorUIResource(BUTTON_COLOR));
        UIManager.put("Button.foreground",           new ColorUIResource(Color.WHITE));
        // default border is still bottom-shadow; we override per-button below
        UIManager.put("Button.border", new CompoundBorder(
            BorderFactory.createMatteBorder(0,0,4,0,BUTTON_SHADOW),
            BorderFactory.createEmptyBorder(15,40,15,40)
        ));
    }

    protected JPanel content;

    /**
     * Constructs a styled frame with animated background, common colors/fonts, and window icon.
     */
      public FrameStyle(String title, String iconResource) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,600);
        setLocationRelativeTo(null);
        ImageIcon ico = new ImageIcon(getClass().getResource(iconResource));
        setIconImage(ico.getImage());

        content = createAnimatedPanel();
        content.setBackground(BG_COLOR);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20,20,20,20));
        setContentPane(content);
    }
    // ─── public API for styled dialogs ────────────────────────────────────────

    /**
     * Shows a message dialog on top of the animated background, with styled buttons.
     */
    public void showStyledMessageDialog(String msg, String title, int type) {
        JOptionPane pane = new JOptionPane(msg, type, JOptionPane.DEFAULT_OPTION);
        makeTransparent(pane);
        JDialog dlg = pane.createDialog(this, title);

        JPanel anim = createAnimatedPanel();
        anim.setLayout(new BorderLayout());
        anim.add(pane, BorderLayout.CENTER);

        styleOptionPaneButtons(anim);

        dlg.setContentPane(anim);
        dlg.pack();
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(this);
        dlg.setModal(true);
        dlg.setVisible(true);
    }
    /**
     * Shows a confirm dialog on top of the animated background, with styled buttons.
     * @return the user’s choice (YES/NO/CANCEL)
     */

    protected int showStyledConfirmDialog(String msg, String title, int optType, int msgType) {
        JOptionPane pane = new JOptionPane(msg, msgType, optType);
        makeTransparent(pane);
        JDialog dlg = pane.createDialog(this, title);

        JPanel anim = createAnimatedPanel();
        anim.setLayout(new BorderLayout());
        anim.add(pane, BorderLayout.CENTER);

        styleOptionPaneButtons(anim);

        dlg.setContentPane(anim);
        dlg.pack();
        dlg.setResizable(false);
        dlg.setLocationRelativeTo(this);
        dlg.setModal(true);
        dlg.setVisible(true);

        Object v = pane.getValue();
        return v instanceof Integer ? (Integer)v : JOptionPane.CLOSED_OPTION;
    }
    // ─── internally used helpers ──────────────────────────────────────────────

    /** Recursively sets all sub-components non-opaque so the animation can show through */
    private void makeTransparent(JComponent c) {
        c.setOpaque(false);
        for (Component ch : c.getComponents()) {
            if (ch instanceof JComponent) makeTransparent((JComponent)ch);
        }
    }

    /**
     * Applies the MainMenu button style—only smaller padding—to every JButton in the dialog.
     */
    private void styleOptionPaneButtons(Container cont) {
        for (Component comp : cont.getComponents()) {
            if (comp instanceof JButton) {
                JButton b = (JButton) comp;
                b.setFont(BUTTON_FONT);
                b.setBackground(BUTTON_COLOR);
                b.setForeground(Color.WHITE);
                b.setFocusPainted(false);
                // now a 2px border on all sides + tighter padding
                b.setBorder(new CompoundBorder(
                    BorderFactory.createLineBorder(BUTTON_SHADOW, 2),
                    BorderFactory.createEmptyBorder(6, 20, 6, 20)
                ));
            } else if (comp instanceof Container) {
                styleOptionPaneButtons((Container)comp);
            }
        }
    }

    /**
     * Creates a JPanel that animates falling, rotating silhouette images in the background.
     */
    private JPanel createAnimatedPanel() {
        final int COUNT = 4;
        return new JPanel() {
            private int[] bx = new int[COUNT], by = new int[COUNT];
            private int[] sx = new int[COUNT], sy = new int[COUNT];
            private double[] br = new double[COUNT], sr = new double[COUNT];
            private BufferedImage bugImg, shImg, tmpImg;
            private boolean[] istmp = new boolean[COUNT];
            private boolean init = false;
            {
                try {
                    bugImg = ImageIO.read(getClass().getResource("/images/Insect_Buggernaut.png"));
                    shImg  = ImageIO.read(getClass().getResource("/images/Mushroom_Shroomlet.png"));
                    try {
                        tmpImg = ImageIO.read(getClass().getResource("/tmp/tmp.png"));
                    } catch (IOException ex) {
                        tmpImg = null;
                    }
                } catch(IOException ignored){}

                addComponentListener(new ComponentAdapter(){
                    public void componentResized(ComponentEvent e){
                        if (!init && getWidth() > 0 && getHeight() > 0) {
                            Random r = new Random();
                            int w = getWidth(), h = getHeight(), iw = 100, ih = 100;
                            for (int i = 0; i < COUNT; i++) {
                                bx[i]   = r.nextInt(Math.max(w - iw + 1, 1));
                                by[i]   = r.nextInt(h + ih + 1) - ih;
                                br[i]   = r.nextDouble() * 40 - 20;
                                sx[i]   = r.nextInt(Math.max(w - iw + 1, 1));
                                sy[i]   = r.nextInt(h + ih + 1) - ih;
                                sr[i]   = r.nextDouble() * 40 - 20;
                                istmp[i]= (tmpImg != null && r.nextDouble() < 0.1);
                            }
                            init = true;
                        }
                    }
                });

                new Timer(40, e -> {
                    int w = getWidth(), h = getHeight();
                    for (int i = 0; i < COUNT; i++) {
                        by[i] += 2;
                        if (by[i] > h) {
                            by[i] = -100;
                            bx[i] = new Random().nextInt(Math.max(w - 100 + 1, 1));
                            br[i] = new Random().nextDouble() * 40 - 20;
                            istmp[i] = (tmpImg != null && new Random().nextDouble() < 0.1);
                        }
                        sy[i] += 2;
                        if (sy[i] > h) sy[i] = -100;
                    }
                    repaint();
                }).start();
            }

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
                int iw = 100, ih = 100;
                for (int i = 0; i < COUNT; i++) {
                    AffineTransform old = g2.getTransform();
                    g2.rotate(Math.toRadians(br[i]), bx[i] + iw/2.0, by[i] + ih/2.0);
                    if (istmp[i] && tmpImg != null) {
                        g2.drawImage(tmpImg, bx[i], by[i], iw, ih, null);
                    } else {
                        g2.drawImage(bugImg, bx[i], by[i], iw, ih, null);
                    }
                    g2.setTransform(old);

                    g2.rotate(Math.toRadians(sr[i]), sx[i] + iw/2.0, sy[i] + ih/2.0);
                    g2.drawImage(shImg, sx[i], sy[i], iw, ih, null);
                    g2.setTransform(old);
                }
                g2.dispose();
            }
        };
    }

    /**
     * Creates a uniformly styled menu button with hover color change effect.
     */
    protected JButton createMenuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        Color orig = BUTTON_COLOR, hover = orig.brighter();
        b.setBackground(orig);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0,0,4,0,BUTTON_SHADOW),
            BorderFactory.createEmptyBorder(15,40,15,40)
        ));
        b.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ b.setBackground(hover); }
            public void mouseExited(MouseEvent e){ b.setBackground(orig); }
        });
        return b;
    }

    /**
     * Rotates an image by the specified degrees around its center.
     */
    protected Image rotateImage(Image src, double deg) {
        int w = src.getWidth(null), h = src.getHeight(null);
        if (w <= 0 || h <= 0) { w = 32; h = 32; }
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.rotate(Math.toRadians(deg), w/2.0, h/2.0);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return out;
    }

    // In FrameStyle.java, alongside your other styled-dialog methods:

/**
 * Shows a styled option dialog with custom button labels.
 * @param msg the message to display
 * @param title the dialog title
 * @param options an array of button labels
 * @return the index of the button clicked, or CLOSED_OPTION
 */
public int showStyledOptionDialog(String msg, String title, String[] options) {
    // Create a JOptionPane with your custom labels
    JOptionPane pane = new JOptionPane(
        msg,
        JOptionPane.QUESTION_MESSAGE,
        JOptionPane.DEFAULT_OPTION,
        null,             // no custom icon
        options,          // the labels
        options[0]        // default selection
    );

    makeTransparent(pane);
    JDialog dlg = pane.createDialog(this, title);

    // Wrap in animated panel
    JPanel anim = createAnimatedPanel();
    anim.setLayout(new BorderLayout());
    anim.add(pane, BorderLayout.CENTER);
    styleOptionPaneButtons(anim);

    dlg.setContentPane(anim);
    dlg.pack();
    dlg.setResizable(false);
    dlg.setLocationRelativeTo(this);
    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dlg.setModal(true);
    dlg.setVisible(true);

    // Figure out which button was clicked
    Object v = pane.getValue();
    if (v instanceof String) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(v)) return i;
        }
    }
    return JOptionPane.CLOSED_OPTION;
}


    /** Subclasses must implement to build their specific UI. */
    protected abstract void buildUI();
}
