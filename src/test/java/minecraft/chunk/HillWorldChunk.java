package minecraft.chunk;

import minecraft.Material;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class HillWorldChunk extends Chunk {

    public HillWorldChunk(int xOffset, int zOffset){
        super(xOffset, zOffset);
    }

    @Override
    public void setInitialBlocks() {
        // Note that if you decide to update height, you also need to update subchunks
        for (int x = 0; x < IChunk.CHUNK_SIZE; x++) {
            for (int z = 0; z < IChunk.CHUNK_SIZE; z++) {

                // Sum of sines height formula — tweak frequency/amplitude freely
                final double AMPLITUDE = 1.5;
                final double FREQUENCY = 0.3;
                double height = (IChunk.CHUNK_SIZE / 2.0)
                        + AMPLITUDE * Math.sin(x * FREQUENCY)
                        + AMPLITUDE * Math.sin(z * FREQUENCY)
                        + AMPLITUDE * Math.sin((x + z) * FREQUENCY);

                int surfaceY = (int) Math.round(height);

                for (int y = 0; y < IChunk.CHUNK_SIZE; y++) {
                    if (y < surfaceY - 3) {
                        blocks[y][x][z] = new ChunkBlock(Material.COBBLESTONE);
                    } else if (y < surfaceY) {
                        blocks[y][x][z] = new ChunkBlock(Material.DIRT);
                    } else if (y == surfaceY) { // replace if we want / add grass texture
                        blocks[y][x][z] = new ChunkBlock(Material.DIRT);
                    }
                    // else: leave as air / null
                }
            }
        }
    }
}
