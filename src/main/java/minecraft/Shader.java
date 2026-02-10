package minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    int shaderProgramId;
    private static void compileShader(int shader, String code)
    {

        glShaderSource(
                shader,
                code
            );
         glCompileShader(shader);
         if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
             throw new IllegalStateException("Failed to compile shader.");
         }

    }
    Shader(String vertShaderFilePath, String fragShaderFilePath)
    {
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
}
