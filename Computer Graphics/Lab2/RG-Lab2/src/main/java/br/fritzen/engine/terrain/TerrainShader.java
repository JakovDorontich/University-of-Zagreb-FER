package br.fritzen.engine.terrain;

import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.gameobject.sceneobjects.Camera;
import br.fritzen.engine.rendering.shaders.GenericShader;

public class TerrainShader extends GenericShader {

	final static String FOLDER = "src/main/resources/terrain/shaders/";
	
	
	public TerrainShader() {
	
		super(FOLDER + "vertex.glsl", FOLDER + "fragment.glsl", false);
		bind();
		
		setTesselationControlShader(FOLDER + "tesselationControl.glsl");
		setTesselationEvaluationShader(FOLDER + "tesselationEvaluation.glsl");
		setGeometryShader(FOLDER + "geometry.glsl");
		
		compileShader();
		
		addAllUniforms();
		
	}

	
	@Override
	protected void addAllUniforms() {
		
		addUniform("localMatrix");
		addUniform("worldMatrix");
		addUniform("scaleY");
		addUniform("index");
		addUniform("gap");
		addUniform("lod");
		addUniform("location");
		
		addUniform("tessellationFactor");
		addUniform("tessellationSlope");
		addUniform("tessellationShift");
		
		for (int i = 0; i < 8; i++) {
			addUniform("lodMorphArea[" + i + "]");
		}
		
		addUniform("cameraPosition");
		addUniform("viewProjection");
		
		addUniform("heightmap");
		addUniform("normalmap");
	}
	
	
//	@Override
//	public void setAttributes() {
//		GL20.glBindAttribLocation(this.getShaderProgram(), 0, "position");
//		GL20.glBindAttribLocation(this.getShaderProgram(), 1, "color");
//	}

	
	public void updateUniforms(TerrainNode node) {
		
		Camera cam = Game.getScene().getCamera();
		
		node.getLocalTransform().preProcess();
		node.getWorldTransform().preProcess();
		
		updateUniform("cameraPosition", cam.getTransform().getPosition());
		updateUniform("viewProjection", cam.getViewProjectionBuffer());
		
		TerrainConfig config = node.getConfig();
		
		updateUniform("lod", node.getLod());
		updateUniform("index", node.getIndex());
		updateUniform("gap", node.getGap());
		updateUniform("location", node.getLocation());
		
		updateUniform("localMatrix", node.getLocalTransform().getTransformationBuffer());
		updateUniform("worldMatrix", node.getWorldTransform().getTransformationBuffer());
		
		updateUniform("scaleY", config.getScaleY());
		
		//for (int i = 0; i < 8; i++)
		updateUniform("lodMorphArea[0]", config.getLodMorphingArea()[0]);
		updateUniform("lodMorphArea[1]", config.getLodMorphingArea()[1]);
		updateUniform("lodMorphArea[2]", config.getLodMorphingArea()[2]);
		updateUniform("lodMorphArea[3]", config.getLodMorphingArea()[3]);
		updateUniform("lodMorphArea[4]", config.getLodMorphingArea()[4]);
		updateUniform("lodMorphArea[5]", config.getLodMorphingArea()[5]);
		updateUniform("lodMorphArea[6]", config.getLodMorphingArea()[6]);
		updateUniform("lodMorphArea[7]", config.getLodMorphingArea()[7]);
		
		updateUniform("tessellationFactor", config.getTessellationFactor());
		updateUniform("tessellationSlope", config.getTessellationSlope());
		updateUniform("tessellationShift", config.getTessellationShift());
		
		config.getHeightmap().bind(0);
		updateUniform("heightmap", 0);
		
		config.getNormalMap().bind(1);
		updateUniform("normalmap", 1);
	}
	
}
