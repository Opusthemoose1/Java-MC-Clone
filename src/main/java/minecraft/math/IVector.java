package minecraft.math;

public interface IVector {

    public float getX();

    public float getY();

    public float getZ();

    public IVector setX(float x);

    public IVector setY(float y);

    public IVector setZ(float z);

    public IVector add(IVector vector);

    public IVector add(float x, float y, float z);

    public IVector multiply(float c);

    public IVector divide(float c);

    public IVector clone();

    public float lengthSquared();

    public float length();

    public IVector normalize();

}
