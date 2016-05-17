#version 330

in  vec2 out_texCoord;

out vec4 fragmentColor;

uniform sampler2D textureSampler;
uniform vec3 color;
uniform int useColor;

void main()
{
	if(useColor == 1)
	{
		fragmentColor = vec4(color, 1.0f);
	} else
	{
		fragmentColor = texture(textureSampler, out_texCoord);
	}
}