package br.fritzen.engine.gui;

import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;

public class GUIUtils {

	public static NVGColor vecToColor(Vector4f vec) {
		NVGColor color = NVGColor.create();
		color.r(vec.x);
		color.g(vec.y);
		color.b(vec.z);
		color.a(vec.w);
		return color;
	}
	
}
