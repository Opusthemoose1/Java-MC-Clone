package minecraft.block;

import minecraft.chunk.Chunk;

public class Block {

    private Material material;
    private Chunk chunk;

    private int x, y, z;

    public Block(Material material, Chunk chunk, int x, int y, int z) {
        this.material = material;
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Material getMaterial() {
        return material;
    }

    public Chunk getChunk() {
        return chunk;
    }

}
