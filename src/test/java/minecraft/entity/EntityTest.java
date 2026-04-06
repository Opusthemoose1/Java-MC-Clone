package minecraft.entity;

import minecraft.TestChunkLoader;
import minecraft.WorldContext;
import minecraft.chunk.ChunkLoader;
import org.junit.jupiter.api.Test;

public class EntityTest {

    private final WorldContext context = new WorldContext(new TestChunkLoader(), new EntityManager());
    private final EntityFactory factory = new EntityFactory(context);

    @Test
    public void testEntityDies() {
        Entity chicken = factory.createChicken();

        chicken.addHealth(-1.5f);

        assert chicken.getHealth() == Chicken.INITIAL_HEALTH - 1.5;

        chicken.addHealth(-1 * Chicken.INITIAL_HEALTH);

        assert chicken.isDead();
    }

    @Test
    public void testVelocityIsChanagedAfterAttack() {
        Entity chicken = factory.createChicken();
        Player player = (Player) factory.createPlayer();

        player.attack(chicken);

        assert player.getVelocity().length() > 0;
        assert chicken.getVelocity().length() > 0;
        assert player.getVelocity().length() < chicken.getVelocity().length(); //the player has a larger weight than the chicken, F = ma
    }


}
