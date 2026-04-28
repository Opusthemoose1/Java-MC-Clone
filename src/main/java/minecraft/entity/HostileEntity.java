package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;
import minecraft.timer.ITimer;

import java.util.Optional;

abstract public class HostileEntity extends AttackingEntity {

    private static final float DEFAULT_CHASE_RADIUS = 10.0f,
            CHASE_OUT_OF_RADIUS_SECONDS = 5.0f,
            ATTACK_RADIUS = 2.5f,
            ATTACK_PROBABILITY_PER_SECOND = 0.6f / Minecraft.TICKS_PER_SECOND;

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
//        if (walkSpeed > 0) return new Vector((float) Math.sin(getLocation().getYaw()), 0, (float) Math.cos(getLocation().getYaw()));
        if (walkSpeed > 0) {
            if (target != null) return getTargetDirection();
            return getLocation().getDirection().multiply(-1);
        }
        else return new Vector();
    }

    private IVector getTargetDirection() {
        if (target == null) return Vector.newZeroVector();
        return target.getLocation().toVector().subtract(getLocation().toVector());
    }

    @Override
    public void tick() {
        findOrUpdateTarget();
        if (target == null) {
            setWalkSpeed(0);
        } else {
            IVector direction = getTargetDirection();
            setYaw(Location.getYaw(direction));
            setWalkSpeed(getWalkSpeed());
            addInstantaneousForce(getWalkingForce());

            if (getLocation().getDistance(target.getLocation()) <= ATTACK_RADIUS && random.nextFloat() < ATTACK_PROBABILITY_PER_SECOND) {
                attack(target);
            }
        }
        super.tick();
    }

    public void setTarget(Entity entity) {
        target = entity;
        chaseTimer.reset();
    }

    public float getChaseRadius() {
        return DEFAULT_CHASE_RADIUS;
    }

    private void findOrUpdateTarget() {
        if (target == null) {
            Optional<Entity> first = context.getEntityManager().getEntitiesNearby(getLocation(), getChaseRadius()).stream().filter(Entity::isPlayer).findFirst();
            first.ifPresent(this::setTarget);
        } else { //check that the target is still in range and stop chasing after a certain amount of time otherwise
            if (getLocation().getDistance(target.getLocation()) > getChaseRadius() || chaseTimer.getTimeInSeconds() > CHASE_OUT_OF_RADIUS_SECONDS) {
                setTarget(null);
            }
        }
    }
}
