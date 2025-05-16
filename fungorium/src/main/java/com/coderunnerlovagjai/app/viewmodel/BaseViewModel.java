package com.coderunnerlovagjai.app.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base class for all ViewModels in the MVVM architecture.
 * Implements the Observable interface to allow Views to subscribe to changes.
 * 
 * @param <T> The type of data this ViewModel provides
 */
public abstract class BaseViewModel<T> implements Observable<T> {
    
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
