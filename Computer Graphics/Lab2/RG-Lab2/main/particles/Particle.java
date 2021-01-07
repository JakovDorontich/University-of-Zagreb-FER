package particles;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.Game;

public class Particle {
	
	private Matrix4f modelMatrix;
	private Matrix4f modelViewMatrix;
	private Matrix4f viewMatrix;
	
	private Vector3f position;
	private Vector3f velocity;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private float elapsedTime = 0;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float texBlend;
	
	public Particle(Vector3f position, Vector3f velocity, float lifeLength, float rotation, float scale) {
		
		this.position = position;
		this.velocity = velocity;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		
		this.modelMatrix = new Matrix4f();
		this.modelViewMatrix = new Matrix4f();
		
	}
	
	
	public boolean updateOrDie() {
		position.add(velocity);
		elapsedTime++;
		return !(elapsedTime < lifeLength / 10);
	}
	

	public Matrix4f updateModelViewMatrix() {
		
		viewMatrix = Game.getScene().getCamera().getView();
		
		modelMatrix.identity();
		modelMatrix.translate(position);
		
		modelMatrix.m00(viewMatrix.m00());
	    modelMatrix.m01(viewMatrix.m10());
	    modelMatrix.m02(viewMatrix.m20());
	    modelMatrix.m10(viewMatrix.m01());
	    modelMatrix.m11(viewMatrix.m11());
	    modelMatrix.m12(viewMatrix.m21());
	    modelMatrix.m20(viewMatrix.m02());
	    modelMatrix.m21(viewMatrix.m12());
	    modelMatrix.m22(viewMatrix.m22());
	    
	    modelMatrix.rotate((float)(Math.toRadians(this.getRotation())), GS.Z_AXIS);
	    modelMatrix.scale(this.getScale());
	    
	    modelViewMatrix.identity();
	    viewMatrix.mul(modelMatrix, modelViewMatrix);
		
	    return modelViewMatrix;
	}


	
	
	public Vector3f getPosition() {
		return position;
	}


	public float getRotation() {
		return rotation;
	}


	public float getScale() {
		return scale;
	}
	
	
	public Vector2f getTexOffset1() {
		return texOffset1;
	}


	public Vector2f getTexOffset2() {
		return texOffset2;
	}


	public float getTexBlend() {
		return texBlend;
	}


	public Vector3f getVelocity() {
		return velocity;
	}


	public void setLifeLength(float lifeLength) {
		this.lifeLength = lifeLength;
	}


	public void setElapsedTime(float elapsedTime) {
		this.elapsedTime = elapsedTime;
	}


	public void setPosition(Vector3f position) {
		this.position = position;
	}


	public void setRotation(float rotation) {
		this.rotation = rotation;
	}


	public void setScale(float scale) {
		this.scale = scale;
	}

	
	

}
