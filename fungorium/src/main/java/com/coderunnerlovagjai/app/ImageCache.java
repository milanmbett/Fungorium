package com.coderunnerlovagjai.app;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageCache {
    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getImage(String resourcePath) {
        if (!cache.containsKey(resourcePath)) {
            try {
                Image img = ImageIO.read(ImageCache.class.getClassLoader().getResourceAsStream(resourcePath));
                if (img != null) {
                    cache.put(resourcePath, img);
                } else {
                    System.err.println("Warning: Could not load image from resource: " + resourcePath);
                    // Optionally, put a placeholder image or return null
                    // For now, returning null if loading fails after logging
                    return null;
                }
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Error loading image " + resourcePath + ": " + e.getMessage());
                // Optionally, put a placeholder image or return null
                cache.put(resourcePath, null); // Cache null to avoid repeated attempts for missing/bad images
                return null;
            }
        }
        return cache.get(resourcePath);
    }
}
