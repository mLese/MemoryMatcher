package com.deceax.memorymatcher;


public class Game {
    private GameBoard board;

    private int resX, resY;

    public Game(float screenWidth, float screenHeight, int resX, int resY) {
        this.resX = resX;
        this.resY = resY;
        board = new GameBoard(screenWidth, screenHeight, 5, 4);
    }

    public void onDraw(float[] mvp) {
        //board.colorize();
        board.draw(mvp);
    }

    public void onTouch(float x, float y) {
        board.onTouch(x, y, resX, resY);
    }
}
