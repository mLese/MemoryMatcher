package com.deceax.memorymatcher;


public class Game {
    private GameBoard board;

    private int resX, resY;

    public Game(float screenWidth, float screenHeight, int resX, int resY) {
        this.resX = resX;
        this.resY = resY;
        board = new GameBoard(screenWidth, screenHeight, 4, 3);
    }

    public void onDraw(float[] mvp) {
        board.draw(mvp);
    }

    public void onTouch(float x, float y) {
        board.onTouch(x, y, resX, resY);
    }
}
