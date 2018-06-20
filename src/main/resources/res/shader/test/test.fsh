#version 330 core

in vec3 vertexPos;
in vec2 uvs;

out vec4 fragColor;

uniform sampler2D tex;

// All this to outline a cube...what am I doing with my life?
void main() {
    //vec4 color = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 color = texture(tex, uvs);
    //color = vec4(1.0, 1.0, 1.0, 1.0);
    
    // This is equal to double the width of the outlines
    float epsilona = 10.0 / 64.0;
    
    // The smaller that this number is, the less likely that there are gaps, but the GPU may interpret it as 0 and break
    float epsilonb = (epsilona / 2) + 0.000001;
    
    // Calculate the fractions relative the the nearest 1.0x1.0 "gridlines"
    float fractxa = fract(vertexPos.x + (epsilona / 2) - 0.5);
    float fractxb = 1.0 - fract(vertexPos.x - (epsilona / 2) - 0.5);
    float fractya = fract(vertexPos.y + (epsilona / 2) - 0.5);
    float fractyb = 1.0 - fract(vertexPos.y - (epsilona / 2) - 0.5);
    float fractza = fract(vertexPos.z + (epsilona / 2) - 0.5);
    float fractzb = 1.0 - fract(vertexPos.z - (epsilona / 2) - 0.5);
    
    // Determine whether this pixel is part of the border
    bool doX = (fractxa > epsilonb && fractxa <= epsilona) || (fractxb > epsilonb && fractxb <= epsilona);
    bool doY = (fractya > epsilonb && fractya <= epsilona) || (fractyb > epsilonb && fractyb <= epsilona);
    bool doZ = (fractza > epsilonb && fractza <= epsilona) || (fractzb > epsilonb && fractzb <= epsilona);
    
    // Darken if this pixel is part of the border
    if (doX || doY || doZ) {
        color = color * 0.75;
    }
    
    // Apply the color
    fragColor = color;
}