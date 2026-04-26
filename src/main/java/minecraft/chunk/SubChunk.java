package minecraft.chunk;

import minecraft.Material;
import org.joml.Matrix4f;
import org.joml.Vector3f;


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

public class SubChunk{

    private final int xOffset, yOffset, zOffset;
    private static final int GPU_BUFFER_SIZE = 1028;

    private int VAO;
    private int indexCount;
    private int vertexCount;

    private int visibleFaces;
    private Matrix4f modelMatrix;

    float[] GPUVertexData;
    int[] GPUIndexData;

    final int STRIDE = 6;
    Chunk parentChunk;


    public SubChunk(Chunk parentChunk, int xOffset, int yOffset, int zOffset)
    {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;

        // Initialize vertex data
        this.vertexCount = 0;
        this.indexCount = 0;

        this.visibleFaces = 0;
        GPUVertexData = new float[GPU_BUFFER_SIZE];
        GPUIndexData = new int[GPU_BUFFER_SIZE];

        this.parentChunk = parentChunk;

        uploadChunkData();
    }
    private static final float[][] FACE_VERTICES = {
            // Positive X
            {
                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                    1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                    1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f
            },

            // Negative X
            {
                    0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f
            },

            // Positive Y
            {
                    1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                    1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f
            },

            // Negative Y
            {
                    1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f
            },

            // Positive Z
            {
                    1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f,
                    1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f
            },

            // Negative Z
            {
                    1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                    1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
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


    private void addBlockVertices(int x, int y, int z) {
        final int[] faceIndices = {
                0, 1, 3,
                1, 2, 3
        };

        for (ChunkDirction direction : ChunkDirction.values()) {
            int nx = x + direction.getX();
            int ny = y + direction.getY();
            int nz = z + direction.getZ();
            final int VERTICES_PER_FACE= 4;
            if (parentChunk.isAir(nx, ny, nz))
            {
                float[] faceVertices = FACE_VERTICES[direction.getIndex()];
                // Update the position of each vertex in the face
                for (int i = 0; i < VERTICES_PER_FACE; i++)
                {
                    int base = i * STRIDE;

                    // First three are xyz
                    addVertex(faceVertices[base]     + x + xOffset);
                    addVertex(faceVertices[base + 1] + y);
                    addVertex(faceVertices[base + 2] + z + zOffset);

                    // Last two are uv (texture coordinates)
                    addVertex(faceVertices[base + 3]);
                    addVertex(faceVertices[base + 4]);

                   addVertex(parentChunk.blocks[y][x][z].materialId() - 1);

                }
                // Copy over the index data
                int baseVertex = this.visibleFaces * VERTICES_PER_FACE;
                for (int faceIndex : faceIndices) {
                    addIndex(faceIndex + baseVertex);
                }
                this.visibleFaces++;
            }
        }
    }


    public void uploadChunkData() {
        GPUVertexData = new float[GPU_BUFFER_SIZE];
        GPUIndexData = new int[GPU_BUFFER_SIZE];

        this.vertexCount = 0;
        this.indexCount = 0;
        this.visibleFaces = 0;

        for (int x1 = 0; x1 < IChunk.CHUNK_SIZE; x1++) {
            for (int y1 = yOffset; y1 < yOffset + IChunk.SUBCHUNK_HEIGHT; y1++) {
                for (int z1 = 0; z1 < IChunk.CHUNK_SIZE; z1++) {
                    if (!parentChunk.getChunkBlock(x1, y1, z1).isType(Material.AIR)) {
                        addBlockVertices(x1, y1, z1);
                      }
                    }
                }
            }
            // Some changes relative to C/C++ OpenGL. glGenBuffers doesn't take any parameters, and will just return some buffer to write too
            int VBO = glGenBuffers();
            this.VAO = glGenVertexArrays();
        int EBO = glGenBuffers();

            glBindBuffer(GL_ARRAY_BUFFER, VBO);
            glBindVertexArray(this.VAO);

            GPUVertexData = Arrays.copyOf(GPUVertexData, vertexCount);
            GPUIndexData = Arrays.copyOf(GPUIndexData, indexCount);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
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


    protected void render()
    {
        glBindVertexArray(VAO);

        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
    }

    public int getXOffset() {return xOffset; }
    public int getYOffset() {return yOffset; }
    public int getZOffset() {return zOffset; }


}
