package minecraft.chunk;

import java.util.Map;

public interface IChunkLoader {

    public Chunk getChunk(int x, int y);

    public ChunkBlock getBlock(double x, double y, double z);


}
