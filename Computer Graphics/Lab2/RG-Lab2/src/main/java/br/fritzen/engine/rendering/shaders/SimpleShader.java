package br.fritzen.engine.rendering.shaders;

import org.lwjgl.opengl.GL20;

/**
 * This shader class consider just position and color to render.
 * It is fast and projected to add small debuf features as boxcolliders, axis3d, ...
 * 
 * @author Vinicius
 *
 */
public class SimpleShader extends GenericShader {

	private static SimpleShader instance = null;
	
	public SimpleShader(String filenameVertexShader, String filenameFragmentShader) {
		super(filenameVertexShader, filenameFragmentShader);
	}

	
	@Override
	protected void addAllUniforms() {
		addUniform("model");
		addUniform("view");
		addUniform("projection");
	}
	
	
	@Override
	public void setAttributes() {
		GL20.glBindAttribLocation(this.getShaderProgram(), 0, "position");
		GL20.glBindAttribLocation(this.getShaderProgram(), 1, "color");
	}

	public static SimpleShader getInstance() {
		
		if (instance == null) {
			instance = new SimpleShader("src/main/resources/shaders/poscolor/generic.vs", "src/main/resources/shaders/poscolor/generic.fs");
		}
		
		return instance;
		
	}
		
}
