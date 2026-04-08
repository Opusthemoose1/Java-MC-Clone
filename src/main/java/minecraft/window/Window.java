package minecraft.window;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.window.text.ITextRenderer;
import minecraft.window.input.IInput;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


// TODO: Singleton class, as we only want one instance of this
public class Window implements IWindow, FrameRenderPublisher {

    private int width, height;
    private long windowHandle; // Window handle for the GLFW context
    private double lastFrameTime = 0;
    private double deltaTime;

    private Camera camera;
    private ITextRenderer textRenderer;
    private final GLFWErrorCallback loggerCallback;

    private final Set<FrameRenderObserver> observers = new HashSet<>();

    private IInput input;

    public Window(int width, int height) {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        loggerCallback = GLFWErrorCallback.create((error, description) -> {
            String errorMsg = GLFWErrorCallback.getDescription(description);
            Minecraft.getLogger().error("GLFW Error [{}]: {}", error, errorMsg);
        });
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        this.width = width;
        this.height = height;

        initializeWindow();
    }

    public void setCamera(Camera camera) {this.camera = camera; }

    public Camera getCamera() {return camera;}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getDeltaTime() {return deltaTime; }

    public void setInput(IInput input) {
        this.input = input;
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

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);
            // Sometimes, the window won't be the size specified (shoutouts hyprland)
            width = pWidth.get();
            height = pHeight.get();

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.windowHandle);
    }

    public long getWindowHandle() {return this.windowHandle;}

    public boolean canContinueLoop() {
        return !glfwWindowShouldClose(getWindowHandle());
    }

    public void loop(WorldContext context){

        double currentFrameTime = glfwGetTime();
        deltaTime = currentFrameTime - lastFrameTime;
        double framesPerSecond = Math.round( 1.0f / deltaTime);
        lastFrameTime = currentFrameTime;

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        // Draw the chunks, entities, etc.
        for (FrameRenderObserver observer : observers) {
            observer.render(context);
        }

        final String fpsCounter = String.valueOf(framesPerSecond);
        textRenderer.renderText(camera.getOrtho(), new Vector2f(10, 100), 0.3f,"FPS: " + fpsCounter); //TODO remove line or remove magic numbers

        glfwSwapBuffers(getWindowHandle()); // swap the color buffers

        pollInputs((float) deltaTime);
    }

    @Override
    public void attach(FrameRenderObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(FrameRenderObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void free() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(getWindowHandle());
        glfwDestroyWindow(getWindowHandle());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        loggerCallback.free();
    }

    // Poll for window events. The key callback above will only be invoked during this call.
    private void pollInputs(float deltaTime) {
        if (input == null) return;
        if (input.isKeyDown(GLFW_KEY_ESCAPE)) glfwSetWindowShouldClose(getWindowHandle(), true);
        camera.mouseControl(input.getMousePos());
    }
}
