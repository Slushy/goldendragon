#version 330

out vec4 frag_Color;

uniform vec3 color;

void main() {
	// Set each fragments color
	frag_Color = vec4(color, 1.0);
}