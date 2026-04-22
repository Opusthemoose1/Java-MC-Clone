package minecraft.window.rendering;

import minecraft.window.Camera;
import minecraft.window.texture.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class MeshNoNormals implements IMesh{
    List<Vertex> vertices;
    List<Integer> indices;
    Shader shader;
    int VAO, EBO;
    final int STRIDE; // Stride is the number of elements until the next set of vertices. 3 for position, 2 for uv
    Matrix4f modelMatrix;

    public MeshNoNormals(List<Vertex> vertices, List<Integer> indices) {
        this.vertices = vertices;
        this.indices = indices;
        this.STRIDE = 5;
        this.modelMatrix = new Matrix4f();

        initializeGLData();
    }

    public MeshNoNormals(Shader shader, List<Vertex> vertices, List<Integer> indices) {
        this.shader = shader;
        this.vertices = vertices;
        this.indices = indices;
        this.STRIDE = 5;
        this.modelMatrix = new Matrix4f();

        initializeGLData();
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
    private void initializeGLData()
    {
        // Some changes relative to C/C++ OpenGL. glGenBuffers doesn't take any parameters, and will just return some buffer to write too
        int VBO = glGenBuffers();
        this.VAO = glGenVertexArrays();
        this.EBO = glGenBuffers();

        glBindVertexArray(this.VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        float[] floatArray = new float[vertices.size() * 5];
        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            floatArray[i * 5 + 0] = v.x();
            floatArray[i * 5 + 1] = v.y();
            floatArray[i * 5 + 2] = v.z();
            floatArray[i * 5 + 3] = v.u();
            floatArray[i * 5 + 4] = v.v();
        }
        int[] indexArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indexArray[i] = indices.get(i);
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexArray, GL_STATIC_DRAW);
        // The vertices size in bytes no longer needs to be passed in
        glBufferData(GL_ARRAY_BUFFER, floatArray, GL_STATIC_DRAW);


        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE * Float.BYTES, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        // Initialize the model matrix to identity
        modelMatrix = new Matrix4f();
        modelMatrix.translate(new Vector3f(0.0f, 20.0f, 0.0f));
    }

    public void draw(Camera camera){
        if (shader == null) {
            System.err.println("Shader is null! Can't draw entity");
            return;
        }
        shader.bind();
        shader.setMatrix4(camera.getProjectionMatrix(), "projection");
        shader.setMatrix4(camera.getViewMatrix(), "view");
        shader.setMatrix4(modelMatrix, "model");

        glBindVertexArray(VAO);

        glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_INT, 0);


    }

    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }
    @Override
    public List<Integer> getIndices() {
        return indices;
    }

    @Override
    public int getStride() {
        return STRIDE;
    }
}
