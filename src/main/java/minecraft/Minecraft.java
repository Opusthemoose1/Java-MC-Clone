package minecraft;

import minecraft.chunk.IChunkLoader;
import minecraft.window.*;
import minecraft.window.texture.Shader;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Minecraft {

    private IWindow window;

    public Minecraft(IWindow window) {
        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init
        this.window = window;
    }

    public void run() {
        glClearColor(0.0f, 0.2f, 0.8f, 0.0f);

        while (window.canContinueLoop()) loop();

        window.free();
    }

    private void loop() {

        // Set the clear color


        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        window.loop();
    }
}
