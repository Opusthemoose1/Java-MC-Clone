package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;
import minecraft.timer.ITimer;

public class Player extends AttackingEntity {

    public static final float INITIAL_HEALTH = 20f,
            ATTACK_DAMAGE = 4f,
            WEIGHT = 0.8f,
            HEIGHT = 1.75F;

    private static final float WALK_SPEED = 1.5f / Minecraft.TICKS_PER_SECOND, SPRINT_SPEED = 2.5f / Minecraft.TICKS_PER_SECOND;

    private boolean sprinting = false;
    private final ITimer blockBreakPlaceTimer;

    public Player(Location location, WorldContext context, ITimer blockBreakPlaceTimer) {
        super(location, INITIAL_HEALTH, context);
        this.blockBreakPlaceTimer = blockBreakPlaceTimer;
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
    public boolean isSprinting() {
        return sprinting;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

    public EntityType getType() {
        return EntityType.PLAYER;
    }
}
