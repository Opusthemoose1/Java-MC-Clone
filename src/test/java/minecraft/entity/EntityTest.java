package minecraft.entity;

import minecraft.TestTimerFactory;
import minecraft.chunk.TestChunkLoader;
import minecraft.TestTimer;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;
import org.junit.jupiter.api.Test;

public class EntityTest {

    public static final int Y_LEVEL = 10;

    private final WorldContext context = new WorldContext(new TestChunkLoader(Y_LEVEL), new EntityManager());
    private final EntityFactory entityFactory = new EntityFactory(context, new TestTimerFactory());

    @Test
    public void testEntityDies() {
        Entity chicken = entityFactory.createChicken();

        chicken.loseHealth(1.5f);

        assert chicken.getHealth() == Chicken.INITIAL_HEALTH - 1.5;

        chicken.loseHealth(Chicken.INITIAL_HEALTH);

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
        int y = Y_LEVEL + 10;
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
        // must standing on ground. Must be exactly y=10, not 9.8999
        if (chicken.getLocation().getY() != 10) throw new RuntimeException("Location y level must be exactly 10, was " + chicken.getLocation().getY());
    }

    @Test
    public void testFriction() {
        Entity glob = entityFactory.createOgre();
        glob.setLocation(Location.createLocation(0, 10, 0));
        IVector initVelocity = new Vector(2, 0, 2);
        glob.setVelocity(initVelocity.clone());
        Location initPosition = glob.getLocation();

        glob.tick();

        //make sure entity is moving
        assert glob.getVelocity().length() > 0;
        assert glob.getVelocity().length() < initVelocity.length(); //friction is applied each tick
        assert glob.getVelocity().getX() > 0;
        assert glob.getVelocity().getY() == 0;
        assert glob.getVelocity().getZ() > 0;
        assert glob.getLocation().getX() > initPosition.getX(); //moved along x axis, same as velocity vector

        for (int i = 0; i < 1500; i++) { //after some time, glob should come to a stop, since he is on ground
            glob.tick();
        }

        if (glob.getVelocity().length() != 0) throw new RuntimeException("Velocity after friction is complete must be exatctly zero, got " + glob.getVelocity().length());
    }

    @Test
    public void testDeath() {
        Entity chicken = entityFactory.createChicken();

        chicken.loseHealth(-100);

        assert chicken.isDead();
    }

}
