package minecraft.chunk;

import minecraft.Material;
import minecraft.chunk.location.Location;

public class Block {

    private final Location location;
    private final ChunkBlock chunkBlock;
    private final IChunkLoader chunkLoader;

    public Block(Location location, ChunkBlock chunkBlock, IChunkLoader chunkLoader) {
        this.location = location.clone();
        this.chunkBlock = chunkBlock;
        this.chunkLoader = chunkLoader;

        this.location.setX((int) this.location.getX());
        this.location.setY((int) this.location.getY());
        this.location.setZ((int) this.location.getZ());
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
