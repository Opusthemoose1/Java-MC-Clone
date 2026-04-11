package minecraft.entity;

import minecraft.Minecraft;
import minecraft.WorldContext;
import minecraft.block.Material;
import minecraft.chunk.ChunkBlock;
import minecraft.chunk.location.Location;
import minecraft.chunk.location.YawPitchObserver;
import minecraft.math.IVector;
import minecraft.math.Vector;

abstract public class Entity implements YawPitchObserver {

    public static final float GRAVITY = -0.4f / Minecraft.TICKS_PER_SECOND,
            FRICTION_MULTIPLIER = 0.85f,
            MINIMUM_VELOCITY = 0.01f / Minecraft.TICKS_PER_SECOND,
            FREEFALL_VELOCITY_MULTIPLIER = 0.2f,
            FALL_DAMAGE_DEFAULT_MULTIPLIER = 0.2f,
            EPSILON = 0.01f,
            DEFAULT_WALK_SPEED = 1.75f / Minecraft.TICKS_PER_SECOND;

    private Location location;
    private IVector velocity = Vector.newZeroVector(), instantaneousVelocity = Vector.newZeroVector();
    protected WorldContext context;
    private float health, startingFreefallY = -1;

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
        return location.getY() <= 0 || !blockIsAir(-EPSILON);
    }

    protected boolean blockIsAir(float yOffset) {
        ChunkBlock block = context.getChunkLoader().getBlock(location.getZ(), location.getY() + yOffset, location.getX());
        return block == null || block.materialId() == Material.AIR.getId();
    }

    public IVector getInstantaneousVelocity() {
        IVector velocity = instantaneousVelocity.clone().setY(0);
        if (!velocity.isZero()) velocity.normalize().multiply(getWalkSpeed());
        return velocity;
    }

    public void addInstantaneousVelocity(IVector vector) {
        instantaneousVelocity.add(vector.getX(), vector.getY(), vector.getZ());
    }

    public void tick() {
        if (isOnSolidGround()) tickOnGround();
        else tickInAir();
        instantaneousVelocity = Vector.newZeroVector();
    }

    protected void tickOnGround() {
        IVector walkVelocity = getInstantaneousVelocity();
        velocity.multiply(FRICTION_MULTIPLIER);
        velocity = new Vector(Math.max(velocity.getX(), walkVelocity.getX()), //maintain walking speed or exponentially decay with friction if stopped
                Math.max(velocity.getY(), walkVelocity.getY()),
                Math.max(velocity.getZ(), walkVelocity.getZ()));
        velocity.add(walkVelocity);

        if (!velocity.isZero()) {
            if (velocity.lengthSquared() < MINIMUM_VELOCITY) velocity = Vector.newZeroVector();
            location.add(velocity);
            if (!walkVelocity.isZero()) velocity = walkVelocity;
        }

        handleFallDamage();
    }

    protected void tickInAir() {
        if (startingFreefallY < 0) startingFreefallY = location.getY();
        IVector walkVelocity = getInstantaneousVelocity();
        walkVelocity.multiply(FREEFALL_VELOCITY_MULTIPLIER); //let entity still move a little bit while in the air
        velocity.add(0, GRAVITY, 0);
        location.add(velocity.clone().add(walkVelocity));
        checkIfLandedInsideBlock();
    }

    /**
     * Round the y level to the nearest whole integer once the entity lands onto a block
     */
    private void checkIfLandedInsideBlock() {
        if (!blockIsAir(0) && blockIsAir(1)) {
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
}
