package minecraft.chunk;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IChunkLoader {

    public Chunk getChunk(int x, int y);

    public ChunkBlock getBlock(double x, double y, double z);

    public List<Chunk> getRenderedChunks();


}
