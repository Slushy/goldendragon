#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normals;

out vec2 pass_textureCoords;
out vec3 pass_viewSpaceNormals;
out vec3 pass_viewSpacePosition;

// Matrix uniforms
uniform mat4 projectionMatrix; // Matrix representing camera FOV and clipping planes
uniform mat4 worldViewMatrix;  // Matrix representing current object transformation in relation to camera position

void main() {
	// The position in view space
	vec4 worldViewPosition = worldViewMatrix * vec4(position, 1.0);
	
	// Pass along our texture coordinates to our fragment shader
	pass_textureCoords = textureCoords;
	
	// Before we compare the light diffuse against our normals, we must bring them into
	// world view space
	pass_viewSpaceNormals = normalize(worldViewMatrix * vec4(normals, 0)).xyz;
	
	// Pass along our transformed position 
	pass_viewSpacePosition = worldViewPosition.xyz;
	
	// Set the position for each vertex 
	// ORDER MATTERS - projection must be first
	gl_Position = projectionMatrix * worldViewPosition;
}