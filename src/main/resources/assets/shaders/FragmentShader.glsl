#version 330

in vec3 out_vertex;
in vec2 out_texCoord;
in vec3 out_normal;

out vec4 fragmentColor;

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation att;
};

struct Material
{
	vec4 color;
	int useColor;
	int useTexture;
	int useLight;
	float reflectance;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform vec3 cameraPos;

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
    vec4 diffuseColor = vec4(0.0, 0.0, 0.0, 0.0);
    vec4 specColor = vec4(0.0, 0.0, 0.0, 0.0);

    vec3 lightDirection = light.position - position;
    vec3 toLightSource  = normalize(lightDirection);
    float diffuseFactor = max(dot(normal, toLightSource), 0.0);
    diffuseColor = vec4(light.color, 1.0) * light.intensity * diffuseFactor;

    vec3 cameraDirection = normalize(cameraPos - position);
    vec3 fromLightSource = -toLightSource;
    vec3 reflectedLight = normalize(reflect(fromLightSource, normal));
    float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColor = specularFactor * material.reflectance * vec4(light.color, 1.0);

    float dist = length(lightDirection);
    float attenuationInv = light.att.constant + light.att.linear * dist + light.att.exponent * dist * dist;
    return (diffuseColor + specColor) / attenuationInv;
}

void main()
{
    vec4 baseColor = vec4(0.0, 0.0, 0.0, 0.0);
    if(material.useColor == 1) { baseColor += material.color; }

    if(material.useTexture == 1)
    {
        vec4 texture = texture(textureSampler, out_texCoord);
        if(texture.w < 0.5) { discard; }
        baseColor += texture;
    }

    if(material.useLight == 1)
    {
        vec4 totalLight = vec4(ambientLight, 1.0) + calcPointLight(pointLight, out_vertex, out_normal);
        fragmentColor = baseColor * totalLight;
    } else { fragmentColor = baseColor; }
}