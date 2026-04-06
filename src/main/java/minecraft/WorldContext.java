package minecraft;

import minecraft.chunk.IChunkLoader;
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
}
