package minecraft.entity;

import java.util.ArrayList;

/*
EntityManager is a singleton point of access for managing all entities in the game.
It'll handle updating, registering, deregistering etc.
This is more of a facade pattern on top of everything but it reduces parameter passing everywhere

 */
public class EntityManager {

    static ArrayList<Entity> entities = new ArrayList<>();
    private static EntityManager instance = null;
    private static final EntityFactory entityFactory = new EntityFactory();

    private EntityManager() {
    }

    public synchronized static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    public static void addEntity(Entity entity) {
        entities.add(entity);
    }

    public static void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public static Entity createEntity(EntityFactory.EntityType type) {
        Entity entity = entityFactory.createEntity(type);
        addEntity(entity);
        return entity;
    }

    // Force an update on all registered entities
    public static void tickAllEntities()
    {
        for (Entity entity : entities)
        {
            entity.tick();
        }
    }

}
