package br.fritzen.engine.terrain;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

import br.fritzen.engine.utils.EngineBufferUtils;

public class PatchVBO {

	private int vboId;
	private int vaoId;
	private int size;
	
	
	public PatchVBO() {
		
		vboId = GL15.glGenBuffers();
		vaoId = GL30.glGenVertexArrays();
		
	}
	
	
	public void allocate(Vector2f[] vertices, int patchSize) {
		
		size = vertices.length;
		
		GL30.glBindVertexArray(vaoId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, EngineBufferUtils.createFlippedBuffer(vertices), GL15.GL_STATIC_DRAW);
		
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, Float.BYTES * 2, 0);
		GL40.glPatchParameteri(GL40.GL_PATCH_VERTICES, patchSize);
		
		GL30.glBindVertexArray(0);
	}
	
	
	public void draw() {
		
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glDrawArrays(GL40.GL_PATCHES, 0, size);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		
	}
	
		
	public void delete() {
		
		GL30.glBindVertexArray(vaoId);
		GL15.glDeleteBuffers(vboId);
		GL15.glDeleteBuffers(vaoId);
		GL30.glBindVertexArray(0);
	}
	
}
