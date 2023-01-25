package gameObjects;

import java.util.ArrayList;
import java.util.Random;

import engine.GameObject;
import engine.Sprite;
import map.Room;

public class Bomb extends GameObject {

	
	public Sprite fullFuse = new Sprite ("resources/sprites/bombs/bomb full fuse.png");
	public Sprite halfFuse = new Sprite ("resources/sprites/bombs/bomb half fuse.png");
	public Sprite noFuse = new Sprite ("resources/sprites/bombs/bomb no fuse.png");
	
	public Sprite fullFuseRed = new Sprite ("resources/sprites/bombs/bomb full fuse red.png");
	public Sprite halfFuseRed = new Sprite ("resources/sprites/bombs/bomb half fuse red.png");
	public Sprite noFuseRed = new Sprite ("resources/sprites/bombs/bomb no fuse red.png");
	
	int fuseThird;
	
	int bombTimer = 40;
	int fullTimer = 40;
	
	int tempImunity = 0;
	
	ArrayList <GameObject> owners = new ArrayList <GameObject>();
	
	double bombSize;
	
	int minFrags = 3;
	int maxFrags = 10;
	
	public Bomb (GameObject owner) {
		this.setSprite(fullFuse);
		
		this.setHitboxAttributes(32,32);
		this.owners.add(owner);
		bombSize = 2.5;
	}
	
	public Bomb (GameObject owner, double explosionSize) {
		this.setSprite(fullFuse);
		
		this.setHitboxAttributes(32,32);
		this.owners.add(owner);
		bombSize = explosionSize;
	}
	
	public Bomb (ArrayList <GameObject> owners, double explosionSize) {
		this.setSprite(fullFuse);
		
		this.setHitboxAttributes(32,32);
		this.owners = owners;
		bombSize = explosionSize;
	}
	
	@Override
	public void frameEvent() {
		bombTimer = bombTimer - 1;
		
		if (tempImunity != 0) {
			tempImunity = tempImunity -1;
		}
		
		if (bombTimer == fullTimer * 2/3) {
			fuseThird = 1;
		}
		
		if (bombTimer == fullTimer * 1/3) {
			fuseThird = 2;
		}
		
		if (bombTimer == 0 || (Room.isColliding (this) || this.isCollidingChildren("GameObject") && !owners.contains(this.getCollisionInfo().getCollidingObjects().get(0)) && tempImunity == 0)) {
		
//			System.out.println(this.getCollisionInfo().getCollidingObjects().get(0));
//			System.out.println(owners);
			
			Explosion boom = new Explosion(bombSize);
			boom.declare(this.getX() + this.hitbox().width/2 - boom.getSprite().getWidth()/2,this.getY() + this.hitbox().height/2 - boom.getSprite().getHeight()/2);
			
			
			breakToFragments("bomb fragment",minFrags,maxFrags);
			this.forget();
		}
		
		if (fuseThird == 0) {
			if (bombTimer % 5 == 0) {
				this.setSprite(fullFuseRed);
			} else {
				this.setSprite(fullFuse);
			}
		}
		
		if (fuseThird == 1) {
			if (bombTimer % 3 == 0) {
				this.setSprite(halfFuseRed);
			} else {
				this.setSprite(halfFuse);
			}
		}
		
		if (fuseThird == 2) {
			if (bombTimer % 2 == 0) {
				this.setSprite(noFuseRed);
			} else {
				this.setSprite(noFuse);
			}
		}
	
		super.frameEvent();
		
	}
	
	public void setOwners (ArrayList <GameObject> owners) {
		this.owners = owners;
	}
	
	public void setBombSprites(String type) {
		fullFuse = new Sprite ("resources/sprites/bombs/" + type + " bomb full fuse.png");
		halfFuse = new Sprite ("resources/sprites/bombs/" + type + " bomb half fuse.png");
		noFuse = new Sprite ("resources/sprites/bombs/" + type + " bomb no fuse.png");
		
		fullFuseRed = new Sprite ("resources/sprites/bombs/" + type + " bomb full fuse red.png");
		halfFuseRed = new Sprite ("resources/sprites/bombs/" + type + " bomb half fuse red.png");
		noFuseRed = new Sprite ("resources/sprites/bombs/" + type + " bomb no fuse red.png");
	
		this.setSprite(fullFuse);
		this.setHitboxAttributes(this.getSprite().getWidth(), this.getSprite().getHeight());
		
	}
	
	public void setFrags (int minFrags, int maxFrags) {
		this.minFrags = minFrags;
		this.maxFrags = maxFrags;
	}
	
	
	@Override
	public void gettingSploded() {
		if (tempImunity == 0) {
			Explosion boom = new Explosion(bombSize);
			boom.declare(this.getX(),this.getY());
		
			
			breakToFragments("bomb fragment",minFrags,maxFrags);
			this.forget();
		}
	}
	
	public void giveTemparayImunity(int time) {
		tempImunity = time;
	}
	
	public void setTime (int bombTime) {
		this.bombTimer = bombTime;
		this.fullTimer = bombTime;
	}
	
}
