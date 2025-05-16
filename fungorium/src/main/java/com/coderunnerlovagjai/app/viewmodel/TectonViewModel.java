package com.coderunnerlovagjai.app.viewmodel;

import java.awt.Point;
import java.util.List;

import com.coderunnerlovagjai.app.Basic_Spore;
import com.coderunnerlovagjai.app.Insect_Class;
import com.coderunnerlovagjai.app.Mushroom_Class;
import com.coderunnerlovagjai.app.Tecton_Class;
import com.coderunnerlovagjai.app.Thread_Class;

/**
 * ViewModel for Tecton entities.
 * Provides a representation of Tecton data that is optimized for display in views.
 */
public class TectonViewModel extends BaseViewModel<TectonViewModel.TectonData> {
    
    private final Tecton_Class tectonModel;
    
    /**
     * Create a new ViewModel for the specified Tecton model.
     * 
     * @param tectonModel The Tecton model to wrap
     */
    public TectonViewModel(Tecton_Class tectonModel) {
        this.tectonModel = tectonModel;
    }
    
    /**
     * Get the actual Tecton model object.
     * 
     * @return The underlying Tecton model
     */
    public Tecton_Class getModel() {
        return tectonModel;
    }
    
    /**
     * Notify all listeners that the model has changed.
     * Should be called whenever the underlying model is updated.
     */
    public void modelChanged() {
        updateAndNotify();
    }
    
    /**
     * Get the current data from the model.
     * This method is made public to allow Views to get the current state
     * without waiting for a notification.
     * 
     * @return The current state data
     */
    @Override
    public TectonData getCurrentData() {
        TectonData data = new TectonData();
        
        // Basic properties
        data.id = tectonModel.get_ID();
        data.position = tectonModel.getPosition();
        data.width = tectonModel.getWidth();
        data.height = tectonModel.getHeight();
        data.isDead = tectonModel.isDead();
        
        // Entity references
        data.mushroom = tectonModel.get_Mushroom();
        data.insectsOnTecton = tectonModel.get_InsectsOnTecton();
        data.spore = tectonModel.get_Spore();
        data.thread = tectonModel.get_Thread();
        
        return data;
    }
    
    /**
     * Data class representing the display-relevant state of a Tecton.
     */
    public static class TectonData {
        String id;
        Point position;
        int width;
        int height;
        boolean isDead;
        
        Mushroom_Class mushroom;
        List<Insect_Class> insectsOnTecton;
        Basic_Spore spore;
        Thread_Class thread;
        
        /**
         * Get the tecton's ID.
         * 
         * @return the ID
         */
        public String getId() {
            return id;
        }
        
        /**
         * Get the tecton's position.
         * 
         * @return the position
         */
        public Point getPosition() {
            return position;
        }
        
        /**
         * Get the tecton's width.
         * 
         * @return the width
         */
        public int getWidth() {
            return width;
        }
        
        /**
         * Get the tecton's height.
         * 
         * @return the height
         */
        public int getHeight() {
            return height;
        }
        
        /**
         * Check if the tecton is dead.
         * 
         * @return true if the tecton is dead
         */
        public boolean isDead() {
            return isDead;
        }
        
        /**
         * Get the mushroom on this tecton.
         * 
         * @return the mushroom, or null if none
         */
        public Mushroom_Class getMushroom() {
            return mushroom;
        }
        
        /**
         * Get the insects on this tecton.
         * 
         * @return the list of insects
         */
        public List<Insect_Class> getInsectsOnTecton() {
            return insectsOnTecton;
        }
        
        /**
         * Check if this tecton has a mushroom.
         * 
         * @return true if there is a mushroom on this tecton
         */
        public boolean hasMushroom() {
            return mushroom != null;
        }
        
        /**
         * Check if this tecton has a spore.
         * 
         * @return true if there is a spore on this tecton
         */
        public boolean hasSpore() {
            return spore != null;
        }
        
        /**
         * Check if this tecton has a thread.
         * 
         * @return true if there is a thread on this tecton
         */
        public boolean hasThread() {
            return thread != null;
        }
        
        /**
         * Get the number of insects on this tecton.
         * 
         * @return the number of insects
         */
        public int getInsectCount() {
            return insectsOnTecton != null ? insectsOnTecton.size() : 0;
        }
    }
}
