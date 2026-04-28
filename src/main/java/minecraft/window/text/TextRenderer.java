package minecraft.window.text;

import minecraft.window.texture.IShader;
import org.joml.Matrix4f;
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
public class TextRenderer implements ITextRenderer {

    private final int VAO;
    private final int VBO;
    private final int textureID;
    private final IShader textShader;
    FloatBuffer vertexData;
    private final Glyph[] glyphs;

    // Inject shader
    public TextRenderer(String filePath, IShader shader) {
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

            float atlasSize = 1024.0f;
            float cellSize = 64.0f;

            final int ASCII_MAX_SIZE =  255;
            final int NUM_SPRITES_PER_ROW = 16;
            final int NUM_SPRITE_PER_COL = 16;



            this.glyphs = new Glyph[ASCII_MAX_SIZE];
            for (int i = 0; i < ASCII_MAX_SIZE; i++) {
                char letter = (char)i;

                int col = i % NUM_SPRITES_PER_ROW;
                int row = i / NUM_SPRITE_PER_COL;

                float x = col * cellSize;
                float y = row * cellSize;

                final int sizeX = 64, sizeY = 64;

                float u0 = x / atlasSize, u1 = (x + sizeX) / atlasSize;

                float v0 = y / atlasSize, v1 = (y + sizeY) / atlasSize;

                this.glyphs[i] = new Glyph(letter, sizeX, sizeY, u0, u1, v0, v1, 0, 0);
            }
        }
        this.textShader = shader;
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

    public void renderText(Matrix4f projection, Vector2f screenPos, float scale, String text)
    {
        this.textShader.bind();
        this.textShader.setMatrix4(projection, "projection");
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this.textureID);
        glBindVertexArray(this.VAO);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

        char[] characterArray = text.toCharArray();
        // i will force java to be C++
        for (int i = 0; i < characterArray.length; i++)
        {
            float[] vertices = getFloats(screenPos, scale, characterArray[i], i);
            glBindBuffer(GL_ARRAY_BUFFER, this.VBO);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

            glDrawArrays(GL_TRIANGLES, 0, 6);

        }
        glBindVertexArray(0);
        glDisable(GL_BLEND);

    }

    private float[] getFloats(Vector2f screenPos, float scale, char ch, int index) {
        Glyph glyph = this.glyphs[ch];

        float y = screenPos.y;

        float w = glyph.sizeX() * scale;
        float h = glyph.sizeY() * scale;

        float x = screenPos.x + (w * index);

        return new float[]{
                // First triangle
                x,     y,     glyph.u0(), glyph.v0(),  // top-left
                x+w,   y,     glyph.u1(), glyph.v0(),  // top-right
                x+w,   y+h,   glyph.u1(), glyph.v1(),  // bottom-right

                // Second triangle
                x,     y,     glyph.u0(), glyph.v0(),  // top-left
                x+w,   y+h,   glyph.u1(), glyph.v1(),  // bottom-right
                x,     y+h,   glyph.u0(), glyph.v1()   // bottom-left
        };
    }

    public int getTextureID() {
        return textureID;
    }
}
