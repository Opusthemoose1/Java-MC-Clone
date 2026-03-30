package minecraft.chunk;

import minecraft.math.IVector;
import minecraft.math.Vector;

public class Location {

    private static final float DEFAULT_YAW = 0, DEFAULT_PITCH = 0;

    private float x, y, z, pitch, yaw;

    public static Location createLocation(float x, float y, float z) {
        return createLocation(x, y, z, DEFAULT_YAW, DEFAULT_PITCH);
    }

    public static Location createLocation(float x, float y, float z, float yaw, float pitch) {
        return new Location(x, y, z, yaw, pitch);
    }

    private Location(float x, float y, float z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void add(IVector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        this.z += vector.getZ();
    }

    public Location clone() {
        return createLocation(x, y, z, yaw, pitch);
    }

    public double getDistance(Location other) {
        double deltaX = other.getX() - x;
        double deltaY = other.getY() - y;
        double deltaZ = other.getZ() - z;
        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    public IVector toVector() {
        return new Vector(x, y, z);
    }

}
