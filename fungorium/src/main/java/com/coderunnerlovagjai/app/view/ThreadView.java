package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

/**
 * Visual representation of a Thread in the game, connecting two Tectons.
 */
public class ThreadView extends GraphicsObject<Thread_Class> {
    private static final Color THREAD_COLOR = new Color(255, 255, 255);
    private static final Color THREAD_BORDER = new Color(100, 100, 100);
    private static final float DASH_LENGTH[] = {5.0f, 5.0f};
    private static final BasicStroke DASHED_STROKE = new BasicStroke(
            2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 
            1.0f, DASH_LENGTH, 0.0f);
    
    private Point sourcePoint;
    private Point targetPoint;
    
    public ThreadView(Thread_Class model) {
        super(model);
        updateConnectionPoints();
    }
    
    private void updateConnectionPoints() {
        // Get the source tecton's position (where this thread is)
        Tecton_Class sourceTecton = model.get_Tecton();
        if (sourceTecton != null) {
            Point srcPos = sourceTecton.getPosition();
            sourcePoint = new Point(
                srcPos.x + sourceTecton.getWidth() / 2,
                srcPos.y + sourceTecton.getHeight() / 2
            );
        }
        
        // For visualization purposes, we'll have the thread point to its neighbors
        // This can be updated with actual thread connection logic later
        targetPoint = new Point(sourcePoint.x, sourcePoint.y);
    }
    
    @Override
    protected void updateFromModel() {
        super.updateFromModel();
        updateConnectionPoints();
    }
    
    @Override
    public void render(Graphics2D g) {
        if (sourcePoint == null) return;
        
        // Save original transform and stroke
        AffineTransform oldTransform = g.getTransform();
        Stroke oldStroke = g.getStroke();
        
        // Draw thread as a dashed line
        g.setStroke(DASHED_STROKE);
        
        // Draw connecting line with shadow effect
        g.setColor(THREAD_BORDER);
        g.draw(new Line2D.Float(
            sourcePoint.x + 2, sourcePoint.y + 2, 
            targetPoint.x + 2, targetPoint.y + 2
        ));
        
        g.setColor(THREAD_COLOR);
        g.draw(new Line2D.Float(
            sourcePoint.x, sourcePoint.y, 
            targetPoint.x, targetPoint.y
        ));
        
        // Draw thread "node" at the source
        int nodeRadius = 6;
        g.setColor(THREAD_COLOR);
        g.fillOval(
            sourcePoint.x - nodeRadius, 
            sourcePoint.y - nodeRadius,
            nodeRadius * 2, 
            nodeRadius * 2
        );
        g.setColor(THREAD_BORDER);
        g.drawOval(
            sourcePoint.x - nodeRadius, 
            sourcePoint.y - nodeRadius,
            nodeRadius * 2, 
            nodeRadius * 2
        );
        
        // Restore original state
        g.setStroke(oldStroke);
        g.setTransform(oldTransform);
    }
}
