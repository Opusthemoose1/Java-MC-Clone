package minecraft.window.rendering;

import minecraft.WorldContext;
import minecraft.chunk.Chunk;
import minecraft.window.Camera;
import minecraft.window.FrameRenderObserver;
import minecraft.window.texture.Shader;
import minecraft.window.texture.TextureAtlas;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

// Draws the current actively rendered set of chunks
public class ChunkRenderer implements FrameRenderObserver {
    private final TextureAtlas atlas;
    private final Shader chunkShader;
    private final Camera camera;

    public ChunkRenderer(TextureAtlas atlas, Camera camera, Shader shader) {
        this.camera = camera;
        this.atlas = atlas;
        chunkShader = shader;
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

    public void render(WorldContext context) {
        for (Chunk chunk : context.getChunkLoader().getRenderedChunks()) {
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
