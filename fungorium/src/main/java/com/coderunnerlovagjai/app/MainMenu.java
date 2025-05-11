package com.coderunnerlovagjai.app;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints; // Added for hover effect
import java.awt.GridBagLayout;   // Added for hover effect
import java.awt.Image; // For rotating images
import java.awt.Insets; // For rotating images
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;  // for rotating silhouette images
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField; // Added for focus traversal policy
import javax.swing.SwingConstants; // For animation
import javax.swing.Timer;
import javax.swing.border.CompoundBorder; // For custom painting
import javax.swing.border.EmptyBorder; // For silhouette transparency

public class MainMenu extends JFrame {
    private LeaderBoardFrame lbFrame;
    private JButton startGameButton;
    private JButton showLeaderboardButton;
    private JButton exitMenuButton;
    private JButton submitButton;
    private JButton backButton;
    private JLabel user1Label;
    private JTextField user1TextField;
    private JLabel user2Label;
    private JTextField user2TextField;

    // custom colors
    private static final Color BG_COLOR       = new Color(0x181818);
    private static final Color BUTTON_COLOR   = new Color(0x34978D);
    private static final Color BUTTON_SHADOW  = new Color(0x2A7471);
    private static final Font  TITLE_FONT     = new Font("SansSerif", Font.BOLD, 48);
    private static final Font  BUTTON_FONT    = new Font("SansSerif", Font.BOLD, 32);

 public MainMenu() {
     super("Fungorium");
     setDefaultCloseOperation(EXIT_ON_CLOSE);
     setSize(500, 600);
     setLocationRelativeTo(null);

     // build our custom content pane with animated silhouettes
     final int COUNT = 4;
     JPanel content = new JPanel() {
         private int[] bx = new int[COUNT], by = new int[COUNT];
         private int[] sx = new int[COUNT], sy = new int[COUNT];
         private double[] br = new double[COUNT], sr = new double[COUNT];
         private BufferedImage bugImg, shImg;
         {
             try {
                 bugImg = ImageIO.read(getClass().getResource("/images/Insect_Buggernaut.png"));
                 shImg  = ImageIO.read(getClass().getResource("/images/Mushroom_Shroomlet.png"));
             } catch (IOException ex) { /* ignore */ }
             Random r = new Random();
             int w = getWidth(), h = getHeight();
             for (int i = 0; i < COUNT; i++) {
                 bx[i] = (i % 2 == 0 ? 10 : w - 110);
                 by[i] = -r.nextInt(h+1);
                 br[i] = r.nextDouble() * 40 - 20;
                 sx[i] = (i % 2 == 0 ? w - 110 : 10);
                 sy[i] = -r.nextInt(h+1);
                 sr[i] = r.nextDouble() * 40 - 20;
             }
             new Timer(40, (ActionListener) e -> {
                 for (int i = 0; i < COUNT; i++) {
                     by[i] += 2; if (by[i] > getHeight()) by[i] = -100;
                     sy[i] += 2; if (sy[i] > getHeight()) sy[i] = -100;
                 }
                 repaint();
             }).start();
         }
         @Override protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             Graphics2D g2 = (Graphics2D) g.create();
             g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
             int iw = 100, ih = 100;
             for (int i = 0; i < COUNT; i++) {
                 AffineTransform old = g2.getTransform();
                 g2.rotate(Math.toRadians(br[i]), bx[i] + iw / 2.0, by[i] + ih / 2.0);
                 g2.drawImage(bugImg, bx[i], by[i], iw, ih, null);
                 g2.setTransform(old);
                 g2.rotate(Math.toRadians(sr[i]), sx[i] + iw / 2.0, sy[i] + ih / 2.0);
                 g2.drawImage(shImg, sx[i], sy[i], iw, ih, null);
                 g2.setTransform(old);
             }
             g2.dispose();
         }
     };
     content.setBackground(BG_COLOR);
     content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
     content.setBorder(new EmptyBorder(20, 20, 20, 20));
     setContentPane(content);

     // 1) Load the raw icon
     ImageIcon rawIcon = new ImageIcon(getClass()
     .getResource("/images/fungoriumIcon.png")); 

     // 2) Scale it down to (say) 64×64
     Image scaled = rawIcon.getImage()
     .getScaledInstance(256, 256, Image.SCALE_SMOOTH);
     ImageIcon smallIcon = new ImageIcon(scaled);

     // 1) Title block
     JLabel title = new JLabel("", SwingConstants.CENTER);
     title.setForeground(Color.BLACK);
     title.setAlignmentX(Component.CENTER_ALIGNMENT);
     title.setFont(TITLE_FONT);
     // 4) Prevent it from ballooning to fill the whole pane
     title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 256)); // Max height for the icon
     title.setPreferredSize(new Dimension(450, 256)); // Preferred size for the icon
     title.setIcon(smallIcon);
     title.setIconTextGap(10);
     title.setOpaque(false); // Make background transparent
     // title.setBackground(TITLE_BG); // Keep commented out
     // title.setBorder(new CompoundBorder( // Keep commented out
     // new LineBorder(TITLE_BG.darker(), 1), 
     // new EmptyBorder(10, 20, 10, 20)
     // )); // Keep commented out
     content.add(title);

     content.add(Box.createVerticalStrut(50));

     // Create ALL buttons first
     // 2) Main menu buttons
     startGameButton       = createMenuButton("Play");
     showLeaderboardButton = createMenuButton("Leaderboard");
     exitMenuButton        = createMenuButton("Exit");
     
     // 3) Player entry fields' buttons (needed for unified sizing)
     submitButton    = createMenuButton("Submit");
     backButton      = createMenuButton("Back to Menu");

     // --- Standardize ALL button sizes ---
     // This block replaces the previous separate sizing attempts
     JButton[] allButtons = {
         startGameButton, showLeaderboardButton, exitMenuButton, 
         submitButton, backButton // All buttons are now initialized
     };

     int maxWidth = 0;
     int maxHeight = 0;
     for (JButton btn : allButtons) {
         Dimension prefSize = btn.getPreferredSize(); // Should no longer cause NPE
         if (prefSize.width > maxWidth) {
             maxWidth = prefSize.width;
         }
         if (prefSize.height > maxHeight) {
             maxHeight = prefSize.height;
         }
     }
     Dimension uniformButtonSize = new Dimension(maxWidth, maxHeight);
     for (JButton btn : allButtons) {
         btn.setPreferredSize(uniformButtonSize);
         btn.setMaximumSize(uniformButtonSize); // Crucial for BoxLayout to respect preferred size
     }
     // --- End of button standardization ---

     // Add main menu buttons now that they are sized
     content.add(startGameButton);
     content.add(Box.createVerticalStrut(20));
     content.add(showLeaderboardButton);
     content.add(Box.createVerticalStrut(20));
     content.add(exitMenuButton);
     content.add(Box.createVerticalGlue());

     // 4) Bottom icons panel
     JPanel iconPanel = new JPanel();
     iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
     iconPanel.setOpaque(false);
     iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
     content.add(Box.createVerticalStrut(20)); // Space above icons
     
     // Load, scale to 32×32, and rotate Insect_Buggernaut icon
     try {
         BufferedImage bugRaw = ImageIO.read(getClass().getResource("/images/Insect_Buggernaut.png"));
         Image bugScaled = bugRaw.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
         Image bugRotated = rotateImage(bugScaled, -20);
         iconPanel.add(new JLabel(new ImageIcon(bugRotated)));
     } catch (IOException e) {
         e.printStackTrace();
     }
     iconPanel.add(Box.createHorizontalGlue());
     
     // Load, scale to 32×32, and rotate Mushroom_Shroomlet icon
     try {
         BufferedImage shroomRaw = ImageIO.read(getClass().getResource("/images/Mushroom_Shroomlet.png"));
         Image shroomScaled = shroomRaw.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
         Image shroomRotated = rotateImage(shroomScaled, 20);
         iconPanel.add(new JLabel(new ImageIcon(shroomRotated)));
     } catch (IOException e) {
         e.printStackTrace();
     }
     
     content.add(iconPanel);
     content.add(Box.createVerticalStrut(20));
     JLabel footer = new JLabel("© 2025, Borisz, Milán, Balázs, Mirkó, Terék", SwingConstants.CENTER);
     footer.setForeground(Color.LIGHT_GRAY);
     footer.setAlignmentX(Component.CENTER_ALIGNMENT);
     content.add(footer);

     // Player entry fields (JLabels and JTextFields)
     user1Label      = new JLabel("Player 1 name:");
     user1Label.setForeground(Color.WHITE);
     user1TextField  = new JTextField(10);
     user2Label      = new JLabel("Player 2 name:");
     user2Label.setForeground(Color.WHITE);
     user2TextField  = new JTextField(10);
     // submitButton and backButton are already created and sized

     JPanel entry = new JPanel();
     entry.setOpaque(false);
     entry.setLayout(new GridBagLayout());
     GridBagConstraints gbc = new GridBagConstraints();
     gbc.insets = new Insets(8,8,8,8);
     gbc.gridx = 0; gbc.gridy = 0;
     entry.add(user1Label, gbc);
     gbc.gridx = 1;
     entry.add(user1TextField, gbc);
     gbc.gridy = 1; gbc.gridx = 0;
     entry.add(user2Label, gbc);
     gbc.gridx = 1;
     entry.add(user2TextField, gbc);
     gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
     entry.add(submitButton, gbc);
     gbc.gridy = 3;
     entry.add(backButton, gbc);

     // make them invisible until Play is clicked
     for (Component c : entry.getComponents()) c.setVisible(false);
     content.add(entry);

     // --- wiring up listeners (exactly as before) ---
     exitMenuButton.addActionListener(e -> System.exit(0));
     startGameButton.addActionListener(e -> showPlayerEntryScreen());
     showLeaderboardButton.addActionListener(e -> {
         if (lbFrame == null) lbFrame = new LeaderBoardFrame(this);
         lbFrame.setLocationRelativeTo(this);
         lbFrame.setVisible(true);
         setVisible(false);
     });
     backButton.addActionListener(e -> showMainMenu());
     submitButton.addActionListener(e -> {
         String p1 = user1TextField.getText().trim();
         String p2 = user2TextField.getText().trim();
         if (p1.isEmpty() || p2.isEmpty()) {
             JOptionPane.showMessageDialog(this, 
                 "Please enter names for both players!", 
                 "Missing Information", 
                 JOptionPane.WARNING_MESSAGE
             );
             return;
         }
         JOptionPane.showMessageDialog(this, 
             "Starting game with players: " + p1 + " and " + p2
         );
         // TODO: actually launch your GameBoard here
     });

     setVisible(true);
     setContentPane(content);
     pack();                    // shrink‐wrap to your preferred sizes
     setLocationRelativeTo(null);
     // Use container-order policy to prevent sorting errors
     setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
     setVisible(true);
 }

 private JButton createMenuButton(String text) {
     JButton b = new JButton(text);
     b.setFont(BUTTON_FONT);
     final Color originalColor = BUTTON_COLOR;
     // A slightly brighter color for hover, can be adjusted
     final Color hoverColor = originalColor.brighter(); 
     b.setBackground(originalColor);
     b.setForeground(Color.WHITE);
     b.setFocusPainted(false);
     b.setAlignmentX(Component.CENTER_ALIGNMENT);
     // big padding + “shadow” line on bottom
     b.setBorder(new CompoundBorder(
         BorderFactory.createMatteBorder(0,0,4,0, BUTTON_SHADOW),
         BorderFactory.createEmptyBorder(15, 40, 15, 40)
     ));

     b.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent evt) {
             b.setBackground(hoverColor);
         }

         @Override
         public void mouseExited(MouseEvent evt) {
             b.setBackground(originalColor);
         }
     });
     return b;
 }

 private void showPlayerEntryScreen() {
     for (Component c : getContentPane().getComponents()) {
         if (c instanceof JPanel && ((JPanel)c).getComponentCount() > 0 && 
             ((JPanel)c).getComponent(0) == user1Label) {
             // this is our entry panel
             for (Component f : ((JPanel)c).getComponents()) f.setVisible(true);
         } else {
             c.setVisible(false);
         }
     }
     user1TextField.setText("");
     user2TextField.setText("");
 }

 private void showMainMenu() {
     for (Component c : getContentPane().getComponents()) {
         c.setVisible(true);
         // hide entry panel’s children
         if (c instanceof JPanel && ((JPanel)c).getComponentCount() > 0 && 
             ((JPanel)c).getComponent(0) == user1Label) {
             for (Component f : ((JPanel)c).getComponents()) f.setVisible(false);
         }
     }
 }

 /**
  * Rotate an Image by specified degrees around its center.
  */
 private Image rotateImage(Image src, double degrees) {
     int w = src.getWidth(null);
     int h = src.getHeight(null);
     // Fallback if image dimensions are not yet available
     if (w <= 0 || h <= 0) {
         w = 32;
         h = 32;
     }
     BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
     Graphics2D g2 = result.createGraphics();
     g2.rotate(Math.toRadians(degrees), w / 2.0, h / 2.0);
     g2.drawImage(src, 0, 0, w, h, null);
     g2.dispose();
     return result;
 }

}
