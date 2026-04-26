package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;

public class Player extends AttackingEntity {

    public static final float INITIAL_HEALTH = 20f,
            ATTACK_DAMAGE = 4f,
            WEIGHT = 0.8f,
            HEIGHT = 1.75F;

    private static final int JUMP_DELAY_TICKS = 20;

    private static final float WALK_SPEED = 2f / Minecraft.TICKS_PER_SECOND, SPRINT_SPEED = 4f / Minecraft.TICKS_PER_SECOND;

    private boolean sprinting = false;
    private int ticksAfterJump = -1;

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
        return sprinting ? SPRINT_SPEED : WALK_SPEED;
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

    public EntityType getType() {
        return EntityType.PLAYER;
    }

    @Override
    public void tick() {
        super.tick();
        if (hasJumped()) ticksAfterJump++;
    }

    @Override
    public void tickOnGround() {
        if (ticksAfterJump >= JUMP_DELAY_TICKS) {
            ticksAfterJump = -1;
        }
        super.tickOnGround();
    }

    @Override
    public void jump() {
        if (hasJumped() || blockIsSolid(getHeight() + 1)) return;
        addVelocity(0, JUMP_DELTA_Y, 0);
        ticksAfterJump = 0;
    }

    private boolean hasJumped() {
        return ticksAfterJump >= 0;
    }
}
