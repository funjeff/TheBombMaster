package gameObjects;

import engine.Sprite;

public class BombNado extends Item {

	public BombNado () {
		this.setSprite(new Sprite ("resources/sprites/config/bombNado.txt"));
		this.useSpriteHitbox();
		this.getAnimationHandler().setFrameTime(50);
		text.changeText("YOU GOT THE BOMBNADO!");
		text.pushString("THE BOMBNADO WILL SUCK UP ALL BOMBS AND ENEMYS IN ITS RADIUS AND SLOWLY PULL THEM TOWARDS THE BOMB IN THE CENTER");		
	}
	
}