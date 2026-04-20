package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.Material;
import minecraft.chunk.Block;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.location.Location;
import minecraft.chunk.location.LocationObserver;
import minecraft.chunk.location.LocationPublisher;
import minecraft.chunk.location.YawPitchObserver;
import minecraft.math.IVector;
import minecraft.math.Vector;

import java.util.HashSet;
import java.util.Set;

abstract public class Entity implements YawPitchObserver, LocationPublisher {

    public static final float GRAVITY = -0.4f / Minecraft.TICKS_PER_SECOND,
            FRICTION_MULTIPLIER = 0.85f,
            MINIMUM_VELOCITY = 0.01f / Minecraft.TICKS_PER_SECOND,
            FREEFALL_VELOCITY_MULTIPLIER = 0.2f,
            FALL_DAMAGE_DEFAULT_MULTIPLIER = 0.2f, FALL_DAMAGE_MINIMUM_HEIGHT = 3.5f,
            EPSILON = 0.01f,
            DEFAULT_WALK_SPEED = 1.75f / Minecraft.TICKS_PER_SECOND,
            BLOCK_LOOKING_AT_MAX_DISTANCE = 4f, BLOCK_LOOKING_AT_STRIDE = 0.05f;

    private Location location;
    private IVector velocity = Vector.newZeroVector(), instantaneousForce = Vector.newZeroVector();
    protected WorldContext context;
    private float health, startingFreefallY = -1;

    private Set<LocationObserver> locationObservers = new HashSet<>();

    Entity(Location location, float initialHealth, WorldContext context) {
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

    public float getWalkSpeed() {
        return DEFAULT_WALK_SPEED;
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

    public void setVelocity(IVector velocity) {
        this.velocity = velocity;
    }

    abstract float getWeight();

    abstract float getHeight();

    public Location getEyeLocation() {
        return location.clone().add(0, getHeight(), 0);
    }

    public boolean isOnSolidGround() {
        return location.getY() <= 0 || blockIsSolid(-EPSILON);
    }

    protected boolean blockIsSolid(float yOffset) {
        ChunkBlock block = context.getChunkLoader().getBlock(location.getX(), location.getY() + yOffset, location.getZ());
        return block.getMaterial().isSolid();
    }

    public IVector getInstantaneousForce() {
        IVector velocity = instantaneousForce.clone().setY(0);
        if (!velocity.isZero()) velocity.normalize().multiply(getWalkSpeed());
        return velocity;
    }

    public void addInstantaneousForce(IVector vector) {
        instantaneousForce.add(vector.getX(), vector.getY(), vector.getZ());
    }

    public void tick() {
        if (isOnSolidGround()) tickOnGround();
        else tickInAir();
        instantaneousForce = Vector.newZeroVector();
        notifyLocationObservers();
    }

    protected void tickOnGround() {
        IVector walkingForce = getInstantaneousForce();
        velocity.multiply(FRICTION_MULTIPLIER);
        velocity = new Vector(Math.max(velocity.getX(), walkingForce.getX()), //maintain walking speed or exponentially decay with friction if stopped
                Math.max(velocity.getY(), walkingForce.getY()),
                Math.max(velocity.getZ(), walkingForce.getZ()));
        velocity.add(walkingForce);

        if (!velocity.isZero()) {
            if (velocity.lengthSquared() < MINIMUM_VELOCITY) velocity = Vector.newZeroVector();
            location.add(velocity);
            if (!walkingForce.isZero()) velocity = walkingForce;
        }

        handleFallDamage();
    }

    protected void tickInAir() {
        if (startingFreefallY < 0) startingFreefallY = location.getY();
        IVector walkForce = getInstantaneousForce();
        walkForce.multiply(FREEFALL_VELOCITY_MULTIPLIER); //let entity still move a little bit while in the air
        velocity.add(0, GRAVITY, 0);
        location.add(velocity.clone().add(walkForce));
        checkIfLandedInsideBlock();
    }

    /**
     * Round the y level to the nearest whole integer once the entity lands onto a block
     */
    private void checkIfLandedInsideBlock() {
        if (blockIsSolid(0) && !blockIsSolid(1)) {
            if (location.getY() != (int) location.getY()) {
                location.setY((int) location.getY() + 1); //round to upper block
            }
        }
    }

    private void handleFallDamage() {
        if (startingFreefallY < 0) return;
        float deltaY = startingFreefallY - location.getY();
        startingFreefallY = -1;
        if (deltaY > 0) takeFallDamage(deltaY);
    }

    protected void takeFallDamage(float deltaY) {
        if (deltaY < FALL_DAMAGE_MINIMUM_HEIGHT) return;
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

    @Override
    public void updateYawAndPitch(float yaw, float pitch) {
        setYaw(yaw);
        setPitch(pitch);
    }

    public void jump() {

    }

    public void setSprinting(boolean sprinting) {

    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isHostile() {
        return false;
    }

    public Block getBlockLookingAt() {
        IVector direction = location.getDirection().multiply(BLOCK_LOOKING_AT_STRIDE);
        IVector eye = getEyeLocation().toVector();

        float distanceChecked = 0f;
        IVector alreadyChecked = new Vector(-1, -1, -1);
        while (distanceChecked < BLOCK_LOOKING_AT_MAX_DISTANCE - BLOCK_LOOKING_AT_STRIDE) {
            eye.add(direction);
            distanceChecked += BLOCK_LOOKING_AT_STRIDE;
            if ((int) eye.getX() == alreadyChecked.getX() && (int) eye.getY() == alreadyChecked.getY() && (int) eye.getZ() == alreadyChecked.getZ()) continue; //same block coords

            ChunkBlock block = context.getChunkLoader().getBlock(eye.getX(), eye.getY(), eye.getZ());
            if (!block.isType(Material.AIR)) return new Block(Location.createLocation(eye), block, context.getChunkLoader());
            alreadyChecked = new Vector((int) eye.getX(), (int) eye.getY(), (int) eye.getZ());
        }
        return null;
    }

    @Override
    public void attachLocationObserver(LocationObserver observer) {
        locationObservers.add(observer);
    }

    @Override
    public void detachLocationObserver(LocationObserver observer) {
        locationObservers.remove(observer);
    }

    private void notifyLocationObservers() {
        for (LocationObserver observer : locationObservers) {
            observer.updateLocation(location.clone());
        }
    }
}
