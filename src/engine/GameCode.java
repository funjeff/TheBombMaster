package engine;

import java.util.ArrayList;

import gameObjects.Bat;
import gameObjects.BombGolfCounter;
import gameObjects.BombGolfer;
import gameObjects.BombMaster;
import gameObjects.Bombhog;
import gameObjects.Bombmire;
import gameObjects.Cactus;
import gameObjects.CactusDude;
import gameObjects.CactusFarmer;
import gameObjects.WhatsUpDude;
import gameObjects.Heart;
import gameObjects.Hud;
import gameObjects.MachineBomber;
import gameObjects.Tornado;
import gameObjects.TornadoFarmer;
import map.Room;
import java.awt.event.KeyEvent;



public class GameCode {
	
	static int veiwX;
	static int veiwY;
	
	static BombMaster mast;

	static ArrayList <Asker> askers = new ArrayList <Asker> ();
	

	static TornadoFarmer e = new TornadoFarmer ();
	static CactusFarmer e1 = new CactusFarmer ();

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
		
		e.declare (200,100);
		e1.declare(260, 100);
		
		Tornado t = new Tornado();
		
		t.declare(400, 200);
        
		Tornado t2 = new Tornado();
		
		t2.declare(400, 300);
        
		Tornado t3 = new Tornado();
		
		t3.declare(400, 400);
        
		
		Cactus c1 = new Cactus();
		
		c1.declare(100, 200);
        
		Cactus c2 = new Cactus();
		
		c2.declare(100, 300);
		
		Cactus c3 = new Cactus();
		
		c3.declare(100, 400);
		
		
	//	Bat b = new Bat();
	//	b.declare(300,200);

//		CactusDude c = new CactusDude();
//		c.declare(300,100);

//		Bombhog h = new Bombhog();
//		h.declare(200,200);
//		
//		Heart h = new Heart ();
//		h.declare(350,250);
//		
		
//		MachineBomber mb = new MachineBomber();
//		mb.declare(400,200);
		
		//Test
		//Room2 room2 = new Room2 ();
		//room2.loadMap ("big_test.tmj");

	}
		
	public static void resetGame () {
		
		
		ArrayList <ArrayList <GameObject>> objs = ObjectHandler.getChildrenByName("GameObject");
		
		for (int i = 0; i < objs.size(); i++) {
			while(!objs.get(i).isEmpty()) {
				objs.get(i).get(0).forget();
			}
		}
		
		mast = new BombMaster ();
		mast.declare();
		

		Hud hud = new Hud();
		hud.declare();
	
		
	}
	
	
	public static void gameLoopFunc () {
		
		ObjectHandler.callAll();
		
		 for (int i = 0; i < askers.size(); i++) {
		    	for (int j = 0; j < askers.get(i).getKeys().size(); j++) {
		    		if (!GameLoop.getInputImage().keyDown(askers.get(i).heldKeys.get(j))) {
		    			askers.get(i).getKeys().remove(j);
		    			j--;
		    		}
		    	}
		    }
		
	}
	
	  public static void removeAsker(GameObject asker) {
		  Asker toAsk = getAsker(asker);
		  askers.remove(toAsk);
	  }
	  
	  public static boolean keyCheck(int keyCode, GameObject whosAsking) {
			boolean returnValue = GameLoop.getInputImage().keyDown(keyCode);
		    
			Asker asking = getAsker(whosAsking);
			
			if (returnValue) {
				
				asking.getKeys().add(keyCode);
			}
			
			
			return returnValue;
		  }
		
		public static Asker getAsker (GameObject whosAsking) {
		
			Asker asking = null;
			
			boolean foundAsker = false;
			
			for (int i = 0; i < askers.size(); i++) {
				if (askers.get(i).isAsker(whosAsking)) {
					asking = askers.get(i);
					foundAsker = true;
					break;
				}
			}
			
			if (!foundAsker) {
				askers.add(new Asker(whosAsking));
				asking = askers.get(askers.size() -1);
			}
			
			return asking;
		}
		  
		  public static boolean keyPressed(int keyCode, GameObject whosAsking) {
			boolean returnValue = GameLoop.getInputImage().keyPressed(keyCode);
			
			Asker asking = getAsker(whosAsking);
			
			if (returnValue && !asking.getKeys().contains(keyCode)) {
				asking.getKeys().add(keyCode);
				return returnValue;
			} else {
				return false;
			}
			
			
		  }
		  
		  public static boolean keyReleased(int keyCode) {
		    return GameLoop.getInputImage().keyReleased(keyCode);
		  }
	
	
	public static void renderFunc () {
		Room.render();
		ObjectHandler.renderAll();
	}
	
	public static void beforeRender() {
		
	}
	
	public static void afterRender()
	{
		
	}
		
	public static int getResolutionX() {
		return RenderLoop.wind.getResolution()[0];
	}
	public static int getResolutionY() {
		return RenderLoop.wind.getResolution()[1];
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
