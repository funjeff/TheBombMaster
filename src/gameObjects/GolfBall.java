package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.Sprite;

public class GolfBall extends GameObject {
	
	int strokes = 0;
	
	public GolfBall () {
		this.setSprite(new Sprite ("resources/sprites/golf ball.png"));
		this.setHitboxAttributes(0, 0, 4, 4);
	}
	
	@Override
	public void frameEvent() {
		this.speed = this.speed - 1;
		if (this.speed <= 0) {
			this.direction = -1;
		}
		super.frameEvent();
	}
	
	
	@Override
	public void gettingSploded () {
		this.isColliding("Explosion");
		Explosion inQuestion;
		try {
			 inQuestion = (Explosion)this.getCollisionInfo().getCollidingObjects().get(0);
		} catch(IndexOutOfBoundsException e) {
			return;
		}
		
		strokes = strokes + 1;
		
		double xDist = Math.abs(inQuestion.getCenterX() - this.getCenterX());
		double yDist = Math.abs(inQuestion.getCenterY() - this.getCenterY());
		//thanks pathagorus
		double exactDist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist,2));
		
		double ang = Math.atan2(this.getCenterY() - inQuestion.getCenterY(),this.getCenterX() - inQuestion.getCenterX());

	
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
		
		
		this.throwObj(useAngle, 20);
		
	}

	public int getStrokes () {
		return strokes;
	}

}
