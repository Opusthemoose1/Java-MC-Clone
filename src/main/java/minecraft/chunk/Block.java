package minecraft.chunk;

import minecraft.Material;
import minecraft.chunk.location.Location;

public class Block {

    private final Location location;
    private final ChunkBlock chunkBlock;
    private final IChunkLoader chunkLoader;

    public Block(Location location, ChunkBlock chunkBlock, IChunkLoader chunkLoader) {
        this.chunkBlock = chunkBlock;
        this.chunkLoader = chunkLoader;
        this.location = location.getBlockLocation();
    }

    public Material getMaterial() {
        return chunkBlock.getMaterial();
    }

    public Location getLocation() {
        return location.clone();
    }

    public void setType(Material type) {
        chunkLoader.setBlock(location.getX(), location.getY(), location.getZ(), type);
    }
}
