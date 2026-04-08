package minecraft.entity;

import minecraft.FlatWorldChunkLoader;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import org.junit.jupiter.api.Test;

public class EntityPlayerInteractionTest {

    private final WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context);

    @Test
    public void testEntityChasesPlayer() {
        Entity player = entityFactory.createPlayer(Location.createLocation(0f, 0f, 0f));
        HostileEntity ogre = (HostileEntity) entityFactory.createOgre(Location.createLocation(2, 0, 0));

        context.getEntityManager().addEntity(player);
        context.getEntityManager().addEntity(ogre);

        ogre.tick();
        player.tick();

        assert ogre.getTarget() != null;
        assert ogre.getTarget().equals(player);
    }
}
