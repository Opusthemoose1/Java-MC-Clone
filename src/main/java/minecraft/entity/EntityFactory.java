package minecraft.entity;

import minecraft.chunk.Location;

public class EntityFactory {

    private static final float DEFAULT_Y = 10;

    private float nextPositionX = 0;

    public enum EntityType {
        PLAYER,
        CHICKEN,
        OGRE,
    }

    public EntityFactory() {

    }

    public Entity createEntity(EntityType type) {
        Location location = Location.createLocation(nextPositionX, DEFAULT_Y, 0);
        nextPositionX++;

        return switch (type) {
            case PLAYER -> new Player(location);
            case CHICKEN -> new Chicken(location);
            case OGRE -> new Ogre(location);
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

}
