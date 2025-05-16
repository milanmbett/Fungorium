package com.coderunnerlovagjai.app.viewmodel;

import com.coderunnerlovagjai.app.Plane;
import com.coderunnerlovagjai.app.Tecton_Class;

/**
 * Factory for creating ViewModels for game entities.
 * This centralizes the creation of ViewModels and ensures that each entity has only one ViewModel.
 */
public class ViewModelFactory {
    
    /**
     * Get a ViewModel for a Tecton entity.
     * If the ViewModel already exists, it will be returned from the registry.
     * Otherwise, a new one will be created.
     * 
     * @param tecton The tecton model
     * @return The corresponding ViewModel
     */
    public static TectonViewModel getViewModelForTecton(Tecton_Class tecton) {
        return ViewModelRegistry.getInstance().getTectonViewModel(tecton);
    }
    
    /**
     * Initialize ViewModels for all entities in the game.
     * This should be called when the game starts or when the model changes significantly.
     * 
     * @param plane The game plane containing all entities
     */
    public static void createViewModelsForPlane(Plane plane) {
        // Clear existing ViewModels
        ViewModelRegistry.getInstance().clear();
        
        // Create ViewModels for all tectons
        for (Tecton_Class tecton : plane.TectonCollection) {
            getViewModelForTecton(tecton);
        }
    }
    
    /**
     * Notify all ViewModels that their models may have changed.
     * This will trigger updates to all views that are subscribed to these ViewModels.
     */
    public static void refreshAllViewModels() {
        ViewModelRegistry.getInstance().refreshAll();
    }
}
