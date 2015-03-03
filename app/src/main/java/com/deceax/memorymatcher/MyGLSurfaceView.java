package com.deceax.memorymatcher;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

public class MyGLSurfaceView extends GLSurfaceView {

    MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        if (e.getAction() == MotionEvent.ACTION_UP)
            mRenderer.onTouch(x, y);

        return true;
    }
}
