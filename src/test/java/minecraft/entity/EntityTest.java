package minecraft.entity;

import minecraft.TestChunkLoader;
import minecraft.WorldContext;
import minecraft.chunk.Location;
import minecraft.math.Vector;
import org.junit.jupiter.api.Test;

public class EntityTest {

    private final WorldContext context = new WorldContext(new TestChunkLoader(10), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context);

    @Test
    public void testEntityDies() {
        Entity chicken = entityFactory.createChicken();

        chicken.addHealth(-1.5f);

        assert chicken.getHealth() == Chicken.INITIAL_HEALTH - 1.5;

        chicken.addHealth(-1 * Chicken.INITIAL_HEALTH);

        assert chicken.isDead();
    }

    @Test
    public void testVelocityIsChanagedAfterAttack() {
        Entity chicken = entityFactory.createChicken();
        Player player = (Player) entityFactory.createPlayer();

        player.attack(chicken);

        assert player.getVelocity().length() > 0;
        assert chicken.getVelocity().length() > 0;
        assert player.getVelocity().length() < chicken.getVelocity().length(); //the player has a larger weight than the chicken, F = ma
    }

    @Test
    public void testFreeFall() {
        int y = 20;
        Entity chicken = entityFactory.createChicken();
        chicken.setLocation(Location.createLocation(0, y, 0));

        assert !chicken.isOnSolidGround();

        chicken.tick();

        assert chicken.getLocation().getY() < y; //should start falling
        assert !chicken.isOnSolidGround(); //should still be in the air

        for (int i = 0; i < 1000; i++) {
            chicken.tick(); //let the chicken land on ground
        }

        assert chicken.isOnSolidGround();
        assert chicken.getLocation().getY() == 10; //standing on ground
    }

    @Test
    public void testFriction() {
        Entity glob = entityFactory.createOgre();
        glob.setLocation(Location.createLocation(0, 10, 0));
        glob.setVelocity(new Vector(2, 0, 2));

        glob.tick();

        assert glob.getVelocity().length() > 0;
        assert glob.getVelocity().getX() > 0;
        assert glob.getVelocity().getY() == 0;
        assert glob.getVelocity().getZ() > 0;

        for (int i = 0; i < 1000; i++) { //after some time, glob should come to a stop, since he is on ground
            glob.tick();
        }

        assert glob.getVelocity().length() == 0;
    }

}
