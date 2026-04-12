package minecraft.chunk;

import minecraft.Material;

public class FlatWorldChunk extends Chunk {

    public static final int DIRT_LEVEL = 15, HEIGHT = 16;

    public FlatWorldChunk(int xOffset, int zOffset) {
        super(xOffset, zOffset);
    }

    @Override
    public void setInitialBlocks() {
        for (int x = 0; x < IChunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < IChunk.CHUNK_SIZE; z++) {
//                    if (y >= DIRT_LEVEL) setChunkBlock(x, y, z, Material.DIRT);
//                    else setChunkBlock(x, y, z, Material.COBBLESTONE);
                    if (y == IChunk.CHUNK_SIZE - 1) blocks[y][x][z] = new ChunkBlock(Material.DIRT.getId());
                    else blocks[y][x][z] = new ChunkBlock(Material.COBBLESTONE.getId());
                }
            }
        }
    }
}
