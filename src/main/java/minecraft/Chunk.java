package minecraft;

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
    Matrix4f modelMatrix;

    int offset_x, offset_y;
    public Chunk(int x, int y) // x, y are the offsets from world origin
    {
        this.offset_x = x;
        this.offset_y = y;
        int SIDE_LENGTH = 16;
        final int FLOATS_PER_VERTEX = 5;
        final int VERTICES_PER_CUBE = 36;
        final int FLOATS_PER_CUBE = VERTICES_PER_CUBE * FLOATS_PER_VERTEX;

        int cubeCount = SIDE_LENGTH * SIDE_LENGTH * SIDE_LENGTH;
        final float[] chunkData = new float[cubeCount * FLOATS_PER_CUBE]; // VBO For the chunk that gets updated

        for (int i = 0; i < SIDE_LENGTH; i++)
        {
            for (int j = 0; j < SIDE_LENGTH; j++)
            {
                for (int k = 0; k < SIDE_LENGTH; k++)
                {
                    int cubeIndex = i * SIDE_LENGTH * SIDE_LENGTH +
                            j * SIDE_LENGTH +
                            k;
                    int base = cubeIndex * FLOATS_PER_CUBE;

                    System.arraycopy(vertices, 0, chunkData, base, FLOATS_PER_CUBE);
                    for (int v = 0; v < VERTICES_PER_CUBE; v++) {
                        int idx = base + v * FLOATS_PER_VERTEX;
                        chunkData[idx] += (float) i;
                        chunkData[idx + 1] += (float) j;
                        chunkData[idx + 2] += (float) k;
                    }

                }
            }
        }

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
}
