package minecraft.chunk;


import java.util.ArrayList;

public class ChunkLoader implements IChunkLoader {

   // private Chunk chunk;
    private final ArrayList<Chunk> currentlyRenderedChunks;

    public ChunkLoader(){
        currentlyRenderedChunks = new ArrayList<Chunk>();
        currentlyRenderedChunks.add(new Chunk(0, 0));
        currentlyRenderedChunks.add(new Chunk(16, 0));
    }

    public ArrayList<Chunk> getCurrentlyRenderedChunks() { //TODo remove
        return currentlyRenderedChunks;
    }
}
