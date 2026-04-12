package minecraft.entity;

import minecraft.Material;
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
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimer());
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
        ICommand breakCommand = commandFactory.newBreakBlockCommand();
        Entity player = entityFactory.createPlayer(Location.createLocation(0, Y_LEVEL, 0, 0, -89)); //looking down
        IVector locationBelowFeet = player.getLocation().toVector().add(0, -1, 0);

        assert !(context.getChunkLoader().getBlock(locationBelowFeet.getX(), locationBelowFeet.getY(), locationBelowFeet.getZ()).isType(Material.AIR)); //block not broken initially

        breakCommand.execute(player);

        assert context.getChunkLoader().getBlock(locationBelowFeet.getX(), locationBelowFeet.getY(), locationBelowFeet.getZ()).isType(Material.AIR); //block below player is broken
    }

}
