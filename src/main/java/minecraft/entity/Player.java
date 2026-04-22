package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;

public class Player extends AttackingEntity {

    public static final float INITIAL_HEALTH = 20f,
            ATTACK_DAMAGE = 4f,
            WEIGHT = 0.8f,
            HEIGHT = 1.75F,
            JUMP_DELTA_Y = 5.5f / Minecraft.TICKS_PER_SECOND;

    private static final float WALK_SPEED = 1.4f / Minecraft.TICKS_PER_SECOND, SPRINT_SPEED = 2.2f / Minecraft.TICKS_PER_SECOND;

    private boolean hasJumped = false, sprinting = false;

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

    public void jump() {
        if (hasJumped || blockIsSolid(getHeight() + 1)) return;
        addVelocity(0, JUMP_DELTA_Y, 0);
        hasJumped = true;
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    @Override
    protected void tickOnGround() {
        super.tickOnGround();
        hasJumped = false;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

    public EntityType getType() {
        return EntityType.PLAYER;
    }
}
