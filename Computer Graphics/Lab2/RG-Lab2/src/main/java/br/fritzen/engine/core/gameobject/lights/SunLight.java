package br.fritzen.engine.core.gameobject.lights;

import org.joml.Vector3f;

public class SunLight {

	private Vector3f color;

	private float intensity;

	private Vector3f direction;


	public SunLight(Vector3f color, float intensity, Vector3f direction) {
		this.color = color;
		this.intensity = intensity;
		this.direction = direction.normalize();
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


	public Vector3f getDirection() {
		return direction;
	}


	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

}
