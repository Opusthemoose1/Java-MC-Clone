package minecraft.window;

import minecraft.chunk.ChunkLoader;
import minecraft.chunk.IChunkLoader;
import minecraft.window.text.ITextRenderer;
import minecraft.window.texture.IShader;
import minecraft.block.Material;
import minecraft.window.input.IInput;
import minecraft.window.texture.Texture;
import minecraft.window.texture.TextureMap;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


// TODO: Singleton class, as we only want one instance of this
public class Window implements IWindow {

    private final int width, height;
    private long windowHandle; // Window handle for the GLFW context
    private double lastFrameTime = 0;

    private final Camera camera;
    private final TextureMap textureMap;
    private IShader shader;
    private ITextRenderer textRenderer;
    private IChunkLoader chunkLoader;

    private IInput input;

    public Window(int width, int height, TextureMap textureMap, Camera camera) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        this.width = width;
        this.height = height;
        this.textureMap = textureMap;
        this.camera = camera;

        initializeWindow();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setInput(IInput input) {
        this.input = input;
    }

    public void setChunkLoader(ChunkLoader chunkLoader) {
        this.chunkLoader = chunkLoader;
    }

    public void setShader(IShader shader) {
        this.shader = shader;
    }

    public void setTextRenderer(ITextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    private void initializeWindow() { //TODO functional breakdown/abstraction
        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        this.windowHandle = glfwCreateWindow(this.width, this.height, "Minecraft", NULL, NULL);
        if ( this.windowHandle == NULL ) throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(this.windowHandle, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });
        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
//            glfwSetWindowPos(
//                    this.windowHandle,
//                    (vidmode.width() - pWidth.get(0)) / 2,
//                    (vidmode.height() - pHeight.get(0)) / 2
//            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.windowHandle);
    }

    public IChunkLoader getChunkLoader() {
        return chunkLoader;
    }

    public long getWindowHandle() {return this.windowHandle;}

    public boolean canContinueLoop() {
        return !glfwWindowShouldClose(getWindowHandle());
    }

    public void loop(){
        if (!canContinueLoop()) return;

        double currentFrameTime = glfwGetTime();
        double deltaTime = currentFrameTime - lastFrameTime;
        double framesPerSecond = Math.round( 1.0f / deltaTime);
        lastFrameTime = currentFrameTime;

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        this.shader.bind();
        this.shader.setMatrix4(camera.getViewMatrix(), "view");

        Texture cobblestoneTexture = textureMap.getTexture(Material.COBBLESTONE);
        glBindTexture(GL_TEXTURE_2D, cobblestoneTexture.getTextureID());
        glBindVertexArray(chunkLoader.getChunk().getVAO());
        glDrawArrays(GL_TRIANGLES, 0, chunkLoader.getChunk().getVisibleBlocks() * 36);

        final String fpsCounter = String.valueOf(framesPerSecond);
        textRenderer.renderText(camera.getOrtho(), new Vector2f(10, 64), 0.3f,"FPS: " + fpsCounter);

        glfwSwapBuffers(getWindowHandle()); // swap the color buffers

        pollInputs((float) deltaTime);

        if (input != null) camera.mouseControl(input.getMousePos());
    }

    @Override
    public void free() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(getWindowHandle());
        glfwDestroyWindow(getWindowHandle());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) callback.free();
    }

    // Poll for window events. The key callback above will only be invoked during this call.
    private void pollInputs(float deltaTime) {
        if (input == null) return;
        input.poll();
        if (input.isKeyDown(GLFW_KEY_W)) this.camera.processInput(Camera.CameraDirection.FORWARD, deltaTime);
        if (input.isKeyDown(GLFW_KEY_S)) this.camera.processInput(Camera.CameraDirection.BACKWARD, deltaTime);
        if (input.isKeyDown(GLFW_KEY_D)) this.camera.processInput(Camera.CameraDirection.RIGHT, deltaTime);
        if (input.isKeyDown(GLFW_KEY_A)) this.camera.processInput(Camera.CameraDirection.LEFT, deltaTime);
        if (input.isKeyDown(GLFW_KEY_ESCAPE)) glfwSetWindowShouldClose(getWindowHandle(), true);

    }
}
