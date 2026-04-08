package minecraft;

import minecraft.block.Material;
import minecraft.chunk.Chunk;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.IChunkLoader;

import java.util.*;

public class FlatWorldChunkLoader implements IChunkLoader {

    private final HashMap<Integer, Chunk> renderedChunks = new HashMap<>();

    private static final int RENDER_DISTANCE = 4;

    public FlatWorldChunkLoader() {
        // Render it from the center of the player. Divide render distance by 2, go from -renderDistance/2 to renderDistance/2
        int halfRender = RENDER_DISTANCE/2;
        for (int i = -halfRender; i < halfRender; i++) {
            for (int j = -halfRender; j < halfRender; j++) {
                registerChunk(new Chunk(i * Chunk.CHUNK_SIZE, j * Chunk.CHUNK_SIZE));
            }
        }
    };

    public int roundToNearest16(int n) {
        return (int) (Math.round((double) n / 16) * 16);
    }

    // Registers the chunk based on the X and Z position. X and Z are indexed by 16 (0, 16), (-16, 16)
    protected void registerChunk(Chunk chunk) {
        renderedChunks.put(chunk.hashCode(), chunk); //hash code is directly the x and z offset, using this for lookup, which is why it is not a Set.
    }

    public Chunk getChunk(int x, int z) {
        return renderedChunks.get(Chunk.getChunkHash((short) x, (short) z));
    }

    @Override
    public List<Chunk> getRenderedChunks() {
        List<Chunk> chunks = new ArrayList<>();
        for (Map.Entry<Integer, Chunk> entry : renderedChunks.entrySet()) {
            chunks.add(entry.getValue());
        }
        return chunks;
    }

    // Get the block at the specified position. THIS ROUNDS IT DOWN TO THE NEAREST WHOLE NUMBER!
    public ChunkBlock getBlock(double x, double y, double z) {
        final int chunkOffsetX = roundToNearest16((int)x);
        final int chunkOffsetZ = roundToNearest16((int)z);
        final Chunk chunkToSearch = getChunk(chunkOffsetX, chunkOffsetZ);
        if (chunkToSearch == null) {
            Minecraft.getLogger().error("Chunk at ({}, {}) does not exist", chunkOffsetX, chunkOffsetZ);
            return new ChunkBlock(Material.AIR.getId());
        }
        return chunkToSearch.getChunkBlock(chunkOffsetX % Chunk.CHUNK_SIZE, (int)y % Chunk.CHUNK_SIZE, chunkOffsetZ % Chunk.CHUNK_SIZE);
    }
}
