package minecraft.math;

import org.joml.Vector3f;

public interface IVector {

    float getX();

    float getY();

    float getZ();

    IVector setX(float x);

    IVector setY(float y);

    IVector setZ(float z);

    IVector add(IVector vector);

    IVector add(float x, float y, float z);

    IVector subtract(IVector vector);

    IVector subtract(float x, float y, float z);

    IVector multiply(float c);

    IVector divide(float c);

    IVector clone();

    float lengthSquared();

    float length();

    IVector normalize();

    boolean isZero();

    IVector cross(IVector vector);

    Vector3f toJOML();

}
