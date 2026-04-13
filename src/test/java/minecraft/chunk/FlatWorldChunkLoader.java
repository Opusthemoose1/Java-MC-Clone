package minecraft.chunk;

import minecraft.Material;
import minecraft.Minecraft;
import minecraft.math.IVector;

import java.util.*;

public class FlatWorldChunkLoader implements IChunkLoader {

    private final HashMap<Integer, IChunk> renderedChunks = new HashMap<>();

    private static final int RENDER_DISTANCE = 8;

    public FlatWorldChunkLoader() {
        // Render it from the center of the player. Divide render distance by 2, go from -renderDistance/2 to renderDistance/2
//        int halfRender = RENDER_DISTANCE/2;
//        for (int i = -halfRender; i < halfRender + 1; i++) {
//            for (int j = -halfRender; j < halfRender + 1; j++) {
//                IChunk chunk = new FlatWorldChunk(i * Chunk.CHUNK_SIZE, j * IChunk.CHUNK_SIZE);
//                chunk.setInitialBlocks();
//                registerChunk(chunk);
//                chunk.uploadGPUData();
//            }
//        }
        IChunk chunk = new FlatWorldChunk(0, 0);
        chunk.setInitialBlocks();
        registerChunk(chunk);
        chunk.uploadGPUData();

    };

    public int getChunkOffset(int n) { //get the coordinates of a chunk in chunk map
        return (int) Math.floor((double) n / IChunk.CHUNK_SIZE);
    }

    public int getChunkRelativeCoordinates(double n) { //get the coordinates of a block within a chunk
        return (int) n % IChunk.CHUNK_SIZE;
    }

    // Registers the chunk based on the X and Z position. X and Z are indexed by 16 (0, 16), (-16, 16)
    protected void registerChunk(IChunk chunk) {
        renderedChunks.put(chunk.hashCode(), chunk); //hash code is directly the x and z offset, using this for lookup, which is why it is not a Set.
    }

    public IChunk getChunk(int x, int z) {
        return renderedChunks.get(IChunk.getChunkHash((short) x, (short) z));
    }

    @Override
    public List<IChunk> getRenderedChunks() {
        List<IChunk> chunks = new ArrayList<>();
        for (Map.Entry<Integer, IChunk> entry : renderedChunks.entrySet()) {
            chunks.add(entry.getValue());
        }
        return chunks;
    }

    // Get the block at the specified position
    @Override
    public ChunkBlock getBlock(double x, double y, double z) {
        int chunkOffsetX = getChunkOffset((int) x);
        int chunkOffsetZ = getChunkOffset((int) z);
        IChunk chunkToSearch = getChunk(chunkOffsetX, chunkOffsetZ);

        if (chunkToSearch == null) {
            Minecraft.getLogger().error("Chunk at ({}, {}) does not exist", chunkOffsetX, chunkOffsetZ);
            return new ChunkBlock(Material.AIR.getId());
        }
        return chunkToSearch.getChunkBlock((int) x % Chunk.CHUNK_SIZE, (int) y % Chunk.SUBCHUNK_HEIGHT, (int) z % Chunk.CHUNK_SIZE);
    }

    @Override
    public void setBlock(double x, double y, double z, Material type) {
        int chunkOffsetX = getChunkOffset((int) x);
        int chunkOffsetZ = getChunkOffset((int) z);
        IChunk chunk = getChunk(chunkOffsetX, chunkOffsetZ);
        chunk.setChunkBlock((int) x % IChunk.CHUNK_SIZE, (int) y % Chunk.CHUNK_SIZE, (int) z % IChunk.CHUNK_SIZE, type);
        chunk.uploadGPUData();
    }
}
