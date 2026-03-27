package minecraft;

import minecraft.entity.Entity;
import minecraft.entity.EntityFactory;
import minecraft.entity.EntityManager;
import minecraft.entity.Player;
import minecraft.window.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Minecraft {

    private final IWindow window;

    public Minecraft(IWindow window) {
        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init
        this.window = window;

        EntityManager.GetInstance();
        Entity player = EntityManager.newEntity(EntityFactory.EntityType.PLAYER);
        EntityManager.attach(player);

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
