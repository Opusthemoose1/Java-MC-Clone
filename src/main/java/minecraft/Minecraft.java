package minecraft;

import minecraft.command.ICommand;
import minecraft.command.MoveBackwardsCommand;
import minecraft.command.MoveForwardCommand;
import minecraft.entity.Player;
import minecraft.math.Vector;
import minecraft.timer.Timer;
import minecraft.window.*;
import minecraft.window.input.Input;
import minecraft.window.input.InputManager;
import org.slf4j.Logger;

import java.util.List;

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
    private final Timer tickTimer;

    private final static float CAMERA_Y_OFFSET = 1;
    public final static int TICKS_PER_SECOND = 20;
    public final static long SECONDS_PER_TICK = 1L/TICKS_PER_SECOND;

    public Minecraft(IWindow window, Input input, Player player) {
        this.window = window;
        this.input = input;
        this.context = player.getContext();
        this.player = player;
        this.inputManager = new InputManager(input);
//        inputManager.bind(GLFW_KEY_W, new MoveCommand(player, window.getCamera(), Camera.CameraDirection.FORWARD  ));
        inputManager.bind(GLFW_KEY_W, new MoveForwardCommand(player));
        inputManager.bind(GLFW_KEY_S, new MoveBackwardsCommand(player));
        tickTimer = new Timer();
    }

    public void run() {
        glClearColor(0.0f, 0.2f, 0.8f, 0.0f);

        while (window.canContinueLoop()) loop();

        window.free();
    }

    private void loop() {

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.

        List<ICommand> commands = inputManager.pollInputs(input);
        player.setWalkingVelocity(new Vector());
        for (ICommand command : commands) command.execute((float) window.getDeltaTime());

        tickGame();

        window.loop(context);
        window.getCamera().setPosition(player.getLocation().toVector().add(0, CAMERA_Y_OFFSET, 0));
    }

    private void tickGame() {
        if (tickTimer.getTimeInSeconds() < SECONDS_PER_TICK) return;

        // Tick all registered entities
        context.getEntityManager().tickAllEntities();

        tickTimer.reset();
    }

    public Player getPlayer() {
        return player;
    }
}
