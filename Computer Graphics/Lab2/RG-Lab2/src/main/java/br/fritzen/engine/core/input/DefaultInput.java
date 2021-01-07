package br.fritzen.engine.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

import java.util.logging.Logger;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import br.fritzen.engine.core.GS;

public class DefaultInput implements Input {

	private static final Logger LOG = Logger.getLogger(Input.class.getName());
	
	private long windowHandler;
	
	private static final int MAX_KEYS = 350;

	private static final int MOUSE_CLICK_HOLD = 25;
	
	private boolean[] keys = new boolean[MAX_KEYS];

	private Vector2f mousePosition; 
	
	private int mouseClick;
	
	private boolean mouseHold = false;
	
	private int mouseClickHold = -1;

	private int actualMouseState;
	
	private boolean leftButton = false;
	
	
	public DefaultInput() {
	
		for (int i = 0; i < keys.length; i++) {
			keys[i] = false;
		}
		
		mousePosition = new Vector2f(0, 0);
		actualMouseState = Input.FREE;
	}
	
	
	@Override
	public void keyboardCallback(long window, int key, int scancode, int action, int mods) {

		if (GS.INPUT_DEBUG)
			LOG.info("KEY PRESSED: " + key);
		
		if (key < MAX_KEYS && key >= 0) {
			if (action == GLFW_RELEASE) {
				this.keys[key] = false;
			} else if ((action == GLFW_PRESS) || (action == GLFW_REPEAT)) {
				this.keys[key] = true;
			}
		}
	}


	@Override
	public void mouseButtonCallback(long window, int button, int action, int mods) {
		
		if (GS.INPUT_DEBUG)
			LOG.info("MOUSE EVENT - BUTTON: " + button);
		
		if (action == GLFW_RELEASE) {
			this.mouseClick = button;
			mouseHold = false;
			this.mouseClickHold = -1;
			actualMouseState = Input.FREE;
			
			if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
				this.leftButton = false;
			}
		
		} else {
			actualMouseState = Input.PRESSED;
			mouseHold = true;
			this.mouseClick = button;
			
			if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
				this.leftButton = true;
			}
		}	
	}

	
	@Override
	public void mouseMoveCallback(long window, double xpos, double ypos) {
		
		if (GS.INPUT_DEBUG)
			LOG.info("MOUSE IN POSITION: " + xpos + ", " + ypos);
		
		mousePosition.x = (float) xpos;
		mousePosition.y = (float) ypos;
	}


	@Override
	public boolean getKey(int keycode) {
		
		boolean status = false;

		if (keycode < MAX_KEYS) {
			status = this.keys[keycode];
		}
		
		return status;
	}
	
	
	@Override
	public boolean getClickedKey(int keycode) {
		
		boolean status = false;

		if (keycode < MAX_KEYS) {
			status = this.keys[keycode];
			this.keys[keycode] = false;
		}
		
		return status;
	}
	
	
	@Override
	public Vector2f getMousePosition() {
		return mousePosition;
	}


	@Override
	public int getMouseClick() {
		
		if (!mouseHold) {
			int click = this.mouseClick;
			this.mouseClick = -1;
			return click;
		} else {
			mouseClickHold++;
			if (mouseClickHold >= MOUSE_CLICK_HOLD) {
				mouseClickHold = MOUSE_CLICK_HOLD;
				actualMouseState = Input.HOLD;
				return this.mouseClick;
			}
		}

		return -1;
	}


	@Override
	public int getMouseState() {
		return this.actualMouseState;
	}

	
	@Override
	public boolean getMouseLeftButton() {
		return this.leftButton;
	}
	

	@Override
	public void setMousePosition(int x, int y) {
		GLFW.glfwSetCursorPos(this.windowHandler, x, y);
	}


	@Override
	public void setWindowHandler(long windowHanlder) {
		this.windowHandler = windowHanlder;
	}
	
	
}
