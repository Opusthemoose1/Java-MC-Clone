package minecraft.entity;

import minecraft.chunk.Location;

public class Ogre extends AttackingEntity {

    static final float INITIAL_HEALTH = 11f, ATTACK_DAMAGE = 2f, WEIGHT = 1.0f;

    public Ogre(Location location) {
        super(location, INITIAL_HEALTH);
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
