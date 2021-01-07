package br.fritzen.engine.core.gameobject.sceneobjects;

import org.joml.Vector3f;

import br.fritzen.engine.core.gameobject.Scene;
import br.fritzen.engine.core.gameobject.utils.Mesh;
import br.fritzen.engine.rendering.shaders.GenericShader;

public class Skydome extends ISky {

	private SkydomeShader shader;
	
	private Mesh mesh;
	
	private Scene scene;
	
	private float scale;
	
	private Vector3f camPos;
	
	public Skydome(Scene scene, float scale) {
		super();
		
		this.scene = scene;
		
		shader = new SkydomeShader("src/main/resources/shaders/sky/skydome.vs", "src/main/resources/shaders/sky/skydome.fs");
		
		mesh = new Mesh("src/main/resources/models/dome/model.obj");
		
		this.getTransform().setScale(new Vector3f(scale, scale, scale));
		this.scale = scale;
		
		this.camPos = new Vector3f();
	}
	
	
	@Override
	public GenericShader getShader() {
		return this.shader;
	}

	@Override
	public void updateUniforms() {
		
		camPos.set(scene.getCamera().getTransform().getPosition());	
		camPos.y -= this.scale/10;;
		this.getTransform().setPosition(camPos);
		
		shader.updateUniform("view", scene.getCamera().getViewBuffer());
		shader.updateUniform("projection", scene.getCamera().getProjectionBuffer());
		shader.updateUniform("model", this.getTransform().getTransformationBuffer());
		shader.updateUniform("scale", this.scale);
	}

	@Override
	public void render() {
		mesh.draw();
	}

}
