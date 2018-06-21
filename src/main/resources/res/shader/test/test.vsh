#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normals;
layout (location = 2) in vec2 textureCoords;

out vec3 normal;
out vec2 uvs;
out vec3 fragPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main() {
    vec4 pos = vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * pos;
    
    normal = mat3(transpose(inverse(modelMatrix))) * normals;
    uvs = textureCoords;
    fragPos = vec3(modelMatrix * pos);
}