package com.coderunnerlovagjai.app;

public class User
{
    private String name;
    private int points;
    private boolean destroyedTecton_Base;

    public User(String n, int p, boolean d)
    {
        this.name = n;
        this.points = p;
        this.destroyedTecton_Base = d;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String n)
    {
        this.name = n;
    }
    public int getPoints()
    {
        return points;
    }
    public void setPoints(int p)
    {
        this.points = p;
    }
    public boolean getDestroyedTecton_Base()
    {
        return destroyedTecton_Base;
    }
    public void setDestroyedTecton_Base(boolean d)
    {
        this.destroyedTecton_Base = d;
    }
}
