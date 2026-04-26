package minecraft.chunk;

import minecraft.Material;
import minecraft.entity.EntityFactory;
import minecraft.math.IVector;

import java.util.List;

public interface IChunkLoader {

    IChunk getChunk(int x, int y);

    ChunkBlock getBlock(double x, double y, double z);

    void setBlock(double x, double y, double z, Material type);

    List<IChunk> getRenderedChunks();
}
