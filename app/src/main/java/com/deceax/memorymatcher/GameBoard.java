package com.deceax.memorymatcher;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameBoard {

    private static final String TAG = "GameBoard";

    private Card[][] gameBoard;
    private Vec2[][] matchingKey;

    private int numTouched = 0;
    private int matchCount = 0;
    Card touch1;

    private static boolean disableTouch = false;

    private int boardWidth;
    private int boardHeight;

    public GameBoard(float screenWidth, float screenHeight, int boardWidth, int boardHeight) {
        // calculate width and height for each card
        float cardWidth = (screenWidth / boardWidth);
        float cardHeight = (screenHeight / boardHeight);

        float drawWidth = cardWidth * 0.80f;
        float drawHeight = cardHeight * 0.80f;

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        Log.v(TAG, "screenWidth " + screenWidth);
        Log.v(TAG, "screenHeight " + screenHeight);

        // initialize the game board
        float x, y;
        float initialX = (-1.0f * (screenWidth / 2.0f)) + (cardWidth / 2.0f);
        float initialY = (screenHeight / 2.0f) - (cardHeight / 2.0f);

        Log.v(TAG, "initial X " + initialX);
        Log.v(TAG, "initial Y " + initialY);

        x = initialX;
        y = initialY;

        gameBoard = new Card[boardWidth][boardHeight];

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                gameBoard[j][i] = new Card(drawWidth, drawHeight);
                gameBoard[j][i].move(x, y);
                gameBoard[j][i].setColor(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
                x += cardWidth;
            }
            x = initialX;
            y -= cardHeight;
        }

        createMatches();
    }

    private void createMatches() {
        matchingKey = new Vec2[boardWidth][boardHeight];
        ArrayList<Vec2> potentialMatches = new ArrayList<>(boardWidth * boardHeight);

        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                potentialMatches.add(new Vec2(i, j));
            }
        }

        Random random = new Random();
        int matchIdx;

        Vec2 card1Pos;
        Vec2 card2Pos;

        while (potentialMatches.size() > 0) {
            matchIdx = 0;
            while (matchIdx == 0) {
                matchIdx = random.nextInt(potentialMatches.size());
            }

            card1Pos = potentialMatches.get(0);
            card2Pos = potentialMatches.get(matchIdx);

            matchingKey[(int)card1Pos.x()][(int)card1Pos.y()] = card2Pos;
            matchingKey[(int)card1Pos.x()][(int)card1Pos.y()] = card1Pos;

            float cardColor[] = new float[3];
            cardColor[0] = random.nextFloat();
            cardColor[1] = random.nextFloat();
            cardColor[2] = random.nextFloat();

            gameBoard[(int)card1Pos.x()][(int)card1Pos.y()].setMatchColor(cardColor.clone());
            gameBoard[(int)card2Pos.x()][(int)card2Pos.y()].setMatchColor(cardColor.clone());

            potentialMatches.remove(matchIdx);
            potentialMatches.remove(0);
        }
    }

    public void draw(float mvp[]) {
        for (int i = 0; i < boardWidth; i++ ) {
            for (int j = 0; j < boardHeight; j++) {
                gameBoard[i][j].draw(mvp);
            }
        }
    }

    public void onTouch(float x, float y, int resX, int resY) {
        if (disableTouch) return; // eat touches when board is inactive

        // convert the x/y touch to board index positions
        int xIndex = 0;
        int yIndex = 0;

        float gridWidth = (resX / boardWidth);
        float gridHeight = (resY / boardHeight);

        int startX = 0;

        for (int i = 0; i < boardWidth; i++) {
            if ( (x > startX) && (x < (startX + gridWidth))) {
                xIndex = i;
                break;
            }
            startX += gridWidth;
        }

        int startY = 0;

        for (int i = 0; i < boardHeight; i++) {
            if ( (y > startY) && (y < (startY + gridHeight))) {
                yIndex = i;
                break;
            }
            startY += gridHeight;
        }

        numTouched += 1;

        final int xi = xIndex;
        final int yi = yIndex;

        if (numTouched == 1) {
            touch1 = gameBoard[xIndex][yIndex];
            touch1.setColor(touch1.getMatchColor());
        } else if (numTouched == 2) {
            disableTouch = true;
            gameBoard[xIndex][yIndex].setColor(gameBoard[xIndex][yIndex].getMatchColor());

            new Thread(new secondTouchRunnable(new SecondTouchCallback() {
                @Override
                public void onReturn() {
                    if (Arrays.equals(touch1.getMatchColor(),gameBoard[xi][yi].getMatchColor())) {
                        Log.d("LESE","MATCH");
                        matchCount+=2;
                    } else {
                        Log.d("LESE", "Nomatch");
                        touch1.setColor(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
                        gameBoard[xi][yi].setColor(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
                    }
                    numTouched = 0;
                    disableTouch = false;
                }
            })).run();

        }
        Log.d("MatchCount", ""+matchCount);
        if (matchCount == (boardWidth * boardHeight)) {
            Log.d("winner", "winner winner");
        }
    }

    private class secondTouchRunnable implements Runnable {

        SecondTouchCallback callback;

        public secondTouchRunnable(SecondTouchCallback callback) {
            this.callback = callback;
        }

        public void run() {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                Log.e("Lese", "lOL exception EATing");
            }
            callback.onReturn();
        }
    }

    public interface SecondTouchCallback {
        public void onReturn();
    }
}
