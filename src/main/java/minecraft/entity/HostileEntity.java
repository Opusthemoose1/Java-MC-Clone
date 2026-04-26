package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;
import minecraft.timer.ITimer;

import java.util.Optional;

abstract public class HostileEntity extends AttackingEntity {

    private static final float CHASE_RADIUS = 10.0f;
    private static final float CHASE_OUT_OF_RADIUS_SECONDS = 5.0f;

    private Entity target;
    private final ITimer chaseTimer;
    private float walkSpeed = 0;

    protected HostileEntity(Location location, float initialHealth, WorldContext context, ITimer chaseTimer) {
        super(location, initialHealth, context);
        this.chaseTimer = chaseTimer;
    }

    @Override
    public boolean isHostile() {
        return true;
    }

    public Entity getTarget() {
        return target;
    }

    public void setWalkSpeed(float speed) {
        walkSpeed = Math.abs(speed);
    }

    protected IVector getWalkingForce() {
        if (walkSpeed > 0) return new Vector((float) Math.sin(getLocation().getYaw()), 0, (float) Math.cos(getLocation().getYaw()));
        else return new Vector();
    }

    @Override
    public void tick() {
        findOrUpdateTarget();
        if (target == null) {
            setWalkSpeed(0);
        } else {
            IVector direction = target.getLocation().toVector().subtract(getLocation().toVector());
            setYaw(Location.getYaw(direction));
            setWalkSpeed(getWalkSpeed());
            addInstantaneousForce(getWalkingForce());
        }
        super.tick();
    }

    public void setTarget(Entity entity) {
        target = entity;
        chaseTimer.reset();
    }

    private void findOrUpdateTarget() {
        if (target == null) {
            Optional<Entity> first = context.getEntityManager().getEntitiesNearby(getLocation(), CHASE_RADIUS).stream().filter(Entity::isPlayer).findFirst();
            first.ifPresent(this::setTarget);
        } else { //check that the target is still in range and stop chasing after a certain amount of time otherwise
            if (getLocation().getDistance(target.getLocation()) > CHASE_RADIUS || chaseTimer.getTimeInSeconds() > CHASE_OUT_OF_RADIUS_SECONDS) {
                setTarget(null);
            }
        }
    }
}
