package gameObjects;

import engine.GameObject;
import engine.Sprite;

public class BombMaster extends GameObject {
	
	public static final Sprite FORWARD = new Sprite ("resources/sprites/config/bombMaster/top/idle/front.txt");
	public static final Sprite BACK = new Sprite ("resources/sprites/config/bombMaster/top/idle/back.txt");
	public static final Sprite SIDE = new Sprite ("resources/sprites/config/bombMaster/top/idle/side.txt");
	public static final Sprite DIAGONAL_BACK = new Sprite ("resources/sprites/config/bombMaster/top/idle/diagonalBack.txt");
	public static final Sprite DIAGONAL_FORWARD = new Sprite ("resources/sprites/config/bombMaster/top/idle/diagonalFront.txt");
	
	public static final Sprite FORWARD_THROW = new Sprite ("resources/sprites/config/bombMaster/top/throw/front.txt");
	public static final Sprite BACK_THROW = new Sprite ("resources/sprites/config/bombMaster/top/throw/back.txt");
	public static final Sprite SIDE_THROW = new Sprite ("resources/sprites/config/bombMaster/top/throw/side.txt");
	public static final Sprite DIAGONAL_BACK_THROW = new Sprite ("resources/sprites/config/bombMaster/top/throw/diagonalBack.txt");
	public static final Sprite DIAGONAL_FORWARD_THROW = new Sprite ("resources/sprites/config/bombMaster/top/throw/diagonalFront.txt");
	
	public static final Sprite FORWARD_LEGS_IDLE = new Sprite ("resources/sprites/config/bombMaster/legs/idle/front.txt");
	public static final Sprite SIDE_LEGS_IDLE = new Sprite ("resources/sprites/config/bombMaster/legs/idle/sides.txt");
	public static final Sprite DIAGONAL_LEGS_IDLE = new Sprite ("resources/sprites/config/bombMaster/legs/idle/digonal.txt");
	
	public static final Sprite FORWARD_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/front.txt");
	public static final Sprite SIDE_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/sides.txt");
	public static final Sprite DIAGONAL_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/digonal.txt");
	
	
	boolean throwingBomb = false;
	

	Legs myLegs = new Legs();
	
	public BombMaster()
	{
	
		this.setSprite(FORWARD);
		myLegs.setSprite(FORWARD_LEGS_WALK);
		
		this.getAnimationHandler().setFrameTime(100);
		myLegs.getAnimationHandler().setFrameTime(100);
	}
	
	@Override
	public void frameEvent () {
		updateSprite();
	}
	
	@Override
	public void draw() {
		super.draw();
		myLegs.setY(this.getY() + this.getSprite().getHeight());
		myLegs.setX(this.getX());
		myLegs.draw();
	}
	
	public void updateSprite() {
		if (!keyDown('A') && !keyDown('D') && !keyDown('W') && !keyDown('S')) {
			setIdleLegs();
			return;
		}
		
		//handles walking right
		if (keyDown('D') && !keyDown('W') && !keyDown('S')) {
			this.setSprite(SIDE);
			myLegs.setSprite(SIDE_LEGS_WALK);
			
			this.getAnimationHandler().setFlipHorizontal(false);
			myLegs.getAnimationHandler().setFlipHorizontal(false);
			
			this.setX(this.getX() + 4);
		}
		
		//handles walking left
		if (keyDown('A') && !keyDown('W') && !keyDown('S')) {
			this.setSprite(SIDE);
			myLegs.setSprite(SIDE_LEGS_WALK);
			
			this.getAnimationHandler().setFlipHorizontal(true);
			myLegs.getAnimationHandler().setFlipHorizontal(true);
			
			this.setX(this.getX() - 4);
		}
		
		//handles walking up
		if (keyDown('W') && !keyDown('A') && !keyDown('D')) {
			this.setSprite(BACK);
			myLegs.setSprite(FORWARD_LEGS_WALK);
			
			this.getAnimationHandler().setFlipHorizontal(false);
			myLegs.getAnimationHandler().setFlipHorizontal(false);
			
			this.setY(this.getY() - 4);
		}
		
		
		//handles walking down
		if (keyDown('S') && !keyDown('A') && !keyDown('D')) {
			this.setSprite(FORWARD);
			myLegs.setSprite(FORWARD_LEGS_WALK);
			
			this.getAnimationHandler().setFlipHorizontal(false);
			myLegs.getAnimationHandler().setFlipHorizontal(false);
			
			this.setY(this.getY() + 4);
		}
		
		//handles walking downleft
		if (keyDown('S') && keyDown('A')) {
			this.setSprite(DIAGONAL_FORWARD);
			myLegs.setSprite(DIAGONAL_LEGS_WALK);
			
			this.getAnimationHandler().setFlipHorizontal(false);
			myLegs.getAnimationHandler().setFlipHorizontal(false);
			
			this.setX(this.getX() - 4);
			this.setY(this.getY() + 4);	
		}
		
		//handles walking downright
		if (keyDown('S') && keyDown('D')) {
			this.setSprite(DIAGONAL_FORWARD);
			myLegs.setSprite(DIAGONAL_LEGS_WALK);
					
			this.getAnimationHandler().setFlipHorizontal(true);
			myLegs.getAnimationHandler().setFlipHorizontal(true);
					
			this.setX(this.getX() + 4);
			this.setY(this.getY() + 4);	
		}
		
		//handles walking upright
			if (keyDown('W') && keyDown('D')) {
				this.setSprite(DIAGONAL_BACK);
				myLegs.setSprite(DIAGONAL_LEGS_WALK);
						
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
						
				this.setX(this.getX() + 4);
				this.setY(this.getY() - 4);	
			}
			
			//handles walking upleft
			if (keyDown('W') && keyDown('A')) {
				this.setSprite(DIAGONAL_BACK);
				myLegs.setSprite(DIAGONAL_LEGS_WALK);
						
				this.getAnimationHandler().setFlipHorizontal(true);
				myLegs.getAnimationHandler().setFlipHorizontal(true);
						
				this.setX(this.getX() - 4);
				this.setY(this.getY() - 4);	
			}
			
		
	}
	
	public void setIdleLegs() {
		if (myLegs.getSprite().equals(DIAGONAL_LEGS_WALK)) {
			myLegs.setSprite(DIAGONAL_LEGS_IDLE);
		}
		if (myLegs.getSprite().equals(SIDE_LEGS_WALK)) {
			myLegs.setSprite(SIDE_LEGS_IDLE);
		}
		if (myLegs.getSprite().equals(FORWARD_LEGS_WALK)) {
			myLegs.setSprite(FORWARD_LEGS_IDLE);
		}
		
	}
	
	

	public class Legs extends GameObject {
		
		public Legs() {
			
		}
		
	}
}
