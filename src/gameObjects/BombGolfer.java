package gameObjects;

import engine.Sprite;

public class BombGolfer extends NPC {

	boolean shotBomb = false;
	
	public BombGolfer () {
		super();
		
		text.changeText("GOLFING IS FUN YOU SHOULD GIVE IT A TRY!");
		
		this.setSprite(new Sprite ("resources/sprites/config/bombGolfer.txt"));
		this.setHitboxAttributes(27, 13, 33, 77);
		this.setRenderPriority(-1);	
		this.adjustHitboxBorders();
		this.getAnimationHandler().setFrameTime(100);
	}
	
	
	@Override
	public void updateForObjectives() {
		boolean hole1 = BombMaster.objectives.hasCompleteGrassHole();
		boolean hole2 = BombMaster.objectives.hasCompleteDesertHole();
		boolean hole3 = BombMaster.objectives.hasCompleteMountainHole();
		
		if (hole1 && hole2 && hole3) {
			resetTextbox("WOW YOU CLEARED THE NEW COURSE YOUR NUTS CAN I HAVE YOUR AUTOGRAPH?");
		}
		
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent();
		if (this.getAnimationHandler().getFrame() >= 5) {
			if (!shotBomb) {
				shotBomb = true;
				Bomb b = new Bomb (this);
				b.declare(this.getX() +70, spriteY + 75);
				b.throwObj(Math.PI/8, 16);
			}
		} else {
			shotBomb = false;
		}
	}
	
}