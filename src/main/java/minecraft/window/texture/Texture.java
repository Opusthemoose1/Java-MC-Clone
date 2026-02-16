package minecraft.window.texture;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {
    private int textureID;

    Texture(String filePath) {
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load(
                    filePath,
                    width, height, channels,
                    4
            );
            if (image == null) throw new RuntimeException(STBImage.stbi_failure_reason());

            this.textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, this.textureID);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(image);
        }
    }

    public int getTextureID() {return this.textureID; };
}
