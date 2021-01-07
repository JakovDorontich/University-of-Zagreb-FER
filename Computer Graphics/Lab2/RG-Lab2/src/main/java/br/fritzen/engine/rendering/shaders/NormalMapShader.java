package br.fritzen.engine.rendering.shaders;

import br.fritzen.engine.core.gameobject.material.Texture;

public class NormalMapShader  extends GenericShader {

	final static String FOLDER = "src/main/resources/terrain/shaders/";
	
	private static NormalMapShader instance = null; //TODO all shaders must be singletons..
	

	public NormalMapShader() {
			
		super();
		
		bind();
		
		setComputeShader(FOLDER + "normalMap.glsl");
		
		compileShader();
		
		addAllUniforms();
	}

	
	@Override
	protected void addAllUniforms() {
		
		addUniform("displacementmap");
		addUniform("N");
		addUniform("strength");
		
	}
	
	
	public void updateUniforms(Texture heightmap, int N, float strength) {
		
		heightmap.bind();
		updateUniform("displacementmap", 0);
		updateUniform("N", N);
		updateUniform("strength", strength);
		
	}


	public static NormalMapShader getInstance() {
		if (instance == null) {
			instance = new NormalMapShader();
		}
		return instance;
	}

}
