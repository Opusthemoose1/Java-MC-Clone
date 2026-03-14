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

    private long turnOffsetIntoKey(int x, int y)
    {
        return (((long)x) << 32) | (y & 0xffffffffL);
    }
    private void registerChunk(Chunk chunk)
    {
        // ChatGPT generated this, but it compacts the x, y position of a chunk into a single 64 bit long
        // This requires less code for insertion, but makes it less readable. But this goes on "under the hood"
        long key = turnOffsetIntoKey(chunk.getXPosition(), chunk.getYPosition());

        currentlyRenderedChunks.put(key, chunk);
    }
    public Map<Long, Chunk> getCurrentlyRenderedChunks() {
        return currentlyRenderedChunks;
    }

    public Chunk getChunk(int x, int y)
    {
        return  currentlyRenderedChunks.get(turnOffsetIntoKey(x, y));
    }
    // Get the block at the specified position. THIS ROUNDS IT DOWN TO THE NEAREST WHOLE NUMBER!
    public ChunkBlock getBlock(double x, double y, double z)
    {
        final Chunk chunkToSearch = getChunk((int)x / Chunk.SIDE_LENGTH, (int)y / Chunk.SIDE_LENGTH);
        if (chunkToSearch == null)
        {
            System.err.println("Chunk at (" + x + "," + y + ") does not exist");
            return null;
        }
        return chunkToSearch.getChunkBlock((int)x % Chunk.SIDE_LENGTH, (int)y % Chunk.SIDE_LENGTH, (int)z % Chunk.SIDE_LENGTH);


    }
}
