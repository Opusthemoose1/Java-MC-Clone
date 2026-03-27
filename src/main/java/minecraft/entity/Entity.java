package minecraft.entity;

import minecraft.chunk.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;

abstract public class Entity {

    private static final float GRAVITY = -0.5f, FRICTION_MULTIPLIER = 0.98f, MINIMUM_VELOCITY = 0.01f;

    private final Location location;
    private IVector velocity = Vector.newZeroVector();
    private float health;

    protected Entity(Location location, float initialHealth) {
        this.location = location.clone();
        this.health = initialHealth;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public float getHealth() {
        return health;
    }

    public void addHealth(float healthPoints) {
        if (isDead()) return;
        health += healthPoints;
    }

    public Location getLocation() {
        return location.clone();
    }

    public IVector getVelocity() {
        return velocity.clone();
    }

    public void addVelocity(float x, float y, float z) {
        velocity.add(x, y, z);
    }

    public void addVelocity(IVector vector) {
        velocity.add(vector);
    }

    abstract float getWeight();

    public boolean isOnSolidGround() {




        return true; //TODO check if the block -1 in y direction is not air
    }

    public void tick() {
        location.add(velocity);
        if (isOnSolidGround()) {
            velocity.multiply(FRICTION_MULTIPLIER);
            if (velocity.lengthSquared() < MINIMUM_VELOCITY) velocity = Vector.newZeroVector();
        }
        else velocity.add(0, GRAVITY, 0);
    }

}
