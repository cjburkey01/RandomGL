#version 330 core

in vec3 normal;
in vec2 uvs;
in vec3 fragPos;

out vec4 fragColor;

// Color
uniform sampler2D tex;

// Lighting
uniform vec3 ambientLightColor;
uniform vec3 pointLightPos;
uniform vec3 pointLightColor;
uniform float specularIntensity;
uniform vec3 viewPos;

void main() {
    vec4 color = texture(tex, uvs);
    
    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(pointLightPos - fragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * pointLightColor;
    
    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 256);
    vec3 specular = specularIntensity * spec * pointLightColor;
    
    vec3 light = ambientLightColor + diffuse + specular;
    
    fragColor = vec4(light, 1.0) * color;
}