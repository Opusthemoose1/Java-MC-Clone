package minecraft.chunk;

import minecraft.block.Material;

import java.util.HashMap;
import java.util.Map;

// TODO: Facade pattern around chunk renderer, chunk loader etc to have all the chunk API in a single class
public class ChunkLoader implements IChunkLoader {

    private static Map<Long, Chunk> currentlyRenderedChunks;
    private static int renderDistance = 16;

    private static ChunkLoader chunkLoader = null;

    private ChunkLoader() {};

    public static ChunkLoader GetInstance()
    {
        if (chunkLoader == null)
        {
            InitializeChunks(renderDistance);
            chunkLoader = new ChunkLoader();
        }
        return chunkLoader;
    }


    private static void InitializeChunks(int renderDistance){
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

    private static long turnOffsetIntoKey(int x, int y)
    {
        return (((long)x) << 32) | (y & 0xffffffffL);
    }
    private static void registerChunk(Chunk chunk)
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
        final Chunk chunkToSearch = getChunk((int)x / Chunk.SIDE_LENGTH, (int)z / Chunk.SIDE_LENGTH);
        if (chunkToSearch == null)
        {
            System.err.println("Chunk at (" + (int)x + "," + (int)z + ") does not exist");
            return new ChunkBlock((byte) 0);
        }
        return chunkToSearch.getChunkBlock((int)x % Chunk.SIDE_LENGTH, (int)y % Chunk.SIDE_LENGTH, (int)z % Chunk.SIDE_LENGTH);


    }
}
