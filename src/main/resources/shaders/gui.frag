#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform sampler2D mainTexture;
uniform vec3 color;
uniform bool useTexture;

void main() {
	vec4 baseColor;
	
	if (useTexture) {
		// Set the texture for the pixel
		baseColor = texture(mainTexture, pass_textureCoords);
	} else {
		// Set each fragments color
		baseColor = vec4(color, 1.0);
	}
	
	// The last pixel color is just the base color for right now
	out_color = baseColor;
}