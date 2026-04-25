package minecraft.window.texture;

import minecraft.Minecraft;
import minecraft.Material;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TextureMap implements ITextureMap {

    private final String texturePath;
    private final HashMap<Material, Texture> textureMap = new HashMap<>();

    public TextureMap(String texturePath) {
        if (!texturePath.endsWith("/")) throw new IllegalArgumentException("Texture path must end with a slash (/).");
        this.texturePath = texturePath;

        if (texturePath.equals("src/resources/textures/blocks/")) {
            loadBlockTextures();
        }
    }
    private static JsonNode loadTextureConfig(String path) {
        try {
            return new ObjectMapper().readTree(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture config: " + path, e);
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

        JsonNode root = loadTextureConfig(texturePath + "TextureMap.json");
        JsonNode entries = root.get("textures");

        for (JsonNode entry : entries) {
            String name = entry.get("material").asText();           // "grass"
            String fileName = entry.get("fileName").asText();

            Material material;
            try {
                material = Material.valueOf(name);
            } catch (Exception ex) {
                throw new RuntimeException("Invalid material: " + name);
            }

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
