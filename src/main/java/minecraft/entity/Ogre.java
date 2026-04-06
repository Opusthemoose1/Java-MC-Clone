package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.Location;

public class Ogre extends HostileEntity {

    static final float INITIAL_HEALTH = 11f, ATTACK_DAMAGE = 2f, WEIGHT = 1.0f;

    public Ogre(Location location, WorldContext context) {
        super(location, INITIAL_HEALTH, context);
    }

    @Override
    public float getAttackDamage() {
        return ATTACK_DAMAGE;
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

}
