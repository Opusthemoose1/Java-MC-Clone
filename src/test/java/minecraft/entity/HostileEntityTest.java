package minecraft.entity;

import minecraft.TestTimerFactory;
import minecraft.chunk.FlatWorldChunkLoader;
import minecraft.TestTimer;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import org.junit.jupiter.api.Test;

public class HostileEntityTest {

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

        player.setLocation(Location.createLocation(100, 0, 0));

        ogre.tick();
        player.tick();

        assert ogre.getTarget() == null;
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
