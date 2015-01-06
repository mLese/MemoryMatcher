package com.deceax.memorymatcher;

public interface GLDrawable {
    public void draw(float mvp[]);
    public void update();
    public void move(float x, float y);
    public void setColor(float[] color);
}
