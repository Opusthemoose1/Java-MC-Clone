package minecraft.window.rendering;


import minecraft.WorldContext;
import minecraft.entity.AttackingEntity;
import minecraft.entity.Entity;
import minecraft.window.FrameRenderObserver;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class AttackingEntityRenderer extends EntityRenderer {
    IMesh mesh;
    Entity entity;
    Matrix4f modelMatrix;

    private int VAO;
    private int EBO;

    public AttackingEntityRenderer(AttackingEntity entity, IMesh mesh) {
        this.entity = entity;
        this.mesh = mesh;

        generateOpenGLData(mesh);

        // Initialize the model matrix to identity
        this.modelMatrix = new Matrix4f();
        modelMatrix.translate(entity.getLocation().toVector().toJOML());

    }
    private void generateOpenGLData(IMesh mesh)
    {
        int VBO = glGenBuffers();
        this.VAO = glGenVertexArrays();
        this.EBO = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBindVertexArray(this.VAO);

        final int VERTEX_COUNT = mesh.getVertices().size();
        final int INDEX_COUNT = mesh.getIndices().size();

        float[] GPUVertexData = new float[VERTEX_COUNT];
        int[] GPUIndexData = new int[INDEX_COUNT];

        final int STRIDE = mesh.getStride();
        for (int i = 0; i < VERTEX_COUNT; i++)
        {
            Vertex vertex = mesh.getVertices().get(i);
            GPUVertexData[i * STRIDE    ] = vertex.x();
            GPUVertexData[i * STRIDE + 1] = vertex.y();
            GPUVertexData[i * STRIDE + 2] = vertex.z();

            GPUVertexData[i * STRIDE + 3] = vertex.u();
            GPUVertexData[i * STRIDE + 4] = vertex.v();
        }
        for (int i = 0; i < INDEX_COUNT; i++)
        {
            GPUIndexData[i] = mesh.getIndices().get(i);
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, GPUIndexData, GL_STATIC_DRAW);
        // The vertices size in bytes no longer needs to be passed in
        glBufferData(GL_ARRAY_BUFFER, GPUVertexData, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE * Float.BYTES, 0L);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE * Float.BYTES, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

}
