package gameObjects;

import java.io.File;
import java.util.Random;

import engine.GameObject;
import engine.Sprite;

public class Fragment extends GameObject {
	
	
	double direction;
	
	double speed = 0;
	
	int activeTime;
	
	public Fragment(String fragName) {
		
		Random r = new Random();
		
		int testNum = 1;
		while (true) {
			File test = new File ("resources/sprites/fragments/" + fragName + testNum + ".png");
			
			if (test.exists()) {
				testNum = testNum + 1;
			} else {
				break;
			}
			
		}
		
		testNum = testNum -1;
		
		int fragNum = r.nextInt(testNum) + 1;
		
		
		this.setSprite(new Sprite ("resources/sprites/fragments/" + fragName + fragNum +".png"));
	
		activeTime = r.nextInt(100);
	}
	
	
	@Override
	public void frameEvent() {
	
		if (speed > 0) {
			speed = speed -.2;
		}
		
		if (activeTime == 0) {
			despawnAllCoolLike(20);
		} else {
			activeTime = activeTime -1;
		}
		
		super.frameEvent();
	}
	
}
