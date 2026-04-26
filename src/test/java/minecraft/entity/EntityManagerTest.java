package minecraft.entity;

import minecraft.TestTimer;
import minecraft.WorldContext;
import minecraft.chunk.FlatWorldChunkLoader;
import minecraft.chunk.location.Location;
import org.junit.jupiter.api.Test;

public class EntityManagerTest {

    private final WorldContext context = new WorldContext(new FlatWorldChunkLoader(), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimer());

    @Test
    public void testEntityManagerEntitiesNearby() {
        Entity player = entityFactory.createPlayer(new Location(0, 20, 0));
        Entity chicken = entityFactory.createChicken(new Location(0, 20, 1));
        Entity ogre = entityFactory.createOgre(new Location(0, 20, 20));

        context.getEntityManager().addEntity(player);
        context.getEntityManager().addEntity(chicken);
        context.getEntityManager().addEntity(ogre);

        assert context.getEntityManager().getEntityCount() == 3;
        assert context.getEntityManager().getEntitiesNearby(player.getLocation(), 2).size() == 2;
        assert context.getEntityManager().getEntitiesNearby(player.getLocation(), 20).size() == 3;
    }
}
