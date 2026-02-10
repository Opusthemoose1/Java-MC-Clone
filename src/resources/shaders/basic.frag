#version 330
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D textureID;
void main()
{
    FragColor = texture(textureID, TexCoord);
}