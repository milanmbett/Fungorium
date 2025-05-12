package com.coderunnerlovagjai.app.view;

import com.coderunnerlovagjai.app.Tecton_Class;

/**
 * Interface for components that want to be notified when a tecton is selected
 */
public interface TectonSelectionListener {
    /**
     * Called when a tecton is selected by the user
     * @param tecton The tecton that was selected
     */
    void onTectonSelected(Tecton_Class tecton);
}
