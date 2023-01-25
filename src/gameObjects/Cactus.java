package gameObjects;

import engine.GameObject;
import engine.Sprite;

public class Cactus extends Item {

	public Cactus () {
		this.setSprite(new Sprite ("resources/sprites/potted cactus.png"));
		this.useSpriteHitbox();
		text.changeText("YOU FOUND A POTTED CACTUS");
		text.pushString("WASEN'T SOMEBODY LOOKING FOR THREE OF THESE?");
		
	}
	
	@Override
	public void onPickup() {
		super.onPickup();
		BombMaster.objectives.findCactus();
	}
	
}
