package com.coderunnerlovagjai.app;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javazoom.jl.player.Player;
import java.io.IOException;
import java.util.Random;

public class MainMenu extends FrameStyle {
    private LeaderBoardFrame lbFrame;
    private JButton play, leaderboard, exit, submit, back;
    private JButton soundButton;
    private boolean soundOn;
    private JLabel u1l, u2l;
    private JTextField u1t, u2t;
    private JPanel entry;

    // Sound icons
    private ImageIcon soundIcon, noSoundIcon;
    // MP3 playback
    private Player player;
    private Thread musicThread;

    /**
     * Constructs the main menu frame, initializes audio and builds UI components.
     */
    public MainMenu() {
        super("Fungorium", "/images/fungoriumIcon3.png");
        initAudio();     // start music
        buildUI();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
    }

    /**
     * Initializes and starts background music playback in a loop using JLayer.
     */
    private void initAudio() {
        soundOn = true;
        Random random = new Random();
        int r = random.nextInt(10);
        musicThread = new Thread(() -> {
            try {
                while (soundOn) {
                    if (r == 7) {
                        try (InputStream is = getClass().getResourceAsStream("/sounds/chillsong2.mp3")) {
                            if (is == null) break;
                            player = new Player(is);
                            player.play();
                        }
                    } else {
                        try (InputStream is = getClass().getResourceAsStream("/sounds/chillsong1.mp3")) {
                            if (is == null) break;
                            player = new Player(is);
                            player.play();
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Audio playback error: " + e.getMessage());
            }
        });
        musicThread.setDaemon(true);
        musicThread.start();
    }

    /**
     * Builds and lays out all UI components: sound toggle, title, buttons, entry panel, footer.
     */
    @Override
    protected void buildUI() {
        // --- 0) Sound toggle button ---
        try {
            ImageIcon rawSound = new ImageIcon(getClass().getResource("/images/sound.png"));
            soundIcon = new ImageIcon(rawSound.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            ImageIcon rawNoSound = new ImageIcon(getClass().getResource("/images/nosound.png"));
            noSoundIcon = new ImageIcon(rawNoSound.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        } catch (Exception ignored) {}
        soundButton = new JButton(soundIcon);
        soundButton.setBorderPainted(false);
        soundButton.setContentAreaFilled(false);
        soundButton.setFocusPainted(false);
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.add(soundButton);
        header.add(Box.createHorizontalGlue());
        content.add(header);
        content.add(Box.createVerticalStrut(10));
        soundButton.addActionListener(e -> {
            soundOn = !soundOn;
            if (!soundOn && player != null) {
                player.close();
            } else if (soundOn) {
                initAudio();
            }
            if (soundIcon != null && noSoundIcon != null) {
                soundButton.setIcon(soundOn ? soundIcon : noSoundIcon);
            }
        });

        // --- 1) Title ---
        ImageIcon raw = new ImageIcon(getClass().getResource("/images/fungoriumIcon3.png"));
        Image small = raw.getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH);
        JLabel title = new JLabel(new ImageIcon(small), SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 256));
        title.setPreferredSize(new Dimension(450, 256));
        content.add(title);
        content.add(Box.createVerticalStrut(50));

        // --- 2) Main menu buttons ---
        play        = createMenuButton("Play");
        leaderboard = createMenuButton("Leaderboard");
        exit        = createMenuButton("Exit");
        submit      = createMenuButton("Submit");
        back        = createMenuButton("Back to Menu");

        JButton[] all = { play, leaderboard, exit, submit, back };
        int mw = 0, mh = 0;
        for (JButton b : all) {
            Dimension d = b.getPreferredSize();
            mw = Math.max(mw, d.width);
            mh = Math.max(mh, d.height);
        }
        for (JButton b : all) {
            b.setPreferredSize(new Dimension(mw, mh));
            b.setMaximumSize(new Dimension(mw, mh));
        }

        content.add(play);
        content.add(Box.createVerticalStrut(20));
        content.add(leaderboard);
        content.add(Box.createVerticalStrut(20));
        content.add(exit);

        // --- 3) ENTRY PANEL (centered) ---
        u1l = new JLabel("Player 1 name:");
        u2l = new JLabel("Player 2 name:");
        Font lblFont = new Font("SansSerif", Font.BOLD, 18);
        u1l.setFont(lblFont);
        u2l.setFont(lblFont);
        u1l.setForeground(Color.WHITE);
        u2l.setForeground(Color.WHITE);
        u1l.setHorizontalAlignment(SwingConstants.RIGHT);
        u2l.setHorizontalAlignment(SwingConstants.RIGHT);

        u1t = new JTextField(10);
        u2t = new JTextField(10);
        Font tfFont = new Font("SansSerif", Font.PLAIN, 18);
        Color tfBg = new Color(0x2E2E2E);
        Color tfFg = Color.WHITE;
        Border tfBorder = BorderFactory.createCompoundBorder(
            new RoundedBorder(8),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );
        for (JTextField tf : new JTextField[] { u1t, u2t }) {
            tf.setFont(tfFont);
            tf.setBackground(tfBg);
            tf.setForeground(tfFg);
            tf.setCaretColor(tfFg);
            tf.setBorder(tfBorder);
        }

        entry = new JPanel(new GridBagLayout());
        entry.setOpaque(false);
        entry.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        entry.add(u1l, gbc);
        gbc.gridx = 1;
        entry.add(u1t, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        entry.add(u2l, gbc);
        gbc.gridx = 1;
        entry.add(u2t, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        entry.add(submit, gbc);
        gbc.gridy = 3;
        entry.add(back, gbc);
        entry.setVisible(false);
        content.add(Box.createVerticalStrut(30));
        content.add(entry);
        content.add(Box.createVerticalGlue());

        // --- 4) Footer icons & text ---
        JPanel icons = new JPanel();
        icons.setOpaque(false);
        icons.setLayout(new BoxLayout(icons, BoxLayout.X_AXIS));
        icons.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            BufferedImage bi = ImageIO.read(getClass().getResource("/images/Insect_Buggernaut.png"));
            Image r = rotateImage(bi.getScaledInstance(32, 32, Image.SCALE_SMOOTH), -20);
            icons.add(new JLabel(new ImageIcon(r)));
        } catch (IOException ignored) {}
        icons.add(Box.createHorizontalGlue());
        try {
            BufferedImage si = ImageIO.read(getClass().getResource("/images/Mushroom_Shroomlet.png"));
            Image r = rotateImage(si.getScaledInstance(32, 32, Image.SCALE_SMOOTH), 20);
            icons.add(new JLabel(new ImageIcon(r)));
        } catch (IOException ignored) {}
        content.add(icons);
        content.add(Box.createVerticalStrut(20));
        JLabel foot = new JLabel("© 2025, Borisz, Milán, Balázs, Mirkó, Zoltán", SwingConstants.CENTER);
        foot.setForeground(Color.LIGHT_GRAY);
        foot.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(foot);

        // --- 5) Listeners ---
        exit.addActionListener(e -> System.exit(0));
        play.addActionListener(e -> {
            for (Component c : content.getComponents()) c.setVisible(false);
            entry.setVisible(true);
            for (Component f : entry.getComponents()) f.setVisible(true);
            u1t.setText("");
            u2t.setText("");
        });
        back.addActionListener(e -> {
            for (Component c : content.getComponents()) c.setVisible(true);
            entry.setVisible(false);
        });
        leaderboard.addActionListener(e -> {
            if (lbFrame == null) lbFrame = new LeaderBoardFrame(this);
            lbFrame.setLocationRelativeTo(this);
            lbFrame.setVisible(true);
            setVisible(false);
        });
        submit.addActionListener(e -> handleSubmit());
    }

    /**
     * Handles submission of player names, validating input and showing styled dialogs.
     */
    private void handleSubmit() {
        String p1 = u1t.getText().trim(), p2 = u2t.getText().trim();
        if (p1.isEmpty() || p2.isEmpty()) {
            showStyledMessageDialog(
                "Please enter names for both players!",
                "Missing Info",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        showStyledMessageDialog(
            "Starting game with players: " + p1 + " and " + p2,
            "Game Start",
            JOptionPane.INFORMATION_MESSAGE
        );
        new Game(p1, p2);
    }

    /**
     * A rounded-border implementation for text fields.
     */
    private static class RoundedBorder implements Border {
        private final int radius;
        public RoundedBorder(int radius) { this.radius = radius; }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(radius + 2, radius + 2, radius + 2, radius + 2);
        }
        @Override public boolean isBorderOpaque() { return false; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0x555555));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }
    }
}
