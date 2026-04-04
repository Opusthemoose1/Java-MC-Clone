package minecraft.entity;

import minecraft.block.Material;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.ChunkLoader;
import minecraft.chunk.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;

abstract public class Entity {

    public static final float GRAVITY = -0.5f, FRICTION_MULTIPLIER = 0.98f, MINIMUM_VELOCITY = 0.01f, WALK_SPEED = 5.0f, FREEFALL_VELOCITY_MULTIPLIER = 0.2f;

    private Location location;
    private IVector velocity = Vector.newZeroVector();
    private float health, walkSpeed;

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

    public void setLocation(Location location) {this.location = location; }

    public IVector getVelocity() {
        return velocity.clone();
    }

    public void addVelocity(float x, float y, float z) {
        velocity.add(x, y, z);
    }

    public void addVelocity(IVector vector) {
        velocity.add(vector);
    }

    public void setVelocity(IVector velocity) {this.velocity = velocity; }

    abstract float getWeight();

    public boolean isOnSolidGround() {
        ChunkBlock block = ChunkLoader.GetInstance().getBlock(location.getX(), location.getY() - 1, location.getZ());
        // Check if the block below us is
        return block.materialId() != Material.AIR.getId();//TODO check if the block -1 in y direction is not air
    }

    public void setWalkSpeed(float speed) {
        walkSpeed = Math.abs(speed);
    }

    public void tick() {
//        IVector walkVelocity;
//        if (walkSpeed > 0) walkVelocity = new Vector((float) Math.sin(getLocation().getYaw()), 0, (float) Math.cos(getLocation().getYaw()));
//        else walkVelocity = new Vector();
//
//        if (isOnSolidGround()) {
//            velocity.setY(0);
//            velocity.multiply(FRICTION_MULTIPLIER);
//            velocity = new Vector(Math.max(velocity.getX(), walkVelocity.getX()), //maintain walking speed or exponentially decay with friction if stopped
//                    Math.max(velocity.getY(), walkVelocity.getY()),
//                    Math.max(velocity.getZ(), walkVelocity.getZ()));
//            if (velocity.lengthSquared() < MINIMUM_VELOCITY) velocity = Vector.newZeroVector();
//            location.add(velocity);
//        } else {
//            walkVelocity.multiply(FREEFALL_VELOCITY_MULTIPLIER);
//            velocity.add(0, GRAVITY, 0);
//            location.add(velocity.clone().add(walkVelocity));
//        }
    }

    public void setYaw(float yaw) {
        location.setYaw(yaw);
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isHostile() {
        return false;
    }
}
