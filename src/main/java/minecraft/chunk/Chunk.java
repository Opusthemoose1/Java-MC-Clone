package minecraft.chunk;

import minecraft.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

/*
Chunks are specified by their offset from world origin.
Each chunk is made from subchunks, where each subchunk is 16x16x16
Each subchunk contains its own VBO (mesh data). Meshes are constructed based on which block faces are visible
Each block is specified as an offset from the subchunk origin. It's then multiplied in the shader to map blocks to
world space
For simplicity and convience, I'll treat a chunk as a subchunk for now
 */
abstract public class Chunk implements IChunk {

    short offsetX, offsetZ;
    protected ChunkBlock[][][] blocks = new ChunkBlock[CHUNK_HEIGHT][CHUNK_SIZE][CHUNK_SIZE];


    // A list of subChunks that get managed by the chunk
    List<SubChunk> subChunks = new ArrayList<>();


    // x, z are the offsets from world origin
    public Chunk(int xOffset, int zOffset) {

        this.offsetX = (short) xOffset;
        this.offsetZ = (short) zOffset;

       // setInitialBlocks();
        final int SUBCHUNK_COUNT = CHUNK_HEIGHT / SUBCHUNK_HEIGHT;
        for (int i = 0; i < SUBCHUNK_COUNT; i++)
        {
           addSubChunk(new SubChunk(this, xOffset, i * IChunk.SUBCHUNK_HEIGHT, zOffset));
        }

    }
    private void addSubChunk(SubChunk subChunk)
    {
        subChunks.add(subChunk);
    }

    public List<SubChunk> getSubChunks()
    {
        return subChunks;
    }

    private SubChunk getSubChunk(int x, int y, int z)
    {
        for (SubChunk subChunk : subChunks)
        {
            if (subChunk.getXOffset() == x && subChunk.getYOffset() == y && subChunk.getZOffset() == z)
            {
                return subChunk;
            }
        }
        return null;
    }

    //abstract protected void setInitialBlocks();

    // Returns true if the face is touching air
    public boolean isAir(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= CHUNK_SIZE || y >= CHUNK_HEIGHT || z >= CHUNK_SIZE) return true;
        ChunkBlock block = blocks[y][x][z];
        return block != null && block.getMaterial().equals(Material.AIR);

    }

    @Override
    public ChunkBlock getChunkBlock(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= CHUNK_SIZE || y >= CHUNK_HEIGHT || z >= CHUNK_SIZE) return new ChunkBlock(Material.AIR);
        ChunkBlock block = blocks[y][x][z];
        return block == null ? new ChunkBlock(Material.AIR) : block;
    }

    @Override
    public void setChunkBlock(int x, int y, int z, Material type) {
        if (IChunk.isInvalidChunkCoordinates(x, y, z)) return;
        blocks[y][x][z] = new ChunkBlock(type);

    }

    @Override
    public int hashCode() {
        return IChunk.getChunkHash(this.offsetX, this.offsetZ);
    }

    // Push CPU block data to the GPU. Do this after every time you place / destroy a block
    // Note this is bad and slow rn but im just trying to get it working
    public void uploadGPUData()
    {
        for (SubChunk subChunk : subChunks)
        {
           subChunk.uploadChunkData();
        }

    }


    @Override
    public void render()
    {
        for (SubChunk subChunk : subChunks)
        {
            subChunk.render();
        }
    }

    public void setInitialBlocks() {};

}
