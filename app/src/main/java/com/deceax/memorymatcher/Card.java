package com.deceax.memorymatcher;

public class Card {
    private GLDrawable cardDrawable;

    public Card(float width, float height) {
        cardDrawable = new Rectangle(width, height);

    }

    public void draw(float[] mvp) {
        cardDrawable.draw(mvp);
    }

    public void move(float x, float y) {
        cardDrawable.move(x, y);
    }

    public void setColor(float[] color) {
        cardDrawable.setColor(color);
    }
}
