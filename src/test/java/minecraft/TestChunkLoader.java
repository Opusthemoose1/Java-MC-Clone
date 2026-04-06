package minecraft;

import minecraft.block.Material;
import minecraft.chunk.Chunk;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.IChunkLoader;

public class TestChunkLoader implements IChunkLoader {

    @Override
    public Chunk getChunk(int x, int y) {
        return null; //TODO
    }

    @Override
    public ChunkBlock getBlock(double x, double y, double z) {
        return new ChunkBlock(y < 10 ? Material.COBBLESTONE.getId() : Material.AIR.getId());
    }
}
