package minecraft;

import minecraft.command.*;
import minecraft.entity.Entity;
import minecraft.timer.Timer;
import minecraft.window.*;
import minecraft.window.input.IInputManager;
import org.slf4j.Logger;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Minecraft {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(Minecraft.class);
    public static Logger getLogger() {
        return logger;
    }

    private final IInputManager inputManager;
    private final IWindow window;
    private final Entity player;
    private final WorldContext context;
    private final Timer tickTimer;

    public final static int TICKS_PER_SECOND = 20;
    public final static long SECONDS_PER_TICK = 1/TICKS_PER_SECOND;

    public Minecraft(IWindow window, IInputManager inputManager, Entity player) {
        this.window = window;
        this.context = player.getContext();
        this.player = player;
        this.inputManager = inputManager;

        bindKeys();
        tickTimer = new Timer();
    }

    private void bindKeys() {
        inputManager.bindDownKey(GLFW_KEY_W, new MoveForwardCommand());
        inputManager.bindDownKey(GLFW_KEY_S, new MoveBackwardsCommand());
        inputManager.bindDownKey(GLFW_KEY_A, new MoveLeftCommand());
        inputManager.bindDownKey(GLFW_KEY_D, new MoveRightCommand());
        inputManager.bindDownKey(GLFW_KEY_SPACE, new JumpCommand());
        inputManager.bindDownKey(GLFW_KEY_LEFT_CONTROL, new SprintingStartCommand());
        inputManager.bindUpKey(GLFW_KEY_LEFT_CONTROL, new SprintingStopCommand());
    }

    public void run() {
        glClearColor(0.0f, 0.2f, 0.8f, 0.0f);

        while (window.canContinueLoop()) loop();

        window.free();
    }

    private void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        List<ICommand> commands = inputManager.pollInputs();
        for (ICommand command : commands) command.execute(player);

        tickGame();

        window.loop(context);
        window.getCamera().setLocation(player.getLocation());
    }

    private void tickGame() {
        if (tickTimer.getTimeInSeconds() < SECONDS_PER_TICK) return;

        // Tick all registered entities
        context.getEntityManager().tickAllEntities();

        tickTimer.reset();
    }

    public Entity getPlayer() {
        return player;
    }
}
