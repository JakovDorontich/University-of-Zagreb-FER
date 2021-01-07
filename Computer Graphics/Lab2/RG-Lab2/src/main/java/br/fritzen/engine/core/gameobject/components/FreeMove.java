package br.fritzen.engine.core.gameobject.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Vector3f;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.core.input.Input;

public class FreeMove extends GameComponent {

	private Input input;
	
	private float speed;

	private int forwardKey;

	private int backKey;

	private int leftKey;

	private int rightKey;

	private int upKey;

	private int downKey;


	public FreeMove(Input input, float speed) {
		this(input, speed, GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_S, GLFW_KEY_D, GLFW_KEY_R, GLFW_KEY_F);
	}


	public FreeMove(Input input, float speed, int forwardKey, int leftKey, int backKey, int rightKey, int upKey, int downKey) {
		
		this.input = input;
		this.speed = speed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.upKey = upKey;
		this.downKey = downKey;
	}


	@Override
	public void input() {
		
		Transform transform = this.getParent().getTransform();
		
		if (input.getKey(forwardKey)) {
			this.move(transform, transform.getForward(), speed);
		}

		if (input.getKey(backKey)) {
			this.move(transform, transform.getForward(), -speed);
		}

		if (input.getKey(upKey)) {
			this.move(transform, transform.getUp(), speed);
		}

		if (input.getKey(downKey)) {
			this.move(transform, transform.getUp(), -speed);
		}

		if (input.getKey(leftKey)) {
			this.move(transform, transform.getLeft(), speed);
		}

		if (input.getKey(rightKey)) {
			this.move(transform, transform.getRight(), speed);
		}
	}


	private void move(Transform t, Vector3f direction, float value) {

		t.getPosition().add(direction.mul(value));

	}
	
	
	@Override
	public String getComponentName() {
		return GS.COMP_FREE_MOVE;
	}
}
