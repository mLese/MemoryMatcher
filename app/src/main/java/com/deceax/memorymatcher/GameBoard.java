package com.deceax.memorymatcher;

import android.util.Log;

import java.util.Random;

public class GameBoard {

    private static final String TAG = "GameBoard";

    private Card[][] gameBoard;
    private int boardWidth;
    private int boardHeight;

    public GameBoard(float screenWidth, float screenHeight, int boardWidth, int boardHeight) {
        // calculate width and height for each card
        float cardWidth = (screenWidth / boardWidth);
        float cardHeight = (screenHeight / boardHeight);

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        Log.d(TAG, "screenWidth " + screenWidth);
        Log.d(TAG, "screenHeight " + screenHeight);

        // initialize the game board
        float x, y;
        float initialX = (-1.0f * (screenWidth / 2.0f)) + (cardWidth / 2.0f);
        float initialY = (screenHeight / 2.0f) - (cardHeight / 2.0f);

        Log.d(TAG, "initial X " + initialX);
        Log.d(TAG, "initial Y " + initialY);

        x = initialX;
        y = initialY;

        float transFactorX = cardWidth;
        float transFactorY = cardHeight;
        gameBoard = new Card[boardWidth][boardHeight];

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                gameBoard[j][i] = new Card(cardWidth, cardHeight);
                gameBoard[j][i].move(x, y);
                x += transFactorX;
            }
            x = initialX;
            y -= transFactorY;
        }
    }

    public void draw(float mvp[]) {
        for (int i = 0; i < boardWidth; i++ ) {
            for (int j = 0; j < boardHeight; j++) {
                gameBoard[i][j].draw(mvp);
            }
        }
    }

    public void colorize() {
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                float[] color = new float[3];
                Random random = new Random();
                color[0] = random.nextFloat();
                color[1] = random.nextFloat();
                color[2] = random.nextFloat();
                gameBoard[i][j].setColor(color);
            }
        }
    }
}
