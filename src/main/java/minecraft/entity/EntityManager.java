package minecraft.entity;

import minecraft.chunk.location.Location;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
EntityManager is a singleton point of access for managing all entities in the game.
It'll handle updating, registering, deregistering etc.
This is more of a facade pattern on top of everything but it reduces parameter passing everywhere

 */
public class EntityManager implements IEntityManager, Iterable<Entity> {

    private final Set<Entity> entities = new HashSet<>();

    public EntityManager() {
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    // Force an update on all registered entities
    public void tickAllEntities() {
        for (Entity entity : entities) entity.tick();
    }

    public int getEntityCount() {
        return entities.size();
    }

    public Set<Entity> getEntitiesNearby(Location location, double radius) {
        Set<Entity> nearby = new HashSet<>();
        for (Entity entity : this) {
            if (entity.getLocation().getDistance(location) <= radius) {
                nearby.add(entity);
            }
        }
        return nearby;
    }

    @NotNull
    @Override
    public Iterator<Entity> iterator() {
        return entities.iterator();
    }
}
