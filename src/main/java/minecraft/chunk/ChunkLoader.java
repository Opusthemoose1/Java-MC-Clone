package minecraft.chunk;


public class ChunkLoader implements IChunkLoader {

    private Chunk chunk;

    public ChunkLoader(){
        this.chunk = new Chunk(0, 0); //testing a single chunk for now
    }

    public Chunk getChunk() { //TODo remove
        return chunk;
    }
}
