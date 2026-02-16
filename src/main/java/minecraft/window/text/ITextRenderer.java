package minecraft.window.text;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public interface ITextRenderer {

    public void renderText(Matrix4f projection, Vector2f screenPos, float scale, String text);
}
