package com.coderunnerlovagjai.app;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javazoom.jl.player.Player;

public class SoundManager {
    private static Player mp3Player;
    private static Thread musicThread;
    private static String currentTrack = null;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void playMusic(String resourcePath, boolean loop) {
        stopMusic();
        musicThread = new Thread(() -> {
            do {
                try {
                    URL url = SoundManager.class.getClassLoader().getResource(resourcePath);
                    if (url == null) return;
                    mp3Player = new Player(url.openStream());
                    mp3Player.play();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            } while (loop);
        });
        musicThread.setDaemon(true);
        musicThread.start();
        currentTrack = resourcePath;
    }

    public static void stopMusic() {
        if (mp3Player != null) {
            mp3Player.close();
            mp3Player = null;
        }
        if (musicThread != null) {
            musicThread.interrupt();
            musicThread = null;
        }
        currentTrack = null;
    }

    public static String getCurrentTrack() {
        return currentTrack;
    }
}
