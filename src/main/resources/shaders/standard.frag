#version 330

const int MAX_LIGHTS = 4;
const float SHININESS_FACTOR = 128;

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

uniform sampler2D mainTexture;
uniform vec3 color;
uniform bool useTexture;

// Lighting uniforms
uniform vec3 ambientLight;
uniform DirectionalLight directionalLight; // the directional light (our sun)
uniform PointLight pointLights[MAX_LIGHTS]; // a point light in our scene
uniform Attenuation attenuation; // the attenuation constants for our point lights
uniform float shininess; // how shiny something is on scale of [0-1]
uniform vec3 specularColor; // color of the shininess

// Calculates the light diffuse which is a float that represents
// how bright a vertex is by comparing the direction of the vertex normal
// with the direction of the light vector
float calcDiffuse(vec3 normalizedLightVector, vec3 normalVec) {
	// Dot product returns between [-1 (pointing opposite light vector) and 1 (pointing in same direction)]
	highp float lightDiff = dot(normalVec, normalizedLightVector);
	
	// If the normal is pointing > 90 degrees away from the light then
	// it will be less than 0, but really we just want 0 to say it wont
	// be any brighter
	return max(0.0, lightDiff);
}

// blinn phong model
// Calculates the light specular, which is the amount of light that is reflected in smooth or metallic surfaces
float calcSpecular(vec3 normalizedLightVector, vec3 worldViewNormal, vec3 worldViewPosition) {
	vec3 cameraDirection = normalize(-worldViewPosition);
	// Compute the halfway vector for an approximation to the phong model
	vec3 halfwayVec = normalize(normalizedLightVector + cameraDirection);
	// [0 to 1], 1 means we are looking directly at the light
	float specAngle = max(dot(worldViewNormal, halfwayVec), 0);
		
	// The actual specular component value
	return pow(specAngle, shininess * SHININESS_FACTOR);
}

// Calculates the final value of a light source combining its color, intensity and calculated diffuse & specular
vec4 calcLightComponents(vec3 normalizedLightVector, vec3 lightColor, float intensity, vec3 worldViewNormal, vec3 worldViewPosition) {
		// Calculates diffuse and specular based on base light color
		vec4 diffuse = vec4(lightColor, 1.0) * intensity * calcDiffuse(normalizedLightVector, worldViewNormal);
		vec4 specular = vec4(specularColor, 1.0) * intensity * calcSpecular(normalizedLightVector, worldViewNormal, worldViewPosition);
		return diffuse + specular;
}

// Calculates the total value of lighting to be applied per vertex
vec4 calcAppliedLighting(vec3 ambientLight, DirectionalLight directionalLight, PointLight pointLights[MAX_LIGHTS], vec3 worldViewNormal, vec3 worldViewPosition) {
	vec4 appliedLighting = vec4(ambientLight, 1.0);
	
	// Calculate directional light
	if (directionalLight.intensity > 0) {
		// We don't need to subtract position for directional light because directional light is everywhere, all we care about is its direction
		appliedLighting += calcLightComponents(-directionalLight.direction, directionalLight.color, directionalLight.intensity, worldViewNormal, worldViewPosition);
	} 
	
	// Calculate each point light (includes spot lights)
	for (int i = 0; i < MAX_LIGHTS; i++) {
		PointLight pointLight = pointLights[i];
		// This should probably be controlled on CPU not GPU
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
		
		// Calculate the light color based on diffuse/specular
		vec4 pLightColor = calcLightComponents(normalDistance, pointLight.color, pointLight.intensity, worldViewNormal, worldViewPosition);
		
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
		baseColor = texture(mainTexture, pass_textureCoords);
	} else {
		// Set each fragments color
		baseColor = vec4(color, 1.0);
	}
	
	// The last pixel color contains either the base texture or color + any applied lighting
	out_color = baseColor * calcAppliedLighting(ambientLight, directionalLight, pointLights, pass_viewSpaceNormals, pass_viewSpacePosition);
}