package minecraft.entity;

import minecraft.chunk.Location;

import java.util.Set;

public interface IEntityManager extends Iterable<Entity> {

    public void addEntity(Entity entity);

    public void removeEntity(Entity entity);

    public void tickAllEntities();

    public Set<Entity> getEntitiesNearby(Location location, double radius);

}
