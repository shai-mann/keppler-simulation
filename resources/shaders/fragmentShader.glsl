#version 400

in vec2 pass_TextureCoords;
in vec3 surfaceNormal;
in vec3 lightVector;
in vec3 cameraVector;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main(void) {

    vec4 textureColour = texture(textureSampler, pass_TextureCoords);

    if (textureColour.a < 0.5) {
        discard; // if transparency is less than 0.5, don't render
    }

    vec3 unitNormal = normalize(surfaceNormal); // reduce magnitude to 1
    vec3 unitLight = normalize(lightVector);

    float lightValue = dot(unitLight, unitNormal);
    float brightness = max(lightValue, 0.2); // limit brightness to b > 0.2
    vec3 diffuse = brightness * lightColor;

    // specular lighting
    vec3 unitCamera = normalize(cameraVector);
    vec3 lightDirection = -unitLight;

    float specularFactor = dot(reflect(lightDirection, unitNormal), unitCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    out_color = vec4(diffuse, 1.0) * textureColour + vec4(finalSpecular, 1);
}