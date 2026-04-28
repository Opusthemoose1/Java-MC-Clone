package minecraft.entity;

import minecraft.TestTimer;
import minecraft.TestTimerFactory;
import minecraft.WorldContext;
import minecraft.chunk.FlatWorldChunkLoader;
import minecraft.chunk.TestChunkLoader;
import minecraft.chunk.location.Location;
import org.junit.jupiter.api.Test;

public class DemonTest {

    public static final int Y_LEVEL = 10;

    private final WorldContext context = new WorldContext(new TestChunkLoader(Y_LEVEL), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimerFactory());

    @Test
    public void testJumping() {
        Entity demon = entityFactory.createDemon(Location.createLocation(100, Y_LEVEL, 100));

        for (int i = 0; i < 1000; i++) {
            demon.tick();
            if (demon.hasJumped()) return; //demon has jumped eventually
        }

        throw new RuntimeException("Demon must jump randomly");
    }

    @Test
    public void testEntityChasesPlayer() {
        Entity player = entityFactory.createPlayer(Location.createLocation(0f, 0f, 0f));
        HostileEntity demon = (HostileEntity) entityFactory.createDemon(Location.createLocation(2, 0, 0));

        context.getEntityManager().addEntity(player);
        context.getEntityManager().addEntity(demon);

        demon.tick();
        player.tick();

        assert demon.getTarget() != null;
        assert demon.getWalkSpeed() > player.getWalkSpeed();
        assert demon.getWeight() < player.getWeight();

        player.setLocation(Location.createLocation(100, 0, 0));

        demon.tick();
        player.tick();

        assert demon.getTarget() == null;
    }

    @Test
    public void testDemonAttacksPlayer() {
        WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
        EntityFactory entityFactory = new EntityFactory(context, new TestTimerFactory());

        Entity player = entityFactory.createPlayer(Location.createLocation(0f, 0f, 0f));
        HostileEntity demon = (HostileEntity) entityFactory.createDemon(Location.createLocation(2, 0, 0));

        double playerInitHealth = player.getHealth();

        for (int i = 0; i < 1000; i++) {
            demon.tick();
            player.tick();
            if (player.getHealth() < playerInitHealth) break;
        }

        assert player.isDead() || player.getHealth() < playerInitHealth;
    }

    @Test
    public void testHostileEntityOnlyAgainstPlayer() {
        WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
        Entity chicken = entityFactory.createChicken(new Location(0, 10, 0));
        HostileEntity demon = (HostileEntity) entityFactory.createDemon(new Location(2, 10, 0));

        context.getEntityManager().addEntity(chicken);
        context.getEntityManager().addEntity(demon);

        demon.tick();
        chicken.tick();

        assert demon.isHostile();
        assert !chicken.isHostile();
        assert demon.getTarget() == null; //demon cannot target the chicken
    }
}
