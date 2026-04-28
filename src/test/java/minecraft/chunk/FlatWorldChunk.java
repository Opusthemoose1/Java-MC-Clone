package minecraft.chunk;

import minecraft.Material;

public class FlatWorldChunk extends Chunk {

    public static final int HEIGHT = 20, STONE_LEVEL = 16;

    public FlatWorldChunk(int xOffset, int zOffset) {
        super(xOffset, zOffset);

    }

    @Override
    public void setInitialBlocks() {
        // Note that if you decide to update height, you also need to update subchunks
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < IChunk.CHUNK_SIZE; x++) {
                for (int z = 0; z < IChunk.CHUNK_SIZE; z++) {
                    if (y == 0) setChunkBlock(x, y, z, Material.BEDROCK);
                    else if (y <= STONE_LEVEL) setChunkBlock(x, y, z, Material.STONE);
                    else if (y < HEIGHT - 1) setChunkBlock(x, y, z, Material.DIRT);
                    else setChunkBlock(x, y, z, Material.GRASS);
                }
            }
        }
    }
}
