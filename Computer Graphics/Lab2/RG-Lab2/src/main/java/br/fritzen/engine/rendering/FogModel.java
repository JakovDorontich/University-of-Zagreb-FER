package br.fritzen.engine.rendering;

import org.joml.Vector3f;

public class FogModel {
	
	private boolean enabled;

	private float density;
	
	private float gradient;
	
	private Vector3f color;

	
	public FogModel() {
	
	}
	
	
	public FogModel(float density, float gradient, Vector3f color) {
		this.enabled = true;
		this.density = density;
		this.gradient = gradient;
		this.color = color;
	}


	public boolean isHasFog() {
		return enabled;
	}

	
	public void setHasFog(boolean hasFog) {
		this.enabled = hasFog;
	}

	
	public float getFogDensity() {
		return density;
	}

	
	public void setFogDensity(float fogDensity) {
		this.density = fogDensity;
	}

	
	public float getFogGradient() {
		return gradient;
	}

	
	public void setFogGradient(float fogGradient) {
		this.gradient = fogGradient;
	}

	
	public Vector3f getFogColor() {
		return color;
	}

	
	public void setFogColor(Vector3f fogColor) {
		this.color = fogColor;
	}
	
	
}
