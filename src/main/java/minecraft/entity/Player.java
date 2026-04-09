package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.chunk.location.YawPitchObserver;
import minecraft.window.Camera;

public class Player extends AttackingEntity implements YawPitchObserver {

    public static final float INITIAL_HEALTH = 20f,
            ATTACK_DAMAGE = 4f,
            WEIGHT = 0.8f,
            JUMP_DELTA_Y = 8f / Minecraft.TICKS_PER_SECOND;

    private static final float PLAYER_WALK_SPEED = 2f / Minecraft.TICKS_PER_SECOND;

    private boolean hasJumped = false;

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
    public float getWalkSpeed() {
        return PLAYER_WALK_SPEED;
    }

    public void jump() {
        if (hasJumped || blockIsAir(Camera.CAMERA_Y_OFFSET)) return;
        addVelocity(0, JUMP_DELTA_Y, 0);
        hasJumped = true;
    }

    @Override
    protected void tickOnGround() {
        super.tickOnGround();
        hasJumped = false;
    }

    @Override
    public void updateYawAndPitch(float yaw, float pitch) {
        setYaw(yaw);
        setPitch(pitch);
    }
}
