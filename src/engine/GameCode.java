package engine;

import gameObjects.BombMaster;
import gameObjects.Hud;
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
		
<<<<<<< HEAD
		Hud hud = new Hud();
		hud.declare();
		
		//Test
		//Room2 room2 = new Room2 ();
		//room2.loadMap ("big_test.tmj");
=======
>>>>>>> 3fff26d8215b102742f4866a8f35708f0ae367cf
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
