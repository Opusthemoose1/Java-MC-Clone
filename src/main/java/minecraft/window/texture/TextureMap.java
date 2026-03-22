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
                textureMap.put(Material.DIRT, new Texture(file.getPath()  ));
            }

        }

        System.out.println("Loaded " + textureMap.size() + " block textures.");
    }

    private void createTextureArray(String texutrePath)
    {
        int texArray = glGenTextures();
        glBindTexture(GL_TEXTURE_2D_ARRAY, texArray);

        int numTextures = Path.of(texturePath).getNameCount();
        System.out.println("Num of textures: " + numTextures);

        // TODO: Move STBI functions out as it's shared by Texture
//        try (MemoryStack stack = MemoryStack.stackPush())
//        {
//            IntBuffer width = stack.mallocInt(1);
//            IntBuffer height = stack.mallocInt(1);
//            IntBuffer channels = stack.mallocInt(1);
//
//            ByteBuffer image = STBImage.stbi_load(
//                    filePath,
//                    width, height, channels,
//                    4
//            );
//            if (image == null) throw new RuntimeException(STBImage.stbi_failure_reason());
//
//        // Allocate storage for N layers
//        glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, GL_RGBA8, width, height, numTextures, 0, GL_RGBA, GL_UNSIGNED_BYTE, nullptr);
//
//        // Upload each texture as a layer
//        for (int i = 0; i < numTextures; i++) {
//            glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, width, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, textureData[i]);
//        }
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
