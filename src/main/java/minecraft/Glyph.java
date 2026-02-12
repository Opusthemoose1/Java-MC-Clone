package minecraft;

// Records the x, y, width, height, and character of the glyph.
public class Glyph {
    public int letter;
    // Size of the glyph (width, height)
    public int sizeX;
    public int sizeY;

    // Defines the x values of the uv map
    public float u0;
    public float u1;

    // defines y values
    public float v0;
    public float v1;
    // Offset relative to the texture atlas
    public int offsetX;
    public int offsetY;
}
