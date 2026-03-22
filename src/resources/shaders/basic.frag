#version 330
out vec4 FragColor;

in vec2 TexCoord;
flat in int vBlockType;

uniform int uTileMap[16]; // 16 block types supported
uniform int uAtlasColumns; // 16 columns
uniform int uAtlasRows; // Could be anything
uniform sampler2D uAtlas; // Atlas texture

void main()
{
    int tileIndex = uTileMap[vBlockType];

    float col = float(tileIndex % uAtlasColumns);
    float row = float(tileIndex / uAtlasColumns);

    vec2 atlasUV = vec2(
    (col + TexCoord.x) / float(uAtlasColumns),
    (row + TexCoord.y) / float(uAtlasRows)
    );

    FragColor = texture(uAtlas, atlasUV);

}