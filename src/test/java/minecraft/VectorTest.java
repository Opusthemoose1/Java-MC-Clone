package minecraft;

import minecraft.math.IVector;
import minecraft.math.Vector;
import org.junit.jupiter.api.Test;

public class VectorTest {

    @Test
    public void testClone() {
        IVector vector = new Vector(1, 2, 3);
        IVector vector2 = vector.clone();

        vector.setX(8);
        vector.setY(2);
        vector.setZ(1);

        assert vector.getX() == 8;
        assert vector.getY() == 2;
        assert vector.getZ() == 1;

        //original vector unchanged
        assert vector2.getX() == 1;
        assert vector2.getY() == 2;
        assert vector2.getZ() == 3;
    }

    @Test
    public void testAdd() {
        IVector vector = new Vector(1, 2, 3);
        IVector vector2 = new Vector(-1, 4, 2);

        vector.add(vector2);

        assert vector.getX() == 0;
        assert vector.getY() == 6;
        assert vector.getZ() == 5;

        //original vector unchanged
        assert vector2.getX() == -1;
        assert vector2.getY() == 4;
        assert vector2.getZ() == 2;
    }

    @Test
    public void testSubtract() {
        IVector vector = new Vector(6, 4, 2);
        IVector vector2 = new Vector(7, 1, -2);

        vector.subtract(vector2);

        assert vector.getX() == -1;
        assert vector.getY() == 3;
        assert vector.getZ() == 4;

        //original vector unchanged
        assert vector2.getX() == 7;
        assert vector2.getY() == 1;
        assert vector2.getZ() == -2;
    }

    @Test
    public void testToString() {
        IVector vector = new Vector(1, 2, 3);
        assert vector.toString().equals("[1.0, 2.0, 3.0]");
    }

    @Test
    public void testMultiply() {
        float epsilon = 0.001f;
        IVector vector = new Vector(1, 2, 3);

        vector.multiply(1.5f);

        assert Math.abs(vector.getX() - 1.5f) < epsilon;
        assert Math.abs(vector.getY() - 3f) < epsilon;
        assert Math.abs(vector.getZ() - 4.5f) < epsilon;
    }

}