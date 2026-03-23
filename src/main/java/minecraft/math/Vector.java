package minecraft.math;

import org.joml.Vector3f;

/**
 * Adapter class to JOML Vector3f
 */
public class Vector implements IVector {

    private final Vector3f jomlVector;

    public static IVector newZeroVector() {
        return new Vector(0, 0, 0);
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
}
