package minecraft.chunk;

import minecraft.Material;
import minecraft.Minecraft;


import java.util.*;



public class FlatWorldChunkLoader extends ChunkLoader {

    private static final int RENDER_DISTANCE = 8;

    public FlatWorldChunkLoader() {
        // Render it from the center of the player. Divide render distance by 2, go from -renderDistance/2 to renderDistance/2
        int halfRender = RENDER_DISTANCE;
        for (int i = -halfRender; i <= halfRender; i++) {
            for (int j = -halfRender; j <= halfRender; j++) {
                IChunk chunk = new FlatWorldChunk(i * Chunk.CHUNK_SIZE, j * IChunk.CHUNK_SIZE);
                chunk.setInitialBlocks();
                registerChunk(chunk);
                chunk.uploadGPUData();
            }
        }
    }

}
