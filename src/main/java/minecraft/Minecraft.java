package minecraft;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Minecraft {

    private Window window;
    private Shader shader;
    private Chunk chunk;
    private Texture tex;
    private Camera camera;
    private TextRenderer text;

    private Input input;

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
        Shader textShader = new Shader("src/resources/shaders/text.vert", "src/resources/shaders/text.frag");
        this.text = new TextRenderer("src/resources/textures/ascii.png", textShader );

        this.shader.bind();
        this.shader.setMatrix4(chunk.getModelMatrix(), "model");
        this.shader.setMatrix4(camera.getProjectionMatrix(), "projection");

        this.input = new Input(this.window.getWindowHandle());

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
            glDrawArrays(GL_TRIANGLES, 0, chunk.getVisibleBlocks() * 36);

            final String fpsCounter = String.valueOf(framesPerSecond);
            text.renderText(camera.getOrtho(), new Vector2f(10, 64), 0.3f,"FPS: " + fpsCounter);

            glfwSwapBuffers(window.getWindowHandle()); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            input.poll();
            if (input.isKeyDown(GLFW_KEY_W)) this.camera.processInput(Camera.CameraDirection.FORWARD, (float)deltaTime);
            if (input.isKeyDown(GLFW_KEY_S)) this.camera.processInput(Camera.CameraDirection.BACKWARD, (float)deltaTime);
            if (input.isKeyDown(GLFW_KEY_D)) this.camera.processInput(Camera.CameraDirection.RIGHT, (float)deltaTime);
            if (input.isKeyDown(GLFW_KEY_A)) this.camera.processInput(Camera.CameraDirection.LEFT, (float) deltaTime);
            if (input.isKeyDown(GLFW_KEY_ESCAPE)) glfwSetWindowShouldClose(this.window.getWindowHandle(), true);

            camera.mouseControl(input.getMousePos());
        }
    }


    public static void main(String[] args) {
        new Minecraft().run();
    }

}
