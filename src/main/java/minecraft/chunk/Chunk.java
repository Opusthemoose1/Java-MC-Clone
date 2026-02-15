package minecraft.chunk;

import org.joml.Matrix4f;
import org.joml.Vector3f;

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
    // TODO: THIS IS VERY TEMPORARY! THIS WILL CHANGE A LOT!!
    final float[] vertices = {
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    };

    private int VAO, VBO;
    private int visibleBlocks;
    Matrix4f modelMatrix;

    final int SIDE_LENGTH = 16;
    byte[] blocks = new byte[SIDE_LENGTH * SIDE_LENGTH * SIDE_LENGTH];

    int offset_x, offset_y;
    // VERY COOL FUNCTION that gets the block at the specified coordinates
    private int index(int x, int y, int z)
    {
        return x + SIDE_LENGTH * (y + SIDE_LENGTH * z);
    }
    // Returns true if the block can be seen from the outside, false if it's completed surrounded
    private Boolean isBlockVisible(int x, int y, int z)
    {
        // Edge faces are always visible
        if (x - 1 < 0 || y - 1 < 0 || z - 1 < 0) return true;
        if (x + 1 > 15 || y + 1 > 15 || z + 1 > 15) return true;
        return !(blocks[index(x - 1, y, z)] != 0 &&
                blocks[index(x + 1, y, z)] != 0 &&
                blocks[index(x, y - 1, z)] != 0 &&
                blocks[index(x, y + 1, z)] != 0 &&
                blocks[index(x, y, z - 1)] != 0 &&
                blocks[index(x, y, z + 1)] != 0
        );
    }

    public Chunk(int xOffset, int yOffset) // x, y are the offsets from world origin
    {
        this.offset_x = xOffset;
        this.offset_y = yOffset;
        int SIDE_LENGTH = 16;
        final int FLOATS_PER_VERTEX = 5;
        final int VERTICES_PER_CUBE = 36;
        final int FLOATS_PER_CUBE = VERTICES_PER_CUBE * FLOATS_PER_VERTEX;

        int cubeCount = SIDE_LENGTH * SIDE_LENGTH * SIDE_LENGTH;
        final float[] chunkData = new float[cubeCount * FLOATS_PER_CUBE]; // VBO For the chunk that gets updated

        for (int i = 0; i < cubeCount; i++) this.blocks[i] = 1;

        int visibleCount = 0;
        for (int x = 0; x < SIDE_LENGTH; x++)
        {
            for (int y = 0; y < SIDE_LENGTH; y++)
            {
                for (int z = 0; z < SIDE_LENGTH; z++)
                {
                    if (!isBlockVisible(x, y, z)) continue;

                        int base = visibleCount * FLOATS_PER_CUBE;

                        System.arraycopy(vertices, 0, chunkData, base, FLOATS_PER_CUBE);
                        for (int v = 0; v < VERTICES_PER_CUBE; v++) {
                            int idx = base + v * FLOATS_PER_VERTEX;
                            chunkData[idx] += (float) x;
                            chunkData[idx + 1] += (float) y;
                            chunkData[idx + 2] += (float) z;
                        }
                        visibleCount++;


                }
            }
        }

        this.visibleBlocks = visibleCount;
        // Some changes relative to C/C++ OpenGL. glGenBuffers doesn't take any parameters, and will just return some buffer to write too
        this.VBO = glGenBuffers();
        this.VAO = glGenVertexArrays();
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBindVertexArray(this.VAO);
        // The vertices size in bytes no longer needs to be passed in
        glBufferData(GL_ARRAY_BUFFER, chunkData, GL_STATIC_DRAW);

        final int STRIDE = (3 + 2) * Float.BYTES;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE, 0L);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        // Initialize the model matrix to identity
        this.modelMatrix = new Matrix4f();
        modelMatrix.translate(new Vector3f(0.0f, 0.0f, 0.0f));
    }

    public int getVAO() {return this.VAO; };

    public Matrix4f getModelMatrix() {return this.modelMatrix; };

    public int getVisibleBlocks() {return this.visibleBlocks; };
}
