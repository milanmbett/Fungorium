// GraphicsObject.java
package com.coderunnerlovagjai.app;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Base class for any view object that renders an Entity and listens to its ModelEvents.
 */
public abstract class GraphicsObject<T extends Entity> implements ModelListener {
    protected int x, y, width, height;
    protected Image img;
    protected final T model;

    public GraphicsObject(T model) {
        this.model = model;
        updateFromModel();            // pull initial state
        model.addListener(this);      // listen for updates/removals
        GameCanvas.getInstance().addGraphics(this);
    }

    /** Sync view state from the model. */
    protected void updateFromModel() {
        Point p = model.getPosition();
        this.x = p.x; this.y = p.y;
        this.width  = model.getWidth();
        this.height = model.getHeight();
        this.img    = model.getImage();
    }

    @Override
    public void onModelEvent(ModelEvent e) {
        switch (e.getType()) {
            case UPDATED:
                updateFromModel();
                GameCanvas.getInstance().repaint();
                break;
            case REMOVED:
                model.removeListener(this);
                GameCanvas.getInstance().removeGraphics(this);
                break;
        }
    }

    /** Subclasses implement their specific drawing logic here. */
    public abstract void render(Graphics2D g);
}