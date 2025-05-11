package com.coderunnerlovagjai.app;

import java.awt.*;
public class Graphics_Object
{   //Positions on the screen
    protected int pozx;
    protected int pozy;
    //Size of the object
    protected int height;
    protected int width;

    protected Image img;

    protected Entity model;

    public int getPozX()
    {
        return pozx;
    }
    public int getPozY()
    {
        return pozy;
    }
    public void setPozX(int x)
    {
        this.pozx = x;
    }
    public void setPozY(int y)
    {
        this.pozy = y;
    }

    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }
    public void setHeight(int x)
    {
        this.height = x;
    }
    public void setWidth(int y)
    {
        this.width = y;
    }

    public Image getImg()
    {
        return img;
    }
    public void setImg(Image img)
    {
        this.img = img;
    }

    public Entity getModel()
    {
        return model;
    }
    public void setModel(Entity model)
    {
        this.model = model;
    }
}
