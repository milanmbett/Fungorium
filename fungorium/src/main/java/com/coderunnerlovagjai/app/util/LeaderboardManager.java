
// LeaderboardManager: Segédosztály a ponttáblázat (leaderboard) kezeléséhez
// Felelős a játékosok eredményeinek mentéséért egy fájlba
package com.coderunnerlovagjai.app.util;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;


/**
 * Segédosztály a ponttáblázat (leaderboard) kezeléséhez.
 * Felelős a játékosok eredményeinek mentéséért egy helyi fájlba.
 */
public class LeaderboardManager {
    /**
     * Elmenti a játékos eredményét a leaderboard.txt fájlba a felhasználó home könyvtárában.
     * Ha a könyvtár vagy a fájl nem létezik, létrehozza azokat.
     * @param playerName A játékos neve
     * @param score      A játékos pontszáma
     * @param destroyedBase Igaz, ha a játékos elpusztította az ellenség bázisát
     */
    public static void save(String playerName, int score, boolean destroyedBase) {
        // A leaderboard fájl elérési útja: felhasználó home/.fungorium/leaderboard.txt
        String dir = System.getProperty("user.home") + File.separator + ".fungorium";
        new File(dir).mkdirs(); // Könyvtár létrehozása, ha nem létezik
        File file = new File(dir, "leaderboard.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            // Sor formátuma: név,pontszám,elpusztította-e-a-bázist (pl. "Anna,120,true")
            pw.printf("%s,%d,%b%n", playerName, score, destroyedBase);
        } catch(IOException e) {
            // Hiba esetén felugró ablakban jelezzük a hibát
            JOptionPane.showMessageDialog(null, "Could not save score: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
