#version 400

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_TextureCoords;
out vec3 surfaceNormal;
out vec3 lightVector; // vector directed towards light source
out vec3 cameraVector; // vector directed towards camera

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;
uniform float useFakeLighting; // 1 = true, 0 = false

void main(void) {

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_TextureCoords = textureCoords;

    vec3 actualNormal = normal;
    if (useFakeLighting > 0.5) {
        actualNormal = vec3(0, 1, 0);
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0)).xyz;
    lightVector = lightPosition - worldPosition.xyz;
    cameraVector = (inverse(viewMatrix) * vec4(0, 0, 0, 1) - worldPosition).xyz;
}