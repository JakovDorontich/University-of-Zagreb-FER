package br.fritzen.engine.gui.elements;

import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.omg.PortableInterceptor.INACTIVE;

import br.fritzen.engine.core.input.Input;
import br.fritzen.engine.gui.GUIElement;
import br.fritzen.engine.gui.GUIUtils;

public abstract class Button extends GUIElement {

	ByteBuffer text = memUTF8("ABC");
	
	NVGColor color, textColor, colorOnMouseOver;
	
	NVGColor currentColor;
	
	Vector2f textOffset;
	
	float roundedRadius = 0.0f;
	
	private boolean clicked = false;
	
	public Button(Vector4f bounds, Vector4f color, Vector4f textColor) {
	
		this(bounds, color, textColor, textColor);
		
	}
	
	
	public Button(Vector4f bounds, Vector4f color, Vector4f textColor, Vector4f colorOnMouseOver) {
		super();
		this.bounds = bounds;
		this.textOffset = new Vector2f();
		
		this.color = GUIUtils.vecToColor(color);
			
		this.textColor = GUIUtils.vecToColor(textColor);
		
		this.colorOnMouseOver = GUIUtils.vecToColor(colorOnMouseOver);
		
		this.currentColor = this.color;
	}
	
	
	protected abstract void clickAction();
	
	
	@Override
	public void render(long vg) {
		super.render(vg);

		NanoVG.nvgSave(vg);
		
		
		NanoVG.nvgTranslate(vg, getParentOffsetX() * windowWidth, getParentOffsetY() * windowHeight);
		NanoVG.nvgRotate(vg, rotation);
		
		NanoVG.nvgBeginPath(vg);
		
		NanoVG.nvgRoundedRect(vg, 0, 0, bounds.z * getParentSizeX() * windowWidth,  bounds.w * getParentSizeY() * windowHeight, roundedRadius);
		NanoVG.nvgFillColor(vg, this.currentColor);
		NanoVG.nvgFill(vg);
		
		
		NanoVG.nvgBeginPath(vg);
		
		NanoVG.nvgStrokeColor(vg, this.textColor);
		NanoVG.nvgFillColor(vg, this.textColor);
		
		
		/*
		NanoVG.nvgText(vg, 
				bounds.x * windowWidth + textOffset.x * bounds.z * windowWidth, 
				bounds.y * windowHeight + textOffset.y * bounds.w * windowHeight + 10, 
				text );
		*/
		NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_CENTER);
		
		NanoVG.nvgText(vg, 
				(1 + this.textOffset.x) * this.bounds.z/2 * getParentSizeX() * windowWidth ,
				(1 + this.textOffset.y) * this.bounds.w/2 * getParentSizeY() * windowHeight, 
				text );
		
		
		NanoVG.nvgRestore(vg);
	}
	
	@Override
	public void onMouseOver(Vector2f mouse, int click) {
		this.currentColor = this.colorOnMouseOver;
		
		if (click == 1 && !clicked) {
			//this.currentColor = GUIUtils.vecToColor(new Vector4f(0, 0, 0, 1));
			this.clickAction();
			clicked = true;
		} else if (click == 0) {
			clicked = false;
		}
		
	}

	
	@Override
	public void onMouseOut(Vector2f mouse, int click) {
		this.currentColor = this.color;
		
	}
	

	public void setTextOffset(float x, float y) {
		this.textOffset.x = x;
		this.textOffset.y = y;
	}


	public void setText(String text) {
		this.text = memUTF8(text);
	}

	public void setRoundedRadius(float value) {
		this.roundedRadius = value;
	}
}

