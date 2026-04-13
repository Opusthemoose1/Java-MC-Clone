package minecraft.chunk;

import minecraft.Material;

public interface IChunk {

    static final int CHUNK_SIZE = 16, CHUNK_HEIGHT = 64;
    static final int SUBCHUNK_HEIGHT = 16;

    ChunkBlock getChunkBlock(int x, int y, int z);

    void setChunkBlock(int x, int y, int z, Material type);

    static int getChunkHash(short x, short z) {
        return (x << 16) | z; // [16-bit-x][16-bit-z]
    }

    boolean isAir(int x, int y, int z);

    void render();

    static boolean isInvalidChunkCoordinates(int x, int y, int z) {
        return (x < 0 || y < 0 || z < 0 ||
                x >= CHUNK_SIZE || y >= CHUNK_HEIGHT || z >= CHUNK_SIZE);
    }

    void setInitialBlocks();
}
