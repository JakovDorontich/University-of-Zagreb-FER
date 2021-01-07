package br.fritzen.engine.gui;

import static org.lwjgl.system.MemoryUtil.memUTF8;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.system.MemoryUtil;

import br.fritzen.engine.core.GS;
import br.fritzen.engine.core.Game;
import br.fritzen.engine.core.GameWindow;
import br.fritzen.engine.core.gameloop.GameLoop;
import br.fritzen.engine.core.input.Input;
import br.fritzen.engine.gui.elements.Button;
import br.fritzen.engine.gui.elements.Panel;
import br.fritzen.engine.rendering.RenderingSystem;
import br.fritzen.engine.utils.EngineBufferUtils;

public class GUI {

	private final Logger LOG = Logger.getLogger(this.getClass().getName());
	
	private long vg;
	
	private static final String FONT_NAME = "BOLD";
	
	private NVGColor gray, white;

    private ByteBuffer fontBuffer;
	
    private GameWindow gameWindow;
    
    private Input input;
    
    private int w, h; 
    
    private GUIElement root;
   
    private ArrayList<ByteBuffer> texts;
    
    
    public GUI(GameWindow gameWindow, Input input) {
    	this.gameWindow = gameWindow;
    	this.input = input;
    	this.init();
	}
	    
	public void init() {
		
		vg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES);
		
		if (this.vg == MemoryUtil.NULL) {
	        LOG.log(Level.SEVERE, "Could not init nanovg");
	    }
		
		try {
			//fontBuffer = EngineBufferUtils.ioResourceToByteBuffer("/fonts/Roboto-Regular.ttf", 150 * 1024);
			fontBuffer = EngineBufferUtils.ioResourceToByteBuffer("src/main/resources/fonts/Roboto-Regular.ttf", 150 * 1024);
			
			int font = NanoVG.nvgCreateFontMem(vg, FONT_NAME, fontBuffer, 0);
	        if (font == -1) {
	            throw new Exception("Could not add font");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		this.root = new GUIElement() {
		};
		this.root.setGameWindow(this.gameWindow);
		
        gray = GUIUtils.vecToColor(new Vector4f(0.4f, 0.4f, 0.4f, 0.5f));
        white = GUIUtils.vecToColor(new Vector4f(1));
        
       
        texts = new ArrayList<ByteBuffer>();
		
        
	}
	
	
	public void update() {
		
		if (GS.GUI_FPS_DEBUG) {
			
			texts.clear();
			
			texts.add(memUTF8("UPS: " + GameLoop.getCurrentUPS() +"    FPS: " + GameLoop.getCurrentFPS(), false));
			texts.add(memUTF8("Scene updated: " + GameLoop.getGameUpdateTime() + " ms", false));
			texts.add(memUTF8("Scene rendered: " + GameLoop.getGameRenderTime() + " ms", false));
		}
		
		for (int i = 0; i < root.getChildren().size(); i++) {
			root.getChildren().get(i).checkMouseIn(this.input.getMousePosition(), this.input.getMouseLeftButton() ? 1 : 0);
		}
		
		updateAll(root);
	}
	
	
	private void updateAll(GUIElement element) {
		
		for (int i = 0; i < element.getChildren().size(); i++) {
			element.update();
			updateAll(element.getChildren().get(i));
		}
		
	}
	
	
	public void render() {
		
		/*
		NanoVG.nvgSave(vg);
		
		if (GS.GUI_FPS_DEBUG) {
			NanoVG.nvgBeginFrame(vg, Game.getGameWindow().getWidth(), Game.getGameWindow().getHeight(), 1);
			
			NanoVG.nvgBeginPath(vg);
			NanoVG.nvgRect(vg, 0, 0, 200, 150);
			NanoVG.nvgFillColor(vg, gray);
			NanoVG.nvgFill(vg);
			
			NanoVG.nvgFillColor(vg, white);
			
			for (int i = 0; i < texts.size(); i++) {
				NanoVG.nvgText(vg, 10, 30 + i * 20, texts.get(i) );
			}
	
			NanoVG.nvgEndFrame(vg);
		}
		

		w = Game.getGameWindow().getWidth();
		h = Game.getGameWindow().getHeight();
		NanoVG.nvgBeginFrame(vg, w, h, 1);
		
		this.renderAll(vg, this.root);
					
		NanoVG.nvgEndFrame(vg);
				
		NanoVG.nvgRestore(vg);
	}
	
	
	public void renderAll(long vg, GUIElement root) {
		
		root.render(vg);
		
		for (int i = 0; i < root.getChildren().size(); i++) {
			renderAll(vg, root.getChildren().get(i));
		}
		*/
	}

	
	public void add(GUIElement guiElement) {
		
		this.root.add(guiElement);
		
	}
}
