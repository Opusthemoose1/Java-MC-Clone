package minecraft.window.rendering;

import java.util.List;

public class MeshNoNormals implements IMesh{
    List<Vertex> vertices;
    List<Integer> indices;
    public MeshNoNormals(List<Vertex> vertices, List<Integer> indices) {
        this.vertices = vertices;
        this.indices = indices;
    }
    public void draw(){}

    @Override
    public List<Vertex> getVertices() {
        return vertices;
    }
    @Override
    public List<Integer> getIndices() {
        return indices;
    }
}
