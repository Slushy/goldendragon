#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform sampler2D modelTexture;

uniform vec3 ambientLight;
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
	
	// Applies the ambient light to every pixel
	out_color = baseColor * vec4(ambientLight, 1.0);
}