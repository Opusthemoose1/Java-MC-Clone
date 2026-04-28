package minecraft.command;

import minecraft.chunk.location.Location;
import minecraft.entity.AttackingEntity;
import minecraft.entity.Entity;

import java.util.Set;

public class AttackCommand implements ICommand {

    public static float ATTACK_REACH_RADIUS = 5f;

    @Override
    public void execute(Entity entity) {
        if (!entity.canAttack()) return;
        Entity closest = getClosestEntity(entity);
        if (closest == null) return;

        AttackingEntity attackingEntity = (AttackingEntity) entity;
        attackingEntity.attack(closest);
    }

    private Entity getClosestEntity(Entity player) {
        Location location = player.getLocation();
        Set<Entity> entitiesNearby = player.getContext().getEntityManager().getEntitiesNearby(location, ATTACK_REACH_RADIUS);
        double closestDistance = Double.MAX_VALUE;
        Entity closestEntity = null;

        for (Entity entity : entitiesNearby) {
            if (entity.equals(player)) continue;
            double distance = entity.getLocation().getDistance(location);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }
        return closestEntity;
    }

}
