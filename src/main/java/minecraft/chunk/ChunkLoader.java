package minecraft.chunk;

import java.util.HashMap;
import java.util.Map;

public class ChunkLoader implements IChunkLoader {

    private final Map<Long, Chunk> currentlyRenderedChunks;

    public ChunkLoader(int renderDistance){
        currentlyRenderedChunks = new HashMap<>();
        final int CHUNK_SIZE = 16;
        // Render it from the center of the player. Divide render distance by 2, go from -renderDistance/2 to renderDistance/2
        final int HALF_RENDER = renderDistance/2;
        for (int i = -HALF_RENDER; i < HALF_RENDER; i++)
        {
            for (int j = -HALF_RENDER; j < HALF_RENDER; j++)
            {
                registerChunk(new Chunk(i * CHUNK_SIZE, j * CHUNK_SIZE));
            }
        }
    }

    private void registerChunk(Chunk chunk)
    {
        // ChatGPT generated this, but it compacts the x, y position of a chunk into a single 64 bit long
        // This requires less code for insertion, but makes it less readable. But this goes on "under the hood"
        long key = (((long)chunk.getXPosition()) << 32) | (chunk.getYPosition() & 0xffffffffL);

        currentlyRenderedChunks.put(key, chunk);
    }
    public Map<Long, Chunk> getCurrentlyRenderedChunks() {
        return currentlyRenderedChunks;
    }
}
