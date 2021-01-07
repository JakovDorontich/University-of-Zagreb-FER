package br.fritzen.engine.core.gameobject.components;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.gameobject.GameComponent;
import br.fritzen.engine.core.gameobject.material.Material;
import br.fritzen.engine.core.gameobject.utils.Mesh;
import br.fritzen.engine.rendering.shaders.GenericShader;

public class MeshRenderer extends GameComponent {

	
	private Mesh mesh;
	
	private Material material;
	
	
	public MeshRenderer(Mesh mesh) {
		this(mesh, null);
	}
	
	
	public MeshRenderer(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
		
	}
	
	
	@Override
	public void render(GenericShader shader) {
		
		this.mesh.draw(shader, material);
		
	}
	
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	
	public void setMaterial(Material material) {
		this.material = material;
	}


	@Override
	public String getComponentName() {
		return GS.COMP_MESH_RENDERER;
	}
}

