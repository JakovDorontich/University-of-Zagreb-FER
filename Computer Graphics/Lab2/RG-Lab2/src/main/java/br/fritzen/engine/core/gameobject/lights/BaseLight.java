package br.fritzen.engine.core.gameobject.lights;

import org.joml.Vector3f;

import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.rendering.shaders.GenericShader;

public abstract class BaseLight extends GameComponent {

	private Vector3f color;

	private float intensity;

		
	public BaseLight(Vector3f color, float intensity) {
		
		this.color = color;
		this.intensity = intensity;
		
	}

	
	public Vector3f getColor() {
		return color;
	}

	
	public void setColor(Vector3f color) {
		this.color = color;
	}

	
	public float getIntensity() {
		return intensity;
	}

	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
}
