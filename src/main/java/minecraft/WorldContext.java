package minecraft;

import minecraft.chunk.Block;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.IChunkLoader;
import minecraft.chunk.location.Location;
import minecraft.entity.IEntityManager;

public class WorldContext {

    private final IChunkLoader chunkLoader;
    private final IEntityManager entityManager;

    public WorldContext(IChunkLoader chunkLoader, IEntityManager entityManager) {
        this.chunkLoader = chunkLoader;
        this.entityManager = entityManager;
    }

    public IChunkLoader getChunkLoader() {
        return chunkLoader;
    }

    public IEntityManager getEntityManager() {
        return entityManager;
    }

    public Block getBlock(Location location) {
        ChunkBlock chunkBlock = chunkLoader.getBlock(location.getX(), location.getY(), location.getZ());
        return new Block(location, chunkBlock, chunkLoader);
    }

}
