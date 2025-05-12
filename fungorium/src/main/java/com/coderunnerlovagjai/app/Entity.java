package com.coderunnerlovagjai.app;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

// --- Model layer ---


/** Base class for all data-model objects. */
public abstract class Entity {
    private final List<ModelListener> listeners = new ArrayList<>();
    private int x, y, width, height;
    private double rotation;
    private Image image;

    public void addListener(ModelListener l)    { listeners.add(l); }
    public void removeListener(ModelListener l) { listeners.remove(l); }

    protected void fireEvent(ModelEvent.Type type) {
        ModelEvent e = new ModelEvent(this, type);
        for (var l : new ArrayList<>(listeners)) {
            l.onModelEvent(e);
        }
    }

    // Position
    public Point getPosition() { return new Point(x, y); }
    public void setPosition(int x, int y) {
        this.x = x; this.y = y;
        fireEvent(ModelEvent.Type.UPDATED);
    }

    // Size
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setSize(int width, int height) {
        this.width = width; this.height = height;
        fireEvent(ModelEvent.Type.UPDATED);
    }

    // Rotation
    public double getRotation() { return rotation; }
    public void setRotation(double rotation) {
        this.rotation = rotation;
        fireEvent(ModelEvent.Type.UPDATED);
    }

    // Optional image
    public Image getImage() { return image; }
    public void setImage(Image image) {
        this.image = image;
        fireEvent(ModelEvent.Type.UPDATED);
    }

    // Mark this entity as removed
    public void remove() {
        fireEvent(ModelEvent.Type.REMOVED);
    }
}