package gameObjects;

import engine.Sprite;

public class CactusFarmer extends NPC {

	public boolean giveItem = false;
	
	public CactusFarmer () {
		super();
		
		text.changeText("THAT TORNADO FARMER OVER THERE IS SO ANNOYING");
		text.pushString("HE STOLE MY STYLE HE STOLE MY PRIME CACTUS GROWING LOCATION");
		text.pushString("HE EVEN TOOK MY 3 POTTED CACTUS'S AND HID THEM AROUND THE DESERT");
		text.pushString("IM GONNA GET THEM BACK JUST YOU WAIT");
		text.pushString("AND WHEN I DO HE IS GOING DOWN YOU HEAR ME");
		
		
		
		this.setSprite(new Sprite ("resources/sprites/cactusFarmer.png"));
		this.setHitboxAttributes(0, 0, 50, 80);
		this.setRenderPriority(-1);	
		this.adjustHitboxBorders();
	}
	
	@Override
	public void initText() {
		
		if (giveItem) {
			giveItem = false;
			BombMaster.objectives.giveCactusBombs();
		}
		
		text.changeText("WHERE COULD THOSE POTTED CACTUSES BE");
		text.pushString("I NEED THEM TO EXACT MY REVENGE");
		if (BombMaster.objectives.hasAllCactuses()) {
			this.resetTextbox ("THANKS AGAIN MAN THAT TORNADO GUY IS GOING DOWN");
		}
		if (BombMaster.objectives.hasBombNado()) {
			this.resetTextbox("AHHHHHHHH I SWEAR ILL GET HIM BACK FOR THIS IF ITS THE LAST THING I DO!");
		}
	}
	
	@Override
	public void updateForObjectives() {
		if (BombMaster.objectives.hasAllCactuses() && !BombMaster.objectives.hasCactusBombs()) {
			giveItem = true;
			this.resetTextbox("WHOA YOU FOUND ALL MY CACTUSES");
			text.pushString("NO WAY HAND EM OVER");
			text.pushString("ACTUALLY HAND OVER YOUR BOMBS TOO I GOT A GOOD IDEA");
			text.pushString("HA IT WORKED YOUR BOMBS SHOULD HAVE SOME CACTUS SPIKES IN THEM NOW");
			text.pushString("NOW TO GET MY REVENGE ON THAT TORNADO GUY");
		} else {
			if (BombMaster.objectives.hasBombNado()) {
				this.setSprite(new Sprite ("resources/sprites/config/tornadoedCactusFarmer.txt"));
				this.getAnimationHandler().setFrameTime(50);
				this.setHitboxAttributes(0, 0, 41, 49);
				this.resetTextbox("AHHHHHHHH I SWEAR ILL GET HIM BACK FOR THIS IF ITS THE LAST THING I DO!");
			}
		}
	}
	
	
}