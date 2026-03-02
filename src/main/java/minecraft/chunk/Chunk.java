package minecraft.chunk;

import minecraft.block.Material;
import minecraft.timer.Timer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

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
public class Chunk {

    private final int VAO;
    private final int EBO;
    private int indexCount;
    private int vertexCount;

    private int visibleBlocks;
    Matrix4f modelMatrix;

    final int SIDE_LENGTH = 16;
    final int STRIDE = 5;

    ChunkBlock[][][] blocks = new ChunkBlock[SIDE_LENGTH][SIDE_LENGTH][SIDE_LENGTH];

    int offset_x, offset_z;

    float[] GPU_vertex_data;
    int[] GPU_index_data;

    private void addVertex(float v)
    {
        if (vertexCount >= GPU_vertex_data.length)
        {
            GPU_vertex_data = Arrays.copyOf(GPU_vertex_data, GPU_vertex_data.length * 2);
        }
        GPU_vertex_data[vertexCount++] = v;
    }

    private void addIndex(int i)
    {
        if (indexCount >= GPU_index_data.length)
        {
            GPU_index_data = Arrays.copyOf(GPU_index_data, GPU_index_data.length * 2);
        }
        GPU_index_data[indexCount++] = i;
    }

    private static final float[][] FACE_VERTICES = {
            // Positive X
            {
                    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                    0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                    0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
            },

            // Negative X
            {
                    -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                    -0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                    -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
            },

            // Positive Y
            {
                    0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
                    -0.5f, 0.5f, -0.5f, 1.0f, 0.0f,
                    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
            },

            // Negative Y
            {
                    0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                    -0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                    -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
                    0.5f, -0.5f, 0.5f, 0.0f, 1.0f,
            },

            // Positive Z
            {
                    0.5f, -0.5f, 0.5f, 1.0f, 1.0f,
                    -0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
                    -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
                    0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
            },

            // Negative Z
            {
                    0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
                    -0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
                    -0.5f, 0.5f, -0.5f, 0.0f, 0.0f,
                    0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
            }
    };

    // Returns true if the face is touching air
    private Boolean isAir(int x, int y, int z)
    {
        return getChunkBlock(x, y, z) == null;
    }

    private ChunkBlock getChunkBlock(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 ||
                x >= SIDE_LENGTH ||
                y >= SIDE_LENGTH ||
                z >= SIDE_LENGTH) return null;

        return blocks[z][y][x];
    }

    private void addBlock(int x, int y, int z)
    {

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
                    addVertex(faceVertices[base]    + x + offset_x);
                    addVertex(faceVertices[base + 1] + y);
                    addVertex(faceVertices[base + 2] + z + offset_z);

                    // Last two are uv (texture coordinates)
                    addVertex(faceVertices[base + 3]);
                    addVertex(faceVertices[base + 4]);
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

    public Chunk(int xOffset, int zOffset) // x, z are the offsets from world origin
    {

        Timer timeToCreateChunk = new Timer();
        timeToCreateChunk.startTimer();

        this.vertexCount = 0;
        this.indexCount = 0;
        this.offset_x = xOffset;
        this.offset_z = zOffset;
        final int SIDE_LENGTH = 16;

        for (int i = 0; i < SIDE_LENGTH; i++)
            for (int j = 0; j < SIDE_LENGTH; j++)
                for (int k = 0; k < SIDE_LENGTH; k++)
                    blocks[i][j][k] = new ChunkBlock((byte) Material.COBBLESTONE.getId());


        this.visibleBlocks = 0;

        GPU_vertex_data = new float[4096];
        GPU_index_data = new int[4096];

        for (int x = 0; x < SIDE_LENGTH; x++)
        {
            for (int y = 0; y < SIDE_LENGTH; y++)
            {
                for (int z = 0; z < SIDE_LENGTH; z++)
                {
                    addBlock(x, y, z);
                }
            }
        }

        // Some changes relative to C/C++ OpenGL. glGenBuffers doesn't take any parameters, and will just return some buffer to write too
        int VBO = glGenBuffers();
        this.VAO = glGenVertexArrays();
        this.EBO = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBindVertexArray(this.VAO);

        GPU_vertex_data = Arrays.copyOf(GPU_vertex_data, vertexCount);
        GPU_index_data = Arrays.copyOf(GPU_index_data, indexCount);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, GPU_index_data, GL_STATIC_DRAW);
        // The vertices size in bytes no longer needs to be passed in
        glBufferData(GL_ARRAY_BUFFER, GPU_vertex_data, GL_STATIC_DRAW);


        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE * Float.BYTES, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        // Initialize the model matrix to identity
        this.modelMatrix = new Matrix4f();
        modelMatrix.translate(new Vector3f(0.0f, 0.0f, 0.0f));

        timeToCreateChunk.endTimer();
        System.out.println("Time to build chunk: " + timeToCreateChunk.getTimeInMilliseconds() + " ms");

    }
    public int getVAO() {return this.VAO; };
    public int getEBO() {return this.EBO; };
    public int getIndexCount() {return this.indexCount; }
    public Matrix4f getModelMatrix() {return this.modelMatrix; };
    public int getVisibleBlocks() {return this.visibleBlocks; };
}
