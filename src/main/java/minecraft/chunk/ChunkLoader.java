package minecraft.chunk;

import minecraft.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class ChunkLoader implements IChunkLoader {

    private final HashMap<Integer, IChunk> renderedChunks = new HashMap<>();

    public ChunkLoader() {

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
        int blockX = (int) Math.floor(x);
        int blockZ = (int) Math.floor(z);

        int chunkOffsetX = Math.floorDiv(blockX, IChunk.CHUNK_SIZE) * IChunk.CHUNK_SIZE;
        int chunkOffsetZ = Math.floorDiv(blockZ, IChunk.CHUNK_SIZE) * IChunk.CHUNK_SIZE;

        IChunk chunkToSearch = getChunk(chunkOffsetX, chunkOffsetZ);
        if (chunkToSearch == null) return new ChunkBlock(Material.AIR.getId()); //out of bounds

        int localX = blockX - chunkOffsetX;
        int localZ = blockZ - chunkOffsetZ;

        return chunkToSearch.getChunkBlock(localX, (int) y, localZ);
    }

    @Override
    public void setBlock(double x, double y, double z, Material type) {
        int blockX = (int) Math.floor(x);
        int blockZ = (int) Math.floor(z);

        int chunkOffsetX = Math.floorDiv(blockX, IChunk.CHUNK_SIZE) * IChunk.CHUNK_SIZE;
        int chunkOffsetZ = Math.floorDiv(blockZ, IChunk.CHUNK_SIZE) * IChunk.CHUNK_SIZE;

        IChunk chunk = getChunk(chunkOffsetX, chunkOffsetZ);
        if (chunk == null) return;

        int localX = blockX - chunkOffsetX;
        int localZ = blockZ - chunkOffsetZ;

        chunk.setChunkBlock(localX, (int) y, localZ, type);
        chunk.uploadGPUData();
    }
}
