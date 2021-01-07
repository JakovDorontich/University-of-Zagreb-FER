//-----------------------------------------------------------------------------
// Copyright (c) 2007-2008 dhpoware. All Rights Reserved.
//
//-----------------------------------------------------------------------------

struct Light
{
	float3 dir;				// world space direction
	float4 ambient;
	float4 diffuse;
	float4 specular;
	float radius;           // applies to point and spot lights only
};

struct Material
{
	float4 ambient;
	float4 diffuse;
	float4 specular;
	float shininess;
};

//-----------------------------------------------------------------------------
// Globals.
//-----------------------------------------------------------------------------

float4x4 worldMatrix;
float4x4 worldInverseTransposeMatrix;
float4x4 worldViewProjectionMatrix;

float3 cameraPos;

Light light;
Material material;

//-----------------------------------------------------------------------------
// Textures.
//-----------------------------------------------------------------------------

texture colorMapTexture;
texture normalMapTexture;

sampler2D colorMap = sampler_state
{
	Texture = <colorMapTexture>;
    MagFilter = Linear;
    MinFilter = Anisotropic;
    MipFilter = Linear;
    MaxAnisotropy = 16;
};

sampler2D normalMap = sampler_state
{
    Texture = <normalMapTexture>;
    MagFilter = Linear;
    MinFilter = Anisotropic;
    MipFilter = Linear;
    MaxAnisotropy = 16;
};

//-----------------------------------------------------------------------------
// Vertex and Pixel Shaders definition
//-----------------------------------------------------------------------------

struct VertexInput
{
	float3 position : POSITION;
	float2 texCoord : TEXCOORD0;
	float3 normal : NORMAL;
	float4 tangent : TANGENT;
};

struct VertexToPixel
{
	float4 position : POSITION;
	float2 texCoord : TEXCOORD0;
	float3 viewDir : TEXCOORD1;
	float3 lightDir : TEXCOORD2;
	float3 normal : TEXCOORD3;
};

//-----------------------------------------------------------------------------
// Vertex Shader
//-----------------------------------------------------------------------------

VertexToPixel Vert(VertexInput IN)
{
	VertexToPixel OUT;
    
    //pozicija vrha u koordinatnom sustavu svijeta   
	float3 worldPos = mul(float4(IN.position, 1.0f), worldMatrix).xyz;
	
	//vektor pogleda
	float3 viewDir = cameraPos - worldPos;
	
	//vektor svijetla
	float3 lightDir = (cameraPos - worldPos) / light.radius;
    
    // normala, tanget i binormal vektori   
    float3 n = mul(IN.normal, (float3x3)worldInverseTransposeMatrix);
	float3 t = mul(IN.tangent.xyz, (float3x3)worldInverseTransposeMatrix);
	float3 b = cross(n, t) * IN.tangent.w;
	
	// iz kojih se raèuna matrica tronsformacije iz
	// svijetskih koordinata u koordinate
	// tangetnog prostora vrha
	float3x3 tbnMatrix = float3x3(t.x, b.x, n.x,
	                              t.y, b.y, n.y,
	                              t.z, b.z, n.z);
			
	// predavanje izlazu transformiranih koordinata, normala i sl.
	OUT.position = mul(float4(IN.position, 1.0f), worldViewProjectionMatrix);
	OUT.texCoord = IN.texCoord;
	OUT.normal = mul(float4(IN.normal,1.0f), worldViewProjectionMatrix);
	OUT.viewDir = mul(viewDir, tbnMatrix);
	OUT.lightDir = mul(lightDir, tbnMatrix);
	
	return OUT;
}


//-----------------------------------------------------------------------------
// Pixel Shader
//-----------------------------------------------------------------------------

float4 Pix(VertexToPixel IN) : COLOR
{
	// ***********************************************************\\
        //  OVO BI TREBALO IMPLEMENTIRATI:

        //----------------------------------------------------------------
        // 1.) Izracunatu normalu (dobivenu interpolacijom izmeðu
	// vrijednosti normala s obližnjih vrhova) zamijenite s vrijednosti
	// iz teksture normalMap dobivene korištenjem funkcije
	//	tex2D(texture_sampler, coordinate)
	// Dobivena vrijednost je u intervalu vrijednosti [0,1] pa ju treba
	// transformirati u interval [-1,-1] te normalizirati funkcijom
	// normalize()
        //---------------------------------------------------------------
    
	float3 n = normalize(2 * tex2D(normalMap, IN.texCoord) + float3(-1, -1, -1));

	  

        //---------------------------------------------------------------
	// 2.) Poluvektor h izraèunajte pomocu normaliziranih vektora 
	// smjera izvora svijetlosti (lightDir) i vektora smjera gledanja
	// (viewDir) koji su sadržani u ulaznoj varijabli IN.
	// Dobiveni vektor takodjer normalizirajte.
	//---------------------------------------------------------------


    	float3 l = normalize(IN.lightDir);
    	float3 v = normalize(IN.viewDir);
		float3 h = normalize(IN.lightDir + IN.viewDir);


	//----------------------------------------------------------------
	// Izraèun velicine difuzne komponente
	//	(vidi izraz u uputama, poglavlje (2.1))
	//----------------------------------------------------------------

	///float nDotL = dot(n, l);
	float nDotL = max(dot(n, l), 0);

	//----------------------------------------------------------------
	// 3.) Izraèunajte velicinu reflektirajuce komponente tako da potencirate
	// (funkcijom pow(x,y)) skalarni umnozak normale i izracunati
	// poluvektor h.
	//----------------------------------------------------------------

	float nSpecL = pow(dot(n, h), material.shininess);
    

	//-----------------------------------------------------------
        // 4.) UKUPNI EFEKT OSVJETLJENJA cine ambijentalna, difuzna i 
	// reflektirajuca komponenta (koju jos treba dodati)
        //-----------------------------------------------------------
    	float4 color = (material.ambient * light.ambient) +

					(material.specular * light.specular * nSpecL)

                  + (material.diffuse * light.diffuse * nDotL );
                   
    

    	// umnozak doprinosa svijetla i boje teksture                
	return color * tex2D(colorMap, IN.texCoord);
}

//-----------------------------------------------------------------------------
// Techniques.
//-----------------------------------------------------------------------------


technique NormalMapping
{
    pass
    {
        VertexShader = compile vs_2_0 Vert();
        PixelShader = compile ps_2_0 Pix();
    }
}
