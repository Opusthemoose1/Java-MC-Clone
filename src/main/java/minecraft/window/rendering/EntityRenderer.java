package minecraft.window.rendering;

import minecraft.WorldContext;
import minecraft.entity.Entity;
import minecraft.window.Camera;
import minecraft.window.FrameRenderObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRenderer implements FrameRenderObserver {

    Map<Entity, IMesh> entityMeshes = new HashMap<>();

    public void addEntityMesh(Entity entity, IMesh mesh) {
        entityMeshes.put(entity, mesh);
    }

    public void removeEntityMesh(Entity entity) {
        entityMeshes.remove(entity);
    }


    @Override
    public void render(WorldContext context, Camera camera) {

        for (Entity entity : context.getEntityManager()) {
            if (entityMeshes.containsKey(entity)) {
                IMesh mesh = entityMeshes.get(entity);
                mesh.draw(camera);
            }


        }
    }
}
