package minecraft;

import minecraft.command.MoveCommand;
import minecraft.entity.Entity;
import minecraft.entity.EntityFactory;
import minecraft.entity.EntityManager;
import minecraft.entity.Player;
import minecraft.window.*;
import minecraft.window.input.IInput;
import minecraft.window.input.Input;
import minecraft.window.input.InputManager;
import org.slf4j.Logger;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Minecraft {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(Minecraft.class);
    public static Logger getLogger() {
        return logger;
    }

    private static InputManager inputManager;
    private static Minecraft minecraftInstance = null;
    private static IWindow window = null;
    private static Input input = null;
    private static Player player = null;

    private Minecraft(IWindow window, Input input) {
        Minecraft.window = window;
        Minecraft.input = input;
    }


    public Minecraft getInstance() {
      if (minecraftInstance == null)
      {
          throw new IllegalStateException("Minecraft not initialized. Call init(window) first.");
      }
      return minecraftInstance;

    }
    public static void init(IWindow window, Input input)
    {
        if (minecraftInstance != null) {
            throw new IllegalStateException("Minecraft already initialized");
        }
        minecraftInstance = new Minecraft(window, input);
        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init

        EntityManager.getInstance();
        player = (Player)EntityManager.createEntity(EntityFactory.EntityType.PLAYER);
        EntityManager.addEntity(player);

        inputManager = new InputManager(Minecraft.input);
        inputManager.bind(GLFW_KEY_W, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.FORWARD  ));
        inputManager.bind(GLFW_KEY_S, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.BACKWARD  ));
        inputManager.bind(GLFW_KEY_A, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.LEFT  ));
        inputManager.bind(GLFW_KEY_D, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.RIGHT  ));

    }

    public static void run() {
        glClearColor(0.0f, 0.2f, 0.8f, 0.0f);

        while (window.canContinueLoop()) loop();

        window.free();
    }

    private static void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        inputManager.pollInputs(input, window.getDeltaTime());
        window.loop();
    }
}
