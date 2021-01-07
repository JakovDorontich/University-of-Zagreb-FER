package br.fritzen.engine.core.gameobject.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.core.input.Input;

public class FreeLook extends GameComponent {

	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);

	private boolean mouseLock = false;

	private int lockKey;

	private Vector2f centerPosition;
	
	private float sensitivity;

	private float mtemp = 0.0f;

	private Input input;
	
	private Vector2f deltaPos;

	
	public FreeLook(Input inputSystem, int lock) {
		this(inputSystem, 0.5f, lock);
	}
	
	
	public FreeLook(Input inputSystem, float sensitivity, int lock) {
		this.input = inputSystem;
		this.sensitivity = sensitivity;
		this.lockKey = lock;
		centerPosition = new Vector2f(0, 0);
		deltaPos = new Vector2f();
	}


	@Override
	public void input() {
		
		int mouseClick = input.getMouseClick();
		
		if (mouseClick == lockKey && mouseLock == false) {
			mouseLock = true;
			centerPosition.set(input.getMousePosition());
		} else if (mouseClick != lockKey){
			mouseLock = false;
		}
		
		mtemp += 0.1f;
		if (mouseLock && mtemp >= 0.3f && input.getMouseState() == Input.HOLD) {
			
			deltaPos.set(input.getMousePosition());
			deltaPos.sub(centerPosition);
			
			Transform transform = this.getParent().getTransform();
			
			transform.rotate(Y_AXIS, (float) Math.toRadians(deltaPos.x * -sensitivity));
					
			transform.rotate(transform.getRight(),
					(float) Math.toRadians(-deltaPos.y * sensitivity));
			
			mtemp = 0.0f;
			centerPosition.set(input.getMousePosition());
			
		}

	}


	@Override
	public String getComponentName() {
		return GS.COMP_FREE_LOOK;
	}
	
}
