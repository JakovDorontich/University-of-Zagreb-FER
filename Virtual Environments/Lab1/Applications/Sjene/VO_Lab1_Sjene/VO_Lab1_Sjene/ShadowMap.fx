//-----------------------------------------------------------------------------
// File: ShadowMap2.fx
//
// Desc: Effect file for high dynamic range cube mapping sample.
//
// Copyright (c) Microsoft Corporation. All rights reserved.
//-----------------------------------------------------------------------------

struct Light
{
    float3 pos;             // Light position in view space
	float3 dir;				// Light direction in view space
	float4 ambient;         // Use an ambient light of 0.3
	float4 diffuse;         // Light diffuse color
	float cosTheta;         // Cosine of theta of the spot light
};


//-----------------------------------------------------------------------------
// Globals.
//-----------------------------------------------------------------------------

float4x4 worldViewMatrix;
float4x4 projMatrix;
float4x4 lightViewProjMatrix;  // Transform from view space to light projection space

Light    light;

float4   material;
float    xMaxDepth;

//-----------------------------------------------------------------------------
// Textures.
//-----------------------------------------------------------------------------

texture  colorMapTexture;
texture  shadowMapTexture;

sampler2D colorMap =
sampler_state
{
    Texture = <colorMapTexture>;
    MinFilter = Point;
    MagFilter = Linear;
    MipFilter = Linear;
};




sampler2D shadowMap =
sampler_state
{
    Texture = <shadowMapTexture>;
    MinFilter = Point;
    MagFilter = Linear;
    MipFilter = Linear;    
    AddressU = Clamp;
    AddressV = Clamp;
};

//-----------------------------------------------------------------------------
// Shadow Map Vertex and Pixel Shaders definition
//-----------------------------------------------------------------------------

struct ShadowVertexInput
{
    float4 pos : POSITION;
    float3 normal : NORMAL;
};

struct ShadowVertexToPixel
{
    float4 pos : POSITION;
    float4 pos2D : TEXCOORD0;
};



//-----------------------------------------------------------------------------
// Vertex Shader: VertShadow
// Desc: Process vertex for the shadow map
//-----------------------------------------------------------------------------
ShadowVertexToPixel VertShadow( ShadowVertexInput IN)
{
    ShadowVertexToPixel OUT;
    //
    // Compute the projected coordinates
    //
    OUT.pos = mul( IN.pos, worldViewMatrix );
    OUT.pos = mul( OUT.pos, projMatrix );

    //
    // We need to pass the projected
    // 2D pixel coordinates to our pixel
    // shader. 
    //
    OUT.pos2D = OUT.pos;
    
    return OUT;
}



//-----------------------------------------------------------------------------
// Pixel Shader: PixShadow
// Desc: Process pixel for the shadow map
//-----------------------------------------------------------------------------
float4 PixShadow( ShadowVertexToPixel IN ) : COLOR
{
    //
    // Depth is z divided by maximum possible
    // depth because the HLSL values need
    // to be between [0,1]
    //
    return IN.pos2D.z / xMaxDepth;
}




//-----------------------------------------------------------------------------
// Scene Vertex and Pixel Shaders definition
//-----------------------------------------------------------------------------


struct SceneVertexInput
{
    float4 pos : POSITION;
    float3 normal : NORMAL;
    float2 texCoord : TEXCOORD0;
};

struct SceneVertexToPixel
{
    float4 pos : POSITION;
    float2 texCoord : TEXCOORD0;
    float4 viewPos : TEXCOORD1;
    float3 normal : TEXCOORD2;
    float4 viewPosLight : TEXCOORD3;
};



//-----------------------------------------------------------------------------
// Vertex Shader: VertScene
// Desc: Process vertex for scene
//-----------------------------------------------------------------------------
SceneVertexToPixel VertScene(SceneVertexInput IN)
{

    SceneVertexToPixel OUT;
    //
    // Transform position to view space
    //
    OUT.viewPos = mul( IN.pos, worldViewMatrix );

    //
    // Transform to screen coord
    //
    OUT.pos = mul( OUT.viewPos, projMatrix );

    //
    // Compute view space normal
    //
    OUT.normal = mul( IN.normal, (float3x3)worldViewMatrix );

    //
    // Propagate texture coord
    //
    OUT.texCoord = IN.texCoord;

    //
    // Transform the position to light projection space, or the
    // projection space as if the camera is looking out from
    // the spotlight.
    //
    OUT.viewPosLight = mul( OUT.viewPos, lightViewProjMatrix );
    
    return OUT;
}


//-----------------------------------------------------------------------------
// Pixel Shader: PixScene
// Desc: Process pixel (do per-pixel lighting) for enabled scene
//-----------------------------------------------------------------------------
float4 PixScene(SceneVertexToPixel IN) : COLOR
{
    float4 color;

    // posLightDir is the unit vector from the light to this pixel
    float3 posLightDir = normalize( float3( IN.viewPos - light.pos ) );

    // Provjera da li se pixel unutar osvijetljenog dijela
    if( dot( posLightDir, light.dir ) > light.cosTheta ) 
    {
        float3 n = normalize(IN.normal);
        float nDotL = dot( -posLightDir,n);

	// ***********************************************************\\
        //  OVO BI TREBALO IMPLEMENTIRATI:
        
        //-----------------------------------------------------------
        // 1.) Da bi se ucitala dubina, prvo je potrebno x i y komponente
	// viewPosLight vektora svesti na normaliziranu vrijednost dijeljenjem sa .w
	// komponentom te translacijom tako dobivenih vrijednosti 
        // sa intervala [-1,1] na [0,1]. Buduæi da je y-os u teksturi
	// obrnuto orijentirana od y-osi u prostoru potrebno je napraviti
	// invertiranje y komponente (oduzeti od 1.0)
        //-----------------------------------------------------------
        



        
        //-----------------------------------------------------------
        // 2.) Da bi se ostvario efekt ShadowMappinga, potrebno je 
        // ucitati spremljenu dubinu iz shadowMap teksture (x komponenta).
        // Koristite funkciju: tex2D(texture_sampler, coordinate).
        //-----------------------------------------------------------



        
        //-----------------------------------------------------------
        // 3.) Dobivenu vrijednost dubine usporedite sa udaljenosti
        // trenutne tocke. Vidi izraz u uputama za vježbu (4.2.).
        // Ako su vrijednosti (priblizno) iste, tocka se nalazi na
        // svjetlu te ju je potrebno u potpunosti osvijetliti.
        // U suprotnom sluèaju, potrebno ju je osvijetliti samo
        // ambijentalnim svjetlom.
        //-----------------------------------------------------------






        
        
        //-----------------------------------------------------------
        // 4.) Ovako se ostvaruje potpuno osvijetljavanje toèke (bez sjene).
        // Ovaj izracun mozete iskoristiti kod konstruiranja prethodnog izraza,
        // ali ga kasnije trebate zakomentirati kako bi se prikazale sjene.
        //-----------------------------------------------------------
        color =(light.ambient + light.diffuse * nDotL) * material;
        
        // ***********************************************************\\

    } else
    {
        color = light.ambient * material;
    }

    // umnozak doprinosa svjetla i boje teksture
    return  color * tex2D( colorMap, IN.texCoord);    
}



//-----------------------------------------------------------------------------
// Light Vertex and Pixel Shaders definition
//-----------------------------------------------------------------------------

struct LightVertexInput
{
    float4 pos : POSITION;
    float3 normal : NORMAL;
    float2 texCoord : TEXCOORD0;
};

struct LightVertexToPixel
{
    float4 pos : POSITION;
    float2 texCoord : TEXCOORD0;
};


//-----------------------------------------------------------------------------
// Vertex Shader: VertLight
// Desc: Process vertex for the light object
//-----------------------------------------------------------------------------
LightVertexToPixel VertLight(LightVertexInput IN )
{
    LightVertexToPixel OUT;
    
    //
    // Transform position to view space
    //
    OUT.pos = mul( IN.pos, worldViewMatrix );

    //
    // Transform to screen coord
    //
    OUT.pos = mul( OUT.pos, projMatrix );

    //
    // Propagate texture coord
    //
    OUT.texCoord = IN.texCoord;
    
    return OUT;
}


//-----------------------------------------------------------------------------
// Pixel Shader: PixLight
// Desc: Process pixel for the light object
//-----------------------------------------------------------------------------
float4 PixLight( LightVertexToPixel IN ) : COLOR
{
    return tex2D( colorMap, IN.texCoord );
}


//-----------------------------------------------------------------------------
// Technique: RenderScene
// Desc: Renders scene objects
//-----------------------------------------------------------------------------
technique RenderScene
{
    pass p0
    {
        VertexShader = compile vs_2_0 VertScene();
        PixelShader = compile ps_2_0 PixScene();
    }
}




//-----------------------------------------------------------------------------
// Technique: RenderLight
// Desc: Renders the light object
//-----------------------------------------------------------------------------
technique RenderLight
{
    pass p0
    {
        VertexShader = compile vs_2_0 VertLight();
        PixelShader = compile ps_2_0 PixLight();
    }
}




//-----------------------------------------------------------------------------
// Technique: RenderShadow
// Desc: Renders the shadow map
//-----------------------------------------------------------------------------
technique RenderShadow
{
    pass p0
    {
        VertexShader = compile vs_2_0 VertShadow();
        PixelShader = compile ps_2_0 PixShadow();
    }
}
