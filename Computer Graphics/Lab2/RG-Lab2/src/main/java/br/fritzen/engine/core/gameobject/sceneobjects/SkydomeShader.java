package br.fritzen.engine.core.gameobject.sceneobjects;

import br.fritzen.engine.rendering.shaders.GenericShader;

public class SkydomeShader extends GenericShader{

	public SkydomeShader(String filenameVertexShader, String filenameFragmentShader) {
		super(filenameVertexShader, filenameFragmentShader);
		
	}

	@Override
	protected void addAllUniforms() {
		addUniform("model");
		addUniform("view");
		addUniform("projection");
		addUniform("scale");
	}
	
}
