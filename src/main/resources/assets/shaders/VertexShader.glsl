#version 330

layout(location = 0) in vec3 in_pos;
layout(location = 1) in vec2 in_texCoord;
layout(location = 2) in vec3 in_normal;

out vec2 out_texCoord;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main()
{
	gl_Position = projectionMatrix * modelViewMatrix * vec4(in_pos, 1.0);
	//gl_Position = vec4(in_pos, 1.0f);
	//gl_Position = projectionMatrix * vec4(in_pos, 1.0f);
	//gl_Position = modelViewMatrix * vec4(in_pos, 1.0);
	out_texCoord = in_texCoord;
}