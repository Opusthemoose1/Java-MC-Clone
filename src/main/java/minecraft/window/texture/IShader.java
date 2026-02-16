package minecraft.window.texture;

import org.joml.Matrix4f;

public interface IShader {

    public void bind();

    public void setMatrix4(Matrix4f matrix, String uniformName);

}
