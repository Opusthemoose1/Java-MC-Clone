package minecraft.chunk;

import minecraft.block.Material;

public record ChunkBlock (
        byte materialId
){

    public ChunkBlock(Material material) {
        this(material.getId());
    }

    public boolean isType(Material material) {
        return material.getId() == materialId;
    }
}
