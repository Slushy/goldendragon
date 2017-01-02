#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform sampler2D modelTexture;

uniform vec3 color;
uniform bool useTexture;

void main() {
	if (useTexture) {
		// Set the texture for the pixel
		out_color = texture(modelTexture, pass_textureCoords);
	} else {
		// Set each fragments color
		out_color = vec4(color, 1.0);
	}
}