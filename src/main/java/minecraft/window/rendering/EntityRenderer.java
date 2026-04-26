package minecraft.window.rendering;

import minecraft.WorldContext;
import minecraft.entity.Entity;
import minecraft.entity.EntityType;
import minecraft.window.Camera;
import minecraft.window.FrameRenderObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityRenderer implements FrameRenderObserver {

    Map<EntityType, IMesh> entityMeshes = new HashMap<>();

    public void setEntityMesh(EntityType type, IMesh mesh) {
        entityMeshes.put(type, mesh);
    }

    @Override
    public void render(WorldContext context, Camera camera) {
        for (Entity entity : context.getEntityManager()) {
            IMesh mesh = entityMeshes.get(entity.getType());
            if (mesh != null) mesh.draw(entity, camera);
        }
    }
}
