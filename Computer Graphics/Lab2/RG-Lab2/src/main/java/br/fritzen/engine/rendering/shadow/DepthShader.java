package br.fritzen.engine.rendering.shadow;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import br.fritzen.engine.core.gameobject.GameObject;
import br.fritzen.engine.rendering.shaders.GenericShader;

public class DepthShader extends GenericShader {

	private static DepthShader instance = null;
	
	public Matrix4f lightViewMatrix;
	
	private FloatBuffer buffer;
	
	private FloatBuffer orthoBuffer;
	
	public DepthShader(String filenameVertexShader, String filenameFragmentShader) {
	
		super(filenameVertexShader, filenameFragmentShader);
		lightViewMatrix = new Matrix4f();
		buffer = BufferUtils.createFloatBuffer(16);
		orthoBuffer = BufferUtils.createFloatBuffer(16);
	}

	
	public static DepthShader getInstance() {
		
		if (instance == null) {
			instance = new DepthShader("src/main/resources/shaders/shadows/shadow.vs", "src/main/resources/shaders/shadows/shadow.fs");
		}
		
		return instance;
		
	}
	
	
	@Override
	protected void addAllUniforms() {
		addUniform("lightViewMatrix");
		addUniform("orthoProjectionMatrix");
		addUniform("model");
	}
	
	
	@Override
	public void setAttributes() {
		
		GL20.glBindAttribLocation(this.getShaderProgram(), 0, "position");
		GL20.glBindAttribLocation(this.getShaderProgram(), 1, "texCoord");
		GL20.glBindAttribLocation(this.getShaderProgram(), 2, "vertexNormal");
		GL20.glBindAttribLocation(this.getShaderProgram(), 3, "tangent");
		
	}

	
	public void updateUniforms(Matrix4f orthoProjectionMatrix) {
		
		this.updateUniform("orthoProjectionMatrix", orthoProjectionMatrix.get(orthoBuffer));
		
	}
	
	
	@Override
	public void updateObjectUniforms(GameObject gameObject) {
		
		this.updateUniform("model", gameObject.getTransform().getTransformationBuffer());
		this.updateUniform("lightViewMatrix", lightViewMatrix.get(buffer));

	}
}
