package gameObjects;

import engine.GameObject;
import engine.Sprite;

public class Bomb extends GameObject {

	
	public static final Sprite FULL_FUSE = new Sprite ("resources/sprites/bomb full fuse.png");
	public static final Sprite HALF_FUSE = new Sprite ("resources/sprites/bomb partialy lit.png");
	public static final Sprite NO_FUSE = new Sprite ("resources/sprites/bomb no fuse.png");
	
	public static final Sprite FULL_FUSE_RED = new Sprite ("resources/sprites/bomb full fuse red.png");
	public static final Sprite HALF_FUSE_RED = new Sprite ("resources/sprites/bomb partialy lit red.png");
	public static final Sprite NO_FUSE_RED = new Sprite ("resources/sprites/bomb no fuse red.png");
	
	
	int fuseThird;
	
	int bombTimer = 120;
	
	double direction;
	
	double speed = 0;
	
	public Bomb () {
		this.setSprite(FULL_FUSE);
	}
	
	@Override
	public void frameEvent() {
		bombTimer = bombTimer - 1;
		
		if (bombTimer == 80) {
			fuseThird = 1;
		}
		
		if (bombTimer == 40) {
			fuseThird = 2;
		}
		
		if (bombTimer == 0) {
			this.forget(); //TODO explosion
		}
		
		if (fuseThird == 0) {
			if (bombTimer % 5 == 0) {
				this.setSprite(FULL_FUSE_RED);
			} else {
				this.setSprite(FULL_FUSE);
			}
		}
		
		if (fuseThird == 1) {
			if (bombTimer % 3 == 0) {
				this.setSprite(HALF_FUSE_RED);
			} else {
				this.setSprite(HALF_FUSE);
			}
		}
		
		if (fuseThird == 2) {
			if (bombTimer % 2 == 0) {
				this.setSprite(NO_FUSE_RED);
			} else {
				this.setSprite(NO_FUSE);
			}
		}
	
		this.setX(this.getX() + (Math.cos(direction)*speed));
		this.setY(this.getY() - (Math.sin(direction)*speed));
		
	}
	
	
	public void throwBomb (double direction, double speed) {
		this.direction = direction;
		this.speed = speed;
	}
	
	
}
