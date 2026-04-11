package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.location.Location;
import minecraft.timer.ITimer;

public class EntityFactory {

    private static final float DEFAULT_Y = 16;

    private float nextPositionX = 0;

    private final WorldContext context;
    private final ITimer timer;

    public enum EntityType {
        PLAYER,
        CHICKEN,
        OGRE,
    }

    public EntityFactory(WorldContext context, ITimer timer) {
        this.context = context;
        this.timer = timer;
    }

    public Entity createEntity(EntityType type) {
        return createEntity(type, null);
    }

    public Entity createEntity(EntityType type, Location location) {
        if (location == null) {
            location = Location.createLocation(nextPositionX, DEFAULT_Y, 0);
            nextPositionX++;
        }

        return switch (type) {
            case PLAYER -> new Player(location, context);
            case CHICKEN -> new Chicken(location, context);
            case OGRE -> new Ogre(location, context, timer);
        };
    }

    public Entity createChicken() {
        return createEntity(EntityType.CHICKEN);
    }

    public Entity createOgre() {
        return createEntity(EntityType.OGRE);
    }

    public Entity createPlayer() {
        return createEntity(EntityType.PLAYER);
    }

    public Entity createChicken(Location location) {
        return createEntity(EntityType.CHICKEN, location);
    }

    public Entity createOgre(Location location) {
        return createEntity(EntityType.OGRE, location);
    }

    public Entity createPlayer(Location location) {
        return createEntity(EntityType.PLAYER, location);
    }
}
