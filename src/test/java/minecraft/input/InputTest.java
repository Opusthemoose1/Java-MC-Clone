package minecraft.input;

import minecraft.TestChunkLoader;
import minecraft.TestTimer;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.command.*;
import minecraft.entity.Entity;
import minecraft.entity.EntityFactory;
import minecraft.entity.EntityManager;
import minecraft.math.IVector;
import minecraft.math.Vector;
import minecraft.window.input.IInputSource;
import minecraft.window.input.InputManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

public class InputTest {

    private final WorldContext context = new WorldContext(new TestChunkLoader(10), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimer());

    @Test
    public void testInputPollingAndCommandExceutionWalkingForward() {
        IInputSource source = new TestInputSource(GLFW_KEY_W);
        InputManager inputManager = new InputManager(source);
        Entity player = entityFactory.createPlayer(new Location(0, 10, 0, 0f, 0));
        IVector initLocation = player.getLocation().toVector();
        inputManager.bindDownKey(GLFW_KEY_W, new MoveForwardCommand());
        player.tick();

        List<ICommand> commands = inputManager.pollInputs();
        assert commands.size() == 1; //should only be one command to move forward

        commands.getFirst().execute(player);

        for (int i = 0; i < 20; i++) player.tick(); //allow friction to complete

        IVector delta = player.getLocation().toVector().subtract(initLocation);

        assert delta.getY() == 0;
        assert delta.dot(player.getLocation().getDirection()) > 0; //player moved in direction they are looking at
        assert player.getLocation().getX() > initLocation.getX();
        assert player.getLocation().getY() == initLocation.getY();
        assert player.getLocation().getZ() == initLocation.getZ();
        assert player.getVelocity().isZero();
    }

    @Test
    public void testWalkingLeft() {
        Entity player = entityFactory.createPlayer(new Location(10, 10, 10, 0f, 0f));
        IVector initLocation = player.getLocation().toVector();
        ICommand leftCommand = new MoveLeftCommand();

        player.tick();
        leftCommand.execute(player);

        for (int i = 0; i < 20; i++) player.tick(); //friction

        assert player.getLocation().getX() == initLocation.getX();
        assert player.getLocation().getY() == initLocation.getY();
        assert player.getLocation().getZ() < initLocation.getZ();
        assert player.getVelocity().isZero();
    }

    @Test
    public void testWalkingRight() {
        Entity player = entityFactory.createPlayer(new Location(-10, 10, 10, 0f, 0f));
        IVector initLocation = player.getLocation().toVector();
        ICommand rightCommand = new MoveRightCommand();

        player.tick();
        rightCommand.execute(player);

        for (int i = 0; i < 20; i++) player.tick(); //friction

        assert player.getLocation().getX() == initLocation.getX();
        assert player.getLocation().getY() == initLocation.getY();
        assert player.getLocation().getZ() > initLocation.getZ();
        assert player.getVelocity().equals(new Vector());
    }

    @Test
    public void testWalkingBackwards() {
        Entity player = entityFactory.createPlayer(new Location(10, 10, -10, 0f, 0f));
        IVector initLocation = player.getLocation().toVector();
        ICommand backwardsCommand = new MoveBackwardsCommand();

        player.tick();
        backwardsCommand.execute(player);

        for (int i = 0; i < 20; i++) player.tick(); //friction

        assert player.getLocation().getX() < initLocation.getX();
        assert player.getLocation().getY() == initLocation.getY();
        assert player.getLocation().getZ() == initLocation.getZ();
        assert player.getVelocity().isZero();
    }

    @Test
    public void testJumpCommand() {
        Entity player = entityFactory.createPlayer(new Location(-10, 10, -10, 0f, 0f));
        IVector initLocation = player.getLocation().toVector();
        ICommand jumpCommand = new JumpCommand();

        player.tick();
        jumpCommand.execute(player);

        player.tick();

        assert player.getLocation().getX() == initLocation.getX();
        assert player.getLocation().getY() > initLocation.getY();
        assert player.getLocation().getZ() == initLocation.getZ();
        assert !player.isOnSolidGround();
    }

    @Test
    public void testMultipleCommandsInOneTick() {
        Entity player = entityFactory.createPlayer(new Location(10, 10, -10, 0f, 0f));
        IVector initLocation = player.getLocation().toVector();
        ICommand forwardsCommand = new MoveForwardCommand();
        ICommand rightCommand = new MoveRightCommand();

        player.tick();
        forwardsCommand.execute(player);
        rightCommand.execute(player);

        player.tick();

        assert player.getLocation().getX() > initLocation.getX();
        assert player.getLocation().getY() == initLocation.getY();
        assert player.getLocation().getZ() > initLocation.getZ();
    }

}
