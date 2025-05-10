package com.coderunnerlovagjai.app;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

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
    private static final Color BG_COLOR       = new Color(0x0D392C);
    private static final Color BUTTON_COLOR   = new Color(0x34978D);
    private static final Color BUTTON_SHADOW  = new Color(0x2A7471);
    private static final Color TITLE_BG       = new Color(0xFFF8E7);
    private static final Font  TITLE_FONT     = new Font("SansSerif", Font.BOLD, 48);
    private static final Font  BUTTON_FONT    = new Font("SansSerif", Font.BOLD, 32);

    public MainMenu() {
        super("Fungorium");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        // set the window icon
        ImageIcon ico = new ImageIcon(getClass().getResource("/images/fungoriumIcon.png"));
        setIconImage(ico.getImage());

        // build our custom content pane
        JPanel content = new JPanel();
        content.setBackground(BG_COLOR);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(content);




        // 1) Load the raw icon
        ImageIcon rawIcon = new ImageIcon(getClass()
        .getResource("/images/mushroom_logo.png")); 

        // 2) Scale it down to (say) 64×64
        Image scaled = rawIcon.getImage()
        .getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon smallIcon = new ImageIcon(scaled);

        // 1) Title block
        JLabel title = new JLabel("Fungorium", SwingConstants.CENTER);
        title.setForeground(Color.BLACK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(TITLE_FONT);
        // 4) Prevent it from ballooning to fill the whole pane
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        title.setPreferredSize(new Dimension(450, 80));
        title.setIcon(smallIcon);
        title.setIconTextGap(10);
        title.setOpaque(true);
        title.setBackground(TITLE_BG);
        title.setBorder(new CompoundBorder(
            new LineBorder(TITLE_BG.darker(), 1), 
            new EmptyBorder(10, 20, 10, 20)
        ));
        content.add(title);

        content.add(Box.createVerticalStrut(50));

        // 2) Main menu buttons
        startGameButton       = createMenuButton("Play");
        showLeaderboardButton = createMenuButton("Leaderboard");
        exitMenuButton        = createMenuButton("Exit");

        content.add(startGameButton);
        content.add(Box.createVerticalStrut(20));
        content.add(showLeaderboardButton);
        content.add(Box.createVerticalStrut(20));
        content.add(exitMenuButton);

        content.add(Box.createVerticalGlue());

        // 3) Player entry fields (hidden initially)
        user1Label      = new JLabel("Player 1 name:");
        user1Label.setForeground(Color.WHITE);
        user1TextField  = new JTextField(10);
        user2Label      = new JLabel("Player 2 name:");
        user2Label.setForeground(Color.WHITE);
        user2TextField  = new JTextField(10);
        submitButton    = createMenuButton("Submit");
        backButton      = createMenuButton("Back to Menu");

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
        setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setBackground(BUTTON_COLOR);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        // big padding + “shadow” line on bottom
        b.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0,0,4,0, BUTTON_SHADOW),
            BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
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

}
