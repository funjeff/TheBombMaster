package gameObjects;

import java.util.ArrayList;
import java.util.Random;

import engine.GameObject;
import engine.Sprite;

public class Explosion extends GameObject {
	
	int curFrame;
	
	ArrayList <GameObject> explodingObjects = new ArrayList <GameObject>();
	
	boolean asteticOnly = false;
	
	public Explosion () {
		this.setHitboxAttributes(26, 27);
		this.setSprite(new Sprite ("resources/sprites/config/explosion.txt"));
		this.getAnimationHandler().setFrameTime(33);
	}
	
	public Explosion (double size) {
		this.setHitboxAttributes(26 * size, 27 * size);
		Sprite unscaled = new Sprite ("resources/sprites/config/explosion.txt");
		int xSize = (int)(26 * size);
		int ySize = (int)(27 * size);
		
		if (xSize <= 0) {
			xSize = 1;
		}

		if (ySize <= 0) {
			ySize = 1;
		}
		
		Sprite.scale(unscaled,xSize,ySize);
		this.setSprite(unscaled);
		this.getAnimationHandler().setFrameTime(33);
	}
	
	
	
	@Override
	public void frameEvent() {
		if (this.getAnimationHandler().getFrameTime() != 0) {
			if (this.getAnimationHandler().getFrame() > curFrame) {
				curFrame = this.getAnimationHandler().getFrame();
			}
			
			if (this.getAnimationHandler().getFrame() < curFrame || this.getAnimationHandler().getFrame() == this.getSprite().getFrameCount() - 1) {
				this.forget();
			}
		}
		if (!asteticOnly) {
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
	
	public void makeRainbow () {
		Random rand = new Random ();
		Sprite.tweekHue(getSprite(),rand.nextDouble());
	}
	
	public void makeAsteticOnly () {
		asteticOnly = true;
	}

}
