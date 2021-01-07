package br.fritzen.engine.core.gameobject.sceneobjects;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import br.fritzen.engine.core.gameobject.GameObject;

public class Camera extends GameObject {

	private Matrix4f projection;

	private FloatBuffer projectionBuffer;
	
	private Matrix4f view;
	
	private FloatBuffer viewBuffer;

	private Matrix4f viewProjection;
	
	private FloatBuffer viewProjectionBuffer;
	
	private Vector3f cameraPos;
	
	private Quaternionf viewQ;
	
	private float fov;
	
	private float aspect;
	
	private float zNear;
	
	private float zFar;
	
	
	public Camera(float fov, float aspect, float zNear, float zFar) {
		
		this.fov = fov;
		this.aspect = aspect;
		this.zNear = zNear;
		this.zFar = zFar;
		
		this.cameraPos = new Vector3f(); 
		this.viewQ = new Quaternionf();
		
		this.projection = new Matrix4f().perspective(fov, aspect, zNear, zFar);
		this.projectionBuffer = BufferUtils.createFloatBuffer(16);
		this.projection.get(projectionBuffer);
		
		this.view = new Matrix4f();
		this.viewBuffer = BufferUtils.createFloatBuffer(16);
		this.view.get(viewBuffer);
		
		this.viewProjection = new Matrix4f();
		this.viewProjection.set(projection).mul(view);
		this.viewProjectionBuffer = BufferUtils.createFloatBuffer(16);
		this.viewProjection.get(viewProjectionBuffer);
		
	}

	
	public void updatePerspective(float aspect) {
	
		this.aspect = aspect;
		this.projection = new Matrix4f().perspective(this.fov, this.aspect, this.zNear, this.zFar);
		this.projection.get(projectionBuffer);
	
	}

	
	public Matrix4f getProjection() {
		return this.projection;
	}

/*
	public Matrix4f getViewProjection() {

		Matrix4f cameraRotation = new Quaternionf(getTransform().getRotation()).conjugate().get(new Matrix4f());
		Vector3f cameraPos = new Vector3f(this.getTransform().getTransformedPosition()).mul(-1);
		//Matrix4f cameraTranslation = new Matrix4f().translate(cameraPos.x, cameraPos.y, cameraPos.z);
		//return new Matrix4f(this.projection).mul(cameraRotation.mul(cameraTranslation));
		
		return new Matrix4f(this.projection).mul(cameraRotation.translate(cameraPos));
	}
*/

	@Override
	public void preProcess() {
		
		super.preProcess();
		this.cameraPos.set(this.getTransform().getTransformedPosition()).mul(-1);
		viewQ.set(getTransform().getRotation()).conjugate().get(view).translate(cameraPos);
		
		this.view.get(viewBuffer);
		
		this.viewProjection.set(projection).mul(view);
		this.viewProjection.get(viewProjectionBuffer);
			
	}
	
	
	public Matrix4f getView() {
		return this.view;
	}
	
	
	public Matrix4f getViewProjection() {
		return this.viewProjection;
	}

	
	public FloatBuffer getProjectionBuffer() {
		return this.projectionBuffer;
	}

	
	public FloatBuffer getViewBuffer() {
		return this.viewBuffer;
	}
	
	
	public FloatBuffer getViewProjectionBuffer() {
		return this.viewProjectionBuffer;
	}


	public Vector3f getCameraPos() {
		return cameraPos;
	}


	public float getFOV() {
		return this.fov;
	}


	public float getAspect() {
		return aspect;
	} 
	
	
	
}
