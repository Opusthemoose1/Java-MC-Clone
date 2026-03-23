package minecraft.entity;

import minecraft.chunk.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;

abstract public class AttackingEntity extends Entity {

    static final float KNOCKBACK_MULTIPLIER = 0.1f;

    protected AttackingEntity(Location location, float initialHealth) {
        super(location, initialHealth);
    }

    public void attack(Entity foe) {
        if (isDead()) throw new IllegalStateException("Cannot attack if already dead");
        if (foe.isDead()) throw new IllegalStateException("Cannot attack an entity that is not alive");

        foe.addHealth(getAttackDamage());
        applyKnockbackToEntity(foe);
    }

    private void applyKnockbackToEntity(Entity foe) {
        Location location = getLocation();
        Location foeLocation = foe.getLocation();
        IVector displacement = new Vector( //displacement from self to foe
                foeLocation.getX() - location.getX(),
                0,
                foeLocation.getZ() - location.getZ()
        );
        displacement = displacement.normalize().multiply(KNOCKBACK_MULTIPLIER);

        addVelocity(displacement.clone().divide(-getWeight())); //opposite direction
        foe.addVelocity(displacement.clone().divide(foe.getWeight()));
    }

    abstract float getAttackDamage();
}
