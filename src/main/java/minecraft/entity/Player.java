package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;
import minecraft.chunk.location.YawPitchObserver;

public class Player extends AttackingEntity implements YawPitchObserver {

    public static final float INITIAL_HEALTH = 20f,
            ATTACK_DAMAGE = 4f,
            WEIGHT = 0.8f,
            JUMP_DELTA_Y = 2f,
            PLAYER_WALK_SPEED = 0.02f;

    private IVector inputWalkSpeed = new Vector();

    public Player(Location location, WorldContext context) {
        super(location, INITIAL_HEALTH, context);
    }

    @Override
    float getAttackDamage() {
        return ATTACK_DAMAGE;
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    protected IVector getWalkingVelocity() {
        return inputWalkSpeed.clone();
    }

    public void setWalkingVelocity(IVector velocity) {
        inputWalkSpeed = velocity.clone();
    }

    public boolean move(Vector displacement)
    {
        this.setLocation(Location.createLocation(displacement.getX(), displacement.getY(), displacement.getZ()));
        if (isOnSolidGround()) {

            return false;
        }
        return true;

    }

    public void jump() {
        addVelocity(0, JUMP_DELTA_Y, 0);
    }

    @Override
    public void updateYawAndPitch(float yaw, float pitch) {
        setYaw(yaw);
        setPitch(pitch);
    }
}
