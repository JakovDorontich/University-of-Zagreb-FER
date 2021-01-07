package br.fritzen.engine.core.gameobject.utils;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.rendering.shaders.SimpleShader;
import br.fritzen.engine.utils.EngineBufferUtils;

public class BoundingBox extends GameComponent {

	public float minX, maxX, minY, maxY, minZ, maxZ;

	private int vao, vbo, ibo;
	
	private int sizeIndices;
	
	private Vector3f color;
	
	private SimpleShader customShader;
	
	private ArrayList<Vector3f> vertices;
	
	private ArrayList<Vector3f> transformedVertices;
	
	private int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 0, 1, 5, 4, 2, 3, 7, 6};
	
	private int iterator;
	
	
	public BoundingBox(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
		
		
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.minZ = minZ;
		this.maxZ = maxZ;
		
		this.color = new Vector3f(0, 1, 0);
		
		
		
		this.customShader = SimpleShader.getInstance();
		
		vertices = new ArrayList<Vector3f>();
		transformedVertices = new ArrayList<Vector3f>();
		
		vertices.add(new Vector3f(minX, minY, minZ)); vertices.add(color);
		vertices.add(new Vector3f(minX, minY, maxZ)); vertices.add(color);
		vertices.add(new Vector3f(maxX, minY, maxZ)); vertices.add(color);
		vertices.add(new Vector3f(maxX, minY, minZ)); vertices.add(color);
		
		vertices.add(new Vector3f(minX, maxY, minZ)); vertices.add(color);
		vertices.add(new Vector3f(minX, maxY, maxZ)); vertices.add(color);
		vertices.add(new Vector3f(maxX, maxY, maxZ)); vertices.add(color);
		vertices.add(new Vector3f(maxX, maxY, minZ)); vertices.add(color);
		
		
		transformedVertices.add(new Vector3f(minX, minY, minZ)); 
		transformedVertices.add(new Vector3f(minX, minY, maxZ)); 
		transformedVertices.add(new Vector3f(maxX, minY, maxZ)); 
		transformedVertices.add(new Vector3f(maxX, minY, minZ)); 
		
		transformedVertices.add(new Vector3f(minX, maxY, minZ));
		transformedVertices.add(new Vector3f(minX, maxY, maxZ)); 
		transformedVertices.add(new Vector3f(maxX, maxY, maxZ)); 
		transformedVertices.add(new Vector3f(maxX, maxY, minZ)); 
		
		
		sizeIndices = indices.length;
		
		this.vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
		
		this.vbo = GL15.glGenBuffers();
		this.ibo = GL15.glGenBuffers();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, EngineBufferUtils.createFloatPositionBuffer(vertices), GL15.GL_DYNAMIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, EngineBufferUtils.createFlippedBuffer(indices), GL15.GL_DYNAMIC_DRAW);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * 4, 0);	// 3 positions * 4 bytes (3 floats - position)
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * 4, 12);
		
		GL30.glBindVertexArray(0);
	}
	
	
	@Override
	public void renderOnce() {
		
		this.customShader.bind();
		
		
		this.customShader.updateUniform("view", Game.getScene().getCamera().getViewBuffer());
		this.customShader.updateUniform("projection", Game.getScene().getCamera().getProjectionBuffer());
		this.customShader.updateUniform("model", this.getParent().getTransform().getTransformationBuffer());
		
		GL11.glLineWidth(2f); //line width
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE); //wireframe
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL30.glBindVertexArray(this.vao);
		GL11.glDrawElements(GL11.GL_QUADS, sizeIndices * 2, GL11.GL_UNSIGNED_INT, 0);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glLineWidth(1f);
		
		
		GL30.glBindVertexArray(0);
		this.customShader.unbind();
		
	}


	public void setColor(Vector3f color) {
		
		if (this.color.equals(color))
			return;
		
		this.color = color;
		
		for (iterator = 1; iterator < 16; iterator+=2)
			vertices.set(iterator, color);
		
		GL30.glBindVertexArray(this.vao);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, EngineBufferUtils.createFloatPositionBuffer(vertices), GL15.GL_DYNAMIC_DRAW);
		GL30.glBindVertexArray(0);
	}


	public void transform(Matrix4f transformation) {
		
		
		for (iterator = 0; iterator < transformedVertices.size(); iterator++) {
			vertices.get(iterator*2).mulPosition(transformation, transformedVertices.get(iterator));
		}
		
		minX = transformedVertices.get(0).x;
		minY = transformedVertices.get(0).y;
		minZ = transformedVertices.get(0).z;
		maxX = transformedVertices.get(0).x;
		maxY = transformedVertices.get(0).y;
		maxZ = transformedVertices.get(0).z;
		
		for (iterator = 0; iterator < transformedVertices.size(); iterator++) {
			
			Vector3f v = transformedVertices.get(iterator);
			
			if (v.x < minX) {
				minX = v.x;
			}
			
			if (v.x > maxX) {
				maxX = v.x;
			}
			
			if (v.y < minY) {
				minY = v.y;
			}
			
			if (v.y > maxY) {
				maxY = v.y;
			}
			
			if (v.z < minZ) {
				minZ = v.z;
			}
			
			if (v.z > maxZ) {
				maxZ = v.z;
			}
			
		}
		
	}
	
	
	@Override
	public String toString() {
		return String.format("Min %f, %f, %f\tMax %f, %f, %f", minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	
	@Override
	public String getComponentName() {
		return GS.COMP_BOUNDING_BOX;
	}
}
