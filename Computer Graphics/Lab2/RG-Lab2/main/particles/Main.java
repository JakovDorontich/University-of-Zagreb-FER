package particles;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.gameobject.GameObject;
import br.fritzen.engine.core.gameobject.Scene;
import br.fritzen.engine.core.gameobject.components.FreeLook;
import br.fritzen.engine.core.gameobject.components.FreeMove;
import br.fritzen.engine.core.gameobject.material.ParticleTexture;
import br.fritzen.engine.core.gameobject.material.Texture;
import br.fritzen.engine.core.gameobject.sceneobjects.Camera;

public class Main extends Game{

	Texture tex = new Texture("src/main/resources/particles/balloon.bmp");
	float particlesInSecond = 100f;
	float speed = 0.25f;
	float lifeLength = 2000f; // Vrijednost u milisekundama
	ParticleSystem particleSystem = new ParticleSystem(new ParticleTexture(tex, 1), particlesInSecond, speed, lifeLength);
	
	GameObject particleEmitter;
	
	public Main() {
		super(800, 800, "Sustav cestica");
	}

	public static void main(String[] args) {
		new Main().start();
	}

	@Override
	protected void init() {
		
		this.setAmbientColor(new Vector4f(0.0f));
		
		Scene scene = Game.getScene();
		
		Camera camera = new Camera((float) Math.toRadians(70f), 720f/480f, 0.01f, 1000.0f);
		camera.getTransform().setPosition(new Vector3f(0, 5, 30));
		camera.addGameComponent(new FreeMove(this.getInput(), 0.5f));
		camera.addGameComponent(new FreeLook(this.getInput(), 0.3f, GLFW.GLFW_MOUSE_BUTTON_RIGHT));
		scene.setCamera(camera);
		
		
		particleEmitter = new GameObject();
		scene.addGameObject(particleEmitter);
		particleEmitter.getTransform().getPosition().z = 5;
		particleEmitter.addGameComponent(particleSystem);
		
	}

	@Override
	public void input() {
		super.input();
	}
	
	/*
	@Override
	protected void update() {
		super.gui.update();
		
		updateGameComponents(scene.getRootGameObject());
		scene.preProcess();
		
		particleEmitter.getTransform().getPosition().x += 0.2;
	}
	*/
	
}
