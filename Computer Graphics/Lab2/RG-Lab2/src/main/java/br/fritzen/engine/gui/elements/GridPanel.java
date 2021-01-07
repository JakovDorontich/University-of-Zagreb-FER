package br.fritzen.engine.gui.elements;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.nanovg.NanoVG;

import br.fritzen.engine.gui.GUIElement;

public class GridPanel extends Panel {

	private int rows;
	private int columns;
	
	private float cellWidth;
	private float cellHeight;
	
	private int elements;
	
	public GridPanel(Vector4f bounds, Vector4f color, int rows, int columns) {
		
		super(bounds, color);
		
		this.rows = rows;
		this.columns = columns;
		
		this.elements = 0;
		
		this.cellWidth = 1f / columns;
		this.cellHeight = 1f / rows;
		
	}

	
	public void addCell(GUIElement guiElement) {
		
		guiElement.setBounds(new Vector4f( (elements % columns) * cellWidth, (elements / columns) * cellHeight, cellWidth, cellHeight));
		elements++;
		add(guiElement);
		
	}
	
	
}
