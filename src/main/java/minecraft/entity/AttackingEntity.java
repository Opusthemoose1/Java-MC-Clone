package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;

abstract public class AttackingEntity extends Entity {

    static final float KNOCKBACK_MULTIPLIER = 0.05f, ATTACK_DISPLACEMENT_Y = 0.1f;

    protected AttackingEntity(Location location, float initialHealth, WorldContext context) {
        super(location, initialHealth, context);
    }

    public void attack(Entity foe) {
        if (foe == this || isDead() || foe.isDead()) return;
        foe.loseHealth(getAttackDamage());
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

        addVelocity(displacement.clone().divide(-getWeight()).add(0, ATTACK_DISPLACEMENT_Y, 0)); //opposite direction
        foe.addVelocity(displacement.clone().divide(foe.getWeight()).add(0, ATTACK_DISPLACEMENT_Y, 0));
    }

    @Override
    public boolean canAttack() {
        return true;
    }

    abstract float getAttackDamage();
}
