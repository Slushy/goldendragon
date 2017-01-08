#version 330

in vec2 pass_textureCoords;
in vec4 pass_light;

out vec4 out_color;

uniform sampler2D modelTexture;
uniform vec3 color;
uniform bool useTexture;

void main() {
	vec4 baseColor;
	
	if (useTexture) {
		// Set the texture for the pixel
		baseColor = texture(modelTexture, pass_textureCoords);
	} else {
		// Set each fragments color
		baseColor = vec4(color, 1.0);
	}
	
	// The last pixel color contains either the base texture or color + any applied lighting
	out_color = baseColor * pass_light;
}