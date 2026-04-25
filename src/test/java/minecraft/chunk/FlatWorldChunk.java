package minecraft.chunk;

import minecraft.Material;

public class FlatWorldChunk extends Chunk {

    public static final int DIRT_LEVEL = 15, HEIGHT = 16;

    public FlatWorldChunk(int xOffset, int zOffset) {
        super(xOffset, zOffset);

    }

    @Override
    public void setInitialBlocks() {
        // Note that if you decide to update height, you also need to update subchunks
        for (int x = 0; x < IChunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < IChunk.CHUNK_SIZE; y++) {
                for (int z = 0; z < IChunk.CHUNK_SIZE; z++) {

                    if (y == IChunk.CHUNK_SIZE - 1) blocks[y][x][z] = new ChunkBlock(Material.DIRT);
                    else blocks[y][x][z] = new ChunkBlock(Material.COBBLESTONE);
                }
            }
        }
    }
}
