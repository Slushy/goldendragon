#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 projectionMatrix; // Matrix representing camera FOV and clipping planes
uniform mat4 worldViewMatrix;  // Matrix representing current object transformation in relation to camera position

void main() {
	// Set the position for each vertex 
	// ORDER MATTERS - projection must be first
	gl_Position = projectionMatrix * worldViewMatrix * vec4(position, 1.0);
	
	// Pass along our texture coordinates to our fragment shader
	pass_textureCoords = textureCoords;
}