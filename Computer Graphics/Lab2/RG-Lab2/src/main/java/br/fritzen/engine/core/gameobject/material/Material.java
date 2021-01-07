package br.fritzen.engine.core.gameobject.material;

import org.joml.Vector3f;

import br.fritzen.engine.rendering.shaders.GenericShader;
import br.fritzen.engine.rendering.shadow.DepthShader;

public class Material {

	private String name;
	
	private Texture texture; //map_Kd
	
	private Texture normalMap; //map_...
	
	private Vector3f ambientColor; //Ka
	
	private Vector3f diffuseColor; //Kd
	
	private Vector3f specularColor; //Ks
	
	private float specularExponent; //Ns
	
	private float refractionIndex; //Ni
	
	private Vector3f emissiveCoeficient; //Ke
	
	private float dissolveFactor; //d
	
	private int illuminationModel; //illumn
	
	
	public Material() {
		
		createBlank();
	
	}
	
	
	public void bind(GenericShader shader) {
		
		if (!shader.getClass().equals(DepthShader.class)) {
			this.getTexture().bind(0);
			shader.updateUniform("diffuse", 0);
			
			this.getNormalMap().bind(1);
			shader.updateUniform("normalMap", 1);
			
			shader.updateUniform("color", this.getDiffuseColor());
			shader.updateUniform("specularPower", this.getSpecularExponent());
			shader.updateUniform("specularIntensity", this.getSpecularColor());
		}
	}
	
	
	public Material createBlank() {
		
		ambientColor = new Vector3f(1f);
		diffuseColor = new Vector3f(.5f);
		specularColor = new Vector3f(1f);
		
		specularExponent = 1;
		
		texture = new Texture("src/main/resources/images/blank.png");
		normalMap = new Texture("src/main/resources/images/flatNormalMap.jpg");
		
		return this;
	}

	
	public Material createDefaultMaterial() {
		return null;
	}


	public void setName(String name) {
		this.name = name;	
	}

	
	public String getName() {
		return name;
	}


	public Texture getTexture() {
		return texture;
	}


	public Material setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}


	public Vector3f getAmbientColor() {
		return ambientColor;
	}


	public void setAmbientColor(Vector3f ambientColor) {
		this.ambientColor = ambientColor;
	}


	public Vector3f getDiffuseColor() {
		return diffuseColor;
	}


	public Material setDiffuseColor(Vector3f diffuseColor) {
		this.diffuseColor = diffuseColor;
		return this;
	}


	public Vector3f getSpecularColor() {
		return specularColor;
	}


	public void setSpecularColor(Vector3f specularColor) {
		this.specularColor = specularColor;
	}


	public float getSpecularExponent() {
		return specularExponent;
	}


	public Material setSpecularExponent(float specularExponent) {
		this.specularExponent = specularExponent;
		return this;
	}


	public float getRefractionIndex() {
		return refractionIndex;
	}


	public void setRefractionIndex(float refractionIndex) {
		this.refractionIndex = refractionIndex;
	}


	public Vector3f getEmissiveCoeficient() {
		return emissiveCoeficient;
	}


	public void setEmissiveCoeficient(Vector3f emissiveCoeficient) {
		this.emissiveCoeficient = emissiveCoeficient;
	}


	public float getDissolveFactor() {
		return dissolveFactor;
	}


	public void setDissolveFactor(float dissolveFactor) {
		this.dissolveFactor = dissolveFactor;
	}


	public int getIlluminationModel() {
		return illuminationModel;
	}


	public void setIlluminationModel(int illuminationModel) {
		this.illuminationModel = illuminationModel;
	}
	
	
	public Texture getNormalMap() {
		return normalMap;
	}


	public Material setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
		return this;
	}
	
	
}
