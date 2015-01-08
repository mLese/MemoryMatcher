package com.deceax.memorymatcher;

public class Vec2 {

    private Vec3 vector;

    public Vec2() {
        vector = new Vec3();
    }

    public Vec2(float x, float y) {
        vector = new Vec3();
        vector.setX(x);
        vector.setY(y);
    }

    public float x() {
        return vector.x();
    }

    public float y() {
        return vector.y();
    }

    public void setX(float val) {
        vector.setX(val);
    }

    public void setY(float val) {
        vector.setY(val);
    }

}
