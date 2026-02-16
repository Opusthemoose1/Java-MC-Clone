package minecraft.window.texture;

import minecraft.block.Material;

import java.util.HashMap;

public class TextureMap implements ITextureMap {

    private final String texturePath;
    private final HashMap<Material, Texture> textureMap = new HashMap<>();

    public TextureMap(String texturePath) {
        if (!texturePath.endsWith("/")) throw new IllegalArgumentException("Texture path must end with a slash (/).");
        this.texturePath = texturePath;
    }

    @Override
    public Texture getTexture(Material material) {
        Texture texture = textureMap.get(material);
        if (texture == null) {
            texture = new Texture(texturePath + material.getTextureFileName());
            textureMap.put(material, texture);
        }
        return texture;
    }
}
