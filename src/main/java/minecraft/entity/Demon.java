package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.timer.ITimer;

public class Demon extends HostileEntity {

    static final float INITIAL_HEALTH = 15f, ATTACK_DAMAGE = 3f, WEIGHT = 0.5f, HEIGHT = 1.5f,
            JUMP_PROBABILITY = 0.05f, CHASE_RADIUS = 20f, WALK_SPEED = 3f / Minecraft.TICKS_PER_SECOND;

    protected Demon(Location location, WorldContext context, ITimer chaseTimer) {
        super(location, INITIAL_HEALTH, context, chaseTimer);
    }

    @Override
    float getAttackDamage() {
        return ATTACK_DAMAGE;
    }

    @Override
    float getWeight() {
        return WEIGHT;
    }

    @Override
    float getHeight() {
        return HEIGHT;
    }

    @Override
    public EntityType getType() {
        return EntityType.DEMON;
    }

    @Override
    public float getChaseRadius() {
        return CHASE_RADIUS;
    }

    @Override
    public float getWalkSpeed() {
        return WALK_SPEED;
    }

    @Override
    public void tick() {
        super.tick();
        if (random.nextFloat() <= JUMP_PROBABILITY) jump();
    }
}
