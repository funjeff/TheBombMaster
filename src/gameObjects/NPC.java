package gameObjects;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Textbox;

public class NPC extends GameObject {
	
	Textbox text = new Textbox ("");

	public NPC () {
		text.setFont("Bomb");
		text.setBox("Bomb");
		
		text.changeWidth(50);
		text.changeHeight(7);
	}
	
	public void resetTextbox (String startText) {
		text = new Textbox (startText);
		text.setFont("Bomb");
		text.setBox("Bomb");
		
		text.changeWidth(50);
		text.changeHeight(7);
	}
	
	public void onTalk() {
		GameCode.getBombMaster().freeze();
		text.declare(100, 300);
	}

	@Override
	public void frameEvent () {
		if (text.isEmpty()) {
			onClose();
		}
	}

	public void onClose () {
		text.forget();
		GameCode.getBombMaster().unfreeze();
		//many init texts freeze player so it has to go in this order to be correct
		this.initText();
	}
	
	public void updateForObjectives() {
		
	}
	
	@Override
	public void onDeclare() {
		updateForObjectives();
	}
	
	public void initText() {
		
	}
}
