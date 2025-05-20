
// SoundManager: Egyszerű zenelejátszó kezelő osztály MP3-hoz (JLayer)
// Felelős a háttérzene indításáért, leállításáért, és a jelenlegi track nyilvántartásáért.
package com.coderunnerlovagjai.app.util;


import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javazoom.jl.player.Player; // JLayer MP3 lejátszó


/**
 * Statikus segédosztály a háttérzene kezelésére.
 * Csak egy zene szólhat egyszerre, és támogatja a loop-ot is.
 */
public class SoundManager {
    // Jelenleg futó MP3 lejátszó példány
    private static Player mp3Player;
    // A háttérzene lejátszásáért felelős szál
    private static Thread musicThread;
    // A jelenleg játszott track elérési útja (null, ha nincs)
    private static String currentTrack = null;
    // (Nem használt, de fenntartott) végrehajtó szerviz
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();


    /**
     * Elindítja a háttérzenét a megadott erőforrás elérési útról.
     * Ha már szól zene, előbb leállítja azt. Ha loop=true, folyamatosan újraindul.
     * @param resourcePath Az MP3 fájl elérési útja (pl. "sounds/gameMusic.mp3")
     * @param loop Igaz, ha folyamatosan ismétlődjön
     */
    public static void playMusic(String resourcePath, boolean loop) {
        stopMusic(); // Előző zene leállítása
        musicThread = new Thread(() -> {
            do {
                try {
                    // Erőforrás betöltése classpath-ról
                    URL url = SoundManager.class.getClassLoader().getResource(resourcePath);
                    if (url == null) return; // Ha nincs ilyen fájl, kilép
                    mp3Player = new Player(url.openStream()); // MP3 lejátszó példány
                    mp3Player.play(); // Lejátszás (blokkoló hívás)
                } catch (Exception e) {
                    e.printStackTrace();
                    break; // Hiba esetén kilép a loop-ból
                }
            } while (loop); // Ha loop=true, újraindul
        });
        musicThread.setDaemon(true); // Daemon thread, hogy ne blokkolja a kilépést
        musicThread.start();
        currentTrack = resourcePath; // Jelenlegi track beállítása
    }


    /**
     * Leállítja a jelenleg szóló zenét, ha van.
     * Megszakítja a lejátszó szálat és bezárja a lejátszót.
     */
    public static void stopMusic() {
        if (mp3Player != null) {
            mp3Player.close(); // Lejátszó leállítása
            mp3Player = null;
        }
        if (musicThread != null) {
            musicThread.interrupt(); // Szál megszakítása
            musicThread = null;
        }
        currentTrack = null;
    }

    /**
     * Visszaadja a jelenleg játszott zene elérési útját, vagy null-t, ha nincs zene.
     */
    public static String getCurrentTrack() {
        return currentTrack;
    }
}
