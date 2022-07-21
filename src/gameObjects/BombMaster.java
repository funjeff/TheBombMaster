package gameObjects;

import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import map.Room;

import java.awt.event.KeyEvent;
import java.util.Random;

public class BombMaster extends GameObject {
	
	public static final Sprite FORWARD = new Sprite ("resources/sprites/config/bombMaster/top/throw/front.txt");
	public static final Sprite BACK = new Sprite ("resources/sprites/config/bombMaster/top/throw/back.txt");
	public static final Sprite SIDE = new Sprite ("resources/sprites/config/bombMaster/top/throw/side.txt");
	public static final Sprite DIAGONAL_BACK = new Sprite ("resources/sprites/config/bombMaster/top/throw/diagonalBack.txt");
	public static final Sprite DIAGONAL_FORWARD = new Sprite ("resources/sprites/config/bombMaster/top/throw/diagonalFront.txt");
	
	public static final Sprite FORWARD_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/front.txt");
	public static final Sprite SIDE_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/sides.txt");
	public static final Sprite DIAGONAL_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/digonal.txt");
	
	
	boolean throwingBomb = false;
	

	Legs myLegs = new Legs();
	
	Bomb beingThrown = null;
	
	boolean dead = false;
	
	double explodeNum = 1;
	
	boolean frozen = false;
	
	boolean throwStuned = false;
	
	static Objectives objectives = new Objectives ();
	
	public BombMaster()
	{
		this.setRenderPriority(2);
		this.setSprite(FORWARD);
		myLegs.setSprite(FORWARD_LEGS_WALK);
		
		this.getAnimationHandler().setFrameTime(0);
		myLegs.getAnimationHandler().setFrameTime(100);
	}
	
	@Override
	public void frameEvent () {
		
		if (this.isCollidingChildren("NPC") && GameCode.keyPressed(KeyEvent.VK_ENTER, this) &&!frozen) {
			((NPC)this.getCollisionInfo().getCollidingObjects().get(0)).onTalk();
		}

		if (!dead) {
			if (!frozen) {
				updateSprite();
			}
			this.setHitboxAttributes(this.getSprite().getWidth(), this.getSprite().getHeight());
		
			if (getX () - Room.getViewXAcurate () > 700) {
				Room.setView ((int)getX () - 700, Room.getViewYAcurate ());
			}
			if (getY () - Room.getViewYAcurate () > 400) {
				Room.setView (Room.getViewXAcurate (), (int)getY () - 400);
			}
			if (getX () - Room.getViewXAcurate () < 200) {
				Room.setView ((int)getX () - 200, Room.getViewYAcurate ());
			}
			if (getY () - Room.getViewYAcurate () < 100) {
				Room.setView (Room.getViewXAcurate (), (int)getY () - 100);
			}
		
		} else {
			
			Random r = new Random();
			
			Explosion e = new Explosion (explodeNum*1 + .1);
				
			e.declare();
			
			e.setX(this.getX() + this.hitbox().width/2 - r.nextInt(e.hitbox().width/2));
			e.setY(this.getY() + this.hitbox().width/2 - r.nextInt(e.hitbox().height/2));
			
			e.setRenderPriority(10);
			
			e.makeAsteticOnly();
				
			explodeNum = explodeNum + (r.nextDouble()*explodeNum/2);
			
			if (explodeNum >= 20) {
				e.setX(this.getX() + this.hitbox().width/2 - e.hitbox().width/2);
				e.setY(this.getY() + this.hitbox().width/2 - e.hitbox().height/2);
				
				SootPile p = new SootPile ();
				p.setX(this.getX());
				p.setY(this.getY());
				
				p.declare();
				
				this.forget();
			}
		}

	}
	
	@Override
	public void draw() {
		
		
		if (this.getSprite().equals(SIDE) && this.getAnimationHandler().flipHorizontal()) {
			myLegs.setY(this.getY() + this.getSprite().getHeight() - 5);
			myLegs.setX(this.getX() + 13);
		}
		
		if (this.getSprite().equals(SIDE) && !this.getAnimationHandler().flipHorizontal()) {
			myLegs.setY(this.getY() + this.getSprite().getHeight() - 4);
			myLegs.setX(this.getX() + 7);
		}
		
		if (this.getSprite().equals(FORWARD)) {
			myLegs.setY(this.getY() + this.getSprite().getHeight());
			myLegs.setX(this.getX() + 7);
		}
		
		if (this.getSprite().equals(BACK)) {
			myLegs.setY(this.getY() + this.getSprite().getHeight() - 3);
			myLegs.setX(this.getX() + 5);
		}
		if (this.getSprite().equals(DIAGONAL_FORWARD) && this.getAnimationHandler().flipHorizontal()) {
			myLegs.setY(this.getY() + this.getSprite().getHeight() - 11);
			myLegs.setX(this.getX() + 23);
		}
		if (this.getSprite().equals(DIAGONAL_FORWARD) && !this.getAnimationHandler().flipHorizontal()) {
			myLegs.setY(this.getY() + this.getSprite().getHeight() - 11);
			myLegs.setX(this.getX() -4);
		}
		
		if (this.getSprite().equals(DIAGONAL_BACK) && this.getAnimationHandler().flipHorizontal()) {
			myLegs.setY(this.getY() + this.getSprite().getHeight() - 15);
			myLegs.setX(this.getX() + 23);
		}
		if (this.getSprite().equals(DIAGONAL_BACK) && !this.getAnimationHandler().flipHorizontal()) {
			myLegs.setY(this.getY() + this.getSprite().getHeight() - 15);
			myLegs.setX(this.getX());
		}
		
		
		myLegs.draw();
		
		
		super.draw();
		
	}
	
	public void updateSprite() {
		
		if (!throwingBomb) {
			if (this.getAnimationHandler().getFrameTime() != 0) {
				this.getAnimationHandler().setFrameTime(0);
			}
		} else {
			if (this.getAnimationHandler().getFrameTime() != 50) {
				this.getAnimationHandler().setFrameTime(50);
			}
			
			if (this.getSprite().equals(BACK)) {
				
				if (this.getAnimationHandler().getFrame() >= 4 && this.getAnimationHandler().getFrame() < 6 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 4) {
					beingThrown.setX(this.getX());
					beingThrown.setY(this.getY() + 10);
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() + 3);
					beingThrown.setY(this.getY() + 9);
				}
				
				if (this.getAnimationHandler().getFrame() >= 6 && beingThrown != null) {
					beingThrown.setX(this.getX() + 6);
					beingThrown.setY(this.getY() + 2);
					
					beingThrown.throwObj(Math.PI/2,10);
					beingThrown = null;
				}
				
			}
			
			if (this.getSprite().equals(FORWARD)) {
				
				if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 8 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() -11);
					beingThrown.setY(this.getY() + 1);
				
				}
				
				if (this.getAnimationHandler().getFrame() == 6) {
					beingThrown.setX(this.getX() -13);
					beingThrown.setY(this.getY() + 5);
				}
				
				if (this.getAnimationHandler().getFrame() == 7) {
					beingThrown.setX(this.getX() -16);
					beingThrown.setY(this.getY() + 10);
				}
				
				if (this.getAnimationHandler().getFrame() >= 8 && beingThrown != null) {
					beingThrown.setX(this.getX() - 16);
					beingThrown.setY(this.getY() + 20);
					
					beingThrown.throwObj(3*Math.PI/2,10);
					beingThrown = null;
				}
				
				
			}
			
			if (this.getSprite().equals(SIDE) && !this.getAnimationHandler().flipHorizontal()) {
				
				if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 7 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() -1);
					beingThrown.setY(this.getY() + 9);
				}
				
				if (this.getAnimationHandler().getFrame() == 6) {
					beingThrown.setX(this.getX() + 9);
					beingThrown.setY(this.getY() + 3);
				}
				
				if (this.getAnimationHandler().getFrame() >= 7 && beingThrown != null) {
					beingThrown.setX(this.getX() + 21);
					beingThrown.setY(this.getY() + 7);
					
					beingThrown.throwObj(0,10);
					beingThrown = null;
				}
				
			}
			
			if (this.getSprite().equals(SIDE) && this.getAnimationHandler().flipHorizontal()) {
				
				if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 7 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() + 9);
					beingThrown.setY(this.getY() + 9);
				}
				
				if (this.getAnimationHandler().getFrame() == 6) {
					beingThrown.setX(this.getX() - 3);
					beingThrown.setY(this.getY() + 3);
				}
				
				if (this.getAnimationHandler().getFrame() >= 7 && beingThrown != null) {
					beingThrown.setX(this.getX() -16);
					beingThrown.setY(this.getY() + 7);
					
					beingThrown.throwObj(Math.PI,10);
					beingThrown = null;
				}
				
			}
			
			if (this.getSprite().equals(DIAGONAL_FORWARD) && !this.getAnimationHandler().flipHorizontal()) {
				
				if (this.getAnimationHandler().getFrame() >= 4 && this.getAnimationHandler().getFrame() < 6 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 4) {
					beingThrown.setX(this.getX() - 5);
					beingThrown.setY(this.getY() - 2);
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() - 7);
					beingThrown.setY(this.getY() + 4);
				}
				
				if (this.getAnimationHandler().getFrame() >= 6 && beingThrown != null) {
					beingThrown.setX(this.getX() -12);
					beingThrown.setY(this.getY() + 18);
					
					beingThrown.throwObj(5*Math.PI/4,10);
					beingThrown = null;
				}	
			}
			
			if (this.getSprite().equals(DIAGONAL_FORWARD) && this.getAnimationHandler().flipHorizontal()) {
				
				if (this.getAnimationHandler().getFrame() >= 4 && this.getAnimationHandler().getFrame() < 6 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 4) {
					beingThrown.setX(this.getX() + 20);
					beingThrown.setY(this.getY() - 2);
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() + 22);
					beingThrown.setY(this.getY() + 4);
				}
				
				if (this.getAnimationHandler().getFrame() >= 6 && beingThrown != null) {
					beingThrown.setX(this.getX() +28);
					beingThrown.setY(this.getY() + 18);
					
					beingThrown.throwObj(7*Math.PI/4,10);
					beingThrown = null;
				}	
			}
			
			if (this.getSprite().equals(DIAGONAL_BACK) && !this.getAnimationHandler().flipHorizontal()) {
				
				if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 8 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() + 6);
					beingThrown.setY(this.getY() + 12);
				}
				
				if (this.getAnimationHandler().getFrame() == 6) {
					beingThrown.setX(this.getX() + 11);
					beingThrown.setY(this.getY() + 10);
				}
				
				if (this.getAnimationHandler().getFrame() == 7) {
					beingThrown.setX(this.getX() +16);
					beingThrown.setY(this.getY() + 4);
					
				}
				if (this.getAnimationHandler().getFrame() >= 8 && beingThrown != null) {
					beingThrown.setX(this.getX() +29);
					beingThrown.setY(this.getY() + 6);
					
					beingThrown.throwObj(Math.PI/4,10);
					beingThrown = null;
				}
			}
			
			if (this.getSprite().equals(DIAGONAL_BACK) && this.getAnimationHandler().flipHorizontal()) {
				
				if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 8 && beingThrown == null) {
					beingThrown = new Bomb (this);
					beingThrown.declare(this.getX(),this.getY());
				}
				
				if (this.getAnimationHandler().getFrame() == 5) {
					beingThrown.setX(this.getX() + 11);
					beingThrown.setY(this.getY() + 12);
				}
				
				if (this.getAnimationHandler().getFrame() == 6) {
					beingThrown.setX(this.getX() + 5);
					beingThrown.setY(this.getY() + 10);
				}
				
				if (this.getAnimationHandler().getFrame() == 7) {
					beingThrown.setX(this.getX() +5);
					beingThrown.setY(this.getY() + 4);
					
				}
				if (this.getAnimationHandler().getFrame() >= 8 && beingThrown != null) {
					beingThrown.setX(this.getX() -13);
					beingThrown.setY(this.getY() + 6);
					
					beingThrown.throwObj(3 *Math.PI/4,10);
					beingThrown = null;
				}
			}
			
			
			
			if (this.getAnimationHandler().getFrame() == this.getSprite().getFrameCount() - 1) {
				throwingBomb = false;
			}
			
			
			
		}
		
		
		if (keyDown(KeyEvent.VK_ENTER) && !throwStuned) {
			throwingBomb = true;
		}
		
		if (throwStuned && !keyDown(KeyEvent.VK_ENTER)) {
			throwStuned = false;
		}
		
		
		
		if (!keyDown('A') && !keyDown('D') && !keyDown('W') && !keyDown('S')) {
			
			if (myLegs.getAnimationHandler().getFrameTime() != 0) {
				int curFrame = myLegs.getAnimationHandler().getFrame();
				myLegs.getAnimationHandler().setFrameTime(0);
				myLegs.getAnimationHandler().setAnimationFrame(curFrame);
			}
			return;
		}
		
		
		//handles walking right
  		if (keyDown('D') && !keyDown('W') && !keyDown('S')) {
			
			if (!throwingBomb) {
				this.setSprite(SIDE);
				myLegs.setSprite(SIDE_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(100);
			}
			
			this.setX(this.getX() + 4);
		}
		
		//handles walking left
		if (keyDown('A') && !keyDown('W') && !keyDown('S')) {
			
			if (!throwingBomb) {
				this.setSprite(SIDE);
				myLegs.setSprite(SIDE_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(true);
				myLegs.getAnimationHandler().setFlipHorizontal(true);
			
				myLegs.getAnimationHandler().setFrameTime(100);
			}
			
			this.setX(this.getX() - 4);
		}
		
		//handles walking up
		if (keyDown('W') && !keyDown('A') && !keyDown('D')) {
			
			if (!throwingBomb) {
				this.setSprite(BACK);
				myLegs.setSprite(FORWARD_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(50);
			}
			
			this.setY(this.getY() - 4);
		}
		
		
		//handles walking down
		if (keyDown('S') && !keyDown('A') && !keyDown('D')) {
			if (!throwingBomb) {
				this.setSprite(FORWARD);
				myLegs.setSprite(FORWARD_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(50);
			}
			
			this.setY(this.getY() + 4);
		}
		
		//handles walking downleft
		if (keyDown('S') && keyDown('A')) {
			
			if (!throwingBomb) {
				this.setSprite(DIAGONAL_FORWARD);
				myLegs.setSprite(DIAGONAL_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(100);
			
			}
			
			this.setX(this.getX() - 4);
			this.setY(this.getY() + 4);	
		}
		
		//handles walking downright
		if (keyDown('S') && keyDown('D') && !keyDown('A')) {
			
			if (!throwingBomb) {
				this.setSprite(DIAGONAL_FORWARD);
				myLegs.setSprite(DIAGONAL_LEGS_WALK);
						
				this.getAnimationHandler().setFlipHorizontal(true);
				myLegs.getAnimationHandler().setFlipHorizontal(true);
				
				myLegs.getAnimationHandler().setFrameTime(100);
			}
			
			this.setX(this.getX() + 4);
			this.setY(this.getY() + 4);	
		}
		
		//handles walking upright
			if (keyDown('W') && keyDown('D') && !keyDown('A')) {
				if (!throwingBomb) {
					this.setSprite(DIAGONAL_BACK);
					myLegs.setSprite(DIAGONAL_LEGS_WALK);
							
					this.getAnimationHandler().setFlipHorizontal(false);
					myLegs.getAnimationHandler().setFlipHorizontal(false);
					
					myLegs.getAnimationHandler().setFrameTime(100);
				}
				
				this.setX(this.getX() + 4);
				this.setY(this.getY() - 4);	
			}
			
			//handles walking upleft
			if (keyDown('W') && keyDown('A')) {
				
				if (!throwingBomb) {
					this.setSprite(DIAGONAL_BACK);
					myLegs.setSprite(DIAGONAL_LEGS_WALK);
						
					this.getAnimationHandler().setFlipHorizontal(true);
					myLegs.getAnimationHandler().setFlipHorizontal(true);
				
					myLegs.getAnimationHandler().setFrameTime(100);
				}
				
				this.setX(this.getX() - 4);
				this.setY(this.getY() - 4);	
			}
			
			
			
			
		
	}
	
	@Override
	public void gettingSploded() {
		((Hud)(ObjectHandler.getObjectsByName("Hud").get(0))).breakHeart();
	}
	
//	public void setIdleLegs() {
//		if (myLegs.getSprite().equals(DIAGONAL_LEGS_WALK)) {
//			myLegs.setSprite(DIAGONAL_LEGS_IDLE);
//		}
//		if (myLegs.getSprite().equals(SIDE_LEGS_WALK)) {
//			myLegs.setSprite(SIDE_LEGS_IDLE);
//		}
//		if (myLegs.getSprite().equals(FORWARD_LEGS_WALK)) {
//			myLegs.setSprite(FORWARD_LEGS_IDLE);
//		}
//		
//	}
	
	public void die () {
		dead = true;
	}

	public void freeze() {
		frozen = true;
	}
	
	public void unfreeze() {
		frozen = false;
		throwStuned = true;
	}
	
	public class Legs extends GameObject {
		
		public Legs() {
			
		}
		
	}
}
