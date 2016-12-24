#version 330

layout (location = 0) in vec3 position;

void main() {
	// Set the position for each vertex
	gl_Position = vec4(position, 1.0);
}