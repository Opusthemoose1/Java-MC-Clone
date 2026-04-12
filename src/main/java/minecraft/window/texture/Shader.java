package minecraft.window.texture;

import minecraft.Minecraft;
import minecraft.chunk.IChunk;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;

public class Shader implements IShader {

    private final int shaderProgramId;

    private static void compileShader(int shader, String code) {
        glShaderSource(
                shader,
                code
            );
         glCompileShader(shader);

         int status = glGetShaderi(shader, GL_COMPILE_STATUS);

         if (status == GL_FALSE) {
             String infoLog = glGetShaderInfoLog(shader);
             throw new IllegalStateException("Failed to compile shader: \n" + infoLog);
         }
    }

    public Shader(String vertShaderFilePath, String fragShaderFilePath) {
        int v = glCreateShader(GL_VERTEX_SHADER);
        int f = glCreateShader(GL_FRAGMENT_SHADER);
        String vertSource, fragSource;
        try {
            vertSource = Files.readString(Path.of(vertShaderFilePath));
            fragSource = Files.readString(Path.of(fragShaderFilePath));
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load shader files", e);
        }
        compileShader(v, vertSource);
        compileShader(f, fragSource);

        this.shaderProgramId = glCreateProgram();
        glAttachShader(this.shaderProgramId, v);
        glAttachShader(this.shaderProgramId, f);
        glLinkProgram(this.shaderProgramId);

        if (glGetProgrami(this.shaderProgramId, GL_LINK_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Failed to link program.");
        }
        // Once the shaders are bound to a program object we can remove the individual objects
        glDeleteShader(v);
        glDeleteShader(f);
    }

    public void bind()
    {
        glUseProgram(this.shaderProgramId);
    }

    public void setMatrix4(Matrix4f matrix, String uniformName) {
        int location = glGetUniformLocation(this.shaderProgramId, uniformName);
        if (location == -1) Minecraft.getLogger().info("Failed to locate uniform " + uniformName);
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            glUniformMatrix4fv(location, false, matrix.get(stack.mallocFloat(IChunk.CHUNK_SIZE)));
        }

    }

    public void setInt(int integer, String uniformName) {
        int location = glGetUniformLocation(shaderProgramId, uniformName);
        if (location == -1) Minecraft.getLogger().info("Failed to locate uniform " + uniformName);
        glUniform1i(location, integer);
    }

    public int[] createTextureArray(String uniformName) {
        int[] textureArray = new int[IChunk.CHUNK_SIZE];
        for (int i = 0; i < IChunk.CHUNK_SIZE; i++) {
            textureArray[i] = i;
        }

        int location = glGetUniformLocation(shaderProgramId, uniformName);
        if (location == -1) Minecraft.getLogger().info("Failed to locate uniform " + uniformName);
        glUniform1iv(location, textureArray);
        return textureArray;
    }
}
