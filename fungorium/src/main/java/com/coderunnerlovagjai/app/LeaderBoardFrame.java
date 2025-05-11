package com.coderunnerlovagjai.app;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;

public class LeaderBoardFrame extends FrameStyle {
    private MainMenu parent;
    private JTable table;
    private UserData data;
    private JPanel debugPanel;
    private JButton backBtn;
    private static final String APP_DIR  = System.getProperty("user.home") + File.separator + ".fungorium";
    private static final String DATA_FILE = APP_DIR + File.separator + "leaderboard.txt";

    public LeaderBoardFrame(MainMenu p) {
        super("Leaderboard", "/images/fungoriumIcon3.png");
        this.parent = p;
        data = new UserData();
        this.setResizable(false);
        loadData();
        buildUI();
        pack();
        setLocationRelativeTo(p);
        setVisible(true);
    }

    @Override
    protected void buildUI() {
        // --- Table setup ---
        table = new JTable(data);
        table.setFillsViewportHeight(true);
        table.setRowSorter(new TableRowSorter<>(table.getModel()));
        table.setFont(new Font("SansSerif", Font.PLAIN, 18));
        table.setRowHeight(28);
        // transparent background to let the silhouettes show
        table.setOpaque(false);
        table.setBackground(new Color(0,0,0,0));

        // header styling
        JTableHeader th = table.getTableHeader();
        th.setBackground(BUTTON_COLOR);
        th.setForeground(Color.WHITE);
        th.setFont(new Font("SansSerif", Font.BOLD, 18));

        // disable automatic column resizing so we can set our own widths
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // compute and apply a preferred width for each column
        TableColumnModel colModel = table.getColumnModel();
        FontMetrics headerFm = th.getFontMetrics(th.getFont());
        FontMetrics cellFm   = table.getFontMetrics(table.getFont());
        for (int col = 0; col < colModel.getColumnCount(); col++) {
            TableColumn column = colModel.getColumn(col);
            String header = column.getHeaderValue().toString();

            // width needed for header text
            int max = headerFm.stringWidth(header);

            // width needed for cell contents
            for (int row = 0; row < table.getRowCount(); row++) {
                Object value = table.getValueAt(row, col);
                if (value != null) {
                    int w = cellFm.stringWidth(value.toString());
                    max = Math.max(max, w);
                }
            }

            // add some padding
            column.setPreferredWidth(max + 32);
        }

        // wrap in scroll pane
        JScrollPane sp = new JScrollPane(table,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setAlignmentX(Component.CENTER_ALIGNMENT);
        sp.setPreferredSize(new Dimension(480, 200));

        content.add(sp);
        content.add(Box.createVerticalStrut(20));

        // --- Back button ---
        backBtn = createMenuButton("Back to Menu");
        backBtn.addActionListener(e -> {
            saveData();
            parent.setVisible(true);
            dispose();
        });
        content.add(backBtn);
        content.add(Box.createVerticalGlue());

        // debug panel toggle
        setupDebugKeyBinding();
    }

    private void loadData() {
        new File(APP_DIR).mkdirs();
        File f = new File(DATA_FILE);
        try (BufferedReader br = f.exists()
                ? new BufferedReader(new FileReader(f))
                : new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/leaderboard.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("//")) continue;
                String[] p = line.split(",");
                data.addRecord(p[0], Integer.parseInt(p[1]), Boolean.parseBoolean(p[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (User u : data.uR) {
                pw.printf("%s,%d,%b%n", u.getName(), u.getPoints(), u.getDestroyedTecton_Base());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupDebugKeyBinding() {
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();
        im.put(KeyStroke.getKeyStroke('D'), "toggle");
        am.put("toggle", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (debugPanel == null) createDebugPanel();
                debugPanel.setVisible(!debugPanel.isVisible());
                pack();
            }
        });
    }

    private void createDebugPanel() {
        debugPanel = new JPanel(new FlowLayout());
        debugPanel.setOpaque(false);

        JTextField nf = new JTextField(10), pf = new JTextField(5);
        JCheckBox chk = new JCheckBox("Destroyed");
        chk.setForeground(Color.BLACK);

        debugPanel.add(new JLabel("Name:"));
        debugPanel.add(nf);
        debugPanel.add(new JLabel("Points:"));
        debugPanel.add(pf);
        debugPanel.add(chk);

        JButton add = new JButton("Add");
        add.addActionListener(ev -> {
            try {
                data.addRecord(nf.getText(),
                               Integer.parseInt(pf.getText()),
                               chk.isSelected());
                data.fireTableRowsInserted(
                    data.getRowCount()-1, data.getRowCount()-1);
                saveData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    this, "Invalid points", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton rem = new JButton("Remove Selected");
        rem.addActionListener(ev -> {
            int vr = table.getSelectedRow();
            if (vr < 0) {
                JOptionPane.showMessageDialog(
                    this, "No row selected", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            data.removeRecord(table.convertRowIndexToModel(vr));
            saveData();
        });

        debugPanel.add(add);
        debugPanel.add(rem);
        getContentPane().add(debugPanel, 2);
    }
}
