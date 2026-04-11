package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.timer.ITimer;

public class Ogre extends HostileEntity {

    static final float INITIAL_HEALTH = 11f, ATTACK_DAMAGE = 2f, WEIGHT = 1.0f, HEIGHT = 2.5f;

    public Ogre(Location location, WorldContext context, ITimer chaseTimer) {
        super(location, INITIAL_HEALTH, context, chaseTimer);
    }

    @Override
    public float getAttackDamage() {
        return ATTACK_DAMAGE;
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

}
