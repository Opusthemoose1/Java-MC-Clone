package minecraft;

import minecraft.chunk.Location;
import minecraft.command.MoveCommand;
import minecraft.entity.Player;
import minecraft.window.*;
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

    private final InputManager inputManager;
    private final IWindow window;
    private final Input input;
    private final Player player;
    private final WorldContext context;

    public Minecraft(IWindow window, Input input, WorldContext context) {
        this.window = window;
        this.input = input;
        this.context = context;
        glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11); //might need for XWayland to solve an exception on init

        player = new Player(Location.createLocation(0, 0, 0), context);

        inputManager = new InputManager(input);
        inputManager.bind(GLFW_KEY_W, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.FORWARD  ));
        inputManager.bind(GLFW_KEY_S, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.BACKWARD  ));
        inputManager.bind(GLFW_KEY_A, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.LEFT  ));
        inputManager.bind(GLFW_KEY_D, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.RIGHT  ));
    }


    public void run() {
        glClearColor(0.0f, 0.2f, 0.8f, 0.0f);

        while (window.canContinueLoop()) loop();

        window.free();
    }

    private void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        inputManager.pollInputs(input, window.getDeltaTime());

        // Tick all registered entities
        context.getEntityManager().tickAllEntities();

        window.loop(context);
    }

    public Player getPlayer() {
        return player;
    }
}
