package com.coderunnerlovagjai.app;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LeaderBoardFrame extends JFrame {
    // same palette & fonts as MainMenu
    private static final Color BG_COLOR      = new Color(0x0D392C);
    private static final Color BUTTON_COLOR  = new Color(0x34978D);
    private static final Color BUTTON_SHADOW = new Color(0x2A7471);
    private static final Color TITLE_BG      = new Color(0xFFF8E7);
    private static final Font  TITLE_FONT    = new Font("SansSerif", Font.BOLD, 36);
    private static final Font  BUTTON_FONT   = new Font("SansSerif", Font.BOLD, 24);

    private MainMenu parent;
    public UserData data;
    private JPanel debugPanel;
    private JButton backToMenuButton;

    private static final String APP_DIR    =
        System.getProperty("user.home") + File.separator + ".fungorium";
    private static final String DATA_FILE  = APP_DIR + File.separator + "leaderboard.txt";

    public LeaderBoardFrame(MainMenu parent) {
        super("Leaderboard");
        this.parent = parent;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // window icon
        ImageIcon ico = new ImageIcon(getClass().getResource("/images/fungoriumIcon.png"));
        setIconImage(ico.getImage());

        // load data model
        data = new UserData();
        loadData();

        // build styled content pane
        JPanel content = new JPanel();
        content.setBackground(BG_COLOR);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20,20,20,20));
        setContentPane(content);

        // 1) Header bar
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/images/mushroom_logo.png"));
        Image scaled   = rawIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);

        JLabel header = new JLabel("Leaderboard", icon, SwingConstants.CENTER);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setFont(TITLE_FONT);
        header.setOpaque(true);
        header.setBackground(TITLE_BG);
        header.setForeground(Color.BLACK);
        header.setBorder(new CompoundBorder(
            new LineBorder(TITLE_BG.darker(), 1),
            new EmptyBorder(10,20,10,20)
        ));
        content.add(header);
        content.add(Box.createVerticalStrut(20));

        // 2) Table
        JTable table = new JTable(data);
        table.setFillsViewportHeight(true);
        table.setRowSorter(new TableRowSorter<>(table.getModel()));
        table.setFont(new Font("SansSerif", Font.PLAIN, 18));
        table.setRowHeight(28);

        // style header row
        JTableHeader th = table.getTableHeader();
        th.setBackground(BUTTON_COLOR);
        th.setForeground(Color.WHITE);
        th.setFont(new Font("SansSerif", Font.BOLD, 18));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        scroll.setPreferredSize(new Dimension(480, 200));
        content.add(scroll);
        content.add(Box.createVerticalStrut(20));

        // 3) Back button
        backToMenuButton = createMenuButton("Back to Menu");
        backToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMenuButton.addActionListener(e -> {
            saveData();
            parent.setVisible(true);
            dispose();
        });
        content.add(backToMenuButton);
        content.add(Box.createVerticalGlue());

        // keybinding for debug panel
        setupDebugKeyBinding();

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(BUTTON_FONT);
        b.setBackground(BUTTON_COLOR);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setBorder(new CompoundBorder(
            BorderFactory.createMatteBorder(0,0,4,0, BUTTON_SHADOW),
            BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        return b;
    }

    private void loadData() {
        new File(APP_DIR).mkdirs();
        File f = new File(DATA_FILE);

        try (
          BufferedReader br = f.exists()
             // If we have an external file, read from it:
             ? new BufferedReader(new FileReader(f))
                // Otherwise, fall back to the embedded resource:
                : new BufferedReader(new InputStreamReader(
                    getClass().getResourceAsStream("/leaderboard.txt")
                ))
        )   {
           String line;
          while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("//")) continue;
               String[] p = line.split(",");
               data.addRecord(
                  p[0],
                 Integer.parseInt(p[1]),  Boolean.parseBoolean(p[2])
            );
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (User u : data.uR) {
                pw.printf("%s,%d,%b%n",
                    u.getName(), u.getPoints(), u.getDestroyedTecton_Base());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupDebugKeyBinding() {
        InputMap  im = getRootPane()
            .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke('D'), "toggleDebug");
        am.put("toggleDebug", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                if (debugPanel == null) createDebugPanel();
                debugPanel.setVisible(!debugPanel.isVisible());
                pack();
            }
        });
    }

    private void createDebugPanel() {
        debugPanel = new JPanel(new FlowLayout());
        debugPanel.setOpaque(false);

        debugPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(10);
        debugPanel.add(nameField);

        debugPanel.add(new JLabel("Points:"));
        JTextField ptsField = new JTextField(5);
        debugPanel.add(ptsField);

        JCheckBox chk = new JCheckBox("Destroyed");
        debugPanel.add(chk);

        JButton add = new JButton("Add");
        add.addActionListener(ev -> {
            try {
                data.addRecord(
                  nameField.getText(),
                  Integer.parseInt(ptsField.getText()),
                  chk.isSelected()
                );
                data.fireTableRowsInserted(
                  data.getRowCount()-1, data.getRowCount()-1);
                saveData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                  "Invalid points value", "Error",
                  JOptionPane.ERROR_MESSAGE);
            }
        });
        debugPanel.add(add);

        // insert just above the scroll pane
        getContentPane().add(debugPanel, 2);
    }
}
