package gameObjects;

import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;
import engine.SpriteParser;
import map.Room;

//not a virus I swear
public class LetterBomb extends GameObject{

	double speed = 0;
	
	public LetterBomb (char letter) {
		
		ArrayList<String> parseStrs = new ArrayList<String> ();
		parseStrs.add ("grid 16 16");
		
		this.setSprite(new Sprite ("resources/sprites/Text/bomb.png",new SpriteParser(parseStrs)));
		this.getAnimationHandler().setAnimationFrame(letter);
		
		Random rand = new Random ();
		speed = (rand.nextDouble()*5) + 3;
		
		}
	
	@Override
	public void frameEvent () {
		this.setY(this.getY() + speed);
		if (this.getY() + 16 > Room.getViewY() + GameCode.getResolutionY()) {
			Explosion e = new Explosion();
			e.makeAsteticOnly();
			e.setRenderPriority(this.getRenderPriority());
			e.declare(this.getX(),this.getY());
			this.forget();
		}
	}
}
