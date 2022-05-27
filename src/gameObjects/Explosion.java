package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.Sprite;

public class Explosion extends GameObject {
	
	int curFrame;
	
	ArrayList <GameObject> explodingObjects = new ArrayList <GameObject>();
	
	public Explosion () {
		this.setHitboxAttributes(26, 27);
		this.setSprite(new Sprite ("resources/sprites/config/explosion.txt"));
		this.getAnimationHandler().setFrameTime(33);
	}
	
	@Override
	public void frameEvent() {
		
		if (this.getAnimationHandler().getFrame() > curFrame) {
			curFrame = this.getAnimationHandler().getFrame();
		}
		
		if (this.getAnimationHandler().getFrame() < curFrame) {
			this.forget();
		}
		
		this.isCollidingChildren("GameObject");
		
		ArrayList <GameObject> splodedObjects = this.getCollisionInfo().getCollidingObjects();
		
		for (int i = 0; i < splodedObjects.size();i++) {
			if (!(explodingObjects.contains(splodedObjects.get(i)))){
				splodedObjects.get(i).gettingSploded();
				explodingObjects.add(splodedObjects.get(i));
			}
		}
		
	}

}
