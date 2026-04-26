package minecraft.window.texture;

import minecraft.Minecraft;
import minecraft.Material;
import java.io.File;

import java.util.Collection;

import java.util.LinkedHashMap;
import java.util.TreeMap;



public class TextureMap implements ITextureMap {

    private final String texturePath;
    private final LinkedHashMap<Material, Texture> textureMap = new LinkedHashMap<>();

    public TextureMap(String texturePath) {
        if (!texturePath.endsWith("/")) throw new IllegalArgumentException("Texture path must end with a slash (/).");
        this.texturePath = texturePath;

        if (texturePath.equals("src/resources/textures/blocks/")) {
            loadBlockTextures();
        }
    }

    public void loadBlockTextures() {
        final String texturePath = "src/resources/textures/blocks/";
        File textureDir = new File(texturePath);

        if (!textureDir.exists() || !textureDir.isDirectory()) {
            throw new RuntimeException("Texture directory not found: " + texturePath);
        }

        File[] textureFiles = textureDir.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
        });

        if (textureFiles == null || textureFiles.length == 0) {
            throw new RuntimeException("No texture files found in: " + texturePath);
        }
        for (Material material : Material.getIdMap().values()) {
            if (material.getTextureFileName() == null) continue; // Air doesn't have a texture but its a material
            String fileName = material.getTextureFileName() + ".png";

            textureMap.put(material, new Texture(texturePath + fileName));
        }

        Minecraft.getLogger().info("Loaded {} block textures.", textureMap.size());
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

    public Collection<Texture> getTextures() {
        return textureMap.values();
    }


}
