package br.fritzen.engine.gui;

import java.util.ArrayList;

import org.joml.Vector2f;
import org.joml.Vector4f;

import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.GameWindow;

public abstract class GUIElement {

	private GUIElement parent;
	
	private ArrayList<GUIElement> children;
	
	protected GameWindow gameWindow;
	
	protected int windowWidth;
	
	protected int windowHeight;
	
	protected Vector4f bounds;
	
	protected float rotation = 0.0f;
	
	private boolean isVisible = true;
	
	
	public GUIElement() {
		children = new ArrayList<GUIElement>();
		//this.bounds = new Vector4f(0.0f, 0, 1, 1);
	}
	
	public void render(long vg) {
		this.windowWidth = gameWindow.getWidth();
		this.windowHeight = gameWindow.getHeight();
	}
	
	
	public void setGameWindow(GameWindow gameWindow) {
		this.gameWindow = gameWindow;
	}
	
	
	public void add(GUIElement child) {
		this.children.add(child);
		child.parent = this;
		//child.gameWindow = this.gameWindow;
		child.gameWindow = Game.getGameWindow();
	}
	
	
	public void update() {
		
	}
	
	
	public void onMouseOver(Vector2f mouse, int click) {
		
	}
	
	
	public void onMouseOut(Vector2f mouse, int click) {
		
	}
	
	
	public void onClick() {
		
	}
	
	
	public void setBounds(Vector4f bounds) {
		this.bounds = bounds;
	}
	
	
	public void setRotation(float valueInRadians) {
		this.rotation = valueInRadians;
	}

	
	public ArrayList<GUIElement> getChildren() {
		return this.children;
	}
	
	
	protected float getParentSizeX() {
	
		if (parent.bounds == null) {
			return 1;
		}
		
		return parent.bounds.z * parent.getParentSizeX();
	}
	

	protected float getParentSizeY() {
		
		if (parent.bounds == null) {
			return 1;
		}
		
		return parent.bounds.w * parent.getParentSizeY();
	}

	
	
	public float getParentOffsetX() {
	
		if (parent.bounds == null) {
			return bounds.x;
		}
		
		return bounds.x * getParentSizeX() + parent.getParentOffsetX();
	}
	
	
	public float getParentOffsetY() {
		
		if (parent.bounds == null) {
			return bounds.y;
		}
		
		return bounds.y * getParentSizeY() + parent.getParentOffsetY();
	}

	
	public void checkMouseIn(Vector2f mouse, int click) {
		
		boolean mouseOut = true;
		
		if (isVisible) {
			
			if (mouse.x > (getParentOffsetX() * this.windowWidth) && mouse.x < ( (getParentOffsetX() + bounds.z * getParentSizeX()) * windowWidth)) {
			
				if (mouse.y > (getParentOffsetY() * this.windowHeight) && mouse.y < ( (getParentOffsetY() + bounds.w * getParentSizeY()) * windowHeight)) {
					
					mouseOut = false;
					
					this.onMouseOver(mouse, click);
					
					for (int i = 0; i < children.size(); i++) {
						children.get(i).checkMouseIn(mouse, click);
					}
				} 
			} 
		}
		
		if (mouseOut) {
			this.onMouseOut(mouse, click);
			for (int i = 0; i < children.size(); i++) {
				children.get(i).onMouseOut(mouse, click);
			}
		}
		
	}

	
	public Vector4f getBounds() {
		return this.bounds;
	}
	
	
	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}
}
