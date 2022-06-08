package gameObjects;

import engine.GameObject;
import engine.Sprite;

public class SootPile extends GameObject{
	
	int time = 0;
	
	public SootPile () {
		this.setSprite (new Sprite ("resources/sprites/soot.png"));
	}
	
	@Override
	public void frameEvent () {
		time = time + 1;
		if (time == 40) {
			GameOverText t = new GameOverText ();
			t.declare(60, 100);
		}
	}

}
