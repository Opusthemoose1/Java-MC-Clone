package minecraft.chunk;

import java.util.List;

public interface IChunkLoader {

    IChunk getChunk(int x, int y);

    ChunkBlock getBlock(double x, double y, double z);

    List<IChunk> getRenderedChunks();


}
