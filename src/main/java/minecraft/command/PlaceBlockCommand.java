package minecraft.command;

import minecraft.Material;
import minecraft.chunk.Block;
import minecraft.entity.Entity;
import minecraft.math.IVector;

public class PlaceBlockCommand implements ICommand {

    public void execute(Entity player) {
        if (!player.isPlayer()) throw new RuntimeException("Only players can place blocks");
        Block lookingAt = player.getBlockLookingAt();
        if (lookingAt == null) return;

        Block.BlockFace face = lookingAt.getBlockFaceLookingAt(player.getEyeLocation());
        if (face == null) return;

        Block relativeBlock = player.getContext().getBlock(lookingAt.getRelativeLocation(face));
        if (relativeBlock.getMaterial().isSolid()) return;

        for (int i = 0; i < Math.ceil(player.getHeight()); i++) {
            if (relativeBlock.getLocation().equals(player.getLocation().getBlockLocation().add(0, i, 0))) return;
        }
        relativeBlock.setType(Material.COBBLESTONE);
    }
}
