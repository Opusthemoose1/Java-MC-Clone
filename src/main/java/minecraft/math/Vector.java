package minecraft.math;

import org.joml.Vector3f;

/**
 * Adapter class to JOML Vector3f. Vector3f implements three interfaces, this implements one.
 */
public class Vector implements IVector {

    private final Vector3f jomlVector;

    public static IVector newZeroVector() {
        return new Vector(0, 0, 0);
    }

    public static Vector createVector(float x, float y, float z) {
        return new Vector(x, y, z);
    }

    public Vector() {
        jomlVector = new Vector3f();
    }

    public Vector(float x, float y, float z) {
        jomlVector = new Vector3f(x, y, z); //necessary to instantiate Vector3f since it is an external library with no factory pattern
    }

    @Override
    public float getX() {
        return jomlVector.x;
    }

    @Override
    public float getY() {
        return jomlVector.y;
    }

    @Override
    public float getZ() {
        return jomlVector.z;
    }

    @Override
    public IVector setX(float x) {
        jomlVector.x = x;
        return this;
    }

    @Override
    public IVector setY(float y) {
        jomlVector.y = y;
        return this;
    }

    @Override
    public IVector setZ(float z) {
        jomlVector.z = z;
        return this;
    }

    @Override
    public IVector add(IVector vector) {
        setX(getX() + vector.getX());
        setY(getY() + vector.getY());
        setZ(getZ() + vector.getZ());
        return this;
    }

    @Override
    public IVector add(float x, float y, float z) {
        setX(getX() + x);
        setY(getY() + y);
        setZ(getZ() + z);
        return this;
    }

    @Override
    public IVector subtract(IVector vector) {
        setX(getX() - vector.getX());
        setY(getY() - vector.getY());
        setZ(getZ() - vector.getZ());
        return this;
    }

    @Override
    public IVector subtract(float x, float y, float z) {
        setX(getX() - x);
        setY(getY() - y);
        setZ(getZ() - z);
        return this;
    }

    @Override
    public IVector multiply(float c) {
        setX(c * getX());
        setY(c * getY());
        setZ(c * getZ());
        return this;
    }

    @Override
    public IVector divide(float c) {
        multiply(1/c);
        return this;
    }

    @Override
    public IVector clone() {
        return new Vector(getX(), getY(), getZ());
    }

    @Override
    public float lengthSquared() {
        return jomlVector.lengthSquared();
    }

    @Override
    public float length() {
        return jomlVector.length();
    }

    @Override
    public IVector normalize() {
        jomlVector.normalize();
        return this;
    }

    @Override
    public IVector cross(IVector vector) {
        jomlVector.cross(vector.toJOML());
        return this;
    }

    public Vector3f toJOML() {
        return new Vector3f(getX(), getY(), getZ());
    }

    public boolean isZero() {
        return getX() == 0 && getY() == 0 && getZ() == 0;
    }

    public double dot(IVector vector) {
        return jomlVector.dot(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public String toString() {
        return "[" + getX() + ", " + getY() + ", " + getZ() + "]";
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null || object.getClass() != this.getClass()) return false;
        Vector other = (Vector) object;
        return other.getX() == getX() && other.getY() == getY() && other.getZ() == getZ();
    }
}
