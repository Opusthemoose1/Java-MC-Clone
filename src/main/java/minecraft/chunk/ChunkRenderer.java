package minecraft.chunk;

import minecraft.block.Material;
import minecraft.window.Camera;
import minecraft.window.texture.Shader;
import minecraft.window.texture.Texture;
import minecraft.window.texture.TextureMap;

import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

// Draws the current actively rendered set of chunks
public class ChunkRenderer {
    private final TextureMap textures;
    private final Shader chunkShader;
    public ChunkRenderer(TextureMap map, Shader shader) {
        textures = map;
        chunkShader = shader;
    }

    public void drawChunks(Map<Long, Chunk> chunks, Camera camera)
    {
        for (Chunk chunk : chunks.values())
        {
            this.chunkShader.bind();
            this.chunkShader.setMatrix4(camera.getViewMatrix(), "view");
            this.chunkShader.setMatrix4(camera.getProjectionMatrix(), "projection");

            Texture cobblestoneTexture = textures.getTexture(Material.COBBLESTONE);
            glBindTexture(GL_TEXTURE_2D, cobblestoneTexture.getTextureID());
            glBindVertexArray(chunk.getVAO());

            glDrawElements(GL_TRIANGLES, chunk.getIndexCount(), GL_UNSIGNED_INT, 0);
        }
    }


}
