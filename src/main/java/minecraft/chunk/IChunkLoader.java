package minecraft.chunk;

import minecraft.Material;
import minecraft.chunk.location.Location;
import minecraft.entity.EntityFactory;
import minecraft.math.IVector;

import java.util.List;

public interface IChunkLoader {

    IChunk getChunk(int x, int y);

    default ChunkBlock getBlock(Location location) {
        return getBlock(location.getX(), location.getY(), location.getZ());
    }

    ChunkBlock getBlock(double x, double y, double z);

    default void setBlock(Location location, Material type) {
        setBlock(location.getX(), location.getY(), location.getZ(), type);
    }

    default int getSurfaceLevel(float x, float z) {
        int y = 1;
        for (; y < IChunk.CHUNK_HEIGHT; y++) {
            if (getBlock(x, y, z).isType(Material.AIR)) break;
        }
        return y;
    }

    void setBlock(double x, double y, double z, Material type);

    List<IChunk> getRenderedChunks();
}
