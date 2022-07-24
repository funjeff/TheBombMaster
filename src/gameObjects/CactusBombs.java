package gameObjects;

import engine.Sprite;

public class CactusBombs extends Item {

	public CactusBombs () {
		this.setSprite(new Sprite ("resources/sprites/bombs/cactus bomb full fuse.png"));
		this.useSpriteHitbox();
		text.changeText("YOUR BOMBS WERE UPGRADED TO CACTUS BOMBS");
		text.pushString("AFTER EXPLODING CACTUS BOMBS WILL BREAK INTO A SHORT RANGE RING OF SPINE BOMBS");
		
		
	}
	
}