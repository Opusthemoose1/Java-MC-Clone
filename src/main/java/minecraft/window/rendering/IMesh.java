package minecraft.window.rendering;

import java.util.List;

public interface IMesh {
        List<Vertex> getVertices();
        List<Integer> getIndices();
        int getStride();

}
