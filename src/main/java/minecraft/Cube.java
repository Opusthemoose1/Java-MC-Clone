package minecraft;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Cube {
    final float[] vertices = {
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
             0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
             0.0f, 0.5f,  0.0f, 0.0f, 0.5f,
    };
    private int VAO, VBO;
    Matrix4f modelMatrix;
    Cube(Vector3f position)
    {
        // Some changes relative to C/C++ OpenGL. glGenBuffers doesn't take any parameters, and will just return some buffer to write too
        this.VBO = glGenBuffers();
        this.VAO = glGenVertexArrays();
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBindVertexArray(this.VAO);
        // The vertices size in bytes no longer needs to be passed in
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);

        final int STRIDE = (3 + 2) * Float.BYTES;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE, 0L);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        // Initalize the model matrix to identity
        this.modelMatrix = new Matrix4f();
        modelMatrix.translate(position);

    }
    public int getVAO() {return this.VAO; };
    public Matrix4f getModelMatrix() {return this.modelMatrix; };
}
