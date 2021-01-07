package br.fritzen.engine.core.gameobject.utils;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.rendering.shaders.SimpleShader;
import br.fritzen.engine.utils.EngineBufferUtils;

public class Line3D extends GameComponent {

	private int vbo, ibo;
	
	private int sizeIndices;
	
	private SimpleShader customShader;
	
	public Line3D(Vector3f origin, Vector3f dest) {
		
		this.customShader = SimpleShader.getInstance();
		
		Vector3f red = new Vector3f(1, 0, 0);
				
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
		
		vertices.add(origin); vertices.add(red);
		vertices.add(dest); vertices.add(red);
			
		int[] indices = {0, 1};
		
		sizeIndices = indices.length;
		
		this.vbo = GL15.glGenBuffers();
		this.ibo = GL15.glGenBuffers();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, EngineBufferUtils.createFloatPositionBuffer(vertices), GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, EngineBufferUtils.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
	}
	
	
	@Override
	public void renderOnce() {
		
		this.customShader.bind();
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * 4, 0);	// 3 positions * 4 bytes (3 floats - position)
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * 4, 12);
		
		this.customShader.updateUniform("view", this.getParent().getScene().getCamera().getViewBuffer());
		this.customShader.updateUniform("projection", this.getParent().getScene().getCamera().getProjectionBuffer());
		this.customShader.updateUniform("model", this.getParent().getTransform().getUnscaledTransformationBuffer());
		
		GL11.glLineWidth(1f); //line width
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE); //wireframe
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
		GL11.glDrawElements(GL11.GL_LINES, sizeIndices * 2, GL11.GL_UNSIGNED_INT, 0);
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glLineWidth(1f);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		
		this.customShader.unbind();
		
	}
	
	
	@Override
	public String getComponentName() {
		return GS.COMP_LINE_3D;
	}
}
