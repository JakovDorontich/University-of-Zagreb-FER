package br.fritzen.engine.gui.elements;

import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;

import br.fritzen.engine.gui.GUIElement;
import br.fritzen.engine.gui.GUIUtils;

public class Label extends GUIElement {

	private Vector2f position;
	
	private ByteBuffer text;
	
	private NVGColor textColor;
	
	
	public Label(Vector2f position, String text) {
		super();
		this.bounds = new Vector4f(position, 0, 0);
		this.text = memUTF8(text);
		
		this.textColor = GUIUtils.vecToColor(new Vector4f(0, 0, 0, 1));
	}
	
	
	@Override
	public void render(long vg) {
		super.render(vg);

		NanoVG.nvgSave(vg);
		
		
		NanoVG.nvgTranslate(vg, getParentOffsetX() * windowWidth, getParentOffsetY() * windowHeight);
		NanoVG.nvgRotate(vg, rotation);
		
		NanoVG.nvgBeginPath(vg);
		
		//NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_CENTER);
		
		NanoVG.nvgFillColor(vg, this.textColor);
		
		
		NanoVG.nvgText(vg, 
				this.bounds.z/2 * getParentSizeX() * windowWidth ,
				this.bounds.w/2 * getParentSizeY() * windowHeight, 
				text );
		
		
		NanoVG.nvgRestore(vg);
	}
	
	public void setTextColor(Vector4f color) {
		this.textColor = GUIUtils.vecToColor(color);
	}


	public void setText(String text) {
		this.text = memUTF8(text);
	}
}
