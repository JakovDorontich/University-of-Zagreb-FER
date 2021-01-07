package br.fritzen.engine.core.gameobject.lights;

import org.joml.Vector3f;

import br.fritzen.engine.rendering.shaders.GenericShader;

public class PointLight extends BaseLight {

	private static final int COLOR_DEPTH = 256;

	private Attenuation attenuation;

	private float range;


	public PointLight(Vector3f color, float intensity, Attenuation attenuation) {

		super(color, intensity);
		
		this.setAttenuation(attenuation);
	}


	public Attenuation getAttenuation() {
		return attenuation;
	}


	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
		this.calculateRange();
	}


	private void calculateRange() {

		float a = this.attenuation.getExponent();
		float b = this.attenuation.getLinear();
		float c = this.attenuation.getConstant();

		c -= COLOR_DEPTH * getIntensity() * getColor().get(getColor().maxComponent());
		this.range = (float) (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);

	}


	public float getRange() {
		return range;
	}


	public void setRange(float range) {
		this.range = range;
	}


	@Override
	public String getComponentName() {
		return this.getClass().getSimpleName();
	}

	/*
	@Override
	public void addUniforms() {
	
		this.getShader().addUniform("pointLight.base.color");
		this.getShader().addUniform("pointLight.base.intensity");
		this.getShader().addUniform("pointLight.atten.constant");
		this.getShader().addUniform("pointLight.atten.linear");
		this.getShader().addUniform("pointLight.atten.exponent");
		this.getShader().addUniform("pointLight.position");
		this.getShader().addUniform("pointLight.range");
		
	}


	@Override
	public void updateUniforms() {
		
		this.getShader().updateUniform("pointLight.base.color", this.getColor());
		this.getShader().updateUniform("pointLight.base.intensity", this.getIntensity());
		this.getShader().updateUniform("pointLight.atten.constant", this.getAttenuation().getConstant());
		this.getShader().updateUniform("pointLight.atten.linear", this.getAttenuation().getLinear());
		this.getShader().updateUniform("pointLight.atten.exponent", this.getAttenuation().getExponent());
		this.getShader().updateUniform("pointLight.position", this.getParent().getTransform().getTransformedPosition());
		this.getShader().updateUniform("pointLight.range", this.getRange());
	
	}
	*/


}
