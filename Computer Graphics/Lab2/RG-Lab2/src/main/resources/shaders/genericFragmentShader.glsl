#version 400

out vec4 fragColor;

struct BaseLight {
	vec3 color;
	float intensity;
};

struct SunLight {
	BaseLight base;
	vec3 direction;
};


struct Attenuation {
	float constant;
	float linear;
	float exponent;
};


struct PointLight {
	BaseLight base;
	Attenuation atten;
	vec3 position;
	float range;
};


struct SpotLight {
	PointLight point;
	float cutOff;
	vec3 coneDirection;
};


in vec2 texCoord0;
in vec3 normal0;
in vec3 tangent0;

in vec3 worldPos0;

in mat3 toTangentSpace;

//LIGHTS
const int MAX_DIRECTIONAL_LIGHTS = 5;
const int MAX_POINT_LIGHTS = 20;
const int MAX_SPOT_LIGHTS = 20;


uniform vec3 ambientLight;
uniform SunLight sunLight;

uniform PointLight pointLight[MAX_POINT_LIGHTS];
uniform int currentPointLights;

uniform SpotLight spotLight[MAX_SPOT_LIGHTS];
uniform int currentSpotLights;


uniform vec3 cameraPosition;

uniform sampler2D diffuse;
uniform sampler2D normalMap;

const int NUM_SHADOW_CASCADES = 3;

uniform int hasShadow;
uniform sampler2D shadowMap[NUM_SHADOW_CASCADES];
uniform float cascadeEndClipSpace[NUM_SHADOW_CASCADES];

in vec4 mlightviewVertexPos[NUM_SHADOW_CASCADES];
in float clipSpacePosZ;

uniform vec3 color;
uniform vec3 specularIntensity;
uniform float specularPower;


vec3 normalMapColor;

vec4 calcLight(vec3 normal, vec3 worldPos);
vec4 calcLight(BaseLight base, vec3 direction, vec3 normal, vec3 worldPos);
vec4 calcPointLight(PointLight pointLight, vec3 normal, vec3 worldPos);
float calcShadow(int cascadeIndex, vec4 position);

in float fogVisibility;
uniform vec3 fogColor;

void main() {

	normalMapColor = normalize(toTangentSpace * (255.0/128.0 * texture2D(normalMap, texCoord0) - 1.0f).xyz);

	vec4 textureColor = texture2D(diffuse, texCoord0);
	
	
	if (textureColor.w < 0.2) {
		discard;
	}
	
	fragColor = textureColor * vec4(color, 1.0f);
	
	
	float shadow = 1;

	if (hasShadow == 1) {
	
		for (int i = 0 ; i < NUM_SHADOW_CASCADES ; i++) {
	        if (clipSpacePosZ <= cascadeEndClipSpace[i]) {
	            shadow = calcShadow(i, mlightviewVertexPos[i]);
				break;
	        }
		}

	}

	vec4 diffuseSpecLight = vec4(0.0);

	vec3 normalizedNormal = normalize(normalMapColor);
	
	//MAIN LIGHT - SUN
	diffuseSpecLight += calcLight(sunLight.base, sunLight.direction, normalizedNormal, worldPos0);
	
	//JUST SUN LIGHT CONSIDER THE SHADOW
	diffuseSpecLight *=  shadow;
	
	//ALL POINT LIGHTS
	for (int i = 0; i < currentPointLights; i++) {
		diffuseSpecLight += calcPointLight(pointLight[i], normalizedNormal, worldPos0);
	}
	
	
	//ALL SPOT LIGHTS
	for (int i = 0; i < currentSpotLights; i++) {
		vec3 lightDirection = normalize(worldPos0 - spotLight[i].point.position);
		float spotFactor = dot(lightDirection, spotLight[i].coneDirection);
		
		if (spotFactor >= spotLight[i].cutOff) {
			diffuseSpecLight += calcPointLight(spotLight[i].point, normalizedNormal, worldPos0);
		}
	}
	
	
	fragColor = fragColor * (vec4(ambientLight, 1.0f) + diffuseSpecLight);
	
	fragColor = mix(vec4(fogColor, 1.0), fragColor, fogVisibility); 
	
}


/*
vec4 calcLight(vec3 normal, vec3 worldPos) {

	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	
	vec3 direction = normalize(sunLight.direction);
	
	
	float diffuseFactor = max(dot(normal, direction), 0.0);
		
	if (diffuseFactor > 0) {
		
		diffuseColor = vec4(sunLight.color, 1.0) * sunLight.intensity * diffuseFactor;
		
		vec3 directionToEye = normalize(worldPos - cameraPosition);
		vec3 reflectDirection = normalize(reflect(direction, normal));
		
		float specularFactor = max(dot(directionToEye, reflectDirection), 0.0);
		specularFactor = pow(specularFactor, specularPower);
		
		if (specularFactor > 0) {
			specularColor = vec4(sunLight.color, 1.0) * vec4(specularIntensity, 1.0) * specularFactor;
		}	
	}
	return diffuseColor + specularColor;
}
*/


vec4 calcLight(BaseLight base, vec3 direction, vec3 normal, vec3 worldPos) {

	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	
	float diffuseFactor = dot(normal, -direction);
		
	if (diffuseFactor > 0) {
		
		diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
		
		vec3 directionToEye = normalize(cameraPosition - worldPos);
		vec3 reflectDirection = normalize(reflect(direction, normal));
		
		float specularFactor = dot(directionToEye, reflectDirection);
		specularFactor = pow(specularFactor, specularPower);
		
		if (specularFactor > 0) {
			specularColor = vec4(base.color, 1.0) * vec4(specularIntensity, 1.0f) * specularFactor * base.intensity;
		}	
	}
	
	return diffuseColor + specularColor;
}


vec4 calcPointLight(PointLight pointLight, vec3 normal, vec3 worldPos) {
	
	vec3 lightDirection = worldPos - pointLight.position;
	float distanceToPoint = length(lightDirection);

	if (distanceToPoint > pointLight.range)
		return vec4(0,0,0,0);
	
	lightDirection = normalize(lightDirection);
	
	vec4 color = calcLight(pointLight.base, lightDirection, normal, worldPos);
	
	float attenuation =   pointLight.atten.constant 
						+ pointLight.atten.linear * distanceToPoint 
						+ pointLight.atten.exponent * distanceToPoint * distanceToPoint
						+ 0.0001f;
						
	return color / attenuation;
}





//CALCULATES THE AMOUNT OF SHADOWS
float calcShadow(int cascadeIndex, vec4 position) {
    
    float bias = 0.0005f;
    vec3 projCoords = position.xyz / position.w;
   
    // Transform from screen coordinates to texture coordinates
    projCoords = projCoords * 0.5 + 0.5;
    
    float shadowFactor = 0.0;
	vec2 inc = 1.0 / textureSize(shadowMap[cascadeIndex], 0);
	
	
	int value = 1;
	for(int row = -value; row <= value+1; ++row) {
	
	    for(int col = -value; col <= value+1; ++col) {
			
			//vec2 shadowTextCoord = vec2(projCoords.x + (row - 0.5) * inc.x, (projCoords.y + (col -0.5) * inc.y) );
			//float textDepth = texture(shadowMap[cascadeIndex],  shadowTextCoord).r;
	        float textDepth = texture(shadowMap[cascadeIndex],  (projCoords.xy + vec2(row -0.5, col -0.5) * inc)).r; 
	        shadowFactor += projCoords.z - bias > textDepth ? 1.0 : 0.0;        
	
	    }    
	}
	
	//shadowFactor /= 25.0;
	shadowFactor /= (value * 2 + 2)*(value * 2 + 2);
	
	
	if(projCoords.z >= 1.0) {
       shadowFactor = 1.0;
    }

    return 1 - shadowFactor;
} 

