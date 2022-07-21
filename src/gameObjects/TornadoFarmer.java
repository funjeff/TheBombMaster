package gameObjects;

import engine.Sprite;

public class TornadoFarmer extends NPC {

	boolean giveItem = false;
	
	public TornadoFarmer () {
		super();
		
		text.changeText("HI IM A TORNADO FARMER I RAISE MY TORNADOS WITH LOVE AND CARE");
		text.pushString("THAT STUPID CACTUS FARMER DOESN'T RESPECT MY PROFESSION THOUGH");
		text.pushString("SO I HID HIS STUPID CACTUESES TO GET BACK AT HIM");
		text.pushString("I WAS GONNA SEND MY 3 TORNADOS AFTER HIM WHILE HE WAS CRYING BUT THEY SEEM TO HAVE GONE MISSING");
		text.pushString("I BET THERE STILL SOMEWHERE IN THE DESERT BUT I JUST CAN'T FIND THEM WHAT SHOULD I DO?");
		
		
		
		this.setSprite(new Sprite ("resources/sprites/tornadoFarmer.png"));
		this.setHitboxAttributes(0, 0, 50, 80);
		this.setRenderPriority(-1);	
		this.adjustHitboxBorders();
	}
	
	@Override
	public void initText() {
		
		if (giveItem) {
			giveItem = false;
			BombMaster.objectives.giveBombNado();
		}
		
		text.changeText("COME ON TORNADOS GET OVER HERE   PLEASE?");
		text.pushString("ITS NO USE THERE NOWHERE TO BE FOUND");
		if (BombMaster.objectives.hasAllTornados()) {
			this.resetTextbox ("TIME TO PROVE THAT ONCE AND FOR ALL THAT TORNADOS ARE BETTER THAN CACTUSES");
		}
		if (BombMaster.objectives.hasCactusBombs()) {
			this.resetTextbox("AHHH HE GOT ME IM DONE FOR GO ON WITHOUT ME");
		}
		
	}
	
	@Override
	public void updateForObjectives() {
		if (BombMaster.objectives.hasAllTornados() && !BombMaster.objectives.hasBombNado()) {
			giveItem = true;
			this.resetTextbox("YOU FOUND ALL MY TORNADOS?  YOUR A LIFE SAVER");
			text.pushString("WELL UNLESS YOU WERE TRYING TO SAVE THAT CACTUS GUYS LIFE");
			text.pushString("HOLD ON BEFORE YOU GO I GOT A REALLY GOOD IDEA");
			text.pushString("I CALL IT THE BOMBNADO WITH YOUR BOMBS AND MY TORNADOS IT SHOULD BE UNSTOPABLE");
			text.pushString("NOW ITS TIME TO SHOW THAT CACTUS GUY WHO'S BOSS");
		} else {
			if (BombMaster.objectives.hasCactusBombs()) {
				this.setSprite(new Sprite ("resources/sprites/pinnedTornadoFarmer.png"));
				this.setHitboxAttributes(0, 0, 93, 62);
				this.resetTextbox("AHHH HE GOT ME IM DONE FOR GO ON WITHOUT ME");
			}
		}
		
	}
	
	
}