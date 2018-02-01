// 
// //  adaptation of MPChart
// 

package com.github.mikephil.charting.matrix;

public final class Vector3
{
    public float x;
    public float y;
    public float z;
    public static final Vector3 ZERO;
    public static final Vector3 UNIT_X;
    public static final Vector3 UNIT_Y;
    public static final Vector3 UNIT_Z;
    
    static {
        ZERO = new Vector3(0.0f, 0.0f, 0.0f);
        UNIT_X = new Vector3(1.0f, 0.0f, 0.0f);
        UNIT_Y = new Vector3(0.0f, 1.0f, 0.0f);
        UNIT_Z = new Vector3(0.0f, 0.0f, 1.0f);
    }
    
    public Vector3() {
    }
    
    public Vector3(final float[] array) {
        this.set(array[0], array[1], array[2]);
    }
    
    public Vector3(final float xValue, final float yValue, final float zValue) {
        this.set(xValue, yValue, zValue);
    }
    
    public Vector3(final Vector3 other) {
        this.set(other);
    }
    
    public final void add(final Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }
    
    public final void add(final float otherX, final float otherY, final float otherZ) {
        this.x += otherX;
        this.y += otherY;
        this.z += otherZ;
    }
    
    public final void subtract(final Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }
    
    public final void subtractMultiple(final Vector3 other, final float multiplicator) {
        this.x -= other.x * multiplicator;
        this.y -= other.y * multiplicator;
        this.z -= other.z * multiplicator;
    }
    
    public final void multiply(final float magnitude) {
        this.x *= magnitude;
        this.y *= magnitude;
        this.z *= magnitude;
    }
    
    public final void multiply(final Vector3 other) {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
    }
    
    public final void divide(final float magnitude) {
        if (magnitude != 0.0f) {
            this.x /= magnitude;
            this.y /= magnitude;
            this.z /= magnitude;
        }
    }
    
    public final void set(final Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
    
    public final void set(final float xValue, final float yValue, final float zValue) {
        this.x = xValue;
        this.y = yValue;
        this.z = zValue;
    }
    
    public final float dot(final Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
    
    public final Vector3 cross(final Vector3 other) {
        return new Vector3(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
    }
    
    public final float length() {
        return (float)Math.sqrt(this.length2());
    }
    
    public final float length2() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public final float distance2(final Vector3 other) {
        final float dx = this.x - other.x;
        final float dy = this.y - other.y;
        final float dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public final float normalize() {
        final float magnitude = this.length();
        if (magnitude != 0.0f) {
            this.x /= magnitude;
            this.y /= magnitude;
            this.z /= magnitude;
        }
        return magnitude;
    }
    
    public final void zero() {
        this.set(0.0f, 0.0f, 0.0f);
    }
    
    public final boolean pointsInSameDirection(final Vector3 other) {
        return this.dot(other) > 0.0f;
    }
}
