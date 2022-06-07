package gameObjects;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class CactusDude extends GameObject {

	public static final Sprite IN_CACTUS = new Sprite ("resources/sprites/config/cactusDude/inCactus.txt");
	public static final Sprite LEAVING = new Sprite ("resources/sprites/config/cactusDude/leavingCactus.txt");
	public static final Sprite LOOKING_AROUND = new Sprite ("resources/sprites/config/cactusDude/lookingAround.txt");
	public static final Sprite REENTERING = new Sprite ("resources/sprites/config/cactusDude/reenteringCactus.txt");
	public static final Sprite THROWING = new Sprite ("resources/sprites/config/cactusDude/throwingBomb.txt");
	
	
	Bomb [] bombs = new Bomb [7];
	
	int state = 0;
	/*
	 * this dudes a finate state machine lol
	 * 
	 * state 0 in cactus may leave cactus if the bomb master is in front of him
	 * state 1 opening cactus vunerable in this state
	 * state 2 looking around looks for a random but short amount of time before throwing 
	 * 	vunerable in this state as well
	 * state 3 throwing bomb 
	 * state 4 going back into cactus
	 * 
	 * state 5 dead will leave the cactus open but dude will be gone
	 */
	
	public CactusDude() {
		this.setSprite(IN_CACTUS);
		this.setHitbox(54,20, 25,24);
		this.getAnimationHandler().setFrameTime(100);
	}
	
	@Override
	public void frameEvent () {

		
		if (state == 0) {
			this.setSprite(IN_CACTUS);
			Rectangle frontTangle = new Rectangle(this.hitbox().x - 250,this.hitbox().y + 150,530,350);
			
			if (frontTangle.intersects(GameCode.getBombMaster().hitbox())) {
				Random r = new Random ();
				if (r.nextInt(40) == 29) {
					state = 1;
				}
			}	
		}
		
		if (state == 1) {
			this.setSprite(LEAVING);
			this.getAnimationHandler().setRepeat(false);
			if (this.getAnimationHandler().getFrame() == 2) {
				state = 2;
				this.getAnimationHandler().setRepeat(true);
			}
		}
		
		if (state == 2) {
			this.setSprite(LOOKING_AROUND);
			Random r = new Random ();
			if (r.nextInt(20) == 14) {
				state = 3;
			}
		}
		
		if (state == 3) {
			this.setSprite(THROWING);
			this.getAnimationHandler().setRepeat(false);
			
			if (bombs[0] == null && this.getAnimationHandler().getFrame() < 2) {
				
				
				
				Random r = new Random ();
				
				if (r.nextBoolean()) {
					
					//spread bomb attack
					
					ArrayList <GameObject> bombOwners = new ArrayList <GameObject>();
					bombOwners.add(this);
					
					bombs[0] = new Bomb(this,2.5);
					bombs[1] = new Bomb(this,2.5);
					bombs[2] = new Bomb(this,2.5);
					bombs[3] = new Bomb(this,2.5);
					bombs[4] = new Bomb(this,2.5);
					bombs[5] = new Bomb(this,2.5);
					bombs[6] = new Bomb(this,2.5);
					
					bombOwners.add(bombs[0]);
					bombOwners.add(bombs[1]);
					bombOwners.add(bombs[2]);
					bombOwners.add(bombs[3]);
					bombOwners.add(bombs[4]);
					bombOwners.add(bombs[5]);
					bombOwners.add(bombs[6]);
					
					bombs[0].setOwners(bombOwners);
					bombs[1].setOwners(bombOwners);
					bombs[2].setOwners(bombOwners);
					bombs[3].setOwners(bombOwners);
					bombs[4].setOwners(bombOwners);
					bombs[5].setOwners(bombOwners);
					bombs[6].setOwners(bombOwners);
					
					
					bombs[0].declare(this.getX() + 33,this.getY() + 16);
					bombs[1].declare(this.getX() + 33,this.getY() + 16);
					bombs[2].declare(this.getX() + 33,this.getY() + 16);
					bombs[3].declare(this.getX() + 33,this.getY() + 16);
					bombs[4].declare(this.getX() + 33,this.getY() + 16);
					bombs[5].declare(this.getX() + 33,this.getY() + 16);
					bombs[6].declare(this.getX() + 33,this.getY() + 16);
				} else {
					
					//precise bomb attack
					
					bombs[0] = new Bomb(this,2.5);
					bombs[0].declare(this.getX() + 33,this.getY() + 16);
				}
				
			}
			
			if (this.getAnimationHandler().getFrame() == 1) {
				
				
				bombs[0].setX(this.getX() + 40);
				bombs[0].setY(this.getY() + 21);
				
				if (bombs[1] != null) {
					bombs[1].setX(this.getX() + 40);
					bombs[1].setY(this.getY() + 21);
					
					bombs[2].setX(this.getX() + 40);
					bombs[2].setY(this.getY() + 21);
					
					bombs[3].setX(this.getX() + 40);
					bombs[3].setY(this.getY() + 21);
					
					bombs[4].setX(this.getX() + 40);
					bombs[4].setY(this.getY() + 21);
					
					bombs[5].setX(this.getX() + 40);
					bombs[5].setY(this.getY() + 21);
					
					bombs[6].setX(this.getX() + 40);
					bombs[6].setY(this.getY() + 21);
				}
				
			}
			
			if (this.getAnimationHandler().getFrame() >= 2 && bombs[0] != null) {
				
				bombs[0].setX(this.getX() + 41);
				bombs[0].setY(this.getY() + 33);
				
				if (bombs[1] != null) {
					//spread bomb attack
					bombs[1].setX(this.getX() + 41);
					bombs[1].setY(this.getY() + 33);
					
					bombs[2].setX(this.getX() + 41);
					bombs[2].setY(this.getY() + 33);
					
					bombs[3].setX(this.getX() + 41);
					bombs[3].setY(this.getY() + 33);
					
					bombs[4].setX(this.getX() + 41);
					bombs[4].setY(this.getY() + 33);
					
					bombs[5].setX(this.getX() + 41);
					bombs[5].setY(this.getY() + 33);
					
					bombs[6].setX(this.getX() + 41);
					bombs[6].setY(this.getY() + 33);
					
					bombs[0].throwObj(7 * Math.PI/6, 10);
					bombs[1].throwObj(5 * Math.PI/4, 10);
					bombs[2].throwObj(4 * Math.PI/3, 10);
					bombs[3].throwObj(3 * Math.PI/2, 10);
					bombs[4].throwObj(5 * Math.PI/3, 10);
					bombs[5].throwObj(7 * Math.PI/4, 10);
					bombs[6].throwObj(11 * Math.PI/6, 10);
					
					bombs[0] = null;
					bombs[1] = null;
					bombs[2] = null;
					bombs[3] = null;
					bombs[4] = null;
					bombs[5] = null;
					bombs[6] = null;
				} else {
					//precise bomb attack
					
					double mastX = GameCode.getBombMaster().getX();
					double mastY = GameCode.getBombMaster().getY();
					
					double ang = Math.atan2(mastY - (this.getY() + 42/2), mastX - (this.getX() + 125/2));
					
					double useAngle = 0;
					
					if (ang < -Math.PI/2) {
						useAngle = ang + -2*(Math.PI - (ang*-1));
					}
					
					if (ang > -Math.PI/2 && ang < 0) {
						useAngle = ang * -1;
					}
					
					if (ang > 0 && ang < Math.PI/2) {
						useAngle = ang * -1;
					}
					
					if (ang > Math.PI/2) {
						useAngle = ang + 2*(Math.PI - (ang));
					}
					
					
					bombs[0].throwObj(useAngle, 16);
					
					bombs[0] = null;
					
				}
				
			}
			
			if (this.getAnimationHandler().getFrame() == 3) {
				state = 4;
				this.getAnimationHandler().setRepeat(true);
			}
			
		}
		
		if (state == 4) {
			this.setSprite(REENTERING);
			this.getAnimationHandler().setRepeat(false);
			if (this.getAnimationHandler().getFrame() == 2) {
				state = 0;
				this.getAnimationHandler().setRepeat(true);
			}
		}

	}
	
	@Override
	public void gettingSploded() {
		if (state != 0 && state != 5) {
			breakToFragments("cactus guy",1,1,8,15,55,22);
			state = 5;
			this.setSprite(new Sprite("resources/sprites/enemies/empty cactus.png"));
		}
	}
	
//	@Override
//	public void draw() {
//		Rectangle frontTangle = new Rectangle(this.hitbox().x - 250,this.hitbox().y + 150,530,350);
//
//		super.draw();
//		Graphics g = RenderLoop.wind.getBufferGraphics();
//		g.drawRect(frontTangle.x,frontTangle.y,frontTangle.width,frontTangle.height);
//		
//	}
	
	
}
