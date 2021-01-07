package br.fritzen.engine.core.gameobject.components;

import java.nio.FloatBuffer;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.BufferUtils;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.GameObject;


public class Transform {

	private Vector3f position;
	
	private Vector3f scale;
	
	private Quaternionf rotation;

	private Transform parent;
	
	private Matrix4f parentMatrix;
	
	private Matrix4f transformation;
	
	private Matrix4f unscaledTransformation;
	
	private Vector3f transformedPosition;
	
	private FloatBuffer transformationBuffer;
	
	private FloatBuffer unscaledTransformationBuffer;
	
	private AxisAngle4f axisRotation;
	
	private Quaternionf axisRotationQ;
	
	private Vector3f auxDirection = new Vector3f();
	
	
	public Transform() {
		
		parent = null;
		parentMatrix = new Matrix4f();
		
		axisRotation = new AxisAngle4f();
		axisRotationQ = new Quaternionf();
		
		position = new Vector3f(0, 0, 0);
		rotation = new Quaternionf(0, 0, 0, 1);
		scale = new Vector3f(1, 1, 1);

		transformation = new Matrix4f();
		unscaledTransformation = new Matrix4f();
		transformedPosition = new Vector3f();
		
		transformationBuffer = BufferUtils.createFloatBuffer(16);
		unscaledTransformationBuffer = BufferUtils.createFloatBuffer(16);
		
		preProcess();
	}
	
	public void preProcess() {
		
		transformation.set(this.getParentMatrix()).translate(position).rotate(rotation).scale(scale);
		
		unscaledTransformation.set(this.getParentMatrix()).translate(position).rotate(rotation);
				
		this.getParentMatrix().transformPosition(this.position, transformedPosition);
		
		transformation.get(transformationBuffer);
		unscaledTransformation.get(unscaledTransformationBuffer);
	}
	
	
	public FloatBuffer getTransformationBuffer() {
		return transformationBuffer;
	}
	
	public Matrix4f getTransformation() {
		return transformation;
	}
	
	
	public FloatBuffer getUnscaledTransformationBuffer() {
		return unscaledTransformationBuffer;
	}
	
	
	public Matrix4f getUnscaledTransformation() {
		return unscaledTransformation;
	}
	
	
	public Vector3f getTransformedPosition() {
		return transformedPosition;
	}

	
	public Matrix4f getParentMatrix() {
		
		if (this.parent != null) {
			this.parentMatrix = this.parent.getTransformation();
		}

		return this.parentMatrix;
	}
	
	
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	
	public Vector3f getPosition() {
		return position;
	}

	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	
	public Vector3f getScale() {
		return scale;
	}

	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	
	public Quaternionf getRotation() {
		return rotation;
	}

	
	public void setRotation(Quaternionf rotation) {
		this.rotation = rotation;
	}
	
	public void rotate(Vector3f axis, float angle) {
		axisRotation.set(angle, axis).get(this.axisRotationQ).mul(this.rotation, this.rotation);	
	}

	
	public Vector3f getForward() {
		return auxDirection.set(GS.Z_AXIS).mul(-1).rotate(this.rotation);
	}


	public Vector3f getBack() {
		return auxDirection.set(GS.Z_AXIS).rotate(this.rotation);
	}


	public Vector3f getRight() {
		return auxDirection.set(GS.X_AXIS).rotate(this.rotation);
	}


	public Vector3f getLeft() {
		return auxDirection.set(GS.X_AXIS).mul(-1).rotate(this.rotation);
	}


	public Vector3f getUp() {
		return auxDirection.set(GS.Y_AXIS).rotate(this.rotation);
	}


	public Vector3f getDown() {
		return auxDirection.set(GS.Y_AXIS).mul(-1).rotate(this.rotation);
	}

	
}
