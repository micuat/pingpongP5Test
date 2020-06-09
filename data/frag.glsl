#ifdef GL_ES
precision highp float;
#endif

// Type of shader expected by Processing
#define PROCESSING_TEXTURE_SHADER

// Processing specific input
uniform sampler2D texture;
uniform float time;
uniform vec2 resolution;
uniform vec2 mouse;

varying vec4 vertColor;
varying vec4 vertTexCoord;

//-------------------------------------------------------------------------------

uniform vec2		pixel;	// Size of a pixel in [0,0]-[1,0]
uniform sampler2D	tex;	// Refraction texture (the image to be warped)


void main( void )
{
	vec2 uv = vertTexCoord.st; // Texture coordinates

	// Calculate refraction
	vec4 col = texture2D( texture, uv );
	float x = col.r - 0.5;
	float y = col.g - 0.5;

	// Sample the texture from the target position
	gl_FragColor	= mix(col, texture2D( tex, uv + vec2( x, y ) * 0.02 ), 0.15);
}
