package br.fritzen.engine.rendering.shaders;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import br.fritzen.engine.utils.EngineBufferUtils;

public class ParticleShader extends GenericShader {

	private static int MAX_INSTANCES = 1_000_000;
	
	public static int INSTANCE_DATA_LENGTH = 21;
	
	private static ParticleShader instance;
	
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	
	
	protected static class ParticleVao {
		
		private static final float[] VERTICES = {-0.5f, 0.5f,  -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
		
		private static ParticleVao instance;

		private int vao;
		private int vbo;
		private int vbo2;
		
		
		
		private ParticleVao() {
		
			this.vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);
			
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL20.glEnableVertexAttribArray(3);
			GL20.glEnableVertexAttribArray(4);
			GL20.glEnableVertexAttribArray(5);
			GL20.glEnableVertexAttribArray(6);
			
			this.vbo = GL15.glGenBuffers();
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, EngineBufferUtils.createFloatPositionBuffer(VERTICES), GL15.GL_STATIC_DRAW);
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
			GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 2 * 4, 0);
						
			this.vbo2 = createVBO(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
			
			addInstancedAttrib(vao, vbo2, 1, 4, INSTANCE_DATA_LENGTH, 0);
			addInstancedAttrib(vao, vbo2, 2, 4, INSTANCE_DATA_LENGTH, 4);
			addInstancedAttrib(vao, vbo2, 3, 4, INSTANCE_DATA_LENGTH, 8);
			addInstancedAttrib(vao, vbo2, 4, 4, INSTANCE_DATA_LENGTH, 12);
			addInstancedAttrib(vao, vbo2, 5, 4, INSTANCE_DATA_LENGTH, 16);
			addInstancedAttrib(vao, vbo2, 6, 1, INSTANCE_DATA_LENGTH, 20);
			
			GL30.glBindVertexArray(0);
		}
		
		
		private int createVBO(int count) {
			
			int vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, count * 4, GL15.GL_STREAM_DRAW);
			return vbo;
		}
		
		
		private void addInstancedAttrib(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
			
			GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);	
			GL33.glVertexAttribDivisor(attribute, 1);

		}
		
		
		public static ParticleVao getInstance() {
		
			if (instance == null) {
				instance = new ParticleVao();
			}
			return instance;
		}
		
		
		public int getVao() {
			return this.vao;
		}


		public int getVbo2() {
			return this.vbo2;
		}

	}
	
	
	public static ParticleShader getInstance() {
		
		if (instance == null) {
			instance = new ParticleShader();
		}
		
		return instance;
	}
	
	
	private ParticleShader() {
		
		super();
		
		bind();
		
		setVertexShader("src/main/resources/shaders/particles/vertex.glsl");
		setFragmentShader("src/main/resources/shaders/particles/fragment.glsl");
		
		compileShader();
		
		bind();
		
		GL20.glBindAttribLocation(getShaderProgram(), 0, "position");
		GL20.glBindAttribLocation(getShaderProgram(), 1, "modelViewMatrix");
		GL20.glBindAttribLocation(getShaderProgram(), 5, "texOffsets");
		GL20.glBindAttribLocation(getShaderProgram(), 6, "blendFactor");
		
		
		addUniform("projectionMatrix");
		addUniform("numberOfRows");
		addUniform("particleTexture");
		
	}


	public int getParticleVao() {
		return ParticleVao.getInstance().getVao();
	}
	
	
	public void updateVBO(float[] data) {
		
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		
		//GL30.glBindVertexArray(ParticleVao.getInstance().getVao());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ParticleVao.getInstance().getVbo2());
		
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		//System.out.println(data.length);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		//GL30.glBindVertexArray(0);
		
	}
}
