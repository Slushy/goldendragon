#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normals;

out vec2 pass_textureCoords;
out vec4 pass_light;

// Represents constants for light intensity over distance
struct Attenuation {
	float constant;
	float quadratic;
};

// Represents a point light in our scene
struct PointLight {
	vec3 color;
	float intensity;
	vec3 position;
	float range;
};

// Represents a directional light in our scene
struct DirectionalLight {
    vec3 color;
    float intensity;
    vec3 direction;
};

// Matrix uniforms
uniform mat4 projectionMatrix; // Matrix representing camera FOV and clipping planes
uniform mat4 worldViewMatrix;  // Matrix representing current object transformation in relation to camera position

// Lighting uniforms
uniform vec3 ambientLight;
uniform DirectionalLight directionalLight; // the directional light (our sun)
uniform PointLight pointLight; // a point light in our scene
uniform Attenuation attenuation; // the attenuation constants for our point lights

// Calculates the directional light diffuse which is a float that represents
// how bright a vertex is by comparing the direction of the vertex normal
// with the direction of the directional light
float calcDiffuse(vec3 normalizedLightVector, vec3 normalVec) {
	// Dot product returns between [-1 (pointing opposite light vector) and 1 (pointing in same direction)]
	float lightDiff = dot(normalizedLightVector, normalVec);
	
	// If the normal is pointing > 90 degrees away from the light then
	// it will be less than 0, but really we just want 0 to say it wont
	// be any brighter
	return max(0, lightDiff);
}

// Calculates the final value of a light source combining its color, intensity and calculated diffuse
vec4 calcLightComponents(vec3 normalizedLightVector, vec3 lightColor, float intensity, vec3 worldViewNormals) {
		// Calculates diffuse for a light source	
		float diffuse = calcDiffuse(normalizedLightVector, worldViewNormals);
		
		// Returns the final light value
		return vec4(lightColor, 1.0) * intensity * diffuse;
}

// Calculates the total value of lighting to be applied per vertex
vec4 calcAppliedLighting(vec3 ambientLight, DirectionalLight directionalLight, PointLight pointLight, vec3 worldViewNormals, vec3 worldViewPosition) {
	vec4 appliedLighting = vec4(ambientLight, 1.0);
	
	// Calculate directional light
	if (directionalLight.intensity > 0) {
		// We don't need to subtract position for directional light becaue directional light is everywhere, all we care about is its direction
		appliedLighting += calcLightComponents(directionalLight.direction, directionalLight.color, directionalLight.intensity, worldViewNormals);
	} 
	
	// Calculate point light
	if (pointLight.intensity > 0 && pointLight.range > 0) {
		vec3 distanceToLight = pointLight.position - worldViewPosition;
		vec4 pLightColor = calcLightComponents(normalize(distanceToLight), pointLight.color, pointLight.intensity, worldViewNormals);
		
		// Apply attenuation (brightness factor based on distance)
		// att(r) = 1 / (c + q*r*r)
		
		// Object is in range if r is in [0, 1]
		float r = length(distanceToLight) / pointLight.range;
		
		// Only apply lighting if the object is in range of the point lights range (radius)
		if (r <= 1) {
			float att = attenuation.constant + attenuation.quadratic*r*r;
			appliedLighting += (pLightColor / att);
		}
	}
	
	// Returns the total applied lighting
	return appliedLighting;
}

void main() {
	// The position in view space
	vec4 worldViewPosition = worldViewMatrix * vec4(position, 1.0);
	
	// Set the position for each vertex 
	// ORDER MATTERS - projection must be first
	gl_Position = projectionMatrix * worldViewPosition;
	
	// Pass along our texture coordinates to our fragment shader
	pass_textureCoords = textureCoords;
	
	// Before we compare the light diffuse against our normals, we must bring them into
	// world view space
	vec3 worldViewNormals = normalize(worldViewMatrix * vec4(normals, 0)).xyz;
	
	// The total applied light value is the ambient light + directional light + point light
	pass_light = calcAppliedLighting(ambientLight, directionalLight, pointLight, worldViewNormals, worldViewPosition.xyz);
}