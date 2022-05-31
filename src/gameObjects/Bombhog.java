package gameObjects;

import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;

public class Bombhog extends GameObject {
	
	boolean attacking = false;
	
	boolean bombsShot = false;
	
	boolean dying = false;
	
	int deathClock = 0;
	
	public Bombhog() {
	
		this.getAnimationHandler().setFrameTime(0);
		this.setSprite(new Sprite ("resources/sprites/config/bombhog front.txt"));
		this.setHitboxAttributes(6,3,37,22);
		
	}
	
	@Override
	public void frameEvent () {
		
		if (dying) {
			
			Random r = new Random ();
			int action = r.nextInt(2);
			//action 0 is explode
			//action 1 is fire bomb randomly
			
			if (action == 0) {
				Explosion e = new Explosion ((r.nextDouble()*4) + .1);
				
				e.declare();
				
				if (r.nextBoolean()) {
					e.setX(this.getX() + r.nextInt(this.hitbox().width));
				} else {
					e.setX(this.getX() - r.nextInt(this.hitbox().width));
				}
				
				if (r.nextBoolean()) {
					e.setY(this.getY() + r.nextInt(hitbox().height));
				} else {
					e.setY(this.getY() - r.nextInt(hitbox().height));
				}
				
				
			}
			
			if (action == 1) {
				int bombNum = r.nextInt(36) + 1;
				
				Bomb randBomb = new Bomb (this);
				randBomb.setBombSprites("needle");
				randBomb.setFrags(1,3);
				
				randBomb.giveTemparayImunity(50);
				
				double bx = this.getX() + this.getHitboxXOffset() + this.hitbox().width/2;
				double by = this.getY() + this.getHitboxYOffset() + this.hitbox().height/2;
				
				
				randBomb.declare(bx,by);
				
				randBomb.throwObj(bombNum*Math.PI/18, r.nextInt(5) + 3);
				randBomb.setDrawRotation(1.2 - (bombNum*.15));
				
			}
			
			
			if (deathClock == 15) {
				this.breakToFragments("bombhog fragment");
				Explosion e = new Explosion(5);
				e.declare(this.getX() - 32,this.getY() - 30);
				
				this.forget();
				
			}
			deathClock = deathClock + 1;
			
			
		} else {
			
			if (!attacking) {
				
				double mastX = GameCode.getBombMaster().getX();
				double mastY = GameCode.getBombMaster().getY();
				
				double mastDistX = this.getX() + this.hitbox().width/2 - mastX;
				double mastDistY = this.getY() + this.hitbox().height/2 - mastY;
				
				
				double mastDist = Math.sqrt(Math.pow(mastDistX, 2) + Math.pow(mastDistY, 2));
				
				Random r = new Random ();
				
				if (mastDist < 450 && r.nextInt(40)== 29) {
					this.setSprite(new Sprite ("resources/sprites/config/bombhog attack.txt"));
					attacking = true;
					this.getAnimationHandler().setRepeat(false);
					this.getAnimationHandler().setFrameTime(100);
				} 
				
				
			} else {
				
				if (this.getAnimationHandler().getFrame() >= 6 && !bombsShot) {
					bombsShot = true;
					shootBombs();
				}
				
				if (this.getAnimationHandler().getFrame() == 8) {
					attacking = false;
					bombsShot = false;
				}
				
			}
		
		}
		
	}
	
	@Override
	public void gettingSploded() {
		dying = true;
	}
	
	private void shootBombs() {
		//TODO shoot bombs
		
		Bomb b1 = new Bomb(this,1);
		Bomb b2 = new Bomb(this,1);
		Bomb b3 = new Bomb(this,1);
		Bomb b4 = new Bomb(this,1);
		Bomb b5 = new Bomb(this,1);
		Bomb b6 = new Bomb(this,1);
		Bomb b7 = new Bomb(this,1);
		Bomb b8 = new Bomb(this,1);
		Bomb b9 = new Bomb(this,1);
		Bomb b10 = new Bomb(this,1);
		Bomb b11 = new Bomb(this,1);
		Bomb b12 = new Bomb(this,1);
		Bomb b13 = new Bomb(this,1);
		Bomb b14 = new Bomb(this,1);
		Bomb b15 = new Bomb(this,1);
		Bomb b16 = new Bomb(this,1);
		Bomb b17 = new Bomb(this,1);
		Bomb b18 = new Bomb(this,1);
		Bomb b19 = new Bomb(this,1);
		Bomb b20 = new Bomb(this,1);
		Bomb b21 = new Bomb(this,1);
		Bomb b22 = new Bomb(this,1);
		Bomb b23 = new Bomb(this,1);
		Bomb b24 = new Bomb(this,1);
		Bomb b25 = new Bomb(this,1);
		Bomb b26 = new Bomb(this,1);
		Bomb b27 = new Bomb(this,1);
		Bomb b28 = new Bomb(this,1);
		Bomb b29 = new Bomb(this,1);
		Bomb b30 = new Bomb(this,1);
		Bomb b31 = new Bomb(this,1);
		Bomb b32 = new Bomb(this,1);
		Bomb b33 = new Bomb(this,1);
		Bomb b34 = new Bomb(this,1);
		Bomb b35 = new Bomb(this,1);
		Bomb b36 = new Bomb(this,1);
		
		ArrayList <GameObject> owners = new ArrayList <GameObject>();
		
		owners.add(this);
		
		owners.add(b1);
		owners.add(b2);
		owners.add(b3);
		owners.add(b4);
		owners.add(b5);
		owners.add(b6);
		owners.add(b7);
		owners.add(b8);
		owners.add(b9);
		owners.add(b10);
		owners.add(b11);
		owners.add(b12);
		owners.add(b13);
		owners.add(b14);
		owners.add(b15);
		owners.add(b16);
		owners.add(b17);
		owners.add(b18);
		owners.add(b19);
		owners.add(b20);
		owners.add(b21);
		owners.add(b22);
		owners.add(b23);
		owners.add(b24);
		owners.add(b25);
		owners.add(b26);
		owners.add(b27);
		owners.add(b28);
		owners.add(b29);
		owners.add(b30);
		owners.add(b31);
		owners.add(b32);
		owners.add(b33);
		owners.add(b34);
		owners.add(b35);
		owners.add(b36);
		
		
		b1.setOwners(owners);
		b2.setOwners(owners);
		b3.setOwners(owners);
		b4.setOwners(owners);
		b5.setOwners(owners);
		b6.setOwners(owners);
		b7.setOwners(owners);
		b8.setOwners(owners);
		b9.setOwners(owners);
		b10.setOwners(owners);
		b11.setOwners(owners);
		b12.setOwners(owners);
		b13.setOwners(owners);
		b14.setOwners(owners);
		b15.setOwners(owners);
		b16.setOwners(owners);
		b17.setOwners(owners);
		b18.setOwners(owners);
		b19.setOwners(owners);
		b20.setOwners(owners);
		b21.setOwners(owners);
		b22.setOwners(owners);
		b23.setOwners(owners);
		b24.setOwners(owners);
		b25.setOwners(owners);
		b26.setOwners(owners);
		b27.setOwners(owners);
		b28.setOwners(owners);
		b29.setOwners(owners);
		b30.setOwners(owners);
		b31.setOwners(owners);
		b32.setOwners(owners);
		b33.setOwners(owners);
		b34.setOwners(owners);
		b35.setOwners(owners);
		b36.setOwners(owners);
		
		b1.setBombSprites("needle");
		b2.setBombSprites("needle");
		b3.setBombSprites("needle");
		b4.setBombSprites("needle");
		b5.setBombSprites("needle");
		b6.setBombSprites("needle");
		b7.setBombSprites("needle");
		b8.setBombSprites("needle");
		b9.setBombSprites("needle");
		b10.setBombSprites("needle");
		b11.setBombSprites("needle");
		b12.setBombSprites("needle");
		b13.setBombSprites("needle");
		b14.setBombSprites("needle");
		b15.setBombSprites("needle");
		b16.setBombSprites("needle");
		b17.setBombSprites("needle");
		b18.setBombSprites("needle");
		b19.setBombSprites("needle");
		b20.setBombSprites("needle");
		b21.setBombSprites("needle");
		b22.setBombSprites("needle");
		b23.setBombSprites("needle");
		b24.setBombSprites("needle");
		b25.setBombSprites("needle");
		b26.setBombSprites("needle");
		b27.setBombSprites("needle");
		b28.setBombSprites("needle");
		b29.setBombSprites("needle");
		b30.setBombSprites("needle");
		b31.setBombSprites("needle");
		b32.setBombSprites("needle");
		b33.setBombSprites("needle");
		b34.setBombSprites("needle");
		b35.setBombSprites("needle");
		b36.setBombSprites("needle");
		
		
		int minFrags = 1;
		int maxFrags = 3;
		
		b1.setFrags(minFrags,maxFrags);
		b2.setFrags(minFrags,maxFrags);
		b3.setFrags(minFrags,maxFrags);
		b4.setFrags(minFrags,maxFrags);
		b5.setFrags(minFrags,maxFrags);
		b6.setFrags(minFrags,maxFrags);
		b7.setFrags(minFrags,maxFrags);
		b8.setFrags(minFrags,maxFrags);
		b9.setFrags(minFrags,maxFrags);
		b10.setFrags(minFrags,maxFrags);
		b11.setFrags(minFrags,maxFrags);
		b12.setFrags(minFrags,maxFrags);
		b13.setFrags(minFrags,maxFrags);
		b14.setFrags(minFrags,maxFrags);
		b15.setFrags(minFrags,maxFrags);
		b16.setFrags(minFrags,maxFrags);
		b17.setFrags(minFrags,maxFrags);
		b18.setFrags(minFrags,maxFrags);
		b19.setFrags(minFrags,maxFrags);
		b20.setFrags(minFrags,maxFrags);
		b21.setFrags(minFrags,maxFrags);
		b22.setFrags(minFrags,maxFrags);
		b23.setFrags(minFrags,maxFrags);
		b24.setFrags(minFrags,maxFrags);
		b25.setFrags(minFrags,maxFrags);
		b26.setFrags(minFrags,maxFrags);
		b27.setFrags(minFrags,maxFrags);
		b28.setFrags(minFrags,maxFrags);
		b29.setFrags(minFrags,maxFrags);
		b30.setFrags(minFrags,maxFrags);
		b31.setFrags(minFrags,maxFrags);
		b32.setFrags(minFrags,maxFrags);
		b33.setFrags(minFrags,maxFrags);
		b34.setFrags(minFrags,maxFrags);
		b35.setFrags(minFrags,maxFrags);
		b36.setFrags(minFrags,maxFrags);
		
		
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
		b9.declare(bx,by);
		b10.declare(bx,by);
		b11.declare(bx,by);
		b12.declare(bx,by);
		b13.declare(bx,by);
		b14.declare(bx,by);
		b15.declare(bx,by);
		b16.declare(bx,by);
		b17.declare(bx,by);
		b18.declare(bx,by);
		b19.declare(bx,by);
		b20.declare(bx,by);
		b21.declare(bx,by);
		b22.declare(bx,by);
		b23.declare(bx,by);
		b24.declare(bx,by);
		b25.declare(bx,by);
		b26.declare(bx,by);
		b27.declare(bx,by);
		b28.declare(bx,by);
		b29.declare(bx,by);
		b30.declare(bx,by);
		b31.declare(bx,by);
		b32.declare(bx,by);
		b33.declare(bx,by);
		b34.declare(bx,by);
		b35.declare(bx,by);
		b36.declare(bx,by);
		
		int bombSpeed = 7; //im probably gonna wanna tweek this without having to change every throw
		
		b1.throwObj(Math.PI/18,bombSpeed);
		b1.setDrawRotation(1.2);
		
		b2.throwObj(2*Math.PI/18,bombSpeed);
		b2.setDrawRotation(1.1);
		
		b3.throwObj(3*Math.PI/18,bombSpeed);
		b3.setDrawRotation(1);
		
		b4.throwObj(4*Math.PI/18,bombSpeed);
		b4.setDrawRotation(.9);
		
		b5.throwObj(5*Math.PI/18,bombSpeed);
		b5.setDrawRotation(.8);
		
		b6.throwObj(6*Math.PI/18,bombSpeed);
		b6.setDrawRotation(.7);
		
		b7.throwObj(7*Math.PI/18,bombSpeed);
		b7.setDrawRotation(.5);
		
		b8.throwObj(8*Math.PI/18,bombSpeed);
		b8.setDrawRotation(.3);
		
		b9.throwObj(9*Math.PI/18,bombSpeed);
		b9.setDrawRotation(.1);
		
		b10.throwObj(10*Math.PI/18,bombSpeed);
		b10.setDrawRotation(-.1);
		
		b11.throwObj(11*Math.PI/18,bombSpeed);
		b11.setDrawRotation(-.3);
		
		b12.throwObj(12*Math.PI/18,bombSpeed);
		b12.setDrawRotation(-.5);
		
		b13.throwObj(13*Math.PI/18,bombSpeed);
		b13.setDrawRotation(-.7);
		
		b14.throwObj(14*Math.PI/18,bombSpeed);
		b14.setDrawRotation(-.8);
		
		b15.throwObj(15*Math.PI/18,bombSpeed);
		b15.setDrawRotation(-.9);
		
		b16.throwObj(16*Math.PI/18,bombSpeed);
		b16.setDrawRotation(-1);
		
		b17.throwObj(17*Math.PI/18,bombSpeed);
		b17.setDrawRotation(-1.2);
		
		b18.throwObj(18*Math.PI/18,bombSpeed);
		b18.setDrawRotation(-1.4);
		
		b19.throwObj(19*Math.PI/18,bombSpeed);
		b19.setDrawRotation(-1.6);
		
		b20.throwObj(20*Math.PI/18,bombSpeed);
		b20.setDrawRotation(-1.8);
		
		b21.throwObj(21*Math.PI/18,bombSpeed);
		b21.setDrawRotation(-2);
		
		b22.throwObj(22*Math.PI/18,bombSpeed);
		b22.setDrawRotation(-2.1);
		
		b23.throwObj(23*Math.PI/18,bombSpeed);
		b23.setDrawRotation(-2.2);
		
		b24.throwObj(24*Math.PI/18,bombSpeed);
		b24.setDrawRotation(-2.3);
		
		b25.throwObj(25*Math.PI/18,bombSpeed);
		b25.setDrawRotation(-2.6);
		
		b26.throwObj(26*Math.PI/18,bombSpeed);
		b26.setDrawRotation(-2.7);
		
		b27.throwObj(27*Math.PI/18,bombSpeed);
		b27.setDrawRotation(-2.9);
		
		b28.throwObj(28*Math.PI/18,bombSpeed);
		b28.setDrawRotation(-3.1);
		
		b29.throwObj(29*Math.PI/18,bombSpeed);
		b29.setDrawRotation(-3.3);
		
		b30.throwObj(30*Math.PI/18,bombSpeed);
		b30.setDrawRotation(-3.5);
		
		b31.throwObj(31*Math.PI/18,bombSpeed);
		b31.setDrawRotation(-3.7);
		
		b32.throwObj(32*Math.PI/18,bombSpeed);
		b32.setDrawRotation(-3.9);
		
		b33.throwObj(33*Math.PI/18,bombSpeed);
		b33.setDrawRotation(-4.1);
		
		b34.throwObj(34*Math.PI/18,bombSpeed);
		b34.setDrawRotation(-4.3);
		
		b35.throwObj(35*Math.PI/18,bombSpeed);
		b35.setDrawRotation(-4.5);
		
		b36.throwObj(36*Math.PI/18,bombSpeed);
		b36.setDrawRotation(-4.7);
		
	}

}
