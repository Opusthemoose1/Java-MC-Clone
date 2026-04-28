package minecraft.chunk;

import minecraft.Material;
import minecraft.entity.EntityFactory;
import minecraft.math.IVector;

import java.util.List;

public class TestChunkLoader implements IChunkLoader {

    private final int yLevel;
    private final IChunk testChunk;

    private int blockChanges = 0;

    public TestChunkLoader(int yLevel) {
        this(yLevel, null);
    }

    public TestChunkLoader(int yLevel, IChunk testChunk) {
        this.yLevel = yLevel;
        this.testChunk = testChunk;
    }

    @Override
    public IChunk getChunk(int x, int y) {
        return testChunk;
    }

    @Override
    public ChunkBlock getBlock(double x, double y, double z) {
        if (testChunk == null) return new ChunkBlock(y <= yLevel ? Material.COBBLESTONE.getId() : Material.AIR.getId());
        else return testChunk.getChunkBlock((int) x % IChunk.CHUNK_SIZE, (int) y, (int) z % IChunk.CHUNK_SIZE);
    }

    @Override
    public void setBlock(double x, double y, double z, Material type) {
        if (testChunk == null) return;
        testChunk.setChunkBlock((int) x % IChunk.CHUNK_SIZE, (int) y, (int) z % IChunk.CHUNK_SIZE, type);
        blockChanges++;
    }

    @Override
    public List<IChunk> getRenderedChunks() {
        return testChunk == null ? List.of() : List.of(testChunk);
    }

    public int getBlockChangesCount() {
        return blockChanges;
    }
}
