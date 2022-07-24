package gameObjects;

import engine.Sprite;

public class GolfClub extends Item {

	public GolfClub () {
		this.setSprite(new Sprite ("resources/sprites/golf club.png"));
		this.useSpriteHitbox();
		this.getAnimationHandler().setFrameTime(50);
		text.changeText("YOU GOT A FREE GOLF CLUB NICE!");
		text.pushString("YOU CAN SWING IT IN FRONT OF YOU TO REFLECT ENEMY BOMBS BACK AT THEM");			
	}
	
}