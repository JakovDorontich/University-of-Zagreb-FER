package br.fritzen.engine.core;

import org.joml.Vector3f;

/**
 * 
 * The Global State variables recognized in any part of engine
 * 
 * @author Vinicius Fritzen Machado
 *
 */
public class GS {

	//AXIS
	/**
	 * Final Vector3f to represent X axis in 3D space.
	 */
	public static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	
	/**
	 * Final Vector3f to represent Y axis in 3D space.
	 */
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	
	/**
	 * Final Vector3f to represent Z axis in 3D space.
	 */
	public static final Vector3f Z_AXIS = new Vector3f(0, 0, 1);
	
	
	//RENDERING AND WINDOW
	/**
	 * Initial state of window - true for fullscreen | false for windowed.
	 */
	public static boolean WINDOW_FULLSCREEN = false;
	
	
	/**
	 * Number of AntiAliasing Samples
	 */
	public static int ANTI_ALIASING_SAMPLES = 4;
	
	
	/**
	 * Synchronize with Monitor frequency
	 * <br>
	 * Use 0 for unlimited FPS.
	 * USe 1 for sync.
	 */
	public static int GL_SWAP_INTERVALS = 0;
	
	
	/**
	 * Enable / Disable bar with FPS and additional info debug in GameScreen
	 */
	public static boolean GUI_FPS_DEBUG = true;

	
	//IMPORTANT VARIABLES
	public static int UPS = 100;
	
	public static int FPS_CAP = 200;
	
	
	//LIGHTNING
	public static final int MAX_DIRECTIONAL_LIGHTS = 5;
	
	public static final int MAX_POINT_LIGHTS = 20;
	
	public static final int MAX_SPOT_LIGHTS = 20;
	
	
	//CONSOLE DEBUG
	public static boolean FPS_DEBUG = false;
	
	public static boolean TEXTURE_DEBUG = false;
	
	public static boolean OBJLOADER_DEBUG = false;

	public static boolean MESH_DEBUG = false;
	
	public static boolean INPUT_DEBUG = false;

	public static boolean RENDERING_SYSTEM_DEBUG = false;

	
	//SHADOWS
	public static final int NUM_CASCADE_SHADOW_MAPS = 3;

	
	//GAME COMPONENTS
	public static final String COMP_MESH_RENDERER = "MeshRenderer";

	public static final String COMP_AXIS_3D = "Axis3D";

	public static final String COMP_BOUNDING_BOX = "BoundingBox";

	public static final String COMP_LINE_3D = "Line3D";

	public static final String COMP_FREE_LOOK = "FreeLook";
	
	public static final String COMP_FREE_MOVE = "FreeMove";
	
	
	/**
	 * 
	 * Static class with colors.
	 *
	 */
	public static class COLOR {
		
		public static Vector3f RED = new Vector3f(1, 0, 0);
		public static Vector3f GREEN = new Vector3f(0, 1, 0);
		public static Vector3f BLUE = new Vector3f(0, 0, 1);
		public static Vector3f CYAN = new Vector3f(0, 1, 1);
		public static Vector3f BLACK = new Vector3f(0, 0, 0);
		public static Vector3f WHITE = new Vector3f(1, 1, 1);
		public static Vector3f PURPLE = new Vector3f(0.5f, 0, 0.5f);
		public static Vector3f YELLOW = new Vector3f(1, 1, 0);
		
	};

	
}
