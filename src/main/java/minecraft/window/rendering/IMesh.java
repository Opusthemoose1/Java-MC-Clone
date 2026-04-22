package minecraft.window.rendering;

import minecraft.window.Camera;
import minecraft.window.texture.Shader;

import java.util.List;

public interface IMesh {
        List<Vertex> getVertices();
        List<Integer> getIndices();
        int getStride();
        void draw(Camera camera);
        void setShader(Shader shader);

}
