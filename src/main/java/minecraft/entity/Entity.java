package minecraft.entity;

import minecraft.WorldContext;
import minecraft.block.Material;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;

abstract public class Entity {

    public static final float GRAVITY = -0.5f, FRICTION_MULTIPLIER = 0.98f, MINIMUM_VELOCITY = 0.01f,
            FREEFALL_VELOCITY_MULTIPLIER = 0.2f, FALL_DAMAGE_DEFAULT_MULTIPLIER = 0.2f, EPSILON = 0.001f;

    private Location location;
    private IVector velocity = Vector.newZeroVector();
    protected WorldContext context;
    private float health, walkSpeed, startingFreefallY = -1;

    protected Entity(Location location, float initialHealth, WorldContext context) {
        this.location = location.clone();
        this.health = initialHealth;
        this.context = context;
    }

    public WorldContext getContext() {
        return context;
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
        return location.getY() <= 0 || blockIsAir(EPSILON);
    }

    private boolean blockIsAir(float yOffset) {
        ChunkBlock block = context.getChunkLoader().getBlock(location.getZ(), location.getY() - yOffset, location.getX());
        return block != null && block.getMaterialId() != Material.AIR.getId();
    }

    public void setWalkSpeed(float speed) {
        walkSpeed = Math.abs(speed);
    }

    protected IVector getWalkingVelocity() {
        if (walkSpeed > 0) return new Vector((float) Math.sin(getLocation().getYaw()), 0, (float) Math.cos(getLocation().getYaw()));
        else return new Vector();
    }

    public void tick() {
        IVector walkVelocity = getWalkingVelocity();
        if (isOnSolidGround()) tickOnGround(walkVelocity);
        else tickInAir(walkVelocity);
    }

    private void tickOnGround(IVector walkVelocity) {
        velocity.setY(0);
        velocity.multiply(FRICTION_MULTIPLIER);
        velocity = new Vector(Math.max(velocity.getX(), walkVelocity.getX()), //maintain walking speed or exponentially decay with friction if stopped
                Math.max(velocity.getY(), walkVelocity.getY()),
                Math.max(velocity.getZ(), walkVelocity.getZ()));
        if (velocity.lengthSquared() < MINIMUM_VELOCITY) velocity = Vector.newZeroVector();
        location.add(velocity);

        handleFallDamage();
    }

    private void tickInAir(IVector walkVelocity) {
        if (startingFreefallY < 0) startingFreefallY = location.getY();
        walkVelocity.multiply(FREEFALL_VELOCITY_MULTIPLIER);
        velocity.add(0, GRAVITY, 0);
        location.add(velocity.clone().add(walkVelocity));
        checkIfLandedOnBlock();
    }

    /**
     * Round the y level to the nearest whole integer once the entity lands onto a block
     */
    private void checkIfLandedOnBlock() {
        if (location.getY() == (int) location.getY()) return; //no rounding needed
        if (blockIsAir(0) && !blockIsAir(-1)) location.setY((int) location.getY() + 1);
    }

    private void handleFallDamage() {
        if (startingFreefallY < 0) return;
        float deltaY = startingFreefallY - location.getY();
        startingFreefallY = -1;
        if (deltaY > 0) takeFallDamage(deltaY);
    }

    public void takeFallDamage(float deltaY) {
        loseHealth(FALL_DAMAGE_DEFAULT_MULTIPLIER * deltaY);
    }

    public void loseHealth(float amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public void setYaw(float yaw) {
        location.setYaw(yaw);
    }

    public void setPitch(float pitch) {
        location.setPitch(pitch);
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isHostile() {
        return false;
    }
}
