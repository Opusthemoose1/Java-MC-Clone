#version 330
out vec4 FragColor;

in vec2 TexCoord;
in vec4 outColor;

void main()
{
    FragColor = outColor;
}