#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTex; // Tex coordinates

out vec2 TexCoord;
out vec4 outColor;
uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform vec3 color;

void main()
{
    gl_Position =  projection * view * model * vec4(aPos, 1.0);
    outColor = vec4(color, 1.0);
    TexCoord = aTex;
}