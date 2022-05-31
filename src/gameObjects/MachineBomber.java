package gameObjects;

import java.awt.Point;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;

public class MachineBomber extends GameObject {

	int timeUp;
	
	int nextShot = 10;
	
	
	public MachineBomber () {
		this.setSprite(new Sprite ("resources/sprites/enemies/machine bomber.png"));
		this.setHitboxAttributes(115,53);
	}
	
	@Override
	public void frameEvent () {
		double mastX = GameCode.getBombMaster().getX();
		double mastY = GameCode.getBombMaster().getY();
		
		this.lookTowards(new Point ((int)mastX,(int)mastY));
		
		timeUp = timeUp + 1;
		
		if (timeUp == nextShot) {
			Random r = new Random ();
			
			nextShot = r.nextInt(15) + 15;
			
			timeUp = 0;
			
		
			double ang = Math.atan2(mastY - (iris.y + this.getY()), mastX - (iris.x + this.getX()));
			
			Bomb shot = new Bomb(this,3.5);
			double circleRadius = Math.sqrt(Math.pow(this.getSprite().getWidth()/2, 2) + Math.pow(this.getSprite().getHeight()/2, 2));
			
			shot.declare(this.getX() + this.hitbox().width/2 + (Math.cos(ang) * circleRadius),this.getY() + this.hitbox().height/2 + (Math.sin(ang) *circleRadius)); 
			
			
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
			
			shot.setTime(120);
			
			shot.throwObj(useAngle, 7);
			
			shot = null;
			
		}
	}
		
}
