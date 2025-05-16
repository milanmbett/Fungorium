package com.coderunnerlovagjai.app.mvvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This file provides a comprehensive example of the MVVM pattern for the Fungorium game.
 * It serves as documentation and reference for implementing this architecture throughout the game.
 * 
 * MVVM Pattern Components:
 * 1. Model: The game's data and business logic (existing classes)
 * 2. ViewModel: The bridge between the Model and View, handling data transformation
 * 3. View: The UI components that display data to the user
 * 
 * Key benefits:
 * - Separation of concerns
 * - Testability
 * - Maintainability
 * - Decoupled components
 */

/*****************************************************************************
 * STEP 1: Create the Observable interface for implementing the Observer pattern
 *****************************************************************************/

/**
 * Interface for objects that can be observed for changes.
 * This is part of the Observer pattern implementation for the MVVM architecture.
 * 
 * @param <T> The type of data this observable provides
 */
interface Observable<T> {
    
    /**
     * Add a listener to be notified when the observed data changes.
     * 
     * @param listener A consumer that will be called with the updated data
     */
    void addListener(Consumer<T> listener);
    
    /**
     * Remove a listener from the notification list.
     * 
     * @param listener The listener to remove
     */
    void removeListener(Consumer<T> listener);
    
    /**
     * Notify all listeners with the current data.
     * 
     * @param data The current data to provide to listeners
     */
    void notifyListeners(T data);
}

/*****************************************************************************
 * STEP 2: Create the BaseViewModel as a foundation for all ViewModels
 *****************************************************************************/

/**
 * Base class for all ViewModels in the MVVM architecture.
 * Implements the Observable interface to allow Views to subscribe to changes.
 * 
 * @param <T> The type of data this ViewModel provides
 */
abstract class BaseViewModel<T> implements Observable<T> {
    
    private final List<Consumer<T>> listeners = new ArrayList<>();
    
    @Override
    public void addListener(Consumer<T> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    @Override
    public void removeListener(Consumer<T> listener) {
        listeners.remove(listener);
    }
    
    @Override
    public void notifyListeners(T data) {
        for (Consumer<T> listener : listeners) {
            listener.accept(data);
        }
    }
    
    /**
     * Update the model with new data and notify all listeners.
     * Subclasses should call this method whenever the underlying model changes.
     */
    protected void updateAndNotify() {
        T data = getCurrentData();
        notifyListeners(data);
    }
    
    /**
     * Get the current data from the model.
     * Subclasses should implement this to provide the current state.
     * 
     * @return The current state data
     */
    public abstract T getCurrentData();
}

/*****************************************************************************
 * STEP 3: Create a ViewModel for a specific Model class (example: Tecton)
 *****************************************************************************/

// Simulated model classes for the example
class Position {
    int x, y;
    
    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class TectonModel {
    private String id;
    private Position position;
    private int width;
    private int height;
    private boolean isDead;
    private Object mushroom;
    private List<Object> insects = new ArrayList<>();
    
    // Constructor, getters, setters...
    
    public String getId() { return id; }
    public Position getPosition() { return position; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isDead() { return isDead; }
    public Object getMushroom() { return mushroom; }
    public List<Object> getInsects() { return insects; }
    
    // Additional methods...
}

/**
 * ViewModel for a Tecton entity.
 * Transforms and prepares tecton data for display in views.
 */
class TectonViewModel extends BaseViewModel<TectonViewModel.TectonData> {
    
    private final TectonModel model;
    
    /**
     * Create a new ViewModel for the specified Tecton model.
     * 
     * @param model The Tecton model to wrap
     */
    public TectonViewModel(TectonModel model) {
        this.model = model;
    }
    
    /**
     * Get the actual model object.
     * 
     * @return The underlying model
     */
    public TectonModel getModel() {
        return model;
    }
    
    /**
     * Notify all listeners that the model has changed.
     * Should be called whenever the underlying model is updated.
     */
    public void modelChanged() {
        updateAndNotify();
    }
    
    @Override
    public TectonData getCurrentData() {
        TectonData data = new TectonData();
        
        // Map model properties to ViewModel data
        data.id = model.getId();
        data.position = model.getPosition();
        data.width = model.getWidth();
        data.height = model.getHeight();
        data.isDead = model.isDead();
        data.mushroom = model.getMushroom();
        data.insects = model.getInsects();
        
        return data;
    }
    
    /**
     * Data class representing the display-relevant state of a Tecton.
     */
    public static class TectonData {
        public String id;
        public Position position;
        public int width;
        public int height;
        public boolean isDead;
        public Object mushroom;
        public List<Object> insects;
        
        // Helper methods for view display logic
        public boolean hasMushroom() {
            return mushroom != null;
        }
        
        public int getInsectCount() {
            return insects != null ? insects.size() : 0;
        }
    }
}

/*****************************************************************************
 * STEP 4: Create a ViewModel registry to manage all ViewModels
 *****************************************************************************/

/**
 * Registry that stores and manages all ViewModels.
 * This ensures that we have a single ViewModel per model object.
 */
class ViewModelRegistry {
    
    private static ViewModelRegistry instance;
    
    // Maps that store ViewModels by their model objects
    private final Map<TectonModel, TectonViewModel> tectonViewModels = new HashMap<>();
    
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
     * Get or create a TectonViewModel for a TectonModel.
     * 
     * @param tecton The tecton model
     * @return The corresponding ViewModel
     */
    public TectonViewModel getTectonViewModel(TectonModel tecton) {
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

/*****************************************************************************
 * STEP 5: Implement a View that observes a ViewModel
 *****************************************************************************/

/**
 * A View component that displays a Tecton.
 * It observes changes to the TectonViewModel and updates accordingly.
 */
class TectonView implements Consumer<TectonViewModel.TectonData> {
    
    private final TectonViewModel viewModel;
    private String id;
    private int x, y, width, height;
    private boolean hasMushroom;
    private int insectCount;
    
    /**
     * Create a new TectonView that observes the given ViewModel.
     * 
     * @param viewModel The ViewModel to observe
     */
    public TectonView(TectonViewModel viewModel) {
        this.viewModel = viewModel;
        
        // Subscribe to the ViewModel for updates
        viewModel.addListener(this);
        
        // Initialize with current data
        accept(viewModel.getCurrentData());
    }
    
    /**
     * Called when the ViewModel notifies of a data change.
     * This method is part of the Consumer interface.
     * 
     * @param data The updated data from the ViewModel
     */
    @Override
    public void accept(TectonViewModel.TectonData data) {
        // Update view state from the ViewModel data
        this.id = data.id;
        this.x = data.position.x;
        this.y = data.position.y;
        this.width = data.width;
        this.height = data.height;
        this.hasMushroom = data.hasMushroom();
        this.insectCount = data.getInsectCount();
        
        // Repaint or update the UI
        repaint();
    }
    
    /**
     * Repaint this view component.
     * This would typically update the UI in the real implementation.
     */
    private void repaint() {
        System.out.println("Repainting Tecton [" + id + "] at (" + x + "," + y + ")");
        System.out.println("  - Has mushroom: " + hasMushroom);
        System.out.println("  - Insect count: " + insectCount);
    }
}

/*****************************************************************************
 * STEP 6: Update the controller to use the MVVM pattern
 *****************************************************************************/

/**
 * A controller that coordinates models and ViewModels.
 */
class GameController {
    
    /**
     * Create a new mushroom on the given tecton.
     * 
     * @param tectonModel The tecton to place the mushroom on
     */
    public void createMushroom(TectonModel tectonModel) {
        // Create and place mushroom in model...
        
        // Get ViewModel and notify of changes
        TectonViewModel viewModel = ViewModelRegistry.getInstance()
                .getTectonViewModel(tectonModel);
        viewModel.modelChanged();
    }
    
    /**
     * Refresh all views to reflect current game state.
     */
    public void refreshViews() {
        // Notify all ViewModels that models have changed
        ViewModelRegistry.getInstance().refreshAll();
    }
}

/*****************************************************************************
 * STEP 7: Usage Example
 *****************************************************************************/

/**
 * Demonstration of using the MVVM pattern.
 */
public class MVVMExample {
    
    public static void main(String[] args) {
        // Create a model
        TectonModel tecton = new TectonModel();
        
        // Get ViewModel from registry
        TectonViewModel viewModel = ViewModelRegistry.getInstance().getTectonViewModel(tecton);
        
        // Create view that observes the ViewModel
        TectonView view = new TectonView(viewModel);
        
        // When model changes, notify ViewModel
        // This would typically happen in response to user actions or game logic
        tecton.getMushroom(); // Simulate model change
        viewModel.modelChanged(); // Notify ViewModel of change
        
        // Controller can also refresh all ViewModels at once
        GameController controller = new GameController();
        controller.refreshViews();
    }
}
