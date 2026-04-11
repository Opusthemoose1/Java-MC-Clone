package minecraft.window.rendering;

import java.util.List;

public class MeshNoNormals implements IMesh{
    List<Vertex> vertices;
    List<Integer> indices;
    final int STRIDE; // Stride is the number of elements until the next set of vertices. 3 for position, 2 for uv
    public MeshNoNormals(List<Vertex> vertices, List<Integer> indices) {
        this.vertices = vertices;
        this.indices = indices;
        this.STRIDE = 5;
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

    @Override
    public int getStride() {
        return STRIDE;
    }
}
