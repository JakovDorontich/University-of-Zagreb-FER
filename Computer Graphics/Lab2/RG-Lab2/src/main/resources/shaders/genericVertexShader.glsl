#version 400

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec3 tangent;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 texCoord0;
out vec3 normal0;
out vec3 worldPos0;
out vec3 tangent0;

out mat3 toTangentSpace;

const int NUM_SHADOW_CASCADES = 3;

uniform mat4 lightViewMatrix[NUM_SHADOW_CASCADES];;
uniform mat4 orthoProjectionMatrix[NUM_SHADOW_CASCADES];

out vec4 mlightviewVertexPos[NUM_SHADOW_CASCADES];
out float clipSpacePosZ;

uniform bool hasFog;
uniform float fogDensity;
uniform float fogGradient;

out float fogVisibility;

void main() {

	texCoord0 = texCoord;
	
	normal0 = (model * vec4(normal, 0.0f)).xyz;
	tangent0 = (model * vec4(tangent, 0.0f)).xyz;;

	mat4 MVP = projection * view * model;
	gl_Position = MVP * vec4(position.xyz, 1.0f);
	
	worldPos0 = (model * vec4(position, 1.0f)).xyz;


	vec3 norm = normalize(normal0);
	vec3 tang = normalize(tangent0);
	vec3 bitang = cross(tang, norm);
	
	tang = normalize(tang - dot(tang, norm) * norm);
	
	toTangentSpace = mat3(
		tang, bitang, norm
	);
	
	for (int i = 0 ; i < NUM_SHADOW_CASCADES; i++) {
		mlightviewVertexPos[i] = orthoProjectionMatrix[i] * lightViewMatrix[i] * model * vec4(position, 1.0);
	}
	
	
	clipSpacePosZ = gl_Position.z;
	//clipSpacePosZ = (view * model * vec4(position.xyz, 1.0f)).z;
	
	//fog computation
	if (hasFog) {
		vec4 positionToCam = view * model * vec4(position, 1.0);
		float distance = length(positionToCam.xyz);
		fogVisibility = exp(-pow((distance * fogDensity), fogGradient));
	} else {
		fogVisibility = 1;
	}
	
}
