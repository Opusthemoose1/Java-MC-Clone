package minecraft.entity;

import minecraft.Material;
import minecraft.WorldContext;
import minecraft.chunk.IChunk;
import minecraft.chunk.location.Location;
import minecraft.timer.ITimer;
import minecraft.timer.ITimerFactory;

public class EntityFactory {

    private static final float DEFAULT_Y = 16;

    private float nextPositionX = 0;

    private final WorldContext context;
    private final ITimerFactory timerFactory;

    public EntityFactory(WorldContext context, ITimerFactory timerFactory) {
        this.context = context;
        this.timerFactory = timerFactory;
    }

    public Entity createEntity(EntityType type) {
        return createEntity(type, null);
    }

    public Entity createEntityAtSurface(EntityType type, float x, float z) {
        return createEntity(type, Location.createLocation(x, context.getChunkLoader().getSurfaceLevel(x, z) + 1, z));
    }

    public Entity createEntity(EntityType type, Location location) {
        if (location == null) {
            location = Location.createLocation(nextPositionX, DEFAULT_Y, 0);
            nextPositionX++;
        }

        Entity entity = switch (type) {
            case PLAYER -> new Player(location, context);
            case CHICKEN -> new Chicken(location, context);
            case OGRE -> new Ogre(location, context, timerFactory.createTimer());
            case DEMON -> new Demon(location, context, timerFactory.createTimer());
        };
        context.getEntityManager().addEntity(entity);
        return entity;
    }

    public Entity createChicken() {
        return createEntity(EntityType.CHICKEN);
    }

    public Entity createOgre() {
        return createEntity(EntityType.OGRE);
    }

    public Entity createDemon() {
        return createEntity(EntityType.DEMON);
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

    public Entity createDemon(Location location) {
        return createEntity(EntityType.DEMON, location);
    }

    public Entity createPlayer(Location location) {
        return createEntity(EntityType.PLAYER, location);
    }
}
