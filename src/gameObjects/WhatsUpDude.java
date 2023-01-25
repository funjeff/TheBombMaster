package gameObjects;

import engine.GameCode;
import engine.Sprite;

public class WhatsUpDude extends NPC {

	public WhatsUpDude () {
		super();
		
		text.changeText("HEY");
		text.pushString ("WHATS UP");
		
		this.setSprite(new Sprite ("resources/sprites/bomb elder.png"));
		this.setHitboxAttributes(61, 75);
		this.setRenderPriority(-1);	
	}

	
	
}
