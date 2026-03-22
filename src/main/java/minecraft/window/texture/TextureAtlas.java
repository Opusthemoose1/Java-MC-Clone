package minecraft.window.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.stb.STBImageWrite;

/*
    Texture atlas takes in a single texture map, and converts it to a texture atlas
 */

record TileImage(ByteBuffer data, int width, int height) {}
public class TextureAtlas {
    private int textureID; // Used for OpenGL textures
    private int atlasWidth, atlasHeight;
    TextureMap map; // Map used to store all textures and material properties
    private final ArrayList<TileImage> tiles = new ArrayList<>();
    ByteBuffer atlas;

    public TextureAtlas(TextureMap map)
    {

        this.map = map;
        STBImage.stbi_set_flip_vertically_on_load(true);

        for (Texture texture : map.getTextureMap().values())
        {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer ch = stack.mallocInt(1);

                ByteBuffer data = STBImage.stbi_load(texture.getFilePath(), w, h, ch, 4);
                if (data == null)
                    throw new RuntimeException("Failed to load: " + texture.getFilePath()
                            + " — " + STBImage.stbi_failure_reason());

                tiles.add(new TileImage(data, w.get(0), h.get(0)));
            }
        }
        BuildAtlas();

    }
    private void BuildAtlas()
    {
        // read dimensions from the first tile rather than a separate field
        int tileW = tiles.getFirst().width();
        int tileH = tiles.getFirst().height();

        final int COLUMNS = 16;
        final int ROWS = (tiles.size() + COLUMNS - 1) / COLUMNS;

        atlasWidth  = COLUMNS * tileW;
        atlasHeight = ROWS    * tileH;

        atlas = BufferUtils.createByteBuffer(atlasWidth * atlasHeight * 4);

        for (int i = 0; i < tiles.size(); i++) {
            TileImage tile = tiles.get(i);
            int destX = (i % COLUMNS) * tileW;
            int destY = (i / COLUMNS) * tileH;

            for (int y = 0; y < tileH; y++) {
                int srcPos  = y * tileW * 4;
                int destPos = ((destY + y) * atlasWidth + destX) * 4;

                ByteBuffer srcSlice = tile.data().duplicate();
                srcSlice.position(srcPos).limit(srcPos + tileW * 4);

                atlas.position(destPos);
                atlas.put(srcSlice);
            }
        }

        saveAtlasPng(atlas, atlasWidth, atlasHeight, "src/resources/textures/atlas.png");
        atlas.flip(); // reset position to 0 before passing to OpenGL
        UploadAtlas(); // Create the OpenGL texture
        // Free the memory associated with each texture (blame the fact this is a C library)
        for (TileImage tile : tiles) STBImage.stbi_image_free(tile.data());

    }


    void saveAtlasPng(ByteBuffer atlas, int atlasWidth, int atlasHeight, String path) {
        // duplicate so we don't disturb the buffer's position
        ByteBuffer slice = atlas.duplicate();
        slice.rewind();

        STBImageWrite.stbi_write_png(
                path,
                atlasWidth,
                atlasHeight,
                4,                    // RGBA channels
                slice,
                atlasWidth * 4        // row stride in bytes
        );
    }

    private void UploadAtlas()
    {
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, atlasWidth, atlasHeight,
                0, GL_RGBA, GL_UNSIGNED_BYTE, atlas);
        glGenerateMipmap(GL_TEXTURE_2D);
    }
    public int getTextureID() {return textureID; }
}
