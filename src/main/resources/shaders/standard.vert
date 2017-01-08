#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normals;

out vec2 pass_textureCoords;
out vec4 pass_light;

// Represents a directional light in our scene
struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

// Matrix uniforms
uniform mat4 projectionMatrix; // Matrix representing camera FOV and clipping planes
uniform mat4 worldViewMatrix;  // Matrix representing current object transformation in relation to camera position

// Lighting uniforms
uniform vec3 ambientLight;
uniform DirectionalLight directionalLight; // the directional light (our sun)
uniform bool hasDirectionalLight; // whether to use the directional light in our scene or not

// Calculates the directional light diffuse which is a float that reprsents
// how bright a vertex is by comparing the direction of the vertex normal
// with the direction of the directional light
float calcDiffuse(vec3 lightDirection, vec3 normalVec) {
	// Dot product returns between [-1 (pointing opposite light vector) and 1 (pointing in same direction)]
	float lightDiff = dot(lightDirection, normalVec);
	
	// If the normal is pointing > 90 degrees away from the light then
	// it will be less than 0, but really we just want 0 to say it wont
	// be any brighter
	return max(lightDiff, 0);
}


void main() {
	// Set the position for each vertex 
	// ORDER MATTERS - projection must be first
	gl_Position = projectionMatrix * worldViewMatrix * vec4(position, 1.0);
	
	// Pass along our texture coordinates to our fragment shader
	pass_textureCoords = textureCoords;
	
	// Calculate directional light if we have it
	vec4 calcDirectionalLight;
	if (hasDirectionalLight) {
		// Before we compare the diffuse against our normals, we must bring them into
		// world view space
		vec3 calcNormals = normalize(worldViewMatrix * vec4(normals, 0)).xyz;
	
		// Calc diffuse and set light components
		float diffuse = calcDiffuse(directionalLight.direction, calcNormals);
		calcDirectionalLight = vec4(directionalLight.color, 1.0) * directionalLight.intensity * diffuse;
	} 
	// Else set it to zero'd vector
	else {
		calcDirectionalLight = vec4(0, 0, 0, 0);
	}
	
	// The total applied light value is the ambient light + directional light
	pass_light = vec4(ambientLight, 1.0) + calcDirectionalLight;
}