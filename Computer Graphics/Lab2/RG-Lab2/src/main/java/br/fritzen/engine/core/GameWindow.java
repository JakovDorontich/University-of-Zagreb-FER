package br.fritzen.engine.core;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.logging.Logger;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import br.fritzen.engine.core.input.DefaultInput;
import br.fritzen.engine.core.input.Input;

/**
 * This class is managed by {@link Game} <br>
 * Don't worry to use this unless you know what are you doing.
 * 
 * @author Vinicius Fritzen Machado
 *
 */
public class GameWindow {

	private static final Logger LOG = Logger.getLogger(GameWindow.class.getName());
	
	private long handler;
	
	private int width;
	
	private int height;
	
	private String title;
	
	private GLCapabilities glCapabilities;
	
	private Input input;

	private boolean hasSizeChanged;
	
	private int[] viewport;
	
	
	/**
	 * Default constructor use the {@link DefaultInput}
	 * @param width - window size 
	 * @param height - window size
	 * @param title - title of window
	 */
	public GameWindow(final int width, final int height, final String title) {
		
		this(width, height, title, new DefaultInput());
		
	}

	
	/**
	 * Specialized Constructor - you can use this with a custom InputSystem since it implements the {@link Input}
	 * @param width - window size 
	 * @param height - window size
	 * @param title - title of window
	 * @param input - yout implementation of Input
	 */
	public GameWindow(final int width, final int height, final String title, Input input) {
		
		this.width = width;
		this.height = height;
		this.title = title;
		this.input = input;
		
		this.viewport = new int[] {0, 0, width, height};
		
		this.initGameWindow();
		this.initWindowCallbacks();
		this.initControls();
		
	}


	private void initGameWindow() {
		
		LOG.info("Initializing System");
		boolean glfwStatus = GLFW.glfwInit();
		if (!glfwStatus) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, GS.ANTI_ALIASING_SAMPLES);
		
		long monitor = NULL;
		if (GS.WINDOW_FULLSCREEN) {
			monitor = GLFW.glfwGetPrimaryMonitor();
		}
		
		handler = GLFW.glfwCreateWindow(width, height, title, monitor, NULL);
		if (handler == NULL) {
			LOG.severe("GLFW CAN'T START. Error code: " + GLFW.glfwGetError());
			throw new RuntimeException("Error creating GLFW Window");
		}
		
		GLFW.glfwMakeContextCurrent(handler);
		glCapabilities = GL.createCapabilities();
		
		GLFW.glfwSwapInterval(GS.GL_SWAP_INTERVALS);
	}

	
	private void initWindowCallbacks() {
		
		LOG.info("Initializing Window Callbacks");
		
		GLFW.glfwSetWindowSizeCallback(handler, (window, width, height) -> {
			this.width = width;
			this.height = height;
			this.hasSizeChanged = true;
			
		});
		
	}
	
	
	private void initControls( ) {
		
		LOG.info("Initializing Controls");
		
		input.setWindowHandler(handler);
		
		GLFW.glfwSetKeyCallback(handler, (window, key, scancode, action, mods) -> {
			input.keyboardCallback(window, key, scancode, action, mods);			
		});
		
		GLFW.glfwSetMouseButtonCallback(handler, (window, button, action, mods) -> {
			input.mouseButtonCallback(window, button, action, mods);
		});
		
		GLFW.glfwSetCursorPosCallback(handler, (window, xpos, ypos) -> {
			input.mouseMoveCallback(window, xpos, ypos);
		});
		
	}
	
	
	/**
	 * Default setMethod for {@link Input}
	 * @param input
	 */
	public void setInput(Input input) {
		this.input = input;
	}
	
	
	/**
	 * Default getMethod for {@link Input}
	 * @return
	 */
	public Input getInput() {
		return this.input;
	}
	
	
	/**
	 * Default getMethod for GLFW windowHandler 
	 * @return a long that contains the handler for a GLFW window.
	 */
	public long getWindowHandler() {
		return handler;
	}


	/**
	 * Set the window visible
	 */
	public void show() {
		GLFW.glfwShowWindow(handler);
	}


	/**
	 * Default getMethod for {@link GLCapabilities}
	 * @return the object with OpenGL setup
	 */
	public GLCapabilities getCapabilities() {
		return this.glCapabilities;
	}


	/**
	 * 
	 * @return true/false according if windows has been changed.
	 */
	public boolean hasSizeChanged() {
		return hasSizeChanged;
	}


	/**
	 * Return the aspect width/height of the game window.
	 * @return
	 */
	public float getAspect() {
		
		hasSizeChanged = false;
		GL11.glViewport(0, 0, width, height);
		this.viewport = new int[] {0, 0, width, height};
		
		return ((float)this.width)/this.height;
	}


	/**
	 * 
	 * @return width of the window
	 */
	public int getWidth() {
		return this.width;
	}
	
	
	/**
	 * 
	 * @return height of the window
	 */
	public int getHeight() {
		return this.height;
	}
	
	
	/**
	 * 
	 * @return window viewport, commonly [0, 0, width, height]
	 */
	public int[] getViewPort() {
		return this.viewport;
	}
}
