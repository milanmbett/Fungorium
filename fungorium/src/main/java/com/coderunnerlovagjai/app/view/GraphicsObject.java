package com.coderunnerlovagjai.app.view;

import java.awt.Graphics2D;

/**
 * Interface for renderable model objects.
 */
public interface GraphicsObject<T> {
    void render(Graphics2D g);
}
