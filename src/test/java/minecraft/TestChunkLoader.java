package minecraft;

import minecraft.block.Material;
import minecraft.chunk.Chunk;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.IChunk;
import minecraft.chunk.IChunkLoader;

import java.util.List;

public class TestChunkLoader implements IChunkLoader {

    private final int yLevel;

    public TestChunkLoader(int yLevel) {
        this.yLevel = yLevel;
    }

    @Override
    public Chunk getChunk(int x, int y) {
        return null;
    }

    @Override
    public ChunkBlock getBlock(double x, double y, double z) {
        return new ChunkBlock(y <= yLevel ? Material.COBBLESTONE.getId() : Material.AIR.getId());
    }

    @Override
    public List<IChunk> getRenderedChunks() {
        return List.of();
    }
}
