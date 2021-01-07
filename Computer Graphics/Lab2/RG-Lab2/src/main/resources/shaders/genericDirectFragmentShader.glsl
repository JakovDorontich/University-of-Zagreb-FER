#version 400

struct SunLight {
	vec3 color;
	float intensity;
	vec3 direction;
};

in vec2 texCoord0;
in vec3 normal0;

in vec3 worldPos0;

uniform SunLight sunLight;

uniform vec3 cameraPosition;

uniform sampler2D diffuse;
uniform vec3 color;
uniform vec3 specularIntensity;
uniform float specularPower;


out vec4 fragColor;

vec4 calcLight(vec3 normal, vec3 worldPos);

void main() {

	vec4 textureColor = texture2D(diffuse, texCoord0);
	fragColor = textureColor * vec4(color, 1.0f);
	fragColor = fragColor * calcLight(normalize(normal0), worldPos0);	

}

vec4 calcLight(vec3 normal, vec3 worldPos) {

	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	
	float diffuseFactor = dot(normal, -sunLight.direction);
		
	if (diffuseFactor > 0) {
		
		diffuseColor = vec4(sunLight.color, 1.0) * sunLight.intensity * diffuseFactor;
		
		vec3 directionToEye = normalize(cameraPosition - worldPos);
		vec3 reflectDirection = normalize(reflect(sunLight.direction, normal));
		
		float specularFactor = dot(directionToEye, reflectDirection);
		specularFactor = pow(specularFactor, specularPower);
		
		if (specularFactor > 0) {
			specularColor = vec4(sunLight.color, 1.0) * vec4(specularIntensity, 1.0f) * specularFactor;
		}	
	}
	
	return diffuseColor + specularColor;
}
