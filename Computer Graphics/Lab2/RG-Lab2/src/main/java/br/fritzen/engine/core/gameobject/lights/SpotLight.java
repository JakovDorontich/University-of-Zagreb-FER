package br.fritzen.engine.core.gameobject.lights;

import org.joml.Vector3f;

import br.fritzen.engine.rendering.shaders.GenericShader;

public class SpotLight extends BaseLight {

	private static final int COLOR_DEPTH = 256;

	private Attenuation attenuation;

	private float range;
	
	private float cutOff;
	
	
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float cutOff) {
		
		super(color, 
			intensity);
		
		this.cutOff = cutOff;
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


	private Vector3f getDirection() {
		return this.getParent().getTransform().getForward();
	}
	
	
	@Override
	public String getComponentName() {
		return this.getClass().getSimpleName();
	}


	public float getCutOff() {
		return cutOff;
	}


	public void setCutOff(float cutOff) {
		this.cutOff = cutOff;
	}
	
	
	public Vector3f getConeDirection() {
		return this.getDirection();
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
		
		this.getShader().addUniform("cutOff");
		this.getShader().addUniform("coneDirection");
		
	}

	@Override
	public void updateUniforms() {
		
		this.getShader().updateUniform("pointLight.base.color", getColor());
		this.getShader().updateUniform("pointLight.base.intensity", getIntensity());
		this.getShader().updateUniform("pointLight.atten.constant", getAttenuation().getConstant());
		this.getShader().updateUniform("pointLight.atten.linear", getAttenuation().getLinear());
		this.getShader().updateUniform("pointLight.atten.exponent", getAttenuation().getExponent());
		this.getShader().updateUniform("pointLight.position", this.getParent().getTransform().getTransformedPosition());
		this.getShader().updateUniform("pointLight.range", this.getRange());
		
		this.getShader().updateUniform("cutOff", cutOff);
		this.getShader().updateUniform("coneDirection", this.getDirection());
		
	}
*/
}
