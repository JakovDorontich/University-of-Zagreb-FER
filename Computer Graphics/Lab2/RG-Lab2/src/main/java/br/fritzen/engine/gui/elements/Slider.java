package br.fritzen.engine.gui.elements;

import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import br.fritzen.engine.gui.GUIElement;
import br.fritzen.engine.gui.GUIUtils;

public class Slider extends GUIElement {

	private ByteBuffer text;
	private NVGColor textColor;
	
	private float value;
	private float minValue;
	private float maxValue;
	
	private NVGColor axisColor;
	private NVGColor paddleColor;
	
	private float paddlePosition;
	private float paddleX = 5; 
	
	private boolean selected;
	
	public Slider(float value, float minValue, float maxValue) {
		
		this.value = value;
		this.minValue = minValue;
		this.maxValue = maxValue;
		
		this.axisColor = GUIUtils.vecToColor(new Vector4f(0.2f, 0.2f, 0.2f, 1f));
		this.paddleColor = GUIUtils.vecToColor(new Vector4f(0.7f, 0.7f ,0.7f , 1f));
		this.textColor = GUIUtils.vecToColor(new Vector4f(1f, 1f ,1f , 1f));
		
		this.text = memUTF8(String.format("%.2f", this.value));
		
		this.selected = false;
	}
	
	
	@Override
	public void render(long vg) {
		
		
		super.render(vg);
		NanoVG.nvgSave(vg);
		
		NanoVG.nvgBeginPath(vg);
	
		NanoVG.nvgTranslate(vg, getParentOffsetX() * windowWidth, getParentOffsetY() * windowHeight);
		NanoVG.nvgRotate(vg, rotation);
		
		NanoVG.nvgFillColor(vg, axisColor);
		NanoVG.nvgRoundedRect(vg, 0, 4, bounds.z * getParentSizeX() * windowWidth,  bounds.w * getParentSizeY() * windowHeight -8, 0);
		NanoVG.nvgFill(vg);
		
		NanoVG.nvgBeginPath(vg);
		calculatePaddlePosition();
		
		NanoVG.nvgFillColor(vg, paddleColor);
		NanoVG.nvgRoundedRect(vg, paddlePosition, 0, paddleX,  12, 0);
		NanoVG.nvgFill(vg);
		
		
		NanoVG.nvgBeginPath(vg);
		
		NanoVG.nvgStrokeColor(vg, this.textColor);
		NanoVG.nvgFillColor(vg, this.textColor);
		NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_CENTER);
		
		NanoVG.nvgText(vg, 
				this.bounds.z/2 * getParentSizeX() * windowWidth ,
				this.bounds.w/2 * getParentSizeY() * windowHeight - 10, 
				text );
		
		NanoVG.nvgRestore(vg);
	
	}
	
	
	public void calculatePaddlePosition() {
		
		paddlePosition = (value-minValue)/(maxValue - minValue) * bounds.z * getParentSizeX() * windowWidth - paddleX/2;
		
	}
	
	
	@Override
	public void onMouseOver(Vector2f mouse, int click) {
		
		if (click == 1) {
			selected = true;
			this.setValue((mouse.x - getParentOffsetX() * windowWidth)/((bounds.z) * getParentSizeX() * windowWidth) * (maxValue - minValue) + minValue);
			
		} else {
			selected = false;
		}
	}
	
	
	@Override
	public void onMouseOut(Vector2f mouse, int click) {
		
		if (selected && click == 1) {
			this.setValue((mouse.x - getParentOffsetX() * windowWidth)/((bounds.z) * getParentSizeX() * windowWidth) * (maxValue - minValue) + minValue);
		} else {
			selected = false;
		}
		
	}
	
	@Override
	public void update() {
		
	}

	
	public float getValue() {
		return this.value;
	}


	public void setValue(float value) {
		if (value <= minValue) {
			this.value = minValue;
		} else if (value >= maxValue) {
			this.value = maxValue;
		} else {
			this.value = value;
		}
		
		this.text = memUTF8(String.format("%.2f", this.value));

	}
}
