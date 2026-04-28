package minecraft.chunk;

import minecraft.Material;

public class HillWorldChunk extends Chunk {

    public static final int BASE_HEIGHT = 20, DIRT_HEIGHT = 3;
    public static final double AMPLITUDE = 1.5, FREQUENCY = 0.3;

    public HillWorldChunk(int xOffset, int zOffset){
        super(xOffset, zOffset);
    }

    @Override
    public void setInitialBlocks() {

        int stoneLayerStart = 1;

        // Note that if you decide to update height, you also need to update subchunks
        for (int x = 0; x < IChunk.CHUNK_SIZE; x++) {
            for (int z = 0; z < IChunk.CHUNK_SIZE; z++) {

                // Sum of sines height formula — tweak frequency/amplitude freely
                double height = BASE_HEIGHT
                        + AMPLITUDE * Math.sin(x * FREQUENCY)
                        + AMPLITUDE * Math.sin(z * FREQUENCY)
                        + AMPLITUDE * Math.sin((x + z) * FREQUENCY);

                int surfaceY = (int) Math.round(height);

                setChunkBlock(x, 0, z, Material.BEDROCK);

                for (int y = stoneLayerStart; y < surfaceY - DIRT_HEIGHT; y++) {
                    setChunkBlock(x, y, z, Material.STONE);
                }

                for (int y = surfaceY - DIRT_HEIGHT; y < surfaceY; y++) {
                    setChunkBlock(x, y, z, Material.DIRT);
                }

                setChunkBlock(x, surfaceY, z, Material.GRASS);
            }
        }
    }
}
