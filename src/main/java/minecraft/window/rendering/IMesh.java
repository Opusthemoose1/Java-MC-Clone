package minecraft.window.rendering;

import minecraft.entity.Entity;
import minecraft.window.Camera;
import minecraft.window.texture.Shader;

import java.util.List;

public interface IMesh {
        List<Vertex> getVertices();
        List<Integer> getIndices();
        int getStride();
        void draw(Entity entity, Camera camera);
        void setShader(Shader shader);

}
