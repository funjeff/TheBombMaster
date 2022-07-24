package gameObjects;

import java.util.ArrayList;
import java.util.Random;

import engine.GameObject;
import engine.Sprite;

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
	
	boolean spawnedOnExplosion = false;
	
	ArrayList <GameObject> owners = new ArrayList <GameObject>();
	
	double bombSize;
	
	int minFrags = 3;
	int maxFrags = 10;
	
	String explsionSprite = "default";
	
	String fragmentString = "bomb fragment";
	
	boolean asteticOnly = false;
	
	boolean cancelExplosion = false;
	
	boolean cactusEffect = false;
	
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
		
		if (spawnedOnExplosion && !this.isColliding("Explosion")) {
			spawnedOnExplosion = false;
		}
		
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
		
		if (bombTimer == 0 || (!asteticOnly && this.isCollidingChildren("GameObject") && !owners.contains(this.getCollisionInfo().getCollidingObjects().get(0)) && tempImunity == 0)) {
		
//			System.out.println(this.getCollisionInfo().getCollidingObjects().get(0));
//			System.out.println(owners);
			
			Explosion boom = new Explosion(bombSize);
			
			if (!cancelExplosion) {
				boom.declare(this.getX() + this.hitbox().width/2 - boom.getSprite().getWidth()/2,this.getY() + this.hitbox().height/2 - boom.getSprite().getHeight()/2);
			}
			
			if (!this.explsionSprite.equals("default")) {
				boom.setSprite(new Sprite (this.explsionSprite));
			}
			
			if (asteticOnly) {
				boom.makeAsteticOnly();
			}
			boom.setRenderPriority(this.getRenderPriority());
			breakToFragments(fragmentString,minFrags,maxFrags);
			
			if (this.cactusEffect) {
				this.shootCactusBombs(boom);
			}
			
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
	
	
	public void doCactusEffect () {
		cactusEffect = true;
	}
	public void setFrags (int minFrags, int maxFrags) {
		this.minFrags = minFrags;
		this.maxFrags = maxFrags;
	}
	
	public void setFragsType (String fragType) {
		this.fragmentString = fragType;
	}
	
	
	@Override
	public void gettingSploded() {
		if (fullTimer - bombTimer <= 5) {
			spawnedOnExplosion = true;
		}
		
		if (tempImunity == 0 && !this.asteticOnly && !spawnedOnExplosion) {
			Explosion boom = new Explosion(bombSize);
			if (!cancelExplosion) {
				boom.declare(this.getX() + this.hitbox().width/2 - boom.getSprite().getWidth()/2,this.getY() + this.hitbox().height/2 - boom.getSprite().getHeight()/2);
			}
			
			if (!this.explsionSprite.equals("default")) {
				boom.setSprite(new Sprite (this.explsionSprite));
			}
			
			
			boom.setRenderPriority(this.getRenderPriority());
			breakToFragments(fragmentString,minFrags,maxFrags);
			
			
			this.forget();
		}
	}
	
	public void cancelExplosion() {
		cancelExplosion = true;
	}
	
	public void makeAsteticOnly () {
		asteticOnly = true;
	}
	
	public void setExplosionSprite (String explosionSprite) {
		this.explsionSprite = explosionSprite;
	}
	
	public void giveTemparayImunity(int time) {
		tempImunity = time;
	}
	
	public void setTime (int bombTime) {
		this.bombTimer = bombTime;
		this.fullTimer = bombTime;
	}
	
	public void reset () {
		this.bombTimer = fullTimer;
		fuseThird = 0;
	}
	
	
	public ArrayList <GameObject> getOwners (){
		return owners;
	}
	
	private void shootCactusBombs(Explosion beWary) {
		//TODO shoot bombs
		
		Bomb b1 = new Bomb(this,1);
		Bomb b2 = new Bomb(this,1);
		Bomb b3 = new Bomb(this,1);
		Bomb b4 = new Bomb(this,1);
		Bomb b5 = new Bomb(this,1);
		Bomb b6 = new Bomb(this,1);
		Bomb b7 = new Bomb(this,1);
		Bomb b8 = new Bomb(this,1);
	
		ArrayList <GameObject> owners = new ArrayList <GameObject>();
		
		owners.add(this);
		owners.add(beWary);
		
		owners.add(b1);
		owners.add(b2);
		owners.add(b3);
		owners.add(b4);
		owners.add(b5);
		owners.add(b6);
		owners.add(b7);
		owners.add(b8);
		
		
		b1.setOwners(owners);
		b2.setOwners(owners);
		b3.setOwners(owners);
		b4.setOwners(owners);
		b5.setOwners(owners);
		b6.setOwners(owners);
		b7.setOwners(owners);
		b8.setOwners(owners);
		
		b1.setBombSprites("cactus needle");
		b2.setBombSprites("cactus needle");
		b3.setBombSprites("cactus needle");
		b4.setBombSprites("cactus needle");
		b5.setBombSprites("cactus needle");
		b6.setBombSprites("cactus needle");
		b7.setBombSprites("cactus needle");
		b8.setBombSprites("cactus needle");
		
		b1.setFragsType("cactus bomb fragment");
		b2.setFragsType("cactus bomb fragment");
		b3.setFragsType("cactus bomb fragment");
		b4.setFragsType("cactus bomb fragment");
		b5.setFragsType("cactus bomb fragment");
		b6.setFragsType("cactus bomb fragment");
		b7.setFragsType("cactus bomb fragment");
		b8.setFragsType("cactus bomb fragment");
		
		
		int minFrags = 1;
		int maxFrags = 2;
		
		b1.setFrags(minFrags,maxFrags);
		b2.setFrags(minFrags,maxFrags);
		b3.setFrags(minFrags,maxFrags);
		b4.setFrags(minFrags,maxFrags);
		b5.setFrags(minFrags,maxFrags);
		b6.setFrags(minFrags,maxFrags);
		b7.setFrags(minFrags,maxFrags);
		b8.setFrags(minFrags,maxFrags);
		
		double bx = this.getX() + this.getHitboxXOffset() + this.hitbox().width/2;
		double by = this.getY() + this.getHitboxYOffset() + this.hitbox().height/2;
		
		
		b1.declare(bx,by);
		b2.declare(bx,by);
		b3.declare(bx,by);
		b4.declare(bx,by);
		b5.declare(bx,by);
		b6.declare(bx,by);
		b7.declare(bx,by);
		b8.declare(bx,by);
		
		int bombTime = 10;
		
		b1.setTime(bombTime);
		b2.setTime(bombTime);
		b3.setTime(bombTime);
		b4.setTime(bombTime);
		b5.setTime(bombTime);
		b6.setTime(bombTime);
		b7.setTime(bombTime);
		b8.setTime(bombTime);
		
		
		int bombSpeed = 7; //im probably gonna wanna tweek this without having to change every throw
		
		b1.throwObj(0,bombSpeed);
		b1.setDrawRotation(1.4);
		
		b2.throwObj(Math.PI/4,bombSpeed);
		b2.setDrawRotation(.7);
		
		b3.throwObj(Math.PI/2,bombSpeed);
		
		b4.throwObj(3*Math.PI/4,bombSpeed);
		b4.setDrawRotation(-.7);
		
		b5.throwObj(Math.PI,bombSpeed);
		b5.setDrawRotation(-1.4);
		
		b6.throwObj(5*Math.PI/4,bombSpeed);
		b6.setDrawRotation(-2.1);
		
		b7.throwObj(3*Math.PI/2,bombSpeed);
		b7.setDrawRotation(-2.8);
		
		b8.throwObj(7*Math.PI/4,bombSpeed);
		b8.setDrawRotation(-3.5);
		
		
		
	}
	
}
