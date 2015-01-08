package com.deceax.memorymatcher;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

public class  Rectangle implements GLDrawable {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private int mProgram;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    static float squareCoords[] = {
            -0.2f, 0.2f, 0.0f,  // top left
            -0.2f, -0.2f, 0.0f, // bottom left
            0.2f, -0.2f, 0.0f,  // bottom right
            0.2f, 0.2f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    float color[] = { 1.0f, 0.75f, 0.0f, 1.0f};
    Vec3 position;

    public Rectangle() {
        position = new Vec3();
        allocateBuffers();
        loadShaders();
    }

    public Rectangle(float width, float height) {
        position = new Vec3();

        // top left
        squareCoords[0] = -1 * (width/2.0f);
        squareCoords[1] = (height/2.0f);
        squareCoords[2] = 0.0f;

        // bottom left
        squareCoords[3] = -1 * (width/2.0f);
        squareCoords[4] = -1 * (height/2.0f);
        squareCoords[5] = 0.0f;

        // bottom right
        squareCoords[6] = (width/2.0f);
        squareCoords[7] = -1 * (height/2.0f);
        squareCoords[8] = 0.0f;

        // top right
        squareCoords[9] = (width/2.0f);
        squareCoords[10] = (height/2.0f);
        squareCoords[11] = 0.0f;

        // random colors
        Random r = new Random();
        color[0] = r.nextFloat();
        color[1] = r.nextFloat();
        color[2] = r.nextFloat();

        allocateBuffers();
        loadShaders();
    }

    private void allocateBuffers() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(squareCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());

        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    private void loadShaders() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mvpMatrix) {
        float[] mvpCopy = mvpMatrix.clone();

        // Apply Translation
        Matrix.translateM(mvpCopy, 0, position.x(), position.y(), position.z());

        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpCopy, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void update() {
        // frame update code
    }

    public void move(float x, float y) {
        position.setX(x);
        position.setY(y);
    }

    public void setColor(float[] color) {
        this.color[0] = color[0];
        this.color[1] = color[1];
        this.color[2] = color[2];
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

}
