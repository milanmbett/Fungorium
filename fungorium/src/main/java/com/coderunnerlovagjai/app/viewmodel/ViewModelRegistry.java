package com.coderunnerlovagjai.app.viewmodel;

import java.util.HashMap;
import java.util.Map;

import com.coderunnerlovagjai.app.Tecton_Class;

/**
 * Registry that stores and manages all ViewModels.
 * This ensures that we have a single ViewModel per model object.
 */
public class ViewModelRegistry {
    
    private static ViewModelRegistry instance;
    
    // Maps that store ViewModels by their model objects
    private final Map<Tecton_Class, TectonViewModel> tectonViewModels = new HashMap<>();
    
    /**
     * Get the singleton instance of the registry.
     * 
     * @return The ViewModelRegistry instance
     */
    public static ViewModelRegistry getInstance() {
        if (instance == null) {
            instance = new ViewModelRegistry();
        }
        return instance;
    }
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private ViewModelRegistry() {
        // Private constructor
    }
    
    /**
     * Get or create a TectonViewModel for a Tecton_Class model.
     * 
     * @param tecton The tecton model
     * @return The corresponding ViewModel
     */
    public TectonViewModel getTectonViewModel(Tecton_Class tecton) {
        if (!tectonViewModels.containsKey(tecton)) {
            tectonViewModels.put(tecton, new TectonViewModel(tecton));
        }
        return tectonViewModels.get(tecton);
    }
    
    /**
     * Clear all registered ViewModels.
     * Should be called when starting a new game.
     */
    public void clear() {
        tectonViewModels.clear();
    }
    
    /**
     * Notify all registered ViewModels that their models may have changed.
     * This will trigger updates to all views.
     */
    public void refreshAll() {
        // Refresh all tecton ViewModels
        for (TectonViewModel viewModel : tectonViewModels.values()) {
            viewModel.modelChanged();
        }
    }
}
