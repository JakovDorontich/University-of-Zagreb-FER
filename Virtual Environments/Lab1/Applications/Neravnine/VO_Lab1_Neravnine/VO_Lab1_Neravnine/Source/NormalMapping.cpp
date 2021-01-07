//--------------------------------------------------------------------------------------
// File: NormalMapping.cpp
//
//
// Copyright (c) Microsoft Corporation. All rights reserved.
//--------------------------------------------------------------------------------------
#include "DXUT.h"
#include "DXUTcamera.h"
#include "DXUTsettingsdlg.h"
#include "SDKmisc.h"
#include "resource.h"

// #define DEBUG_VS   // Uncomment this line to debug vertex shaders 
// #define DEBUG_PS   // Uncomment this line to debug pixel shaders 


//--------------------------------------------------------------------------------------
// Defines
//--------------------------------------------------------------------------------------
#define VERTS_PER_EDGE 64

#define ShadowMap_SIZE 512

const float LIGHT_RADIUS = 10.0f;
const float LIGHT_SPOT_INNER_CONE = D3DXToRadian(10.0f);
const float LIGHT_SPOT_OUTER_CONE = D3DXToRadian(30.0f);

//-----------------------------------------------------------------------------
// User Defined Types.
//-----------------------------------------------------------------------------

struct Vertex
{
    float pos[3];
    float texCoord[2];
    float normal[3];
    float tangent[4];
};

struct Light
{
    float dir[3];
    float ambient[4];
    float diffuse[4];
    float specular[4];
    float spotInnerCone;
    float spotOuterCone;
    float radius;
};

struct Material
{
    float ambient[4];
    float diffuse[4];
    float emissive[4];
    float specular[4];
    float shininess;
};

struct Triangle
{
    short first;
    short second;
    short third; 
};




//--------------------------------------------------------------------------------------
// Global variables
//--------------------------------------------------------------------------------------
ID3DXFont*                      g_pFont = NULL;         // Font for drawing text
ID3DXSprite*                    g_pTextSprite = NULL;   // Sprite for batching draw text calls
ID3DXEffect*                    g_pEffect = NULL;       // D3DX effect interface
IDirect3DTexture9*              g_pColorMap = NULL;     // Texture Color map
IDirect3DTexture9*              g_pNormalMap = NULL;    // Texture Normal map
IDirect3DTexture9*              g_pFloorMap = NULL;    // Texture map
IDirect3DTexture9*              g_pWallMap = NULL;    // Texture map
IDirect3DTexture9*              g_pCealingMap = NULL;    // Texture map
CModelViewerCamera              g_Camera;               // A model viewing camera
bool                            g_bShowHelp = true;     // If true, it renders the UI control text
bool                            g_bNormalMapping = true;// If true, normal mapping effect will be applied
CDXUTDialogResourceManager      g_DialogResourceManager; // manager for shared resources of dialogs
CD3DSettingsDlg                 g_SettingsDlg;          // Device settings dialog
CDXUTDialog                     g_HUD;                  // dialog for standard controls


// Scene
LPDIRECT3DVERTEXBUFFER9         g_pVB = NULL;
LPDIRECT3DINDEXBUFFER9          g_pIB = NULL;
DWORD                           g_dwNumVertices = VERTS_PER_EDGE * VERTS_PER_EDGE;
DWORD                           g_dwNumIndices = 6 * ( VERTS_PER_EDGE - 1 ) * ( VERTS_PER_EDGE - 1 );
LPDIRECT3DVERTEXSHADER9         g_pVertexShader = NULL;
LPD3DXCONSTANTTABLE             g_pConstantTable = NULL;
LPDIRECT3DVERTEXDECLARATION9    g_pVertexDeclaration = NULL;
float                           g_sceneAmbient[4] = {0.2f, 0.2f, 0.2f, 1.0f};

Vertex g_vertex[36+34] =
{
    // -Z face
    {-1.0f,  1.0f, -1.0f,   0.0f, 0.0f,   0.0f, 0.0f, -1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f,  1.0f, -1.0f,   1.0f, 0.0f,   0.0f, 0.0f, -1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f, -1.0f,   1.0f, 1.0f,   0.0f, 0.0f, -1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f, -1.0f,   1.0f, 1.0f,   0.0f, 0.0f, -1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f, -1.0f,   0.0f, 1.0f,   0.0f, 0.0f, -1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f,  1.0f, -1.0f,   0.0f, 0.0f,   0.0f, 0.0f, -1.0f,   0.0f, 0.0f, 0.0f, 0.0f },

    // +Z face
    { 1.0f,  1.0f,  1.0f,   0.0f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f,  1.0f,  1.0f,   1.0f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f,  1.0f,   1.0f, 1.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f,  1.0f,   1.0f, 1.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f,  1.0f,   0.0f, 1.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f,  1.0f,  1.0f,   0.0f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f, 0.0f, 0.0f },

    // -Y face
    {-1.0f, -1.0f, -1.0f,   0.0f, 0.0f,   0.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f, -1.0f,   1.0f, 0.0f,   0.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f,  1.0f,   1.0f, 1.0f,   0.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f,  1.0f,   1.0f, 1.0f,   0.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f,  1.0f,   0.0f, 1.0f,   0.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f, -1.0f,   0.0f, 0.0f,   0.0f, -1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },

    // +Y face
    {-1.0f,  1.0f,  1.0f,   0.0f, 0.0f,   0.0f, 1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f,  1.0f,  1.0f,   1.0f, 0.0f,   0.0f, 1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f,  1.0f, -1.0f,   1.0f, 1.0f,   0.0f, 1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f,  1.0f, -1.0f,   1.0f, 1.0f,   0.0f, 1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f,  1.0f, -1.0f,   0.0f, 1.0f,   0.0f, 1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f,  1.0f,  1.0f,   0.0f, 0.0f,   0.0f, 1.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },

    // -X face
    {-1.0f,  1.0f,  1.0f,   0.0f, 0.0f,   -1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f,  1.0f, -1.0f,   1.0f, 0.0f,   -1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f, -1.0f,   1.0f, 1.0f,   -1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f, -1.0f,   1.0f, 1.0f,   -1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f, -1.0f,  1.0f,   0.0f, 1.0f,   -1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    {-1.0f,  1.0f,  1.0f,   0.0f, 0.0f,   -1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },

    // +X face
    { 1.0f,  1.0f, -1.0f,   0.0f, 0.0f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f,  1.0f,  1.0f,   1.0f, 0.0f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f,  1.0f,   1.0f, 1.0f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f,  1.0f,   1.0f, 1.0f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f, -1.0f, -1.0f,   0.0f, 1.0f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 1.0f,  1.0f, -1.0f,   0.0f, 0.0f,   1.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    
    //scene vertexes
     { -12.0f,-7.0f,-12.0f,  0.0f,0.0f,  0.0f,1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,-7.0f,-12.0f,  0.0f,2.0f,  0.0f,1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    {-12.0f,-7.0f,12.0f,  2.0f,0.0f,  0.0f,1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,-7.0f,12.0f,  2.0f,2.0f,  0.0f,1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,-7.0f,-12.0f,  0.0f,1.0f,  1.0f,0.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,-7.0f,-12.0f,  6.0f,1.0f,  -0.707107f,0.0f,0.707107f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,6.0f,-12.0f,  0.0f,0.0f,  1.0f,0.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -8.0f,6.0f,-12.0f,  7.666667f,0.083333f,  0.0f,0.0f,1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 8.0f,6.0f,-12.0f,  6.333333f,0.083333f,  0.0f,0.0f,1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,6.0f,-12.0f,  6.0f,0.083333f,  -0.707107f,0.0f,0.707107f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -8.0f,7.0f,-12.0f,  7.666667f,0.0f,  0.0f,0.0f,1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 8.0f,7.0f,-12.0f,  6.333333f,0.0f,  0.0f,0.0f,1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,-7.0f,12.0f,  2.0f,1.0f,  0.894427f,0.0f,-0.447214f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,-7.0f,12.0f,  4.0f,1.0f,  -0.447214f,0.0f,-0.894427f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,6.0f,12.0f,  2.0f,0.083333f,  0.447214f,0.0f,-0.894427f,   0.0f, 0.0f, 0.0f, 0.0f},
    
    { -8.0f,6.0f,12.0f,  2.333333f,0.083333f,  0.0f,0.0f,-1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 8.0f,6.0f,12.0f,  3.666667f,0.083333f,  0.0f,0.0f,-1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,6.0f,12.0f,  4.0f,0.083333f,  -0.894427f,0.0f,-0.447214f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -8.0f,7.0f,12.0f,   2.333333f,0.0f,  0.0f,0.0f,-1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 8.0f,7.0f,12.0f,  3.666667f,0.0f,  0.0f,0.0f,-1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,-7.0f,-12.0f,  8.0f,1.0f,  0.0f,0.0f,1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,6.0f,-12.0f,  8.0f,0.083333f,  0.0f,0.0f,1.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,6.0f,-12.0f,  0.0f,0.0f,  0.0f,-1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -8.0f,6.0f,-12.0f,  0.333333f,0.0f,  0.124035f,-0.992278f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 8.0f,6.0f,-12.0f,  1.833333f,0.0f,  -0.447214f,-0.894427f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,6.0f,-12.0f,  2.166667f,0.0f,  0.0f,-1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -8.0f,7.0f,-12.0f,  0.416667f,0.0f,  0.124035f,-0.992278f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 8.0f,7.0f,-12.0f,  1.750000f,0.0f,  -0.031235f,-0.999512f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -12.0f,6.0f,12.0f,  0.0f,2.0f,  0.0f,-1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -8.0f,6.0f,12.0f,  0.333333f,2.0f,  0.447214f,-0.894427f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 8.0f,6.0f,12.0f,  1.833333f,2.0f,  -0.124035f,-0.992278f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { 12.0f,6.0f,12.0f,  2.166667f,2.0f,  0.0f,-1.0f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f},
    { -8.0f,7.0f,12.0f,  0.416667f,2.0f,  0.031235f,-0.999512f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f },
    { 8.0f,7.0f,12.0f,  1.75f,2.0f,  -0.124035f,-0.992278f,0.0f,   0.0f, 0.0f, 0.0f, 0.0f}
};

Triangle g_indices[24]=
{
 {0,2,1},
 {1,2,3},
 {20,5,21},
 {21,5,9},
 {7,8,10},
 {10,8,11},
 {4,6,12},
 {12,6,14},
 {12,14,13},
 {13,14,17},
 {15,18,16},
 {16,18,19},
 {13,17,5},
 {5,17,9},
 {22,23,28},
 {28,23,29},
 {23,26,29},
 {29,26,32},
 {26,27,32},
 {32,27,33},
 {27,24,33},
 {33,24,30},
 {24,25,30},
 {30,25,31}
};

Light g_light =
{
    0.0f, 0.0f, 1.0f,                               // dir
    1.0f, 1.0f, 1.0f, 0.0f,                         // ambient
    1.0f, 1.0f, 1.0f, 0.0f,                         // diffuse
    1.0f, 1.0f, 1.0f, 0.0f,                         // specular
    LIGHT_SPOT_INNER_CONE,                          // spotInnerCone
    LIGHT_SPOT_OUTER_CONE,                          // spotOuterCone
    LIGHT_RADIUS                                    // radius
};

Material g_material =
{
    0.2f, 0.2f, 0.2f, 1.0f,                         // ambient
    0.8f, 0.8f, 0.8f, 1.0f,                         // diffuse
    0.0f, 0.0f, 0.0f, 1.0f,                         // emissive
    0.7f, 0.7f, 0.7f, 1.0f,                         // specular
    90.0f                                           // shininess
};




//--------------------------------------------------------------------------------------
// UI control IDs
//--------------------------------------------------------------------------------------
#define IDC_TOGGLEFULLSCREEN    1
#define IDC_TOGGLEREF           3
#define IDC_CHANGEDEVICE        4



//--------------------------------------------------------------------------------------
// Forward declarations 
//--------------------------------------------------------------------------------------
bool CALLBACK IsDeviceAcceptable( D3DCAPS9* pCaps, D3DFORMAT AdapterFormat, D3DFORMAT BackBufferFormat, bool bWindowed,
                                  void* pUserContext );
bool CALLBACK ModifyDeviceSettings( DXUTDeviceSettings* pDeviceSettings, void* pUserContext );
HRESULT CALLBACK OnCreateDevice( IDirect3DDevice9* pd3dDevice, const D3DSURFACE_DESC* pBackBufferSurfaceDesc,
                                 void* pUserContext );
HRESULT CALLBACK OnResetDevice( IDirect3DDevice9* pd3dDevice, const D3DSURFACE_DESC* pBackBufferSurfaceDesc,
                                void* pUserContext );
void CALLBACK OnFrameMove( double fTime, float fElapsedTime, void* pUserContext );
void CALLBACK OnFrameRender( IDirect3DDevice9* pd3dDevice, double fTime, float fElapsedTime, void* pUserContext );
LRESULT CALLBACK MsgProc( HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam, bool* pbNoFurtherProcessing,
                          void* pUserContext );
void CALLBACK KeyboardProc( UINT nChar, bool bKeyDown, bool bAltDown, void* pUserContext );
void CALLBACK OnGUIEvent( UINT nEvent, int nControlID, CDXUTControl* pControl, void* pUserContext );
void CALLBACK OnLostDevice( void* pUserContext );
void CALLBACK OnDestroyDevice( void* pUserContext );

void InitApp();
void RenderText();
void CalcTangentVector(const float pos1[3], const float pos2[3],
                          const float pos3[3], const float texCoord1[2],
                          const float texCoord2[2], const float texCoord3[2],
                          const float normal[3], D3DXVECTOR4 &tangent);


//--------------------------------------------------------------------------------------
// Entry point to the program. Initializes everything and goes into a message processing 
// loop. Idle time is used to render the scene.
//--------------------------------------------------------------------------------------
INT WINAPI wWinMain( HINSTANCE, HINSTANCE, LPWSTR, int )
{
    // Enable run-time memory check for debug builds.
#if defined(DEBUG) | defined(_DEBUG)
    _CrtSetDbgFlag( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF );
#endif

    // Set the callback functions. These functions allow DXUT to notify
    // the application about device changes, user input, and windows messages.  The 
    // callbacks are optional so you need only set callbacks for events you're interested 
    // in. However, if you don't handle the device reset/lost callbacks then the sample 
    // framework won't be able to reset your device since the application must first 
    // release all device resources before resetting.  Likewise, if you don't handle the 
    // device created/destroyed callbacks then DXUT won't be able to 
    // recreate your device resources.
    DXUTSetCallbackD3D9DeviceAcceptable( IsDeviceAcceptable );
    DXUTSetCallbackD3D9DeviceCreated( OnCreateDevice );
    DXUTSetCallbackD3D9DeviceReset( OnResetDevice );
    DXUTSetCallbackD3D9FrameRender( OnFrameRender );
    DXUTSetCallbackD3D9DeviceLost( OnLostDevice );
    DXUTSetCallbackD3D9DeviceDestroyed( OnDestroyDevice );
    DXUTSetCallbackMsgProc( MsgProc );
    DXUTSetCallbackKeyboard( KeyboardProc );
    DXUTSetCallbackFrameMove( OnFrameMove );
    DXUTSetCallbackDeviceChanging( ModifyDeviceSettings );

    // Show the cursor and clip it when in full screen
    DXUTSetCursorSettings( true, true );

    InitApp();

    // Initialize DXUT and create the desired Win32 window and Direct3D 
    // device for the application. Calling each of these functions is optional, but they
    // allow you to set several options which control the behavior of the framework.
    DXUTInit( true, true ); // Parse the command line and show msgboxes
    DXUTSetHotkeyHandling( true, true, true );  // handle the defaul hotkeys
    DXUTCreateWindow( L"NormalMapping" );
    DXUTCreateDevice( true, 640, 480 );

    // Pass control to DXUT for handling the message pump and 
    // dispatching render calls. DXUT will call your FrameMove 
    // and FrameRender callback when there is idle time between handling window messages.
    DXUTMainLoop();

    // Perform any application-level cleanup here. Direct3D device resources are released within the
    // appropriate callback functions and therefore don't require any cleanup code here.

    return DXUTGetExitCode();
}


//--------------------------------------------------------------------------------------
// Initialize the app 
//--------------------------------------------------------------------------------------
void InitApp()
{
    // Initialize dialogs
    g_SettingsDlg.Init( &g_DialogResourceManager );
    g_HUD.Init( &g_DialogResourceManager );

    g_HUD.SetCallback( OnGUIEvent ); int iY = 10;
    //g_HUD.AddButton( IDC_TOGGLNORMALMAPPING, L"Toggle normal mapping", 35, iY, 125, 22 );
    //g_HUD.AddButton( IDC_TOGGLEFULLSCREEN, L"Toggle full screen", 35, iY, 125, 22 );
    //g_HUD.AddButton( IDC_TOGGLEREF, L"Toggle REF (F3)", 35, iY += 24, 125, 22 );
    //g_HUD.AddButton( IDC_CHANGEDEVICE, L"Change device (F2)", 35, iY += 24, 125, 22, VK_F2 );
    
    Vertex *pVertex1 = 0;
    Vertex *pVertex2 = 0;
    Vertex *pVertex3 = 0;
    D3DXVECTOR4 tangent;

    for (int i = 0; i < 36; i += 3)
    {
        pVertex1 = &g_vertex[i];
        pVertex2 = &g_vertex[i + 1];
        pVertex3 = &g_vertex[i + 2];

        CalcTangentVector(
            pVertex1->pos, pVertex2->pos, pVertex3->pos,
            pVertex1->texCoord, pVertex2->texCoord, pVertex3->texCoord,
            pVertex1->normal, tangent);

        pVertex1->tangent[0] = tangent.x;
        pVertex1->tangent[1] = tangent.y;
        pVertex1->tangent[2] = tangent.z;
        pVertex1->tangent[3] = tangent.w;
        
        pVertex2->tangent[0] = tangent.x;
        pVertex2->tangent[1] = tangent.y;
        pVertex2->tangent[2] = tangent.z;
        pVertex2->tangent[3] = tangent.w;
        
        pVertex3->tangent[0] = tangent.x;
        pVertex3->tangent[1] = tangent.y;
        pVertex3->tangent[2] = tangent.z;
        pVertex3->tangent[3] = tangent.w;
    }

}

void CalcTangentVector(const float pos1[3], const float pos2[3],
                       const float pos3[3], const float texCoord1[2],
                       const float texCoord2[2], const float texCoord3[2],
                       const float normal[3], D3DXVECTOR4 &tangent)
{
    // Given the 3 vertices (position and texture coordinates) of a triangle
    // calculate and return the triangle's tangent vector. The handedness of
    // the local coordinate system is stored in tangent.w. The bitangent is
    // then: float3 bitangent = cross(normal, tangent.xyz) * tangent.w.

    // Create 2 vectors in object space.
    //
    // edge1 is the vector from vertex positions pos1 to pos2.
    // edge2 is the vector from vertex positions pos1 to pos3.
    D3DXVECTOR3 edge1(pos2[0] - pos1[0], pos2[1] - pos1[1], pos2[2] - pos1[2]);
    D3DXVECTOR3 edge2(pos3[0] - pos1[0], pos3[1] - pos1[1], pos3[2] - pos1[2]);

    D3DXVec3Normalize(&edge1, &edge1);
    D3DXVec3Normalize(&edge2, &edge2);

    // Create 2 vectors in tangent (texture) space that point in the same
    // direction as edge1 and edge2 (in object space).
    //
    // texEdge1 is the vector from texture coordinates texCoord1 to texCoord2.
    // texEdge2 is the vector from texture coordinates texCoord1 to texCoord3.
    D3DXVECTOR2 texEdge1(texCoord2[0] - texCoord1[0], texCoord2[1] - texCoord1[1]);
    D3DXVECTOR2 texEdge2(texCoord3[0] - texCoord1[0], texCoord3[1] - texCoord1[1]);

    D3DXVec2Normalize(&texEdge1, &texEdge1);
    D3DXVec2Normalize(&texEdge2, &texEdge2);

    // These 2 sets of vectors form the following system of equations:
    //
    //  edge1 = (texEdge1.x * tangent) + (texEdge1.y * bitangent)
    //  edge2 = (texEdge2.x * tangent) + (texEdge2.y * bitangent)
    //
    // Using matrix notation this system looks like:
    //
    //  [ edge1 ]     [ texEdge1.x  texEdge1.y ]  [ tangent   ]
    //  [       ]  =  [                        ]  [           ]
    //  [ edge2 ]     [ texEdge2.x  texEdge2.y ]  [ bitangent ]
    //
    // The solution is:
    //
    //  [ tangent   ]        1     [ texEdge2.y  -texEdge1.y ]  [ edge1 ]
    //  [           ]  =  -------  [                         ]  [       ]
    //  [ bitangent ]      det A   [-texEdge2.x   texEdge1.x ]  [ edge2 ]
    //
    //  where:
    //        [ texEdge1.x  texEdge1.y ]
    //    A = [                        ]
    //        [ texEdge2.x  texEdge2.y ]
    //
    //    det A = (texEdge1.x * texEdge2.y) - (texEdge1.y * texEdge2.x)
    //
    // From this solution the tangent space basis vectors are:
    //
    //    tangent = (1 / det A) * ( texEdge2.y * edge1 - texEdge1.y * edge2)
    //  bitangent = (1 / det A) * (-texEdge2.x * edge1 + texEdge1.x * edge2)
    //     normal = cross(tangent, bitangent)

    D3DXVECTOR3 bitangent;
    float det = (texEdge1.x * texEdge2.y) - (texEdge1.y * texEdge2.x);

    if (fabsf(det) < 1e-6f)    // almost equal to zero
    {
        tangent.x = 1.0f;
        tangent.y = 0.0f;
        tangent.z = 0.0f;

        bitangent.x = 0.0f;
        bitangent.y = 1.0f;
        bitangent.z = 0.0f;
    }
    else
    {
        det = 1.0f / det;

        tangent.x = (texEdge2.y * edge1.x - texEdge1.y * edge2.x) * det;
        tangent.y = (texEdge2.y * edge1.y - texEdge1.y * edge2.y) * det;
        tangent.z = (texEdge2.y * edge1.z - texEdge1.y * edge2.z) * det;
        tangent.w = 0.0f;
        
        bitangent.x = (-texEdge2.x * edge1.x + texEdge1.x * edge2.x) * det;
        bitangent.y = (-texEdge2.x * edge1.y + texEdge1.x * edge2.y) * det;
        bitangent.z = (-texEdge2.x * edge1.z + texEdge1.x * edge2.z) * det;

        D3DXVec4Normalize(&tangent, &tangent);
        D3DXVec3Normalize(&bitangent, &bitangent);
    }

    // Calculate the handedness of the local tangent space.
    // The bitangent vector is the cross product between the triangle face
    // normal vector and the calculated tangent vector. The resulting bitangent
    // vector should be the same as the bitangent vector calculated from the
    // set of linear equations above. If they point in different directions
    // then we need to invert the cross product calculated bitangent vector. We
    // store this scalar multiplier in the tangent vector's 'w' component so
    // that the correct bitangent vector can be generated in the normal mapping
    // shader's vertex shader.

    D3DXVECTOR3 n(normal[0], normal[1], normal[2]);
    D3DXVECTOR3 t(tangent.x, tangent.y, tangent.z);
    D3DXVECTOR3 b;
        
    D3DXVec3Cross(&b, &n, &t);
    tangent.w = (D3DXVec3Dot(&b, &bitangent) < 0.0f) ? -1.0f : 1.0f;
}


//--------------------------------------------------------------------------------------
// Called during device initialization, this code checks the device for some 
// minimum set of capabilities, and rejects those that don't pass by returning false.
//--------------------------------------------------------------------------------------
bool CALLBACK IsDeviceAcceptable( D3DCAPS9* pCaps, D3DFORMAT AdapterFormat,
                                  D3DFORMAT BackBufferFormat, bool bWindowed, void* pUserContext )
{
    // Skip backbuffer formats that don't support alpha blending
    IDirect3D9* pD3D = DXUTGetD3D9Object();
    if( FAILED( pD3D->CheckDeviceFormat( pCaps->AdapterOrdinal, pCaps->DeviceType,
                                         AdapterFormat, D3DUSAGE_QUERY_POSTPIXELSHADER_BLENDING,
                                         D3DRTYPE_TEXTURE, BackBufferFormat ) ) )
        return false;

    return true;
}


//--------------------------------------------------------------------------------------
// This callback function is called immediately before a device is created to allow the 
// application to modify the device settings. The supplied pDeviceSettings parameter 
// contains the settings that the framework has selected for the new device, and the 
// application can make any desired changes directly to this structure.  Note however that 
// DXUT will not correct invalid device settings so care must be taken 
// to return valid device settings, otherwise IDirect3D9::CreateDevice() will fail.  
//--------------------------------------------------------------------------------------
bool CALLBACK ModifyDeviceSettings( DXUTDeviceSettings* pDeviceSettings, void* pUserContext )
{
    assert( DXUT_D3D9_DEVICE == pDeviceSettings->ver );

    HRESULT hr;
    IDirect3D9* pD3D = DXUTGetD3D9Object();
    D3DCAPS9 caps;
    V( pD3D->GetDeviceCaps( pDeviceSettings->d3d9.AdapterOrdinal,
                            pDeviceSettings->d3d9.DeviceType,
                            &caps ) );

    // If device doesn't support HW T&L or doesn't support 1.1 vertex shaders in HW 
    // then switch to SWVP.
    if( ( caps.DevCaps & D3DDEVCAPS_HWTRANSFORMANDLIGHT ) == 0 ||
        caps.VertexShaderVersion < D3DVS_VERSION( 2, 0 ) )
    {
        pDeviceSettings->d3d9.BehaviorFlags = D3DCREATE_SOFTWARE_VERTEXPROCESSING;
    }

    // Debugging vertex shaders requires either REF or software vertex processing 
    // and debugging pixel shaders requires REF.
#ifdef DEBUG_VS
    if( pDeviceSettings->d3d9.DeviceType != D3DDEVTYPE_REF )
    {
        pDeviceSettings->d3d9.BehaviorFlags &= ~D3DCREATE_HARDWARE_VERTEXPROCESSING;
        pDeviceSettings->d3d9.BehaviorFlags &= ~D3DCREATE_PUREDEVICE;
        pDeviceSettings->d3d9.BehaviorFlags |= D3DCREATE_SOFTWARE_VERTEXPROCESSING;
    }
#endif
#ifdef DEBUG_PS
    pDeviceSettings->d3d9.DeviceType = D3DDEVTYPE_REF;
#endif

    // For the first device created if its a REF device, optionally display a warning dialog box
    static bool s_bFirstTime = true;
    if( s_bFirstTime )
    {
        s_bFirstTime = false;
        if( pDeviceSettings->d3d9.DeviceType == D3DDEVTYPE_REF )
            DXUTDisplaySwitchingToREFWarning( pDeviceSettings->ver );
    }

    return true;
}


//--------------------------------------------------------------------------------------
// This callback function will be called immediately after the Direct3D device has been 
// created, which will happen during application initialization and windowed/full screen 
// toggles. This is the best location to create D3DPOOL_MANAGED resources since these 
// resources need to be reloaded whenever the device is destroyed. Resources created  
// here should be released in the OnDestroyDevice callback. 
//--------------------------------------------------------------------------------------
HRESULT CALLBACK OnCreateDevice( IDirect3DDevice9* pd3dDevice, const D3DSURFACE_DESC* pBackBufferSurfaceDesc,
                                 void* pUserContext )
{
    HRESULT hr;


    V_RETURN( g_DialogResourceManager.OnD3D9CreateDevice( pd3dDevice ) );
    V_RETURN( g_SettingsDlg.OnD3D9CreateDevice( pd3dDevice ) );
    // Initialize the font
    V_RETURN( D3DXCreateFont( pd3dDevice, 15, 0, FW_BOLD, 1, FALSE, DEFAULT_CHARSET,
                              OUT_DEFAULT_PRECIS, DEFAULT_QUALITY, DEFAULT_PITCH | FF_DONTCARE,
                              L"Arial", &g_pFont ) );

    D3DVERTEXELEMENT9 decl[] =
    {
        {0,  0, D3DDECLTYPE_FLOAT3, D3DDECLMETHOD_DEFAULT, D3DDECLUSAGE_POSITION, 0},
        {0, 12, D3DDECLTYPE_FLOAT2, D3DDECLMETHOD_DEFAULT, D3DDECLUSAGE_TEXCOORD, 0},
        {0, 20, D3DDECLTYPE_FLOAT3, D3DDECLMETHOD_DEFAULT, D3DDECLUSAGE_NORMAL,   0},
        {0, 32, D3DDECLTYPE_FLOAT4, D3DDECLMETHOD_DEFAULT, D3DDECLUSAGE_TANGENT,  0},
        D3DDECL_END()
    };

    // Define DEBUG_VS and/or DEBUG_PS to debug vertex and/or pixel shaders with the 
    // shader debugger. Debugging vertex shaders requires either REF or software vertex 
    // processing, and debugging pixel shaders requires REF.  The 
    // D3DXSHADER_FORCE_*_SOFTWARE_NOOPT flag improves the debug experience in the 
    // shader debugger.  It enables source level debugging, prevents instruction 
    // reordering, prevents dead code elimination, and forces the compiler to compile 
    // against the next higher available software target, which ensures that the 
    // unoptimized shaders do not exceed the shader model limitations.  Setting these 
    // flags will cause slower rendering since the shaders will be unoptimized and 
    // forced into software.  See the DirectX documentation for more information about 
    // using the shader debugger.
    DWORD dwShaderFlags = 0;

#if defined( DEBUG ) || defined( _DEBUG )
    // Set the D3DXSHADER_DEBUG flag to embed debug information in the shaders.
    // Setting this flag improves the shader debugging experience, but still allows 
    // the shaders to be optimized and to run exactly the way they will run in 
    // the release configuration of this program.
    dwShaderFlags |= D3DXSHADER_DEBUG;
    #endif

#ifdef DEBUG_VS
        dwShaderFlags |= D3DXSHADER_SKIPOPTIMIZATION|D3DXSHADER_DEBUG;
    #endif
#ifdef DEBUG_PS
        dwShaderFlags |= D3DXSHADER_SKIPOPTIMIZATION|D3DXSHADER_DEBUG;
    #endif

    /*
    // Assemble the vertex shader from the file
    V_RETURN( D3DXCompileShaderFromFile( strPath, NULL, NULL, "Ripple",
                                         "vs_2_0", dwShaderFlags, &pCode,
                                         NULL, &g_pConstantTable ) );

    // Create the vertex shaderL
    hr = pd3dDevice->CreateVertexShader( ( DWORD* )pCode->GetBufferPointer(),
                                         &g_pVertexShader );
                                         
    */
    
    // Read Color Texture from file
    V_RETURN(D3DXCreateTextureFromFile(pd3dDevice, L".\\Texture\\Logo.png", &g_pColorMap));

    // Read Normal Map Texture from file
    V_RETURN(D3DXCreateTextureFromFile(pd3dDevice, L".\\Texture\\Normal.png", &g_pNormalMap));

    // Read the D3DX effect file
    WCHAR str[MAX_PATH];
    V_RETURN( DXUTFindDXSDKMediaFileCch( str, MAX_PATH, L".\\NormalMapping.fx" ) );


    // If this fails, there should be debug output as to 
    // they the .fx file failed to compile
    ID3DXBuffer *pCompilationErrors = 0;
    //V_RETURN( FAILED(D3DXCreateEffectFromFile( pd3dDevice, str, NULL, NULL, D3DXSHADER_DEBUG,
    //                                    NULL, &g_pEffect, &pCompilationErrors )) );
    
    //V_RETURN(D3DXCreateEffectFromFile( pd3dDevice, str, NULL, NULL, D3DXSHADER_DEBUG,
    //                                    NULL, &g_pEffect, &pCompilationErrors ));
    hr = D3DXCreateEffectFromFile( pd3dDevice, str, NULL, NULL, D3DXSHADER_DEBUG,
                                        NULL, &g_pEffect, &pCompilationErrors );
    if(FAILED (hr)){  
        if(pCompilationErrors){
            char *orig = (char *)pCompilationErrors->GetBufferPointer();
            size_t origsize = strlen(orig) + 1;
            size_t  convertedChars = 0;
            wchar_t wcstring[10000];
            mbstowcs_s(&convertedChars, wcstring, origsize, orig, _TRUNCATE);
            return DXTRACE_ERR_MSGBOX(wcstring,hr);
        }else
            V_RETURN(hr);                                                                          
    }
    if(pCompilationErrors)
        pCompilationErrors->Release();
  
    // Create vertex declaration
    V_RETURN( pd3dDevice->CreateVertexDeclaration( decl, &g_pVertexDeclaration ) );

    // Setup the camera's view parameters
    D3DXVECTOR3 vecEye( 0.0f, 0.0f, 0.0f );
    D3DXVECTOR3 vecAt ( 0.0f, 0.0f, 5.0f );
    g_Camera.SetViewParams( &vecEye, &vecAt );
    float sqrt5 = sqrt(5.0f);
    g_Camera.SetRadius(sqrt5 * 2, sqrt5 + 0.1, 6 - 0.1);


    return S_OK;
}


//--------------------------------------------------------------------------------------
// This callback function will be called immediately after the Direct3D device has been 
// reset, which will happen after a lost device scenario. This is the best location to 
// create D3DPOOL_DEFAULT resources since these resources need to be reloaded whenever 
// the device is lost. Resources created here should be released in the OnLostDevice 
// callback. 
//--------------------------------------------------------------------------------------
HRESULT CALLBACK OnResetDevice( IDirect3DDevice9* pd3dDevice,
                                const D3DSURFACE_DESC* pBackBufferSurfaceDesc, void* pUserContext )
{
    HRESULT hr;

    V_RETURN( g_DialogResourceManager.OnD3D9ResetDevice() );
    V_RETURN( g_SettingsDlg.OnD3D9ResetDevice() );

    if( g_pFont )
        V_RETURN( g_pFont->OnResetDevice() );

    // Create a sprite to help batch calls when drawing many lines of text
    V_RETURN( D3DXCreateSprite( pd3dDevice, &g_pTextSprite ) );

    // Setup render states
    pd3dDevice->SetRenderState( D3DRS_LIGHTING, FALSE );
    pd3dDevice->SetRenderState( D3DRS_CULLMODE, D3DCULL_NONE );

    // Create a vertex buffer for the cube.
    
    V_RETURN(pd3dDevice->CreateVertexBuffer(sizeof(Vertex) * (36+34), 0, 0,
            D3DPOOL_MANAGED, &g_pVB, 0));

    // Fill vertex buffer with the cube's geometry.

    Vertex *pVertices = 0;

    V_RETURN(g_pVB->Lock(0, 0, reinterpret_cast<void**>(&pVertices), 0));

    memcpy(pVertices, g_vertex, sizeof(g_vertex));
    
    g_pVB->Unlock();
    
    // Create a index buffer for the scene.
    
    V_RETURN(pd3dDevice->CreateIndexBuffer(sizeof(Triangle)*24, D3DUSAGE_WRITEONLY, 
    D3DFMT_INDEX16, D3DPOOL_MANAGED, &g_pIB, 0));
    
    // Fill index buffer with the scene's geometry.
    
    Triangle *pIndices = 0;
    
    V_RETURN(g_pIB->Lock(0,0,reinterpret_cast<void**>(&pIndices),0));
    
    memcpy(pIndices, g_indices, sizeof(g_indices));
    
    g_pIB->Unlock();


    // Setup the camera's projection parameters
    float fAspectRatio = pBackBufferSurfaceDesc->Width / ( FLOAT )pBackBufferSurfaceDesc->Height;
    g_Camera.SetProjParams( D3DX_PI / 4, fAspectRatio, 0.1f, 1000.0f );
    g_Camera.SetWindow( pBackBufferSurfaceDesc->Width, pBackBufferSurfaceDesc->Height );

    g_HUD.SetLocation( pBackBufferSurfaceDesc->Width - 170, 0 );
    g_HUD.SetSize( 170, 170 );

    return S_OK;
}


//--------------------------------------------------------------------------------------
// This callback function will be called once at the beginning of every frame. This is the
// best location for your application to handle updates to the scene, but is not 
// intended to contain actual rendering calls, which should instead be placed in the 
// OnFrameRender callback.  
//--------------------------------------------------------------------------------------
void CALLBACK OnFrameMove( double fTime, float fElapsedTime, void* pUserContext )
{
    // Update the camera's position based on user input 
    g_Camera.FrameMove( fElapsedTime );

    
    // Set up the vertex shader constants
    D3DXMATRIXA16 mWorld;
    D3DXMATRIXA16 mView;
    D3DXMATRIXA16 mProj;

    mWorld = *g_Camera.GetWorldMatrix();
    mView = *g_Camera.GetViewMatrix();
    mProj = *g_Camera.GetProjMatrix();

    /*//g_pConstantTable->SetMatrix( DXUTGetD3D9Device(), "mWorldViewProj", &mWorldViewProj );
    //g_pConstantTable->SetFloat( DXUTGetD3D9Device(), "fTime", ( float )fTime );
    */

    static D3DXMATRIX xRot, yRot, translation;
    static D3DXMATRIX worldViewProjectionMatrix;
    static D3DXMATRIX worldInverseTransposeMatrix;

    // Calculate combined world-view-projection matrix.
    worldViewProjectionMatrix = mWorld * mView * mProj;

    // Calculate the transpose of the inverse of the world matrix.
    D3DXMatrixInverse(&worldInverseTransposeMatrix, 0, &mWorld);
    D3DXMatrixTranspose(&worldInverseTransposeMatrix, &worldInverseTransposeMatrix);
    
    // Set the matrices for the shader.
    g_pEffect->SetMatrix("worldMatrix", &mWorld);
    g_pEffect->SetMatrix("worldInverseTransposeMatrix", &worldInverseTransposeMatrix);
    g_pEffect->SetMatrix("worldViewProjectionMatrix", &worldViewProjectionMatrix);

    // Set the camera position.
    g_pEffect->SetValue("cameraPos", g_Camera.GetEyePt(), sizeof(g_Camera.GetEyePt()));
    
    // Set the lighting parameters for the shader.
    g_pEffect->SetValue("light.dir", g_light.dir, sizeof(g_light.dir));
    g_pEffect->SetValue("light.ambient", g_light.ambient, sizeof(g_light.ambient));
    g_pEffect->SetValue("light.diffuse", g_light.diffuse, sizeof(g_light.diffuse));
    g_pEffect->SetValue("light.specular", g_light.specular, sizeof(g_light.specular));
    g_pEffect->SetFloat("light.radius", g_light.radius);

    // Set the material parameters for the shader.
    g_pEffect->SetValue("material.ambient", g_material.ambient, sizeof(g_material.ambient));
    g_pEffect->SetValue("material.diffuse", g_material.diffuse, sizeof(g_material.diffuse));
    g_pEffect->SetValue("material.emissive", g_material.emissive, sizeof(g_material.emissive));
    g_pEffect->SetValue("material.specular", g_material.specular, sizeof(g_material.specular));
    g_pEffect->SetFloat("material.shininess", g_material.shininess);

    // Bind the textures to the shader.
    g_pEffect->SetTexture("colorMapTexture",  g_pColorMap);
    g_pEffect->SetTexture("normalMapTexture", g_pNormalMap);
}


//--------------------------------------------------------------------------------------
// This callback function will be called at the end of every frame to perform all the 
// rendering calls for the scene, and it will also be called if the window needs to be 
// repainted. After this function has returned, DXUT will call 
// IDirect3DDevice9::Present to display the contents of the next buffer in the swap chain
//--------------------------------------------------------------------------------------
void CALLBACK OnFrameRender( IDirect3DDevice9* pd3dDevice, double fTime, float fElapsedTime, void* pUserContext )
{
    // If the settings dialog is being shown, then
    // render it instead of rendering the app's scene
    if( g_SettingsDlg.IsActive() )
    {
        g_SettingsDlg.OnRender( fElapsedTime );
        return;
    }

    HRESULT hr;
    
    //
    // Now that we have the shadow map, render the scene.
    //
    const D3DXMATRIX* pmView = g_Camera.GetViewMatrix();

    // Clear the render target and the zbuffer 
    V( pd3dDevice->Clear( 0, NULL, D3DCLEAR_TARGET | D3DCLEAR_ZBUFFER, D3DCOLOR_ARGB( 0, 45, 50, 170 ), 1.0f, 0 ) );

    // Render the scene
    if( SUCCEEDED( pd3dDevice->BeginScene() ) )
    {
        static UINT totalPasses;
        static D3DXHANDLE hTechnique;

        // Bind vertex buffer.
        pd3dDevice->SetVertexDeclaration(g_pVertexDeclaration);
        pd3dDevice->SetStreamSource(0, g_pVB, 0, sizeof(Vertex));
        pd3dDevice->SetIndices(g_pIB);

        if(g_bNormalMapping)
            hTechnique = g_pEffect->GetTechniqueByName("NormalMapping");
        else
            hTechnique = g_pEffect->GetTechniqueByName("Basic");
        
        
        // Render the cube.
        if (SUCCEEDED(g_pEffect->SetTechnique(hTechnique)))
        {
            if (SUCCEEDED(g_pEffect->Begin(&totalPasses, 0)))
            {
                for (UINT pass = 0; pass < totalPasses; ++pass)
                {
                    if (SUCCEEDED(g_pEffect->BeginPass(pass)))
                    {
                        pd3dDevice->DrawPrimitive(D3DPT_TRIANGLELIST, 0, 12);
                        g_pEffect->EndPass();
                    }
                }

                g_pEffect->End();
            }
        }
        
         hTechnique = g_pEffect->GetTechniqueByName("ShadowMapping");
       
        // Render the scene
                
        if (true)
        {
            
            if (SUCCEEDED(g_pEffect->Begin(&totalPasses, 0)))
            {
                
                for (UINT pass = 0; pass < totalPasses; ++pass)
                {
                    g_pEffect->SetTexture("colorMapTexture",  g_pFloorMap);
                    if (SUCCEEDED(g_pEffect->BeginPass(pass)))
                    {
                        
                        pd3dDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 36,36,34,0, 2);
                        g_pEffect->EndPass();
                    }

                    g_pEffect->SetTexture("colorMapTexture",  g_pWallMap);
                    if (SUCCEEDED(g_pEffect->BeginPass(pass)))
                    {
                        
                        pd3dDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 36,36,34,3*2, 12);
                        g_pEffect->EndPass();
                    }

                    g_pEffect->SetTexture("colorMapTexture",  g_pCealingMap);
                    if (SUCCEEDED(g_pEffect->BeginPass(pass)))
                    {
                        
                        pd3dDevice->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 36,36,34,3*14, 10);
                        g_pEffect->EndPass();
                    }

                }

                g_pEffect->End();
            }
        }
        

        RenderText();
        V( g_HUD.OnRender( fElapsedTime ) );

        V( pd3dDevice->EndScene() );
        V( pd3dDevice->Present(0, 0, 0, 0));
    }
}


//--------------------------------------------------------------------------------------
// Render the help and statistics text. This function uses the ID3DXFont interface for 
// efficient text rendering.
//--------------------------------------------------------------------------------------
void RenderText()
{
    // The helper object simply helps keep track of text position, and color
    // and then it calls pFont->DrawText( g_pSprite, strMsg, -1, &rc, DT_NOCLIP, g_clr );
    // If NULL is passed in as the sprite object, then it will work however the 
    // pFont->DrawText() will not be batched together.  Batching calls will improves performance.
    CDXUTTextHelper txtHelper( g_pFont, g_pTextSprite, 15 );

    // Output statistics
    txtHelper.Begin();
    txtHelper.SetInsertionPos( 5, 5 );
    txtHelper.SetForegroundColor( D3DXCOLOR( 1.0f, 1.0f, 0.0f, 1.0f ) );
    txtHelper.DrawTextLine( DXUTGetFrameStats( DXUTIsVsyncEnabled() ) );
    txtHelper.DrawTextLine( DXUTGetDeviceStats() );

    txtHelper.SetForegroundColor( D3DXCOLOR( 1.0f, 1.0f, 1.0f, 1.0f ) );

    // Draw help
    if( g_bShowHelp )
    {
        const D3DSURFACE_DESC* pd3dsdBackBuffer = DXUTGetD3D9BackBufferSurfaceDesc();
        txtHelper.SetInsertionPos( 10, pd3dsdBackBuffer->Height - 15 * 6 );
        txtHelper.SetForegroundColor( D3DXCOLOR( 1.0f, 0.75f, 0.0f, 1.0f ) );
        txtHelper.DrawTextLine( L"Controls:" );

        txtHelper.SetInsertionPos( 40, pd3dsdBackBuffer->Height - 15 * 5 );
        txtHelper.DrawTextLine( L"Rotate model: Left mouse button\n"
                                L"Rotate camera: Right mouse button\n"
                                L"Zoom camera: Mouse wheel scroll\n");
    }
    else
    {
        txtHelper.SetForegroundColor( D3DXCOLOR( 1.0f, 1.0f, 1.0f, 1.0f ) );
        txtHelper.DrawTextLine( L"Press F1 for help" );
    }

    txtHelper.End();
}


//--------------------------------------------------------------------------------------
// Before handling window messages, DXUT passes incoming windows 
// messages to the application through this callback function. If the application sets 
// *pbNoFurtherProcessing to TRUE, then DXUT will not process this message.
//--------------------------------------------------------------------------------------
LRESULT CALLBACK MsgProc( HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam, bool* pbNoFurtherProcessing,
                          void* pUserContext )
{
    // Always allow dialog resource manager calls to handle global messages
    // so GUI state is updated correctly
    *pbNoFurtherProcessing = g_DialogResourceManager.MsgProc( hWnd, uMsg, wParam, lParam );
    if( *pbNoFurtherProcessing )
        return 0;

    if( g_SettingsDlg.IsActive() )
    {
        g_SettingsDlg.MsgProc( hWnd, uMsg, wParam, lParam );
        return 0;
    }

    // Give the dialogs a chance to handle the message first
    *pbNoFurtherProcessing = g_HUD.MsgProc( hWnd, uMsg, wParam, lParam );
    if( *pbNoFurtherProcessing )
        return 0;

    // Pass all remaining windows messages to camera so it can respond to user input
    g_Camera.HandleMessages( hWnd, uMsg, wParam, lParam );

    return 0;
}


//--------------------------------------------------------------------------------------
// As a convenience, DXUT inspects the incoming windows messages for
// keystroke messages and decodes the message parameters to pass relevant keyboard
// messages to the application.  The framework does not remove the underlying keystroke 
// messages, which are still passed to the application's MsgProc callback.
//--------------------------------------------------------------------------------------
void CALLBACK KeyboardProc( UINT nChar, bool bKeyDown, bool bAltDown, void* pUserContext )
{
    if( bKeyDown )
    {
        switch( nChar )
        {
            case VK_F1:
                g_bShowHelp = !g_bShowHelp; break;
        }
    }
}


//--------------------------------------------------------------------------------------
// Handles the GUI events
//--------------------------------------------------------------------------------------
void CALLBACK OnGUIEvent( UINT nEvent, int nControlID, CDXUTControl* pControl, void* pUserContext )
{
    switch( nControlID )
    {
        case IDC_TOGGLEFULLSCREEN:
            DXUTToggleFullScreen(); break;
    }
}


//--------------------------------------------------------------------------------------
// This callback function will be called immediately after the Direct3D device has 
// entered a lost state and before IDirect3DDevice9::Reset is called. Resources created
// in the OnResetDevice callback should be released here, which generally includes all 
// D3DPOOL_DEFAULT resources. See the "Lost Devices" section of the documentation for 
// information about lost devices.
//--------------------------------------------------------------------------------------
void CALLBACK OnLostDevice( void* pUserContext )
{
    g_DialogResourceManager.OnD3D9LostDevice();
    g_SettingsDlg.OnD3D9LostDevice();
    if( g_pFont )
        g_pFont->OnLostDevice();
    if( g_pEffect )
        g_pEffect->OnLostDevice();
    SAFE_RELEASE( g_pIB );
    SAFE_RELEASE( g_pVB );
    SAFE_RELEASE( g_pTextSprite );
    
}


//--------------------------------------------------------------------------------------
// This callback function will be called immediately after the Direct3D device has 
// been destroyed, which generally happens as a result of application termination or 
// windowed/full screen toggles. Resources created in the OnCreateDevice callback 
// should be released here, which generally includes all D3DPOOL_MANAGED resources. 
//--------------------------------------------------------------------------------------
void CALLBACK OnDestroyDevice( void* pUserContext )
{
    g_DialogResourceManager.OnD3D9DestroyDevice();
    g_SettingsDlg.OnD3D9DestroyDevice();
    SAFE_RELEASE( g_pFont );
    SAFE_RELEASE( g_pEffect );
    SAFE_RELEASE( g_pVertexShader );
    SAFE_RELEASE( g_pConstantTable );
    SAFE_RELEASE( g_pVertexDeclaration );
    SAFE_RELEASE(g_pNormalMap);
    SAFE_RELEASE(g_pColorMap);
    SAFE_RELEASE(g_pWallMap);
    SAFE_RELEASE(g_pFloorMap);
    SAFE_RELEASE(g_pCealingMap);
}

//LPDIRECT3DSURFACE9              g_pDSShadow = NULL;     // Depth-stencil buffer for rendering to shadow map
//float                           g_fLightFov = LIGHT_RADIUS;            // FOV of the spot light (in radian)
//D3DXMATRIXA16                   g_mShadowProj;   

