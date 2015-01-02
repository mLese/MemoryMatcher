package com.deceax.memorymatcher;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "Renderer";

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private Square mSquare1;
    private Square mSquare2;
    private Square mSquare3;
    private Square mSquare4;

    private float screenHeight;
    private float screenWidth;
    private float ratio;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mSquare1 = new Square();
        mSquare2 = new Square();
        mSquare3 = new Square();
        mSquare4 = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        ratio = (float) width/height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        screenHeight = (float)(Math.tan(Math.toRadians(45) / 2.0f) * 3.0f);
        screenWidth = screenHeight * ratio;

        mSquare1.y = screenHeight -0.2f;
        mSquare2.y = -screenHeight + 0.2f;

        mSquare3.x = screenWidth - (0.2f * ratio);
        mSquare4.x = -screenWidth + (0.2f * ratio);

        mSquare1.draw(mMVPMatrix);
        mSquare2.draw(mMVPMatrix);
        mSquare3.draw(mMVPMatrix);
        mSquare4.draw(mMVPMatrix);
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}
