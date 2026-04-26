package minecraft.entity;

import minecraft.TestTimer;
import minecraft.WorldContext;
import minecraft.chunk.TestChunkLoader;
import minecraft.chunk.location.Location;
import org.junit.jupiter.api.Test;

public class DemonTest {

    public static final int Y_LEVEL = 10;

    private final WorldContext context = new WorldContext(new TestChunkLoader(Y_LEVEL), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimer());

    @Test
    public void testJumping() {
        Entity demon = entityFactory.createDemon(Location.createLocation(100, Y_LEVEL, 100));

        for (int i = 0; i < 1000; i++) {
            demon.tick();
            if (demon.hasJumped()) return; //demon has jumped eventually
        }

        throw new RuntimeException("Demon must jump randomly");
    }
}
