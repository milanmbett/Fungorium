package com.coderunnerlovagjai.app;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity 
{
    private final List<ModelListener> listeners = new ArrayList<>();

    public void addListener(ModelListener l) {
        listeners.add(l);
    }
    public void removeListener(ModelListener l) {
        listeners.remove(l);
    }
    protected void fireEvent(ModelEvent.Type type) {
        ModelEvent e = new ModelEvent(this, type);
        for (var l : listeners) {
            l.onModelEvent(e);
        }
    }
}
