package minecraft;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Minecraft {

    private Window window;
    private Shader shader;
    private Chunk chunk;
    private Texture tex;
    private Camera camera;
    private TextRenderer text;
    // For getting JVM info
    private  Runtime rt;
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
        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init
        this.window = new Window(1920, 1080);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        this.shader = new Shader("src/resources/shaders/basic.vert", "src/resources/shaders/basic.frag");
        this.chunk = new Chunk(0, 0);
        this.tex = new Texture("src/resources/textures/cobblestone.png");
        this.camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f));
        this.camera.updateProjectionMatrix(90.0f, 1920.0f, 1080.0f);
        Shader text_shader = new Shader("src/resources/shaders/text.vert", "src/resources/shaders/text.frag");
        this.text = new TextRenderer("src/resources/textures/ascii.png", text_shader );
        this.rt = Runtime.getRuntime();

        this.shader.bind();
        this.shader.setMatrix4(chunk.getModelMatrix(), "model");
        this.shader.setMatrix4(camera.getProjectionMatrix(), "projection");

        Input.init(this.window.getWindowHandle());
        // Turn on depth buffer
        glEnable(GL_DEPTH_TEST);
    }


    private void loop() {

        // Set the clear color
        glClearColor(0.0f, 0.2f, 0.8f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        double lastFrameTime = 0.0;
        while ( !glfwWindowShouldClose(window.getWindowHandle()) ) {
            double currentFrameTime = glfwGetTime();
            double deltaTime = currentFrameTime - lastFrameTime;
            double framesPerSecond = Math.round( 1.0f / deltaTime);
            lastFrameTime = currentFrameTime;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            this.shader.bind();
            this.shader.setMatrix4(camera.getViewMatrix(), "view");
            glBindTexture(GL_TEXTURE_2D, this.tex.getTextureID());
            glBindVertexArray(this.chunk.getVAO());

            glDrawElements(GL_TRIANGLES, chunk.getIndexCount(), GL_UNSIGNED_INT, 0);

            final String fpsCounter = String.valueOf(framesPerSecond);
            // Debug info
            text.renderText(camera.getOrtho(), new Vector2f(10, 64), 0.3f,"FPS: " + fpsCounter);
            final long total = this.rt.totalMemory();
            final long free = this.rt.freeMemory();
            final long max = this.rt.maxMemory();

            final long used = total - free;
            text.renderText(camera.getOrtho(), new Vector2f(10, 128), 0.3f,"Used: " + String.valueOf(used / 1024 / 1024) + " MB");

            glfwSwapBuffers(window.getWindowHandle()); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            Input.poll();
            if (Input.isKeyDown(GLFW_KEY_W))
            {

                this.camera.processInput(Camera_Direction.FORWARD, (float)deltaTime);
            }
            if (Input.isKeyDown(GLFW_KEY_S))
            {
                this.camera.processInput(Camera_Direction.BACKWARD, (float)deltaTime);
            }
            if (Input.isKeyDown(GLFW_KEY_D))
            {
                this.camera.processInput(Camera_Direction.RIGHT, (float)deltaTime);
            }
            if (Input.isKeyDown(GLFW_KEY_A)) {
                this.camera.processInput(Camera_Direction.LEFT, (float) deltaTime);
            }
            if (Input.isKeyDown(GLFW_KEY_ESCAPE))
            {
                glfwSetWindowShouldClose(this.window.getWindowHandle(), true);
            }
            camera.mouseControl(Input.getMousePos());
        }
    }


    public static void main(String[] args) {
        new Minecraft().run();
    }

}
