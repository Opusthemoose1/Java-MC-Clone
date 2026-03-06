package minecraft.chunk;

import java.util.Map;

public interface IChunkLoader {

    Map<Long, Chunk> getCurrentlyRenderedChunks();
}
