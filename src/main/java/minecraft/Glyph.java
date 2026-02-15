package minecraft;

// Records the x, y, width, height, and character of the glyph.
public record Glyph(
        int letter,
        // Size of the glyph (width, height)
        int sizeX,
        int sizeY,

        // Defines the x values of the uv map
        float u0,
        float u1,

        // defines y values
        float v0,
        float v1,
        // Offset relative to the texture atlas
        int offsetX,
        int offsetY
) {
    
}
