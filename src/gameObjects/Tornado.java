package gameObjects;

import engine.GameObject;
import engine.Sprite;

public class Tornado extends Item {

	public Tornado () {
		this.setSprite(new Sprite ("resources/sprites/config/tornado.txt"));
		this.useSpriteHitbox();
		this.getAnimationHandler().setFrameTime(50);
		text.changeText("YOU FOUND A TORNADO");
		text.pushString("WASEN'T SOMEBODY LOOKING FOR THREE OF THESE?");
		
	}
	

	@Override
	public void onPickup() {
		super.onPickup();
		BombMaster.objectives.findTornado();
	}
	
}
