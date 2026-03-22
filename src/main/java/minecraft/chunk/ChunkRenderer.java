package minecraft.chunk;

import minecraft.block.Material;
import minecraft.window.Camera;
import minecraft.window.texture.Shader;
import minecraft.window.texture.Texture;
import minecraft.window.texture.TextureAtlas;
import minecraft.window.texture.TextureMap;

import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

// Draws the current actively rendered set of chunks
public class ChunkRenderer {
    private final TextureMap textures;
    private final TextureAtlas atlas;
    private final Shader chunkShader;
    public ChunkRenderer(TextureMap map, Shader shader) {
        textures = map;
        chunkShader = shader;
        atlas = new TextureAtlas(map);
        chunkShader.bind();
        // TODO: MAGIC
        chunkShader.setInt(16, "uAtlasColumns");
        chunkShader.setInt(1, "uAtlasRows");
        int[] textureArray = new int[16];
        for (int i = 0; i < 16; i++)
        {
            textureArray[i] = i;
        }
        chunkShader.setIntArray(textureArray, "uTileMap");
    }

    public void drawChunks(Map<Long, Chunk> chunks, Camera camera)
    {
        for (Chunk chunk : chunks.values())
        {
            this.chunkShader.bind();
            this.chunkShader.setMatrix4(camera.getViewMatrix(), "view");
            this.chunkShader.setMatrix4(camera.getProjectionMatrix(), "projection");

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, atlas.getTextureID());

            chunkShader.setInt(0, "uAtlas");
            glBindVertexArray(chunk.getVAO());

            glDrawElements(GL_TRIANGLES, chunk.getIndexCount(), GL_UNSIGNED_INT, 0);
        }
    }


}
