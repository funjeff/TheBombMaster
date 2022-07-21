package gameObjects;

import java.awt.Color;
import java.awt.Graphics;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import engine.Textbox;
import map.Room;

public class Item extends GameObject{
	Textbox text = new Textbox ("");
	
	boolean animaitonPlaying = false;

	boolean moving = false;
	
	int xDistPerFrame = 0;
	int yDistPerFrame = 0;
	
	
	int elapsedFrames = 0;
	
	int framesToCenter = 0;
	
	double opacity = 0;
	
	int redAmount = 33;
	int greenAmount = 50;
	int blueAmount = 123;
	
	boolean redSlideUp = false;
	boolean blueSlideUp = true;
	boolean greenSlideUp = true;
	
	ConfettiCannon leftCannon = new ConfettiCannon();
	ConfettiCannon rightCannon = new ConfettiCannon();

	BombCenterPiece b = new BombCenterPiece();
	
	boolean movingBack = false;
	
	public Item () {
		text.setFont("Bomb");
		text.setBox("Bomb");
		
		text.changeWidth(50);
		text.changeHeight(7);
	}

	
	@Override
	public void frameEvent () {
		
		if (redSlideUp) {
			redAmount = redAmount + 1;
			if (redAmount > 200) {
				redAmount = 199;
				redSlideUp = false;
			}
		} else {
			redAmount = redAmount - 1;
			if (redAmount < 0) {
				redAmount = 1;
				redSlideUp = true;
			}
		}
		
		if (greenSlideUp) {
			greenAmount = greenAmount + 1;
			if (greenAmount > 200) {
				greenAmount = 199;
				greenSlideUp = false;
			}
		} else {
			greenAmount = greenAmount - 1;
			if (greenAmount < 0) {
				greenAmount = 1;
				greenSlideUp = true;
			}
		}
		
		if (blueSlideUp) {
			blueAmount = blueAmount + 1;
			if (blueAmount > 200) {
				blueAmount = 199;
				blueSlideUp = false;
			}
		} else {
			blueAmount = blueAmount - 1;
			if (blueAmount < 0) {
				blueAmount = 1;
				blueSlideUp = true;
			}
		}
		
		if (this.isColliding("BombMaster") && !animaitonPlaying && !moving) {
			onPickup();
		}
		
		if (moving) {
			
			this.setX(this.getX() + xDistPerFrame);
			GameCode.getBombMaster().setX(GameCode.getBombMaster().getX() + xDistPerFrame);
			GameCode.getBombMaster().myLegs.setX(GameCode.getBombMaster().myLegs.getX() + xDistPerFrame);

			this.setY(this.getY() + yDistPerFrame);
			GameCode.getBombMaster().setY(GameCode.getBombMaster().getY() + yDistPerFrame);
			GameCode.getBombMaster().myLegs.setY(GameCode.getBombMaster().myLegs.getY() + yDistPerFrame);
			
			elapsedFrames = elapsedFrames + 1;
			
			leftCannon.setX(leftCannon.getX() + 120/framesToCenter);
			rightCannon.setX(rightCannon.getX() - 120/framesToCenter);
			
			opacity = opacity + 1.0/framesToCenter;
			
			if (elapsedFrames == framesToCenter) {
				moving = false;
				text.setRenderPriority(3);
				text.declare(100, 300);
			}
			
		}
		
		if (movingBack) {
			GameCode.getBombMaster().setX(GameCode.getBombMaster().getX() - xDistPerFrame);
			GameCode.getBombMaster().myLegs.setX(GameCode.getBombMaster().myLegs.getX() - xDistPerFrame);

			GameCode.getBombMaster().setY(GameCode.getBombMaster().getY() - yDistPerFrame);
			GameCode.getBombMaster().myLegs.setY(GameCode.getBombMaster().myLegs.getY() - yDistPerFrame);
			
			elapsedFrames = elapsedFrames + 1;
			
			if (elapsedFrames == framesToCenter) {
				this.forget();
				GameCode.getBombMaster().unfreeze();
				GameCode.getBombMaster().enableCollisions();
			}
			
		}
		
		if (text.isEmpty()) {
			onClose();
		}
		
	}
	
	@Override
	public void draw () {
		
		if (opacity != 0) {
			Graphics g = RenderLoop.wind.getBufferGraphics();
			g.setColor(new Color (redAmount,greenAmount,blueAmount,(int)(opacity*255)));
			g.fillRect(0,0,960,540);
			b.setX(Room.getViewX() + 330);
			b.setY(Room.getViewY() + 87);
			b.getSprite().setOpacity((float)opacity);
			b.draw();
		}
		super.draw();
	}
	
	public void onPickup() {
		
		GameCode.getBombMaster().setSprite(new Sprite ("resources/sprites/player holding item.png"));
		GameCode.getBombMaster().myLegs.setSprite(GameCode.getBombMaster().FORWARD_LEGS_WALK);
		GameCode.getBombMaster().myLegs.setY(GameCode.getBombMaster().getY() + GameCode.getBombMaster().getSprite().getHeight());
		GameCode.getBombMaster().myLegs.setX(GameCode.getBombMaster().getX() + 5);
		GameCode.getBombMaster().myLegs.getAnimationHandler().setFrameTime(0);
		
		
		GameCode.getBombMaster().freeze();
		
		this.setX(GameCode.getBombMaster().getX() + 25/2 - this.hitbox().width/2);
		this.setY(GameCode.getBombMaster().getY() - this.hitbox().height);
		this.playPickUpAnimation();
	}
	
	public void playPickUpAnimation () {
		this.setRenderPriority(1);
		
		moving = true;
		
		
		framesToCenter = 5;
		
		leftCannon.declare();
		rightCannon.declare();
		
		rightCannon.setX(Room.getViewX() + 960);
		rightCannon.setY(Room.getViewY() + 458);
		
		leftCannon.setX(Room.getViewX() - 120);
		leftCannon.setY(Room.getViewY() + 458);
		leftCannon.getAnimationHandler().setFlipHorizontal(true);
		
		leftCannon.setRenderPriority(2);
		rightCannon.setRenderPriority(2);
		
		GameCode.getBombMaster().disableCollisions();
		
		xDistPerFrame = ((Room.getViewX() + 480) - (int)this.getX())/framesToCenter;
		yDistPerFrame = ((Room.getViewX() + 160) - (int)this.getY())/framesToCenter;
		
		
//		e = new Explosion (90);
//		e.makeAsteticOnly();
//		e.setRenderPriority(1);
//		e.declare(this.getX() - e.hitbox().width/2,this.getY() -e.hitbox().height/2);
//	
//		animaitonPlaying = true;
		
	}
	
	
	public void onClose () {
		text.forget();
		this.hide();
		leftCannon.forget();
		rightCannon.forget();
		movingBack = true;
		elapsedFrames = 0;
	}
	
	public class ConfettiCannon extends GameObject {
		
		int timer = 70;
		
		public ConfettiCannon () {
			this.setSprite(new Sprite ("resources/sprites/confetii cannon.png"));
		}
		
		@Override
		public void frameEvent () {
			timer = timer + 1;
			
			if (timer == 90) {
				timer = 0;
				
				Bomb partyBomb = new Bomb (this);
				
				partyBomb.setRenderPriority(this.getRenderPriority());
				
				partyBomb.declare();
				
				partyBomb.cancelExplosion();
				partyBomb.makeAsteticOnly();
				partyBomb.setBombSprites("confeti");
				partyBomb.setExplosionSprite("resources/sprites/config/confetiExplosion.txt");
				partyBomb.setFragsType("confeti");
				partyBomb.setFrags(30, 80);
				partyBomb.setTime(80);
				
				if (this.getAnimationHandler().flipHorizontal()) {
					partyBomb.setX(this.getX() + 115);
					partyBomb.setY(this.getY());
					partyBomb.throwObj(Math.PI/4, 7);
					
				} else {
					partyBomb.setX(this.getX());
					partyBomb.setY(this.getY());
					partyBomb.throwObj(3*Math.PI/4, 7);
				}
				
				
			}
			
		}
		
	}
	
	public class BombCenterPiece extends GameObject{
		public BombCenterPiece() {
			this.setSprite (new Sprite ("resources/sprites/bombCenter.png"));
		}
	}
	
}
