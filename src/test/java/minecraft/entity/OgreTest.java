package minecraft.entity;

import minecraft.TestTimerFactory;
import minecraft.chunk.FlatWorldChunkLoader;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import org.junit.jupiter.api.Test;

public class OgreTest {

    private final WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimerFactory());

    @Test
    public void testEntityChasesPlayer() {
        Entity player = entityFactory.createPlayer(Location.createLocation(0f, 0f, 0f));
        HostileEntity ogre = (HostileEntity) entityFactory.createOgre(Location.createLocation(2, 0, 0));

        context.getEntityManager().addEntity(player);
        context.getEntityManager().addEntity(ogre);

        ogre.tick();
        player.tick();

        assert ogre.getTarget() != null;
        assert ogre.getWalkSpeed() < player.getWalkSpeed();
        assert ogre.getWeight() > player.getWeight();

        player.setLocation(Location.createLocation(100, 0, 0));

        ogre.tick();
        player.tick();

        assert ogre.getTarget() == null;
        assert !ogre.isSprinting();
    }

    @Test
    public void testOgreAttacksPlayer() {
        WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
        EntityFactory entityFactory = new EntityFactory(context, new TestTimerFactory());

        Entity player = entityFactory.createPlayer(Location.createLocation(0f, 0f, 0f));
        HostileEntity ogre = (HostileEntity) entityFactory.createOgre(Location.createLocation(2, 0, 0));

        double playerInitHealth = player.getHealth();

        for (int i = 0; i < 1000; i++) {
            ogre.tick();
            player.tick();
            if (player.getHealth() < playerInitHealth) break;
        }

        assert player.isDead() || player.getHealth() < playerInitHealth;
    }

    @Test
    public void testHostileEntityOnlyAgainstPlayer() {
        WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
        Entity chicken = entityFactory.createChicken(new Location(0, 10, 0));
        HostileEntity ogre = (HostileEntity) entityFactory.createOgre(new Location(2, 10, 0));

        context.getEntityManager().addEntity(chicken);
        context.getEntityManager().addEntity(ogre);

        ogre.tick();
        chicken.tick();

        assert ogre.isHostile();
        assert !chicken.isHostile();
        assert ogre.getTarget() == null; //ogre cannot target the chicken
    }
}
