package gameObjects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import gameObjects.DesertHole.GolfPole;
import map.Room;


public class GolfHole extends GameObject {
	
	GolfFlag f = new GolfFlag ();
	
	int explosionSize = 0;
	
	int timer = 0;
	
	Cracks cracks = new Cracks ();
	
	Sign indicationSign = new Sign ("resources/sprites/hole 3 sign.png");
	
	Sign clearedSign = new Sign ("resources/sprites/golf cleared sign.png");
	
	public GolfHole () {
		this.setSprite(new Sprite ("resources/sprites/golf hole.png"));
		this.setHitboxAttributes(-2,-3,16,15);
		
		if (this.getVariantAttribute("num") != null && this.getVariantAttribute("num").equals("1")) {
			indicationSign = new Sign ("resources/sprites/hole 1 sign.png");
		}
		
	}
	
	@Override
	public void frameEvent () {
		
//		if (!f.declared()) {
//			f.declare(this.getX() + 5,this.getY() + 2);
//		}
		
		
		if (this.isColliding("GolfBall") && !f.isRaised()) {
			clearedSign.setStrokes(((GolfBall)this.getCollisionInfo().getCollidingObjects().get(0)).getStrokes());
			this.getCollisionInfo().getCollidingObjects().get(0).forget();	
			f.declare(this.getX() + 5,this.getY() + 2);
		}
	
		if (f.isRaised() && explosionSize != 7) {
				timer = timer + 1;
				if (timer > 10 - explosionSize) {
					timer = 0;
					explosionSize = explosionSize + 1;
					Explosion e;
					if (explosionSize == 7) {
						 e = new Explosion (6);
						e.declare(this.getCenterX() - e.hitbox().width/2, this.getCenterY() - e.hitbox().height/2);
						cracks.declare(this.getX() - 55, this.getY() - 45);
						cracks.setRenderPriority(-1);
						
						clearedSign.declare(this.getX() - 70, this.getY() - 20);
						
						if (this.getVariantAttribute("num") != null && this.getVariantAttribute("num").equals("1")) {
							BombMaster.objectives.completeGrassHole();
						} else {
							BombMaster.objectives.completeMountainHole();
						}
						
					} else {
						e = new Explosion (explosionSize/4.0);
						
						Random r = new Random ();
						
						e.declare(this.getCenterX() - (r.nextInt(40) - 20), this.getCenterY() - (r.nextInt(40) - 20));
					}
					
					e.makeAsteticOnly();
				
				}
		}
	}

	
	@Override
	public void onDeclare () {
		indicationSign.declare(this.getX() + 20, this.getY() - 25);
	}

public class GolfFlag extends GameObject {
		
		double ogY = 0;
		
		GolfPole pole = new GolfPole();
		
		public GolfFlag () {
			this.setSprite(new Sprite ("resources/sprites/golf flag.png"));
			this.setRenderPriority(1);
			
		}
		
		@Override
		public void onDeclare () {
			ogY = this.getY();
			pole.declare(this.getX(), this.getY() + 8);
		}
		
		@Override
		public void frameEvent () {
			if (this.getY() > ogY - 40) {
				this.setY(this.getY() - 2);
				pole.setY(pole.getY() - 2);
			} 
		}
		
		public boolean isRaised () {
			return this.getY() <= ogY - 40;
		}
		
	}
	
	public class GolfPole extends GameObject{
		int ogY = 0;
		public GolfPole () {
			
		}
		
		@Override
		public void draw () {
			Sprite poleSprite = new Sprite ("resources/sprites/golf pole.png");
			
			if (ogY - this.getY() > 0 && ogY - this.getY() < 41 ) {
				Sprite croppedSprite = new Sprite (poleSprite.getFrame(0).getSubimage(0,0,2,ogY - (int)this.getY()));
				this.setSprite(croppedSprite);
				
			}
			
			super.draw();
			
		}
		
		@Override
		public void onDeclare () {
			ogY = (int) this.getY();
		}
		
	}
	
	public class Cracks extends GameObject {
		public Cracks () {
			this.setSprite(new Sprite ("resources/sprites/golf hole cracks.png"));
		}
	}
	
	public class Sign extends GameObject {
		int strokes = -1;
		
		public Sign (String sprite) {
			this.setSprite(new Sprite (sprite));
		}
		
		public void setStrokes (int strokeAmount) {
			if (strokeAmount > 99) {
				strokeAmount = 99;
			} else {
				strokes = strokeAmount;
			}
		}
		
		@Override
		public void draw () {
			super.draw();
			if (strokes != -1) {
				Graphics g = RenderLoop.wind.getBufferGraphics();
				g.setColor(Color.BLACK);
				g.drawString(Integer.toString(strokes),(int)(this.getX() + 2 - Room.getViewX()),(int)(this.getY() + 29 - Room.getViewY()));
			}
		}
		
	}
	
	
}
