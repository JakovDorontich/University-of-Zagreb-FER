package br.fritzen.engine.gui.elements;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import br.fritzen.engine.gui.GUIElement;
import br.fritzen.engine.gui.GUIUtils;

public class Panel extends GUIElement {

	private NVGColor backgroundColor;
	
	private float roundedRadius = 0.0f;
	
	private Vector2f padding;
		
	
	public Panel(Vector4f bounds, Vector4f color) {
		this(bounds, color, new Vector2f());		
	
	}
	
	
	public Panel(Vector4f bounds, Vector4f color, Vector2f padding) {
		super();
		this.bounds = bounds;
		this.backgroundColor = GUIUtils.vecToColor(color);
		this.padding = padding;
	
	}
	
	
	public void setRoundedRadius(float value) {
		this.roundedRadius = value;
	}

	
	@Override
	public void render(long vg) {
		
		super.render(vg);
		NanoVG.nvgSave(vg);
		
		NanoVG.nvgBeginPath(vg);
	
		NanoVG.nvgTranslate(vg, getParentOffsetX() * windowWidth, getParentOffsetY() * windowHeight);
		NanoVG.nvgRotate(vg, rotation);
		
		NanoVG.nvgBeginPath(vg);
		
		NanoVG.nvgRoundedRect(vg, padding.x, padding.y, bounds.z * getParentSizeX() * windowWidth -2*padding.x,  bounds.w * getParentSizeY() * windowHeight -2*padding.y, roundedRadius);
		NanoVG.nvgFillColor(vg, backgroundColor);
		NanoVG.nvgFill(vg);
		
		NanoVG.nvgRestore(vg);
	
	}
	
}
