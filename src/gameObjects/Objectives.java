package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;

public class Objectives {
	
	
	
	//represtent completed golf holes
	boolean grassHole = false;
	boolean desertHole = false;
	boolean mountainHole = false;
	
	//found cactuses and tornados
	int foundCactuses = 0;
	int foundTornados = 0;
	
	boolean hasBombNado = false;
	boolean hasCactusBombs = false;
	
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
	
	public void giveCactusBombs() {
		hasCactusBombs = true;
		this.onObjectiveComplete();
	}
	
	public void giveBombNado() {
		hasBombNado = true;
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
		
		for (int i = 0; i < npcList.size(); i++) {
			for (int j = 0; j < npcList.get(i).size(); j++) {
			NPC cur = (NPC) npcList.get(i).get(j);
			cur.updateForObjectives();
			}
		}
		
		
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

	public boolean hasAllCactuses () {
		return foundCactuses == 3;
	}
	
	public boolean hasAllTornados () {
		return foundTornados == 3;
	}
	
}
