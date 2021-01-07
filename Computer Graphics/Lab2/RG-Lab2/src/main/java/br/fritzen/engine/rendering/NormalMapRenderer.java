package br.fritzen.engine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;

import br.fritzen.engine.core.gameobject.material.Texture;
import br.fritzen.engine.rendering.shaders.NormalMapShader;

public class NormalMapRenderer {

	private float strength;
	
	private Texture normalmap;
	
	private NormalMapShader shader;
	
	private int N;
	
	
	public NormalMapRenderer(int N, float strength) {
		this(N);
		this.setStrength(strength);
	}
	
	public NormalMapRenderer(int N) {
		this.N = N;
		shader = NormalMapShader.getInstance();
		normalmap = new Texture();
		normalmap.bilinearFilter();
		
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, (int) (Math.log(N)/Math.log(2)), GL30.GL_RGBA32F, N, N);
		
	}

	
	public NormalMapRenderer render(Texture texture) {
		
		shader.bind();
		shader.updateUniforms(texture, N, strength);
		GL42.glBindImageTexture(0, normalmap.getId(), 0, false, 0, GL15.GL_WRITE_ONLY, GL30.GL_RGBA32F);
		GL43.glDispatchCompute(N/16, N/16, 1);
		GL11.glFinish();
		
		normalmap.bind();
		normalmap.bilinearFilter();
		
		return this;
	}
	

	public float getStrength() {
		return strength;
	}


	public void setStrength(float strength) {
		this.strength = strength;
	}


	public Texture getNormalMap() {
		return normalmap;
	}


	public void setNormalMap(Texture normalmap) {
		this.normalmap = normalmap;
	}
	
	
	
}
