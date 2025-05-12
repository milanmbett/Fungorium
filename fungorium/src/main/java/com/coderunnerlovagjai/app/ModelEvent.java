package com.coderunnerlovagjai.app;

public class ModelEvent {
    public enum Type { UPDATED, REMOVED }
    private final Entity source;
    private final Type type;

    public ModelEvent(Entity source, Type type) {
        this.source = source;
        this.type = type;
    }
    public Entity getSource() { return source; }
    public Type getType() { return type; }
}

