package minecraft.window.texture;

import minecraft.block.Material;
import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_TEXTURE_2D_ARRAY;
import static org.lwjgl.opengles.GLES20.glGenTextures;

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

    public void loadBlockTextures() {
        String texturePath = "src/resources/textures/blocks";
        File textureDir = new File(texturePath);

        if (!textureDir.exists() || !textureDir.isDirectory()) {
            System.err.println("Texture directory not found: " + texturePath);
            return;
        }

        File[] textureFiles = textureDir.listFiles((dir, name) -> {
            String lower = name.toLowerCase();
            return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
        });

        if (textureFiles == null || textureFiles.length == 0) {
            System.err.println("No texture files found in: " + texturePath);
            return;
        }

        for (File file : textureFiles) {
            String name = file.getName();           // "grass.png"
            String key  = name.substring(0, name.lastIndexOf('.')); // "grass"

            // TODO: FACTORY PATTERN
            if (key.equals("cobblestone"))
            {
                textureMap.put(Material.COBBLESTONE, new Texture(file.getPath() ));
            }
            if (key.equals("dirt"))
            {
                textureMap.put(Material.DIRT, new Texture(file.getPath()));
            }

        }


        System.out.println("Loaded " + textureMap.size() + " block textures.");
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
    public HashMap<Material, Texture> getTextureMap() {return textureMap; };
}
