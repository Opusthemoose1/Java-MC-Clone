package minecraft.window.texture;

import minecraft.block.Material;

import java.util.Collection;

public interface ITextureMap {

    public Texture getTexture(Material material);

    public Collection<Texture> getTextures();
}
