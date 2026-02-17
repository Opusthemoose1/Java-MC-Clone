package minecraft.chunk;

import java.util.ArrayList;

public interface IChunkLoader {

    public ArrayList<Chunk> getCurrentlyRenderedChunks();
}
