package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.*;

/**
 * Factory class for creating view objects for game entities.
 * This implements the Factory pattern for view creation and
 * centralizes the logic for creating the appropriate view for each model entity.
 */
public class ViewFactory {
    
    /**
     * Creates the appropriate view for a game entity.
     * @param entity The model entity to create a view for
     * @return A GraphicsObject representing the view for the entity
     */
    public static GraphicsObject<?> createViewFor(Entity entity) {
        if (entity instanceof Tecton_Class) {
            return new TectonView((Tecton_Class)entity);
        } else if (entity instanceof Insect_Class) {
            return new InsectView((Insect_Class)entity);
        } else if (entity instanceof Mushroom_Class) {
            return new MushroomView((Mushroom_Class)entity);
        } else if (entity instanceof Thread_Class) {
            return new ThreadView((Thread_Class)entity);
        } else if (entity instanceof Basic_Spore) {
            return new SporeView((Basic_Spore)entity);
        }
        
        throw new IllegalArgumentException("Unsupported entity type: " + entity.getClass().getName());
    }
    
    /**
     * Creates views for all entities in the game model.
     * @param plane The game plane containing all entities
     */
    public static void createViewsForPlane(Plane plane) {
        // Clear existing views
        GameCanvas.getInstance().clearAll();
        
        // Create views in proper order:
        // 1. Tectons first (they form the background)
        for (Tecton_Class tecton : plane.TectonCollection) {
            new TectonView(tecton);
        }
        
        // 2. Threads 
        for (Thread_Class thread : plane.ThreadCollection) {
            new ThreadView(thread);
        }
        
        // 3. Spores
        for (Basic_Spore spore : plane.SporeCollection) {
            new SporeView(spore);
        }
        
        // 4. Mushrooms
        for (Mushroom_Class mushroom : plane.MushroomCollection) {
            new MushroomView(mushroom);
        }
        
        // 5. Insects (top layer)
        for (Insect_Class insect : plane.InsectCollection) {
            new InsectView(insect);
        }
    }
}
