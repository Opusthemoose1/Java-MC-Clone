package minecraft;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;

// Bitmap text rendering using resources/textures/ascii.png (Minecraft's font)
// Each glyph is 64 pixels away from the next glyph. Each glyph is 40 pixels wide and 56 pixels tall
// Numbers start on row 3 (counting from 0)
// Capital letters start on row 4 column 1, end at row 5 column 10
// Lowercase are row 6 column 1, row 7 coulmn 10
// I'm being dense, it's literally called ascii.png. It's at their respective position in the ascii table
public class TextRenderer {
    private int VAO, VBO, textureID;
    private Shader text_shader;
    FloatBuffer vertexData;
    private Glyph[] glyphs;
    // Inject shader
    public TextRenderer(String filePath, Shader shader) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(
                    filePath,
                    w, h, channels,
                    4
            );
            if (image == null) throw new RuntimeException(STBImage.stbi_failure_reason());

            this.textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, this.textureID);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);

            this.glyphs = new Glyph[255];
            for (int i = 0; i < 255; i++)
            {
                this.glyphs[i].letter = i;
                // TODO: Fix magic numbers by passing them in the constructor. I call it "kicking the can down the road"
                this.glyphs[i].sizeX = 40;
                this.glyphs[i].sizeY = 56;

                int row = i % 15;
                // top left
                this.glyphs[i].u0 = (row * 64.0f) / 1024.0f;
                this.glyphs[i].u1 = ((row * 64.0f) + 40.0f) / 1024.0f;

                this.glyphs[i].v0 = (row * 64.0f) / 1024.0f;
                this.glyphs[i].v1 = ((row * 64.0f) - 56.0f) / 1024.0f;

            }
        }
        this.text_shader = shader;
        this.VAO = glGenVertexArrays();
        this.VBO = glGenBuffers();

        glBindVertexArray(this.VAO);
        glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
        // Vertex data is posx, posy, u, v. There's 6 vertices in a quad
        this.vertexData = BufferUtils.createFloatBuffer(6 * 4);

        glBufferData(
                GL_ARRAY_BUFFER,
                this.vertexData,
                GL_DYNAMIC_DRAW
        );

        final int STRIDE = 4 * Float.BYTES;

        glVertexAttribPointer(0, 2, GL_FLOAT, false, STRIDE, 0L);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 2, GL_FLOAT, false, STRIDE, 2L * Float.BYTES);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }
    public void renderText(Vector2f screenPos, String text)
    {
        this.text_shader.bind();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.textureID);
        glBindVertexArray(this.VAO);

        float screenPosX = screenPos.x;
        float screenPosY = screenPos.y;

        char[] characterArray = text.toCharArray();
        // i will force java to be C++
        for (char ch : characterArray)
        {
            Glyph glyph = this.glyphs[ch];

            float w = glyph.sizeX;
            float h = glyph.sizeY;

            float[] vertices = {
                    screenPosX + glyph.sizeX, screenPosY,               glyph.u1, glyph.v0, // Top right
                    screenPosX + glyph.sizeX, screenPosY + glyph.sizeY, glyph.u1, glyph.v1, // Bottom right
                    screenPosX              , screenPosY + glyph.sizeY, glyph.u0, glyph.v0, // Bottom left

                    screenPosX + glyph.sizeX, screenPosY,               glyph.u1, glyph.v0, // Top right
                    screenPosX              , screenPosY + glyph.sizeY, glyph.u0, glyph.v0, // Bottom left
                    screenPosX,               screenPosY,               glyph.u0, glyph.v0, // Top left
                };
            glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            glDrawArrays(GL_TRIANGLES, 0, 6);

        }
        glBindVertexArray(0);

    }

    public int getTextureID() {
        return textureID;
    }
}
