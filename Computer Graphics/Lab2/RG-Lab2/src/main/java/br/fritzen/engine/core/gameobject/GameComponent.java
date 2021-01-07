package br.fritzen.engine.core.gameobject;

import br.fritzen.engine.rendering.shaders.GenericShader;

public abstract class GameComponent {

	private GameObject parent;;

	public abstract String getComponentName();
	
	public void input() {
		
	}

	
	public void update() {
		
	}
	
	
	public void renderOnce() {
		
	}
	
	
	public void render(GenericShader shader) {
		
	}
	
	
	public void setParent(GameObject parent) {
		this.parent = parent;
	}


	public GameObject getParent() {
		return this.parent;
	}

	
	
	
}
