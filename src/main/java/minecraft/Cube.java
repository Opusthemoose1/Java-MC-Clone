package minecraft;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Cube {
    final float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };
    private int VAO, VBO;
    Cube()
    {
        // Some changes relative to C/C++ OpenGL. glGenBuffers doesn't take any parameters, and will just return some buffer to write too
        this.VBO = glGenBuffers();
        this.VAO = glGenVertexArrays();
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        glBindVertexArray(this.VAO);
        // The vertices size in bytes no longer needs to be passed in
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false,3 * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }
    public int getVAO() {return this.VAO; };

}
