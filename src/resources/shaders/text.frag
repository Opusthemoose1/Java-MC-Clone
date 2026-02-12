#version 330 core

in vec2 texCoords;
out vec4 FragColor;

uniform sampler2D fontAtlas;

void main()
{
    vec4 sample = texture(fontAtlas, texCoords);
    FragColor = vec4(1.0, 1.0, 1.0, 1.0) * sample;
}