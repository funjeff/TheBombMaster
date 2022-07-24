package gameObjects;

import engine.Sprite;
import engine.Textbox;

public class BombGolfCounter extends NPC {

	boolean talkedOnce = false;
	boolean loadedEndDiolog = false;
	
	
	public BombGolfCounter () {
		super();
		
		text.changeText("YOU WANT TO TRY GOLFING?  WE GOT A NEW CHALLANGE COURSE AND ANYONE WHO CLEARS IT WINS A FREE GOLF CLUB");
		text.pushString("THERES THREE HOLES TO CLEAR ONE HERE IN THE GRASSLAND ONE IN THE DESERT AND ONE IN THE MOUNTAINS");
		text.pushString("IF YOU GET A GOLF BALL IN EACH HOLE YOU WIN!");
		text.pushString("BE SURE TO LET ME KNOW IF YOU FINISH");
		

		
		this.setSprite(new Sprite ("resources/sprites/bombGolfCounter.png"));
		this.setHitboxAttributes(-30, 13, 60, 40);
		this.setRenderPriority(-1);
		this.adjustHitboxBorders();
		
	}
	
	
	@Override
	public void initText () {
		talkedOnce = true;
		
		if (loadedEndDiolog) {
			if (!BombMaster.objectives.hasGolfClub()) {
				BombMaster.objectives.giveGolfClub();
			}
			resetTextbox("WE WILL ALLWAYS REMEMBER YOU AS A HERO");
		}
		
		updateForObjectives();
		
		
	}
	
	@Override
	public void updateForObjectives () {
		
		if (!talkedOnce) {
			return;
		}
		
		boolean hole1 = BombMaster.objectives.hasCompleteGrassHole();
		boolean hole2 = BombMaster.objectives.hasCompleteDesertHole();
		boolean hole3 = BombMaster.objectives.hasCompleteMountainHole();
		
		if (!hole1 && !hole2 && !hole3) {
			resetTextbox ("LOOKS LIKE YOU HAVENT FOUND ANY HOLES YET");
			text.pushString("BUT IM STILL ROOTING FOR YOU!");
			text.pushString("REMEMBER THEY ARE IN THE GRASSLANDS MOUNTAINS AND DESERT");
		}
		
		if (hole1 && !hole2 && !hole3) {
			resetTextbox ("OH YOU FOUND THE GRASS HOLE NICE!");
			text.pushString("LOOKS LIKE YOU STILL NEED TO FIND THE MOUNTAIN AND DESERT HOLES");
		}
		
		if (!hole1 && hole2 && !hole3) {
			resetTextbox ("OH YOU FOUND THE DESERT HOLE NICE!");
			text.pushString("LOOKS LIKE YOU STILL NEED TO FIND THE MOUNTAIN AND GRASS HOLES");
		}
		if (!hole1 && !hole2 && hole3) {
			resetTextbox ("OH YOU FOUND THE MOUNTAIN HOLE NICE!");
			text.pushString("LOOKS LIKE YOU STILL NEED TO FIND THE GRASS AND DESERT HOLES");
		}
		if (!hole1 && hole2 && hole3) {
			resetTextbox ("YOU GOT TWO OF THEM JUST ONE MORE TO GO!");
			text.pushString("AND IT LOOKS LIKE IT IS IN THE GRASSLANDS");
		}
		if (hole1 && !hole2 && hole3) {
			resetTextbox ("YOU GOT TWO OF THEM JUST ONE MORE TO GO!");
			text.pushString("AND IT LOOKS LIKE IT IS IN THE DESERT");
		}
		if (hole1 && hole2 && !hole3) {
			resetTextbox ("YOU GOT TWO OF THEM JUST ONE MORE TO GO!");
			text.pushString("AND IT LOOKS LIKE IT IS IN THE MOUNTAIN");
		}
		if (hole1 && hole2 && hole3) {
			resetTextbox ("NO WAY YOU GOT ALL THE HOLES");
			text.pushString("YOUR INSANE DUDE HERE TAKE THIS GOLF CLUB AS PROMISED");
			loadedEndDiolog = true;
		}
		
	}

	
}