#version 400

out vec4 fragColor;

in vec2 textureCoords1;
in vec2 textureCoords2;
in float blend;

uniform sampler2D particleTexture;

void main(void){

	vec4 color1 = texture(particleTexture, textureCoords1);
	vec4 color2 = texture(particleTexture, textureCoords2);
	fragColor = mix(color1, color2, blend);

}