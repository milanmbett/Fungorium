package com.coderunnerlovagjai.app.util;

import java.io.*;

import javax.swing.JOptionPane;

public class LeaderboardManager {
    public static void save(String playerName, int score, boolean destroyedBase) {
        String dir = System.getProperty("user.home") + File.separator + ".fungorium";
        new File(dir).mkdirs();
        File file = new File(dir, "leaderboard.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.printf("%s,%d,%b%n", playerName, score, destroyedBase);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(null, "Could not save score: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
