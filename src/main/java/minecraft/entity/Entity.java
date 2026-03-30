package minecraft.entity;

import minecraft.block.Material;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.ChunkLoader;
import minecraft.chunk.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;

abstract public class Entity {

    public static final float GRAVITY = -0.5f, FRICTION_MULTIPLIER = 0.98f, MINIMUM_VELOCITY = 0.01f, WALK_SPEED = 0.2f;

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

    public void setVelocity(IVector velocity) {
        if (velocity == null) velocity = new Vector();
        this.velocity = velocity;
    }

    public void addVelocity(float x, float y, float z) {
        velocity.add(x, y, z);
    }

    public void addVelocity(IVector vector) {
        velocity.add(vector);
    }

    abstract float getWeight();

    public boolean isOnSolidGround() {
        ChunkBlock block = ChunkLoader.GetInstance().getBlock(location.getX(), location.getY() - 1, location.getZ());
        // Check if the block below us is
        return block.materialId() != Material.AIR.getId();//TODO check if the block -1 in y direction is not air
    }

    public void tick() {
        location.add(velocity);
        if (isOnSolidGround()) {
            velocity.multiply(FRICTION_MULTIPLIER);
            if (velocity.lengthSquared() < MINIMUM_VELOCITY) velocity = Vector.newZeroVector();
        }
        else velocity.add(0, GRAVITY, 0);
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isHostile() {
        return false;
    }
}
