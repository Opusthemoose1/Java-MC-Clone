package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.Location;
import minecraft.math.IVector;
import minecraft.timer.Timer;

import java.util.Optional;

abstract public class HostileEntity extends AttackingEntity {

    private static final float CHASE_RADIUS = 5.0f;
    private static final float CHASE_OUT_OF_RADIUS_SECONDS = 5.0f;

    private Entity target;
    private Timer chaseTimer;

    protected HostileEntity(Location location, float initialHealth, WorldContext context) {
        super(location, initialHealth, context);
    }

    @Override
    public boolean isHostile() {
        return true;
    }

    public Entity getTarget() {
        return target;
    }

    @Override
    public void tick() {
        findOrUpdateTarget();
        if (target == null) {
            setWalkSpeed(0);
        } else {
            IVector direction = target.getLocation().toVector().subtract(getLocation().toVector());
            setYaw(Location.getYaw(direction));
            setWalkSpeed(WALK_SPEED);
        }
        super.tick();
    }

    private void findOrUpdateTarget() {
        if (target == null) {
            Optional<Entity> first = context.getEntityManager().getEntitiesNearby(getLocation(), CHASE_RADIUS).stream().filter(Entity::isPlayer).findFirst();
            first.ifPresent(entity -> {
                target = entity;

            });
        } else { //check that the target is still in range and stop chasing after a certain amount of time otherwise
            if (getLocation().getDistance(target.getLocation()) > CHASE_RADIUS && chaseTimer.getTimeInSeconds() > CHASE_OUT_OF_RADIUS_SECONDS) {
                target = null; //stop chasing
            }
        }
    }
}
