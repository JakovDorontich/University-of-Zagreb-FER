package br.fritzen.engine.core.gameloop;

import java.util.logging.Logger;

import br.fritzen.engine.core.GS;

/**
 * Class that controls all GameLoop
 * <br>
 * Consider the UPS and FPS independent. So it must play well in different 
 * computers with the same game velocity rendering different amount of frames.
 * You must set the UPS and FPS in the {@link GS} the default values are
 * <br> <ul>
 * <li> UPS 100
 * <li> FPS 200
 * </ul
 * 
 * @author Vinicius Fritzen Machado
 *
 */
public abstract class GameLoop {

	private final static Logger LOG = Logger.getLogger(GameLoop.class.getName());
	
	private boolean isRunning = false;
	
	private static final long SECOND_IN_NANOS = 1_000_000_000L;
	
	private static final long MAX_UPDATE_TIME = SECOND_IN_NANOS / GS.UPS;
	
	private static final long MAX_FPS_TIME = SECOND_IN_NANOS / GS.FPS_CAP;
	
	private static int currentUPS;
	
	private static int currentFPS;

	private long overSleep = 0;
	
	private static int gameUpdateTime;
	
	private static int gameRenderTime;
	
	
	/**
	 * Starts the game loop
	 */
	protected void run() {
		
		int updates = 0;
		int frames = 0;
		long timer = System.nanoTime();

		long beforeTime = timer;
		long afterTime = timer;
		
		long updateTime = 0;
		long renderTime = 0;
		
		LOG.info("Initializing gameloop");
		this.init();
		
		
		isRunning = true;
		while (isRunning) {
			
			long deltaTime = afterTime - beforeTime;
			
			if (deltaTime >= MAX_UPDATE_TIME - deltaTime) {
				
				updateTime = System.nanoTime();
				
				this.input();
				this.update();
				updates++;
				
				updateTime = System.nanoTime() - updateTime;
				GameLoop.gameUpdateTime = (int) (updateTime / 1_000_000L);
				beforeTime = System.nanoTime() - (deltaTime - MAX_UPDATE_TIME + updateTime);
				
			} else {
				
				renderTime = System.nanoTime();
				
				this.render();
				frames++;
				
				renderTime = System.nanoTime() - renderTime;
				GameLoop.gameRenderTime = (int) (renderTime / 1_000_000L);
				
				if (renderTime < MAX_FPS_TIME - updateTime) {
					
					long diff = MAX_FPS_TIME - renderTime - updateTime - overSleep;
					
					if (diff > 1000)
						this.sleep(diff);
					
				}
				
			}
			
			if (System.nanoTime() - timer >= SECOND_IN_NANOS) {
				
				if (GS.FPS_DEBUG)
					LOG.info("UPS: " + updates + "\tFPS: " + frames);
				
				timer = System.nanoTime();
							
				currentUPS = updates;
				currentFPS = frames;
				
				updates = 0;
				frames = 0;
				
			}
			
			afterTime = System.nanoTime();
		}
		
	}
	
	
	/**
	 * Send the command to stop the game loop
	 */
	protected void stop() {
		isRunning = false;
	}

	
	/**
	 * Initialize the system / game
	 */
	protected abstract void init();


	/**
	 * This method must process users input
	 */
	protected abstract void input();


	/**
	 * Updates the logic / game state according to UPS 
	 */
	protected abstract void update();


	/**
	 * Render screen
	 */
	protected abstract void render();
	
	
	/**
	 * 
	 * @return current UPS
	 */
	public static int getCurrentUPS() {
		return currentUPS;
	}

	
	/**
	 * 
	 * @return current FPS
	 */
	public static int getCurrentFPS() {
		return currentFPS;
	}
	
	
	private void sleep(long nanos) {
		
		try {
			
			long beforeSleep = System.nanoTime();
			Thread.sleep(nanos / 1_000_000L, (int) (nanos % 1_000_000L));
			this.overSleep = System.nanoTime() - beforeSleep - nanos;
			
		} catch (Exception e) {
			LOG.severe("Error sleeping between frames and updates" + e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @return the current time in milliseconds to update the game
	 */
	public static int getGameUpdateTime() {
		return gameUpdateTime;
	}


	/**
	 * 
	 * @return the current time in milliseconds to render the game
	 */
	public static int getGameRenderTime() {
		return gameRenderTime;
	}
}
