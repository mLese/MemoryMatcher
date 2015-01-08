package com.deceax.memorymatcher;

public class Vec3 {

    private float[] vector;

    public Vec3() {
        vector = new float[3];
        vector[0] = 0.0f;
        vector[1] = 0.0f;
        vector[2] = 0.0f;
    }

    public Vec3(float x, float y, float z) {
        vector = new float[3];
        vector[0] = 0.0f;
        vector[1] = 0.0f;
        vector[2] = 0.0f;
    }

    public float x() {
        return vector[0];
    }

    public float y() {
        return vector[1];
    }

    public float z() {
        return vector[2];
    }

    public void setX(float val) {
        vector[0] = val;
    }

    public void setY(float val) {
        vector[1] = val;
    }

    public void setZ(float val) {
        vector[2] = val;
    }

}
