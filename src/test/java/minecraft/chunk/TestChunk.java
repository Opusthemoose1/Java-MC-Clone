package minecraft.chunk;

import minecraft.Material;

public class TestChunk implements IChunk {

    private ChunkBlock[][][] blocks = new ChunkBlock[IChunk.CHUNK_HEIGHT][IChunk.CHUNK_SIZE][IChunk.CHUNK_SIZE];

    public TestChunk(int yLevel) {
        for (int x = 0; x < IChunk.CHUNK_SIZE; x++) {
            for (int y = 0; y < yLevel; y++) {
                for (int z = 0; z < IChunk.CHUNK_SIZE; z++) {
                    blocks[y][x][z] = new ChunkBlock(Material.COBBLESTONE);
                }
            }
        }
    }

    @Override
    public ChunkBlock getChunkBlock(int x, int y, int z) {
        ChunkBlock block = blocks[y][x][z];
        return block == null ? new ChunkBlock(Material.AIR) : block;
    }

    @Override
    public void setChunkBlock(int x, int y, int z, Material type) {
        blocks[y][x][z] = new ChunkBlock(type);
    }

    @Override
    public boolean isAir(int x, int y, int z) {
        return false;
    }

    @Override
    public void render() {

    }

    @Override
    public void setInitialBlocks() {

    }

}
