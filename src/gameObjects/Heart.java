package gameObjects;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;

public class Heart extends GameObject {
	
	public Heart() {
		this.setSprite(new Sprite ("resources/sprites/bart.png"));
		this.setHitboxAttributes(27, 21);
	}
	
	@Override
	public void frameEvent () {
		if (this.isColliding(GameCode.getBombMaster())) {
			((Hud)(ObjectHandler.getObjectsByName("Hud").get(0))).healHeart();
			this.forget();
		}
	}

}
