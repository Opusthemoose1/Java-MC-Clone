package minecraft.chunk;

public enum ChunkDirction {

    POSITIVE_X(0, (byte) 1, (byte) 0, (byte) 0),
    NEGATIVE_X(1, (byte) -1, (byte) 0, (byte) 0),
    POSITIVE_Y(2, (byte) 0, (byte) 1, (byte) 0),
    NEGATIVE_Y(3, (byte) 0, (byte) -1, (byte) 0),
    POSITIVE_Z(4, (byte) 0, (byte) 0, (byte) 1),
    NEGATIVE_Z(5, (byte) 0, (byte) 0, (byte) -1);

    private final byte x, y, z;
    private final int index;

    ChunkDirction(int directionIndex, byte x, byte y, byte z) {
        this.index = directionIndex;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getIndex() {
        return index;
    }

    public byte getX() {
        return x;
    }

    public byte getY() {
        return y;
    }

    public byte getZ() {
        return z;
    }

}
