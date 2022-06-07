package engine;

import gameObjects.Bat;
import gameObjects.BombMaster;
import gameObjects.Bombhog;
import gameObjects.CactusDude;
import gameObjects.Heart;
import gameObjects.Hud;
import gameObjects.MachineBomber;
import map.Room;
<<<<<<< HEAD
=======

>>>>>>> 6fde71494f13a24d41baaa14a39f597a0c934f0b


public class GameCode {
	
	static int veiwX;
	static int veiwY;
	
	static BombMaster mast;

	

	public static void testBitch () {
		
		
	}
	
	public static void beforeGameLogic () {
		
	}

	public static void afterGameLogic () {
		
	}

	public static void init () {
		
		//Test
		Room.loadRoom ("resources/mapdata/master_bomber_worldmap.tmj");
		
		mast = new BombMaster ();
		mast.declare();
		

		Hud hud = new Hud();
		hud.declare();
		
		Bat b = new Bat();
		b.declare(300,200);

//		CactusDude c = new CactusDude();
//		c.declare(300,100);

//		Bombhog h = new Bombhog();
//		h.declare(200,200);
		
//		Heart h = new Heart ();
//		h.declare(350,250);
//		
		
//		MachineBomber mb = new MachineBomber();
//		mb.declare(400,200);
		
		//Test
		//Room2 room2 = new Room2 ();
		//room2.loadMap ("big_test.tmj");

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
	
	public static BombMaster getBombMaster() {
		return mast;
	}



	
}
