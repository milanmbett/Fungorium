package com.coderunnerlovagjai.app.viewmodel;

import java.util.function.Consumer;

/**
 * Interface for objects that can be observed for changes.
 * This is part of the Observer pattern implementation for the MVVM architecture.
 * 
 * @param <T> The type of data this observable provides
 */
public interface Observable<T> {
    
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
