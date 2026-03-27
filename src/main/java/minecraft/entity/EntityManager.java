package minecraft.entity;

import java.util.ArrayList;

/*
EntityManager is a global point of access for managing all entities in the game.
It'll handle updating, registering, deregistering etc.
This is more of a facade pattern on top of everything but it reduces parameter passing everywhere

 */
public class EntityManager {

    static ArrayList<Entity> entities = new ArrayList<>();
    private static EntityManager manager = null;
    private static final EntityFactory entityFactory = new EntityFactory();

    private EntityManager() {
    }

    public static EntityManager GetInstance() {
        if (manager == null) {
            manager = new EntityManager();
        }
        return manager;
    }

    public static void attach(Entity entity) {
        entities.add(entity);
    }

    public static void detach(Entity entity) {
        entities.remove(entity);
    }

    // Generate a new entity with the entity factory. Note that you still need to attach the entity
    // to receive tick updates. Just in case we want to create an entity but not tick it
    public static Entity newEntity(EntityFactory.EntityType type){
        return entityFactory.createEntity(type);
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
