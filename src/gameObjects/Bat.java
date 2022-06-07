package gameObjects;

import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;

public class Bat extends GameObject {

	int health = 5;
	
	public Bat () {
		this.setSprite(new Sprite ("resources/sprites/config/bat.txt"));
		this.getAnimationHandler().setFrameTime(0);
		
		this.setHitbox(43, 7,33,29);
		
	}
	
	@Override
	public void frameEvent () {
		double mastX = GameCode.getBombMaster().getX();
		double mastY = GameCode.getBombMaster().getY();
		
		double mastDist = Math.sqrt(Math.pow(this.getX() + 125/2 - mastX, 2) + Math.pow(this.getY() + 42/2 - mastY, 2));
		
		if (mastDist < 300) {
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
			
			this.throwObj(useAngle, 3);
			this.getAnimationHandler().setFrameTime(100);
		} else {
			this.getAnimationHandler().setFrameTime(0);
			this.throwObj(-1, 0);
		}
		
		if (this.isColliding(GameCode.getBombMaster())) {
			Explosion boom = new Explosion(8);
			boom.declare(this.getX() +this.getHitboxXOffset() + this.hitbox().width/2 - boom.getSprite().getWidth()/2,this.getY() +this.getHitboxYOffset()+ this.hitbox().height/2 - boom.getSprite().getHeight()/2);
			
			this.breakToFragments("bat fragment ", 5,7,4,10);
			
			this.forget();
		}
		
		super.frameEvent();
	}
	
	@Override
	public void gettingSploded() {
		health = health -1;
		if (health == 0) {
			Explosion boom = new Explosion(4);
			boom.declare(this.getX() +this.getHitboxXOffset() + this.hitbox().width/2 - boom.getSprite().getWidth()/2,this.getY() +this.getHitboxYOffset()+ this.hitbox().height/2 - boom.getSprite().getHeight()/2);
			this.breakToFragments("bat fragment ", 5,7,4,10);
			
			this.forget();
		}
	}
	
}
