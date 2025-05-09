package com.coderunnerlovagjai.app;

public class ModelEvent 
{
    public enum Type { CREATED, UPDATED, REMOVED /*, … */ }
    private final Object source;
    private final Type type;
    // opcionálisan további adatok: pl. property name, régi és új érték…
    public ModelEvent(Object s, Type t)
    {
        this.source =s;
        this.type = t;
    }   
}
