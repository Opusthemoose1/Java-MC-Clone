package minecraft;

import minecraft.window.rendering.IMesh;
import minecraft.window.rendering.LoadOBJNoNormals;
import org.junit.jupiter.api.Test;

public class LoadOBJFileTest {

    @Test
    public void loadOBJFileTest(){
        LoadOBJNoNormals loadOBJNoNormals = new LoadOBJNoNormals();
        IMesh mesh = loadOBJNoNormals.loadFile("src/resources/models/test_plane.obj");
        assert(mesh != null);

        assert(mesh.getVertices().size() == 4);
        assert(mesh.getIndices().size() == 6);
    }
}
