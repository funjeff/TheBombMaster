package gameObjects;

import java.util.ArrayList;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;

public class Objectives {
	
	
	
	//represtent completed golf holes
	boolean grassHole = false;
	boolean desertHole = false;
	boolean mountainHole = false;
	
	boolean hasGolfClub = true;
	
	//found cactuses and tornados
	int foundCactuses = 0;
	int foundTornados = 0;
	
	boolean hasBombNado = true;
	boolean hasCactusBombs = false;
	boolean hasPegasusBombs = true;
	
	boolean hasMasterBombs = true;
	
	public Objectives() {
		
	}
	
	public void completeGrassHole() {
		grassHole = true;
		this.onObjectiveComplete();
	}
	
	public void completeDesertHole () {
		desertHole = true;
		this.onObjectiveComplete();
	}
	
	public void completeMountainHole () {
		mountainHole = true;
		this.onObjectiveComplete();
	}
	
	public void giveGolfClub () {
		hasGolfClub = true;
		
		GolfClub g = new GolfClub ();
		g.onPickup();
		g.setX(GameCode.getBombMaster().getX());
		g.setY(GameCode.getBombMaster().getY() - 32);
		
		g.declare();
		
		Hud.bar.updateWeaponsBar();
		this.onObjectiveComplete();
	}
	
	public void giveCactusBombs() {
		hasCactusBombs = true;
		CactusBombs c = new CactusBombs ();
		c.onPickup();
		c.setX(GameCode.getBombMaster().getX());
		c.setY(GameCode.getBombMaster().getY() - 32);
		
		c.declare();
		
		this.onObjectiveComplete();
	}
	
	public void givePegasusBombs() {
		hasPegasusBombs = true;
		
		this.onObjectiveComplete();
	}
	
	public void giveBombNado() {
		hasBombNado = true;
		
		BombNado bn = new BombNado ();
		bn.onPickup();
		bn.setX(GameCode.getBombMaster().getX() - 5);
		bn.setY(GameCode.getBombMaster().getY() - 49);
		
		bn.declare();
		
		Hud.bar.updateWeaponsBar();
		this.onObjectiveComplete();
	}
	
	public void findCactus () {
		foundCactuses = foundCactuses + 1;
		if (foundCactuses == 3) {
			onObjectiveComplete();
		}
	}
	public void findTornado () {
		foundTornados = foundTornados + 1;
		if (foundTornados == 3) {
			onObjectiveComplete();
		}
	}
	

	
	
	public void onObjectiveComplete() {
		
		//updates all NPC text
		ArrayList <ArrayList <GameObject>> npcList = ObjectHandler.getChildrenByName("NPC");
		
		if (npcList == null) {
			return;
		}
		
		for (int i = 0; i < npcList.size(); i++) {
			for (int j = 0; j < npcList.get(i).size(); j++) {
			NPC cur = (NPC) npcList.get(i).get(j);
			cur.updateForObjectives();
			}
		}
		
		
	}
	
	public boolean hasGolfClub () {
		return hasGolfClub;
	}
	
	public boolean hasCompleteMountainHole() {
		return mountainHole;
	}
	
	public boolean hasCompleteDesertHole() {
		return desertHole;
	}
	
	public boolean hasCompleteGrassHole() {
		return grassHole;
	}
	
	public boolean hasCactusBombs() {
		return hasCactusBombs;
	}
	
	public boolean hasBombNado() {
		return hasBombNado;
	}
	
	public boolean hasPegasusBombs() {
		return hasPegasusBombs;
	}

	public boolean hasAllCactuses () {
		return foundCactuses == 3;
	}
	
	public boolean hasAllTornados () {
		return foundTornados == 3;
	}
	
	public boolean hasMasterBombs () {
		return hasMasterBombs;
	}
	
}
