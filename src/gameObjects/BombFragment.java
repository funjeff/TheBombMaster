package gameObjects;

import java.util.Random;

import engine.GameObject;
import engine.Sprite;

public class BombFragment extends GameObject {
	
	
	double direction;
	
	double speed = 0;
	
	int activeTime;
	
	public BombFragment() {
		
		Random r = new Random();
		
		int fragNum = r.nextInt(9) + 1;
		
		this.setSprite(new Sprite ("resources/sprites/bomb fragment" + fragNum +".png"));
	
		activeTime = r.nextInt(100);
	}
	
	
	@Override
	public void frameEvent() {
	
		
		if (activeTime == 0) {
			despawnAllCoolLike(20);
		} else {
			activeTime = activeTime -1;
		}
		
		this.setX(this.getX() + (Math.cos(direction)*speed));
		this.setY(this.getY() - (Math.sin(direction)*speed));
		
	}
	
	
	
	public void launchFrag (double direction, double speed) {
		this.direction = direction;
		this.speed = speed;
	}

}
