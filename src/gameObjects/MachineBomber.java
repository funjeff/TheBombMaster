package gameObjects;

import java.awt.Point;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;

public class MachineBomber extends GameObject {

	boolean bombShot = false;
	
	boolean destroyed = false;
	
	public MachineBomber () {
		this.setSprite(new Sprite ("resources/sprites/config/machineBomber.txt"));
		this.setHitboxAttributes(115,53);
		this.getAnimationHandler().setFrameTime(100);
	}
	
	@Override
	public void frameEvent () {
		double mastX = GameCode.getBombMaster().getX();
		double mastY = GameCode.getBombMaster().getY();

		if (!destroyed) {
		
			this.lookTowards(new Point ((int)mastX,(int)mastY));
			
			if (this.getAnimationHandler().getFrame() == 4) {
				
			
				Random r = new Random ();
				
				if (r.nextInt(4) == 1) {
					bombShot = true;
					return;
				}
				
				if (!bombShot) {
				
					bombShot = true;
				
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
				
			} else {
				bombShot = false;
			}
		}
	}
		@Override
		public void gettingSploded() {
			this.setSprite(new Sprite ("resources/sprites/enemies/destroyed canno.png"));
			if (!destroyed) {
				this.breakToFragments("cannonFragment ", 4, 10);
			}
			destroyed = true;
		}
}
