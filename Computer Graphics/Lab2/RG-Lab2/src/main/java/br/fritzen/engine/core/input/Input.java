package br.fritzen.engine.core.input;

import org.joml.Vector2f;

public interface Input {

	public static final int FREE = 0;
	
	public static final int PRESSED = 1;
	
	public static final int HOLD = 2;

	public void setWindowHandler(long windowHanlder);
	
	public void keyboardCallback(long window, int key, int scancode, int action, int mods);
	
	public void mouseButtonCallback(long window, int button, int action, int mods);

	public void mouseMoveCallback(long window, double xpos, double ypos);

	public boolean getKey(int key);
	
	public boolean getClickedKey(int key);
	
	public Vector2f getMousePosition();

	public int getMouseClick();

	public int getMouseState();

	public boolean getMouseLeftButton();
	
	public void setMousePosition(int x, int y);
	
}
