package minecraft.chunk;

import minecraft.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class SubChunk implements IChunk{

    private final int xOffset, yOffset, zOffset;
    private static final int GPU_BUFFER_SIZE = 1028;

    private int VAO;
    private int EBO;
    private int indexCount;
    private int vertexCount;

    private int visibleBlocks;
    private Matrix4f modelMatrix;

    float[] GPUVertexData;
    int[] GPUIndexData;

    final int STRIDE = 6;

    protected ChunkBlock[][][] blocks = new ChunkBlock[SUBCHUNK_HEIGHT][CHUNK_SIZE][CHUNK_SIZE];

    public SubChunk(int xOffset, int yOffset, int zOffset)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;

        // Initialize vertex data
        this.vertexCount = 0;
        this.indexCount = 0;

        this.visibleBlocks = 0;
        GPUVertexData = new float[GPU_BUFFER_SIZE];
        GPUIndexData = new int[GPU_BUFFER_SIZE];

        generateFilledSubChunk();
    }
    private static final float[][] FACE_VERTICES = {
            // Positive X. X Y Z | U V | blockType
            {
                    0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 0.0f,
                    0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
                    0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f
            },

            // Negative X
            {
                    -0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 0.0f,
                    -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
                    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f,
                    -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f
            },

            // Positive Y
            {
                    0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 0.0f,
                    -0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
                    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f
            },

            // Negative Y
            {
                    0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f,
                    -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
                    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f,
                    0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 0.0f
            },

            // Positive Z
            {
                    0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 0.0f,
                    -0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f,
                    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f
            },

            // Negative Z
            {
                    0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f,
                    -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f,
                    -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
                    0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f
            }
    };

    private void addVertex(float v) {
        if (vertexCount >= GPUVertexData.length)
        {
            GPUVertexData = Arrays.copyOf(GPUVertexData, GPUVertexData.length * 2);
        }
        GPUVertexData[vertexCount++] = v;
    }

    private void addIndex(int i) {
        if (indexCount >= GPUIndexData.length) {
            GPUIndexData = Arrays.copyOf(GPUIndexData, GPUIndexData.length * 2);
        }
        GPUIndexData[indexCount++] = i;
    }

    public void generateFilledSubChunk()
    {

    }
    @Override
    public ChunkBlock getChunkBlock(int x, int y, int z) {
        if (IChunk.isInvalidChunkCoordinates(x, y, z)) return new ChunkBlock(Material.AIR);
        ChunkBlock block = blocks[y][x][z];
        return block == null ? new ChunkBlock(Material.AIR) : block;
    }

    private void addBlockVertices(int x, int y, int z) {
        final int[] faceIndices = {
                0, 1, 3,
                1, 2, 3
        };

        for (ChunkDirction direction : ChunkDirction.values()) {
            int nx = x + direction.getX();
            int ny = y + direction.getY();
            int nz = z + direction.getZ();

            if (isAir(nx, ny, nz))
            {
                float[] faceVertices = FACE_VERTICES[direction.getIndex()];
                // Update the position of each vertex in the face
                for (int i = 0; i < 4; i++)
                {
                    int base = i * STRIDE;

                    // First three are xyz
                    addVertex(faceVertices[base]     + x + xOffset);
                    addVertex(faceVertices[base + 1] + y);
                    addVertex(faceVertices[base + 2] + z + zOffset);

                    // Last two are uv (texture coordinates)
                    addVertex(faceVertices[base + 3]);
                    addVertex(faceVertices[base + 4]);

                    addVertex(faceVertices[base + 5] = blocks[y][x][z].materialId() - 1);

                }
                // Copy over the index data
                int baseVertex = this.visibleBlocks * 4;
                for (int faceIndex : faceIndices) {
                    addIndex(faceIndex + baseVertex);
                }
                this.visibleBlocks++;
            }
        }
    }

    @Override
    public void setChunkBlock(int x, int y, int z, Material type) {
        if (IChunk.isInvalidChunkCoordinates(x, y, z)) return;
        blocks[y][x][z] = new ChunkBlock(type);

        // Re-initalize the GPU and index buffer data
        // TODO: This is not DRY at all, Chunk needs (yet another) refactor
        // Either split the GPU and CPU logic into different files or just write better functions
        GPUVertexData = new float[GPU_BUFFER_SIZE];
        GPUIndexData = new int[GPU_BUFFER_SIZE];

        this.vertexCount = 0;
        this.indexCount = 0;
        this.visibleBlocks = 0;

        for (int x1 = 0; x1 < IChunk.CHUNK_SIZE; x1++) {
            for (int y1 = 0; y1 < IChunk.CHUNK_SIZE; y1++) {
                for (int z1 = 0; z1 < IChunk.CHUNK_SIZE; z1++) {
                    if (!getChunkBlock(x1, y1, z1).isType(Material.AIR))
                    {
                        addBlockVertices(x1, y1, z1);
                    }

                }
            }
        }
        uploadChunkData();


    }

    @Override
    public boolean isAir(int x, int y, int z) {
        return getChunkBlock(x, y, z).isType(Material.AIR);
    }


    protected void uploadChunkData() {
        // Some changes relative to C/C++ OpenGL. glGenBuffers doesn't take any parameters, and will just return some buffer to write too
        int VBO = glGenBuffers();
        this.VAO = glGenVertexArrays();
        this.EBO = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBindVertexArray(this.VAO);

        GPUVertexData = Arrays.copyOf(GPUVertexData, vertexCount);
        GPUIndexData = Arrays.copyOf(GPUIndexData, indexCount);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, GPUIndexData, GL_STATIC_DRAW);
        // The vertices size in bytes no longer needs to be passed in
        glBufferData(GL_ARRAY_BUFFER, GPUVertexData, GL_STATIC_DRAW);


        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE * Float.BYTES, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 1, GL_FLOAT, false, STRIDE * Float.BYTES, 5L * Float.BYTES);
        glEnableVertexAttribArray(2);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        // Initialize the model matrix to identity
        this.modelMatrix = new Matrix4f();
        modelMatrix.translate(new Vector3f(0.0f, 0.0f, 0.0f));
    }


    @Override
    public void render()
    {
        glBindVertexArray(VAO);

        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
    }

    @Override
    public void setInitialBlocks() {};

    protected int getXOffset() {return xOffset; };
    protected int getYOffset() {return yOffset; };
    protected int getZOffset() {return zOffset; };
}
