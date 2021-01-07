package particles;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.core.gameobject.material.ParticleTexture;
import br.fritzen.engine.rendering.shaders.ParticleShader;

public class ParticleSystem extends GameComponent {

	public int blendMode = GL11.GL_ONE;
	private ParticleShader shader;
	private ArrayList<Particle> particles;
	private float particlesInSecond;
	private float speed;
	private float lifeLength;
	private float scale;
	private ParticleTexture texture;
	
	private int index = 0;
	
	private float vrhoviUSceni[];
	
	
	public ParticleSystem(ParticleTexture particleTexture, float particlesInSecond, float speed, float lifeLength) {

		this.shader = ParticleShader.getInstance();
		this.particles = new ArrayList<Particle>();
		this.texture = particleTexture;
		this.particlesInSecond = particlesInSecond;
		this.speed = speed;
		this.lifeLength = lifeLength;
		this.scale = 1;
		
		vrhoviUSceni = new float[(int)(particlesInSecond * lifeLength/1000 + 1) * ParticleShader.INSTANCE_DATA_LENGTH];
	}

	
	public void addParticle(Particle particle) {
		this.particles.add(particle);
	}

	
	@Override
	public void update() {

		if (particles.size() < particlesInSecond * lifeLength/1000) {
			
			particles.add(addParticle(this.getParent().getTransform().getTransformedPosition(), null));
		}
		
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).updateOrDie() == true) {
				if (particles.size() < particlesInSecond * lifeLength/1000) {
					addParticle(this.getParent().getTransform().getTransformedPosition(), particles.get(i));
				} else {
					particles.remove(i);
					i--;
				}
			}
			
		}

		index = 0;
		for (Particle p : particles) {
			storeMatrixData(p.updateModelViewMatrix(), vrhoviUSceni);
			updateTexCoordInfo(p, vrhoviUSceni);
		}
		
		this.shader.bind();
		GL30.glBindVertexArray(shader.getParticleVao());
		shader.updateVBO(vrhoviUSceni);
	}
	
	
	private Vector3f velocity = new Vector3f();
	private Particle addParticle(Vector3f center, Particle particle) {
		
		velocity = generateRandomUnitVector();
		velocity.normalize();
		velocity.mul(speed);
		
		scale = (float)(0.1 + Math.random()*(3-0.1));
		
		if (particle == null) {
			
			return new Particle(new Vector3f(center), new Vector3f(velocity), lifeLength, (float)(Math.random() * 360f), scale);
			
		} else {
			
			particle.getPosition().set(center);
			particle.getVelocity().set(velocity);
			particle.setElapsedTime(0);
			particle.setLifeLength(lifeLength);
			particle.setRotation((float)(Math.random() * 360f));
			particle.setScale(scale);
			
			return particle;
		}
	}

	
	private Vector3f generateRandomUnitVector() {
		float z = (float) (-1 + Math.random()*2);
		float x = (float) (-1 + Math.random()*2);
		float y = (float) (-1 + Math.random()*2);
		return new Vector3f(x, y, z);
	}

	
	@Override
	public void renderOnce() {
		this.shader.bind();
		GL30.glBindVertexArray(shader.getParticleVao());
		
		shader.updateUniform("projectionMatrix", Game.getScene().getCamera().getProjectionBuffer());
		shader.updateUniform("numberOfRows", (float)texture.getNumberOfRows());
		this.texture.getTexture().bind(0);
				
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, blendMode);
		GL11.glDepthMask(false);
		
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, 4, particles.size());
		
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);

		GL30.glBindVertexArray(0);
		this.shader.unbind();
	}

	
	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[index++] = matrix.m00();
		vboData[index++] = matrix.m01();
		vboData[index++] = matrix.m02();
		vboData[index++] = matrix.m03();
		vboData[index++] = matrix.m10();
		vboData[index++] = matrix.m11();
		vboData[index++] = matrix.m12();
		vboData[index++] = matrix.m13();
		vboData[index++] = matrix.m20();
		vboData[index++] = matrix.m21();
		vboData[index++] = matrix.m22();
		vboData[index++] = matrix.m23();
		vboData[index++] = matrix.m30();
		vboData[index++] = matrix.m31();
		vboData[index++] = matrix.m32();
		vboData[index++] = matrix.m33();
	}
	
	private void updateTexCoordInfo(Particle particle, float[] data) {
		data[index++]  = particle.getTexOffset1().x;
		data[index++]  = particle.getTexOffset1().y;
		data[index++]  = particle.getTexOffset2().x;
		data[index++]  = particle.getTexOffset2().y;
		data[index++]  = particle.getTexBlend();
	}
	
	
	@Override
	public String getComponentName() {
		return ParticleSystem.class.getSimpleName();
	}


	
}
