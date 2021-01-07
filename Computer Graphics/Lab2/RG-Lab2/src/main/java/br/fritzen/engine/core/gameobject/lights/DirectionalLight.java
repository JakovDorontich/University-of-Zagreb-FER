package br.fritzen.engine.core.gameobject.lights;

import org.joml.Vector3f;

import br.fritzen.engine.rendering.shaders.GenericShader;

public class DirectionalLight extends BaseLight {

	
	public DirectionalLight(Vector3f color, float intensity) {
		super(color, intensity);
		
	}

	/*
	@Override
	public void addUniforms() {

		this.getShader().addUniform("sunLight.color");
		this.getShader().addUniform("sunLight.direction");
		this.getShader().addUniform("sunLight.intensity");
		
	}

	@Override
	public void updateUniforms() {
		
		this.getShader().updateUniform("sunLight.color", this.getColor());
		this.getShader().updateUniform("sunLight.direction", this.getDirection());
		this.getShader().updateUniform("sunLight.intensity", this.getIntensity());
		
	}
	 */

	private Vector3f getDirection() {
		return this.getParent().getTransform().getForward();
	}

	@Override
	public String getComponentName() {
		return this.getClass().getSimpleName();
	}
}
