package minecraft.window.rendering;

import minecraft.WorldContext;
import minecraft.entity.Entity;
import minecraft.window.FrameRenderObserver;

public class EntityRenderer implements FrameRenderObserver {

    @Override
    public void render(WorldContext context) {
        //TODO
        for (Entity entity : context.getEntityManager()) {

        }
    }
}
