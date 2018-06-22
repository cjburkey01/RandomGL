#version 330 core

struct Material {
    float specularBrightness;
    float shininess;
};

in vec3 normal;
in vec2 uvs;
in vec3 fragPos;

out vec4 fragColor;

// Color
uniform sampler2D tex;

// Lighting
uniform vec3 viewPos;
uniform vec3 ambientLightColor;
uniform vec3 pointLightPos;
uniform vec3 pointLightColor;
uniform Material material;

void main() {
    vec4 diffCol = texture(tex, uvs);
    
    vec3 norm = normalize(normal);
    vec3 lightDir = normalize(pointLightPos - fragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * pointLightColor;
    
    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = material.specularBrightness * spec * pointLightColor;
    
    vec4 light = vec4(ambientLightColor + diffuse + specular, 1.0) * diffCol;
    
    fragColor = light;
}