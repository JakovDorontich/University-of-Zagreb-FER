package br.fritzen.engine.core.gameobject.sceneobjects;

import br.fritzen.engine.core.gameobject.GameObject;
import br.fritzen.engine.rendering.shaders.GenericShader;

public abstract class ISky extends GameObject {

	public abstract GenericShader getShader();

	public void updateUniforms() {}

	public void render() {}

	
	
	
}
