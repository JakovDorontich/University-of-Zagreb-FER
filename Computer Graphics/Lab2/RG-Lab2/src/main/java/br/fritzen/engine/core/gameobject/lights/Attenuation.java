package br.fritzen.engine.core.gameobject.lights;

public class Attenuation {

	private float constant;

	private float linear;

	private float exponent;

	
	public Attenuation() {
		this(0.0f, 0.0f, 0.1f);
	}

	
	public Attenuation(float constant, float linear, float exponent) {
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
	}


	public float getConstant() {
		return constant;
	}


	public void setConstant(float constant) {
		this.constant = constant;
	}


	public float getLinear() {
		return linear;
	}


	public void setLinear(float linear) {
		this.linear = linear;
	}


	public float getExponent() {
		return exponent;
	}


	public void setExponent(float exponent) {
		this.exponent = exponent;
	}


	@Override
	public String toString() {
		return "Attenuation: \n\tconstant: " + constant + "\n\tlinear: " + linear + "\n\texponent: " + exponent;
	}

}
