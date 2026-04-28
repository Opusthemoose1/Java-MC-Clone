package minecraft.entity;

import minecraft.Material;
import minecraft.TestTimerFactory;
import minecraft.chunk.Block;
import minecraft.chunk.IChunkLoader;
import minecraft.chunk.TestChunk;
import minecraft.chunk.TestChunkLoader;
import minecraft.TestTimer;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.command.CommandFactory;
import minecraft.command.ICommand;
import minecraft.math.IVector;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    public static final int Y_LEVEL = 10;

    private final WorldContext context = new WorldContext(new TestChunkLoader(Y_LEVEL, new TestChunk(Y_LEVEL)), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimerFactory());
    private final CommandFactory commandFactory = new CommandFactory();

    @Test
    public void testFallDamage() {
        Entity player = entityFactory.createPlayer(Location.createLocation(0, Y_LEVEL+15, 0));
        double initalHealth = player.getHealth();

        for (int i = 0; i < 100; i++) {
            player.tick(); //fall
        }

        assert player.getHealth() < initalHealth; //player lost health due to fall damage
    }

    @Test
    public void testMinimumFallDamage() {
        Entity player = entityFactory.createPlayer(Location.createLocation(0, Y_LEVEL + 1, 0));
        double initalHealth = player.getHealth();

        for (int i = 0; i < 100; i++) {
            player.tick(); //fall
        }

        assert player.getHealth() == initalHealth; //player did not lose health because they fell smaller than the minimum amount to take damage
    }

    @Test
    public void testBreakBlock() {
        TestChunkLoader testChunkLoader = new TestChunkLoader(Y_LEVEL, new TestChunk(Y_LEVEL));
        WorldContext context = new WorldContext(testChunkLoader, new EntityManager());
        ICommand breakCommand = commandFactory.newBreakBlockCommand();
        Entity player = new Player(Location.createLocation(0, Y_LEVEL, 0, 0, -89), context); // looking down
        IVector locationBelowFeet = player.getLocation().toVector().add(0, -1, 0);

        assert !(context.getChunkLoader().getBlock(locationBelowFeet.getX(), locationBelowFeet.getY(), locationBelowFeet.getZ()).isType(Material.AIR)); //block not broken initially

        breakCommand.execute(player);

        assert testChunkLoader.getBlockChangesCount() == 1;
        assert context.getChunkLoader().getBlock(locationBelowFeet.getX(), locationBelowFeet.getY(), locationBelowFeet.getZ()).isType(Material.AIR); //block below player is broken
    }

    @Test
    public void testCannotPlaceBlockWhereStanding() {
        TestChunkLoader testChunkLoader = new TestChunkLoader(Y_LEVEL, new TestChunk(Y_LEVEL));
        WorldContext context = new WorldContext(testChunkLoader, new EntityManager());
        ICommand placeCommand = commandFactory.newPlaceBlockCommand();
        Entity player = new Player(Location.createLocation(10, Y_LEVEL, 0, 0, -89), context);

        assert context.getChunkLoader().getBlock(player.getLocation().getBlockLocation()).isType(Material.AIR); //control conditions
        assert player.getBlockLookingAt() != null; //can place a block

        placeCommand.execute(player);

        assert testChunkLoader.getBlockChangesCount() == 0;
        assert context.getChunkLoader().getBlock(player.getLocation().getBlockLocation()).isType(Material.AIR); //still air, can't place at feet
    }

    @Test
    public void testPlaceBlock() {
        TestChunkLoader testChunkLoader = new TestChunkLoader(Y_LEVEL, new TestChunk(Y_LEVEL));
        WorldContext context = new WorldContext(testChunkLoader, new EntityManager());
        ICommand placeCommand = commandFactory.newPlaceBlockCommand();
        Entity player = new Player(Location.createLocation(10, Y_LEVEL, 0, 0, -42), context);

        //control conditions
        Block lookingAt = player.getBlockLookingAt();
        assert lookingAt != null;
        assert (lookingAt.getLocation().getX() != player.getLocation().getBlockLocation().getX()
                || lookingAt.getLocation().getZ() != player.getLocation().getBlockLocation().getZ()); //ability to place a block, looking at a block that isn't at player's feet

        placeCommand.execute(player);

        assert testChunkLoader.getBlockChangesCount() == 1;
    }

    @Test
    public void testPlayerDeath() {
        Entity player = new Player(Location.createLocation(10, Y_LEVEL, 10), context);

        player.loseHealth(-100);

        //player respawns at world center
        assert player.getLocation().getX() == 0;
        assert player.getLocation().getZ() == 0;
    }

}
