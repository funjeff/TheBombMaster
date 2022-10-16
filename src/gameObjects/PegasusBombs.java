package gameObjects;

import engine.Sprite;

public class PegasusBombs extends Item {

	public PegasusBombs () {
		this.setSprite(new Sprite ("resources/sprites/pegasusBombs.png"));
		this.useSpriteHitbox();
		text.changeText("YOU GOT THE PEGASUS BOMBS");
		text.pushString("THE PEGASUS BOMBS ARE A SECONDARY WEAPON YOU CAN SWITCH WITCH SECONDARY WEAPON YOU HAVE EQUIPED WITH THE NUMBER KEYS OR \\ KEY PRESS SHIFT TO USE A SECONARY WEAPON");
		text.pushString("YOU CAN THROW THE PEGASUS BOMBS BEHIND YOU TO LAUNCH YOURSELF FORWARD WITH AN EXPLOSION GIVE IT A TRY!");
		
		
		
	}
	
}