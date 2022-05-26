package engine;

import gameObjects.BombMaster;
import map.Room;
import map.Room2;


public class GameCode {
	
	static int veiwX;
	static int veiwY;
	

	public static void testBitch () {
		
		
	}
	
	public static void beforeGameLogic () {
		
	}

	public static void afterGameLogic () {
		
	}

	public static void init () {
		
		//Test
		Room.loadRoom ("big_test.tmj");
		
		BombMaster mast = new BombMaster ();
		mast.declare();
		
	}
		
	
	
	public static void gameLoopFunc () {
		
		ObjectHandler.callAll();
		
		
	}
	
	public static void renderFunc () {
		ObjectHandler.renderAll();
		Room.render();
	}
	
	public static void beforeRender() {
		
	}
	
	public static void afterRender()
	{
		
	}
		
	
	public static int getViewX() {
		return veiwX;
	}



	public static void setViewX(int newVeiwX) {
		veiwX = newVeiwX;
	}



	public static int getViewY() {
		return veiwY;
	}



	public static void setViewY(int newVeiwY) {
		veiwY = newVeiwY;
	}



	
}
