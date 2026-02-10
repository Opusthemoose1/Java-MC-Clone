package minecraft;

import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Minecraft {

    private Window window;
    private Shader shader;
    private Cube cube;
    private Texture tex;
    private Camera camera;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window.getWindowHandle());
        glfwDestroyWindow(window.getWindowHandle());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
//        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init
        this.window = new Window(1920, 1080);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        this.shader = new Shader("src/resources/shaders/basic.vert", "src/resources/shaders/basic.frag");
        this.cube = new Cube(new Vector3f(0.0f, 0.0f,0.0f));
        this.tex = new Texture("src/resources/textures/cobblestone.png");
        this.camera = new Camera(new Vector3f(0.0f, 1.0f, 0.0f));
        this.camera.updateProjectionMatrix(90.0f, 1920, 1080);


        this.shader.setMatrix4(cube.getModelMatrix(), "model");
        System.out.println(cube.getModelMatrix().toString());
    }

    private void loop() {


        // Set the clear color
        glClearColor(0.0f, 0.2f, 0.8f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window.getWindowHandle()) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glUseProgram(this.shader.shaderProgramId);
            glBindTexture(GL_TEXTURE_2D, this.tex.getTextureID());
            glBindVertexArray(this.cube.getVAO());
            glDrawArrays(GL_TRIANGLES, 0, 3);

            glfwSwapBuffers(window.getWindowHandle()); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Minecraft().run();
    }

}
