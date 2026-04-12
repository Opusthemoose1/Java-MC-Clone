package minecraft.chunk;

import minecraft.Material;

/**
 * Rather than simply storing a Material for each block, it is possible that there are future additions to blocks that
 * require them to be able to store more data than this. However, we do not need to store a Block, as that object
 * also requires a Location, which would be redundant to store in the chunk's array.
 * @param materialId
 */
public record ChunkBlock(
        byte materialId
){

    public ChunkBlock(Material material) {
        this(material.getId());
    }

    public boolean isType(Material material) {
        return material.getId() == materialId;
    }

    public Material getMaterial() {
        return Material.fromId(materialId);
    }
}
