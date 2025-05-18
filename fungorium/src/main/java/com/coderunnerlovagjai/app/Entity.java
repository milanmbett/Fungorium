package com.coderunnerlovagjai.app;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// --- Model layer ---


/** Base class for all data-model objects. */
public abstract class Entity {
    private final List<ModelListener> listeners = new ArrayList<>();
    private int x, y, width, height;
    private double rotation;
    private Image image;
    private boolean batchMode = false;
    private boolean needsUpdate = false;

    public void addListener(ModelListener l)    { listeners.add(l); }
    public void removeListener(ModelListener l) { listeners.remove(l); }

    /**
     * Start a batch of changes. No events will be fired until endBatch() is called.
     */
    public void beginBatch() {
        batchMode = true;
    }

    /**
     * End a batch of changes and fire a single update event if needed.
     */
    public void endBatch() {
        batchMode = false;
        if (needsUpdate) {
            fireEvent(ModelEvent.Type.UPDATED);
            needsUpdate = false;
        }
    }

    protected void fireEvent(ModelEvent.Type type) {
        // If in batch mode, just mark the need for an update (unless it's a REMOVED event)
        if (batchMode && type == ModelEvent.Type.UPDATED) {
            needsUpdate = true;
            return;
        }
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