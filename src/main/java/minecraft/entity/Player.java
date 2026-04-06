package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.Location;
import minecraft.math.Vector;

public class Player extends AttackingEntity {

    static final float INITIAL_HEALTH = 20f,
            ATTACK_DAMAGE = 4f,
            WEIGHT = 0.8f,
            JUMP_DELTA_Y = 2f;

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
}
