package gameObjects;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;

public class GameOverText extends GameObject {
	
	boolean filledIn = false;
	
	public GameOverText() {
		this.setSprite(new Sprite ("resources/sprites/game over text.png"));
		this.getSprite().setOpacity(0);
	}
	
	@Override
	public void frameEvent () {
		if (filledIn) {
			
			GameCode.resetGame();
			
			Explosion e = new Explosion (50);
			e.setX(-100);
			e.setY(-100);
			e.makeAsteticOnly();
			
			e.declare();
			
			e.setRenderPriority(10);
			
			
		}
	}
	
	
	@Override
	public void draw () {
		if (this.getSprite().getOpacity() + 0.007F < 1.0F) {
			this.getSprite().setOpacity(this.getSprite().getOpacity() + 0.01F);
		} else {
			filledIn = true;
		}
		
		super.draw();
	}

}
