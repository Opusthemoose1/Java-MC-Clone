#version 330 core

in vec2 texCoords;
out vec4 FragColor;

uniform sampler2D fontAtlas;

void main()
{
    vec4 sample = texture(fontAtlas, texCoords);
    //   If the sampled textures is just black, make it transparent
    if (sample.rgb == vec3(0.0, 0.0, 0.0))
    {
        FragColor = vec4(1.0, 1.0, 1.0, 0.0) * sample;
    }
    // Else, just keep it white
    else
    {
        FragColor = vec4(1.0, 1.0, 1.0, 1.0) * sample;
    }
}