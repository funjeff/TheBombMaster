package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.Sprite;

public class Hud extends GameObject {

	int health = 2;
	
	ArrayList <Heart> hearts = new ArrayList <Heart>();
	
	public Hud() {
		
		Heart heart1 = new Heart();
		
		heart1.setX(0);
		heart1.setY(0);
		
		Heart heart2 = new Heart();
		
		heart2.setX(30);
		heart2.setY(0);
		
		Heart heart3 = new Heart();
		
		heart3.setX(60);
		heart3.setY(0);
		
		
		hearts.add(heart1);
		hearts.add(heart2);
		hearts.add(heart3);	
		
		this.setRenderPriority(-100);
		
		
		
	}
	
	@Override
	public void draw() {
		for (int i = 0; i < hearts.size(); i++) {
			hearts.get(i).draw();
		}
	}
	
	public void healHeart() {
		if (health != hearts.size() -1) {
			health = health + 1;
			
			hearts.get(health).healHeart();
			
		}
	}
	
	public void breakHeart() {
		if (health != 0) {
			health = health - 1;
			
			hearts.get(health).takeDamage();
			
		} else {
			//TODO game over
		}
	}
	
	public void newHeart () {
		Heart newHeart = new Heart ();
		
		newHeart.setX(hearts.get(hearts.size() -1).getX() + 30);
		
		hearts.add(newHeart);
		
		health = hearts.size() -1;
		
	}
	

	public class Heart extends GameObject {
		
		boolean isHealed;
		
		public Heart () {
			this.setSprite(new Sprite ("resources/sprites/bart.png"));
			isHealed = true;
		}
		
		public boolean isHealed() {
			return isHealed;
		}
		
		public void takeDamage() {
			isHealed = false;
			this.setSprite(new Sprite ("resources/sprites/config/brokenHeart.txt"));
		}
		
		public void healHeart() {
			isHealed = true;
			this.setSprite(new Sprite ("resources/sprites/bart.png"));
		}
		
		
	}
	
}
