#version 330

const int MAX_LIGHTS = 4;

in vec2 pass_textureCoords;
in vec3 pass_viewSpaceNormals;
in vec3 pass_viewSpacePosition;

out vec4 out_color;

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
	// Spotlight specific
	vec3 direction;
	float cosHalfAngle;
	bool isSpot;
};

// Represents a directional light in our scene
struct DirectionalLight {
    vec3 color;
    float intensity;
    vec3 direction;
};

uniform sampler2D modelTexture;
uniform vec3 color;
uniform bool useTexture;

// Lighting uniforms
uniform vec3 ambientLight;
uniform DirectionalLight directionalLight; // the directional light (our sun)
uniform PointLight pointLights[MAX_LIGHTS]; // a point light in our scene
uniform Attenuation attenuation; // the attenuation constants for our point lights

// Calculates the light diffuse which is a float that represents
// how bright a vertex is by comparing the direction of the vertex normal
// with the direction of the light vector
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
vec4 calcAppliedLighting(vec3 ambientLight, DirectionalLight directionalLight, PointLight pointLights[MAX_LIGHTS], vec3 worldViewNormals, vec3 worldViewPosition) {
	vec4 appliedLighting = vec4(ambientLight, 1.0);
	
	// Calculate directional light
	if (directionalLight.intensity > 0) {
		// We don't need to subtract position for directional light becaue directional light is everywhere, all we care about is its direction
		appliedLighting += calcLightComponents(directionalLight.direction, directionalLight.color, directionalLight.intensity, worldViewNormals);
	} 
	
	// Calculate each point light (includes spot lights)
	for (int i = 0; i < MAX_LIGHTS; i++) {
		PointLight pointLight = pointLights[i];
		if (pointLight.intensity <= 0 || pointLight.range <= 0) {
			continue;
		}
		
		vec3 distanceToLight = pointLight.position - worldViewPosition;
		vec3 normalDistance = normalize(distanceToLight);
		
		// We manipulate the att factor for spot lights
		float attFactor = 1;
		
		// We have a spot light
		if (pointLight.isSpot) {
			// we take the negative bc we want a positive number when the vertex is pointing against the spotlights pointer
			float angleDiff = dot(-normalDistance, normalize(pointLight.direction));
			
			// If the position of the vertex is not inside the view of the cone
			// then it wont be hit by any light
			if (angleDiff < pointLight.cosHalfAngle) {
				continue;
			}
			
			// Because we have a cone, the light is less intense towards the outer angles of the cone
			attFactor = 1 - (1 - angleDiff)/(1 - pointLight.cosHalfAngle);
		}
		
		vec4 pLightColor = calcLightComponents(normalDistance, pointLight.color, pointLight.intensity, worldViewNormals);
		
		// Apply attenuation (brightness factor based on distance)
		// att(r) = 1 / (c + q*r*r)
		
		// Object is in range if r is in [0, 1]
		float r = length(distanceToLight) / pointLight.range;
		
		// Only apply lighting if the object is in range of the point lights range (radius)
		if (r <= 1) {
			// Calculate the attenuation
			float att = attFactor / (attenuation.constant + attenuation.quadratic*r*r);
			
			// Add the light with the attenuation calculation
			appliedLighting += (pLightColor * att);
		}
	}
	
	// Returns the total applied lighting
	return appliedLighting;
}

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
	out_color = baseColor * calcAppliedLighting(ambientLight, directionalLight, pointLights, pass_viewSpaceNormals, pass_viewSpacePosition);
}