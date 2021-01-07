package br.fritzen.engine.core.gameobject.utils;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.rendering.shaders.SimpleShader;
import br.fritzen.engine.utils.EngineBufferUtils;

public class Axis3D extends GameComponent {

	protected static class Axis3DVao {
		
		private static Axis3DVao instance;
		
		private int vao;
		private int vbo, ibo;
		
		
		private Axis3DVao() {
		
			Vector3f red = new Vector3f(1, 0, 0);
			Vector3f green = new Vector3f(0, 1, 0);
			Vector3f blue = new Vector3f(0, 0, 1);
					
			ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
			int s = 2;
			
			vertices.add(new Vector3f(0, 0, 0)); vertices.add(red);
			vertices.add(new Vector3f(s, 0, 0)); vertices.add(red);
			
			vertices.add(new Vector3f(0, 0, 0)); vertices.add(green);
			vertices.add(new Vector3f(0, s, 0)); vertices.add(green);
			
			vertices.add(new Vector3f(0, 0, 0)); vertices.add(blue);
			vertices.add(new Vector3f(0, 0, s)); vertices.add(blue);
				
			int[] indices = {0, 1, 2, 3, 4, 5};
			
			
			this.vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);
			
			this.vbo = GL15.glGenBuffers();
			this.ibo = GL15.glGenBuffers();
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, EngineBufferUtils.createFloatPositionBuffer(vertices), GL15.GL_STATIC_DRAW);
			
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, EngineBufferUtils.createFlippedBuffer(indices), GL15.GL_STATIC_DRAW);
			
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
			GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 6 * 4, 0);	// 3 positions * 4 bytes (3 floats - position)
			GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 6 * 4, 12);
			
			GL30.glBindVertexArray(0);
		}
		
		
		public static Axis3DVao getInstance() {
		
			if (instance == null) {
				instance = new Axis3DVao();
			}
			return instance;
		}
		
		
		public int getVao() {
			return this.vao;
		}
	}
	
	
	private int vao;
	
	private SimpleShader customShader;
	
	public Axis3D() {
		
		this.customShader = SimpleShader.getInstance();
		this.vao = Axis3DVao.getInstance().getVao();
		
	}
	
	
	@Override
	public void renderOnce() {
		
		this.customShader.bind();
		
		this.customShader.updateUniform("view", this.getParent().getScene().getCamera().getViewBuffer());
		this.customShader.updateUniform("projection", this.getParent().getScene().getCamera().getProjectionBuffer());
		this.customShader.updateUniform("model", this.getParent().getTransform().getUnscaledTransformationBuffer());
		
		GL11.glLineWidth(2f); //line width
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE); //wireframe
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL30.glBindVertexArray(this.vao);
		GL11.glDrawElements(GL11.GL_LINES, 12, GL11.GL_UNSIGNED_INT, 0);
		
		GL30.glBindVertexArray(0);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		GL11.glLineWidth(1f);
		
		this.customShader.unbind();
		
	}
	
	
	@Override
	public String getComponentName() {
		return GS.COMP_AXIS_3D;
	}
}
