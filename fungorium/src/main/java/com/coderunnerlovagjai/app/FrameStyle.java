package com.coderunnerlovagjai.app;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

public abstract class FrameStyle extends JFrame {
    protected static final Color BG_COLOR       = new Color(0x181818);
    protected static final Color BUTTON_COLOR   = new Color(0x34978D);
    protected static final Color BUTTON_SHADOW  = new Color(0x2A7471);
    protected static final Font  TITLE_FONT     = new Font("SansSerif", Font.BOLD, 48);
    protected static final Font  BUTTON_FONT    = new Font("SansSerif", Font.BOLD, 32);

    protected JPanel content;

    /**
     * Constructs a styled frame with animated background, common colors/fonts, and window icon.
     * @param title the window title
     * @param iconResource path to the icon resource
     */
    public FrameStyle(String title, String iconResource) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        // window icon
        ImageIcon ico = new ImageIcon(getClass().getResource(iconResource));
        setIconImage(ico.getImage());

        content = createAnimatedPanel();
        content.setBackground(BG_COLOR);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20,20,20,20));
        setContentPane(content);
    }

    /**
     * Creates a JPanel that animates falling, rotating silhouette images in the background.
     * @return an animated JPanel for use as content pane
     */
    private JPanel createAnimatedPanel() {
        final int COUNT = 4;
        return new JPanel() {
            private int[] bx = new int[COUNT], by = new int[COUNT];
            private int[] sx = new int[COUNT], sy = new int[COUNT];
            private double[] br = new double[COUNT], sr = new double[COUNT];
            private BufferedImage bugImg, shImg;
            private BufferedImage tmpImg;
            // per-instance type: true if this slot uses tmp silhouette
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
                } catch(IOException ex){}
                addComponentListener(new ComponentAdapter(){
                    public void componentResized(ComponentEvent e){
                        if(!init && getWidth()>0 && getHeight()>0){
                            Random r = new Random();
                            int w=getWidth(),h=getHeight(),iw=100,ih=100;
                            for(int i=0;i<COUNT;i++){
                                bx[i] = r.nextInt(Math.max(w - iw + 1, 1));
                                by[i] = r.nextInt(h + ih + 1) - ih;
                                br[i] = r.nextDouble() * 40 - 20;
                                sx[i] = r.nextInt(Math.max(w - iw + 1, 1));
                                sy[i] = r.nextInt(h + ih + 1) - ih;
                                sr[i] = r.nextDouble() * 40 - 20;
                                istmp[i] = (tmpImg != null && r.nextDouble() < 0.1);
                            }
                            init=true;
                        }
                    }
                });
                // animate positions and respawn off-screen with new randomization
                final Random rnd = new Random();
                final int iw = 100, ih = 100;
                new Timer(40, e->{
                    int w = getWidth(), h = getHeight();
                    for(int i = 0; i < COUNT; i++) {
                        // fall
                        by[i] += 2;
                        if (by[i] > h) {
                            // respawn above with new random X, rotation, and tmp chance
                            by[i] = -ih;
                            bx[i] = rnd.nextInt(Math.max(w - iw + 1, 1));
                            br[i] = rnd.nextDouble() * 40 - 20;
                            istmp[i] = (tmpImg != null && rnd.nextDouble() < 0.1);
                        }
                        // secondary silhouettes
                        sy[i] += 2;
                        if (sy[i] > h) sy[i] = -ih;
                    }
                    repaint();
                }).start();
            }
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.05f));
                int iw=100,ih=100;
                for(int i=0;i<COUNT;i++){
                    AffineTransform old = g2.getTransform();
                    g2.rotate(Math.toRadians(br[i]), bx[i] + iw/2.0, by[i] + ih/2.0);
                    // draw either bug or tmp based on fixed flag
                    if (istmp[i]) {
                        g2.drawImage(tmpImg, bx[i], by[i], iw, ih, null);
                    } else {
                        g2.drawImage(bugImg, bx[i], by[i], iw, ih, null);
                    }
                    g2.setTransform(old);
                    g2.rotate(Math.toRadians(sr[i]),sx[i]+iw/2.0,sy[i]+ih/2.0);
                    g2.drawImage(shImg,sx[i],sy[i],iw,ih,null);
                    g2.setTransform(old);
                }
                g2.dispose();
            }
        };
    }

    /**
     * Creates a uniformly styled menu button with hover color change effect.
     * @param text the button label
     * @return a styled JButton
     */
    protected JButton createMenuButton(String text) {
        JButton b=new JButton(text);
        b.setFont(BUTTON_FONT);
        Color orig=BUTTON_COLOR, hover=orig.brighter();
        b.setBackground(orig);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0,0,4,0,BUTTON_SHADOW),
            BorderFactory.createEmptyBorder(15,40,15,40)
        ));
        b.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){b.setBackground(hover);}
            public void mouseExited(MouseEvent e){b.setBackground(orig);}
        });
        return b;
    }

    /**
     * Rotates an image by the specified degrees around its center, returning a new Image.
     * @param src source image to rotate
     * @param deg degrees to rotate
     * @return the rotated image
     */
    protected Image rotateImage(Image src, double deg) {
        int w=src.getWidth(null),h=src.getHeight(null);
        if(w<=0||h<=0){w=32;h=32;}
        BufferedImage out=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2=out.createGraphics();
        g2.rotate(Math.toRadians(deg),w/2.0,h/2.0);
        g2.drawImage(src,0,0,w,h,null);
        g2.dispose();return out;
    }

    /**
     * Abstract method for subclasses to build and layout their specific UI components.
     */
    protected abstract void buildUI();

}