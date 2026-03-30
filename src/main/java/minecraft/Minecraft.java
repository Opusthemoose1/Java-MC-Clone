package minecraft;

import minecraft.entity.Entity;
import minecraft.entity.EntityFactory;
import minecraft.entity.EntityManager;
import minecraft.window.*;
import org.slf4j.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Minecraft {

    static Logger logger = org.slf4j.LoggerFactory.getLogger(Minecraft.class);

    public static Logger getLogger() {
        return logger;
    }

    private final IWindow window;

    public Minecraft(IWindow window) {
        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init
        this.window = window;

        EntityManager.getInstance();
        Entity player = EntityManager.createEntity(EntityFactory.EntityType.PLAYER);
        EntityManager.addEntity(player);

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
