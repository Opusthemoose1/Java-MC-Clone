package minecraft.command;

import minecraft.Material;
import minecraft.chunk.Block;
import minecraft.entity.Entity;

public class BreakBlockCommand implements ICommand {

    public void execute(Entity player) {
        if (!player.isPlayer()) throw new RuntimeException("Only players can break blocks");
        Block lookingAt = player.getBlockLookingAt();
        if (lookingAt == null) return;
        if (lookingAt.getMaterial().isBreakable()) lookingAt.setType(Material.AIR);
    }
}
