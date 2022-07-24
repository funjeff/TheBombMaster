package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;

public class BombNadoProjectile extends GameObject {

	public static final int AFFECT_RADIUS = 100;
	
	ArrayList <GameObject> affectedObjs = new ArrayList <GameObject> ();
	
	
	public BombNadoProjectile () {
		this.setSprite(new Sprite ("resources/sprites/config/bombNado.txt"));
		this.getAnimationHandler().setFrameTime(50);
		this.setHitbox(0,0,41,49);
	}
	
	@Override
	public void frameEvent () {
		
		ArrayList <GameObject> allBombs = ObjectHandler.getObjectsByName("Bomb");
		
		
		if (allBombs != null) {
			for (int i = 0; i < allBombs.size(); i++) {
				if (!affectedObjs.contains(allBombs.get(i))) {
					double xDist = Math.abs(this.getCenterX() - allBombs.get(i).getCenterX());
					double yDist = Math.abs(this.getCenterY() - allBombs.get(i).getCenterY());
					//thanks pathagorus
					double exactDist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist,2));
					
					if (exactDist < AFFECT_RADIUS) {
						Bomb toAdd = (Bomb) allBombs.get(i);
						toAdd.setThrowDirection(-1);
						affectedObjs.add(allBombs.get(i));
					}
				}
			}
		}
		
		if (direction != -1) {
			this.setX(this.getX() + (Math.cos(direction)*speed));
			this.setY(this.getY() - (Math.sin(direction)*speed));
			
			for (int i = 0; i < affectedObjs.size(); i++) {
				affectedObjs.get(i).setX(affectedObjs.get(i).getX() + (Math.cos(direction)*speed));
				affectedObjs.get(i).setY(affectedObjs.get(i).getY() + (Math.cos(direction)*speed));
			}
		}
		
		for (int i = 0; i < affectedObjs.size(); i++) {
			GameObject cur = affectedObjs.get(i);
	
			double xDist = Math.abs(this.getCenterX() - affectedObjs.get(i).getCenterX());
			double yDist = Math.abs(this.getCenterY() - affectedObjs.get(i).getCenterY());
			//thanks pathagorus
			double exactDist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist,2));
			
			exactDist = exactDist -1;
			
			
			if (exactDist < 0) {
				cur.setX(this.getX());
				cur.setY(this.getY());
			}
			
			
			
			double ang = Math.atan2(affectedObjs.get(i).getCenterY() - this.getCenterY(),affectedObjs.get(i).getCenterX() - this.getCenterX());
			
			ang = ang + .2;
			
			
			double newSlope = Math.tan(ang);
			
			
			
			//solve system of equations to get y component
			// slope * xdist = ydist 
			// fulldist^2 = xdist^2 + ydist^2
			
			//fulldist^2 = xdist^2 + (xdist*slope)^2
			
			
			//fulldist^2 = xdist^2 + (slope^2)(xdist)^2
			//fulldist^2 = (slope^2 + 1)(xdist)^2
			//fulldist^2/(slope^2 + 1) = xdist^2
			//sqrt(fulldist^2/(slope^2 +1)) = xdist
			
			//slope * xdist = ydist
			
			
			//if I got my math right
			
			double xdistNew = Math.sqrt(Math.pow(exactDist, 2)/(Math.pow(newSlope, 2) + 1));
			
			double ydistNew = xdistNew * newSlope;
			
			
			if (ang > Math.PI/2) {
				xdistNew = xdistNew * -1;
				ydistNew = ydistNew * -1;
				
			}
			
			if (ang < -Math.PI/2) {
				xdistNew = xdistNew * -1;
				ydistNew = ydistNew * -1;
			}
			
		
			
			
//			double rise = newSlope * exactDist;
//			double run = 1 * exactDist;
//			
//			//correct any possible errors with signs
//			//I definitly don't need all of these but I have no idea witch errors will happen
//			//and witch ones wont so might as well catch all of them

			
			
//			//third quadrant

////			//fourth quadrant
//			if (ang > -Math.PI/2 && ang < 0) {
//				
//				if (xdistNew < 0) {
//					xdistNew = xdistNew * -1;
//				}
//				
//				if (ydistNew > 0) {
//					ydistNew = ydistNew * -1;
//				}
//				
//			}
////			
////			//first quadrant
//			if (ang > 0 && ang < Math.PI/2) {
//				
//				if (xdistNew < 0) {
//					xdistNew = xdistNew * -1;
//				}
//				
//				if (ydistNew < 0) {
//					ydistNew = ydistNew * -1;
//				}
//				
//			}
////			
////			//second quadrant
//			
			
		
			affectedObjs.get(i).setCenterX(this.getCenterX() + xdistNew);
			affectedObjs.get(i).setCenterY(this.getCenterY() + ydistNew);
			
			
		}
		
	}
	
	@Override
	public void gettingSploded() {
		this.forget();
	}
	
}
