package gameObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import engine.Textbox;

public class WeaponBar extends GameObject {

	public int selectedWeapon = 0;
	
	public int weaponCount = 0;
	
	public String [] weapons = new String [0];
	
	public ArrayList <WeaponBox> boxes = new ArrayList <WeaponBox> ();
	
	
	

	public static final int GOLF_CLUB_PREFERED_SLOT = 1;
	public static final int BOMBNADO_PREFERED_SLOT = 2;
	public static final int PEGASUS_BOMBS_PREFERED_SLOT = 3;
	
	Textbox weaponName = new Textbox ("empty");
	
	int nameTimer = 0;
	
	public WeaponBar () {
		weaponName.changeBoxVisability();
		this.setGameLogicPriority(2);
		updateWeaponsBar();
	}
	
	@Override
	public void frameEvent () {
		if (GameCode.keyPressed('\\', this)) {
			
			boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
			
			selectedWeapon = selectedWeapon + 1;
			if (selectedWeapon >= weaponCount) {
				selectedWeapon = 0;
			}
			
			boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
			weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
			if (!weaponName.declared()) {
				weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
			}
			nameTimer = 30;
		}
		
		if (GameCode.keyPressed('1', this)) {
			
			if (weaponCount >= 1) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 0;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('2', this)) {
			
			if (weaponCount >= 2) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 1;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('3', this)) {
			
			if (weaponCount >= 3) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 2;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('4', this)) {
			
			if (weaponCount >= 4) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 3;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('5', this)) {
			
			if (weaponCount >= 5) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 4;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('6', this)) {
			
			if (weaponCount >= 6) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 5;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('7', this)) {
			
			if (weaponCount >= 7) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 6;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('8', this)) {
			
			if (weaponCount >= 8) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 7;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('9', this)) {
			
			if (weaponCount >= 9) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 8;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (GameCode.keyPressed('0', this)) {
			
			if (weaponCount >= 10) {
			
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/weaponBox.png"));
				
				selectedWeapon = 9;
				
				boxes.get(selectedWeapon).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
				weaponName.changeText("~S8~" + weapons[selectedWeapon].toUpperCase());
				if (!weaponName.declared()) {
					weaponName.declare(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2 + 15),GameCode.getBombMaster().getY() - 10);
				}
				nameTimer = 30;
			}
		}
		
		if (nameTimer > 0) {
			nameTimer = nameTimer - 1;
			if (nameTimer == 0) {
				weaponName.forget();
			}
			weaponName.setX(GameCode.getBombMaster().getX() - ((weapons[selectedWeapon].length()*8)/2) + 15);
			weaponName.setY(GameCode.getBombMaster().getY() - 10);

		} 
		
	}
	
	@Override
	public void draw () {
		for (int i= 0; i < boxes.size(); i++) {
			boxes.get(i).draw();
		}
	}
	
	public void updateWeaponsBar () {
		weaponCount = 0;
		
		if (BombMaster.objectives.hasGolfClub()) {
			weaponCount = weaponCount + 1;
		}
		
		if (BombMaster.objectives.hasBombNado()) {
			weaponCount = weaponCount + 1;
		}
		
		if (BombMaster.objectives.hasPegasusBombs()) {
			weaponCount = weaponCount + 1;
		}
		
		//weapon count should be finalized by this point
		
		weapons = new String [weaponCount];
		
		Arrays.fill(weapons, "empty");
		
		ArrayList <String> unavailabeSlots = new ArrayList <String> ();
		
		if (BombMaster.objectives.hasGolfClub()) {
			if (weaponCount <= GOLF_CLUB_PREFERED_SLOT) {
				unavailabeSlots.add("golf club");
			} else {
				weapons[GOLF_CLUB_PREFERED_SLOT] = "golf club";
			}
		}
		
		if (BombMaster.objectives.hasBombNado()) {
			if (weaponCount <= BOMBNADO_PREFERED_SLOT) {
				unavailabeSlots.add("bombnado");
			} else {
				weapons[BOMBNADO_PREFERED_SLOT] = "bombnado";
			}
		}
		
		if (BombMaster.objectives.hasPegasusBombs()) {
			if (weaponCount <= PEGASUS_BOMBS_PREFERED_SLOT) {
				unavailabeSlots.add("pegasus bombs");
			} else {
				weapons[PEGASUS_BOMBS_PREFERED_SLOT] = "pegasus bombs";
			}
		}
		
		boxes = new ArrayList <WeaponBox> ();
		
		for (int i = 0; i < weapons.length; i++) {
			if (weapons[i].equals("empty")) {
				weapons[i] = unavailabeSlots.get(0);
				unavailabeSlots.remove(0);
			}
			boxes.add(new WeaponBox ());
			boxes.get(i).setItemString(weapons[i]);
			boxes.get(i).setX(this.getX() + i*36);
			boxes.get(i).setY(this.getY());
			if (i != 10) {
				boxes.get(i).setNumber(i + 1);
			} else {
				boxes.get(i).setNumber(0);
			}
			
		}
		
		selectedWeapon = 0;
		
		boxes.get(0).setSprite(new Sprite ("resources/sprites/selectedWeaponBox.png"));
	}

	
	public boolean bombNadoSelected () {
		if (weaponCount == 0) {
			return false;
		}
		return weapons[selectedWeapon].equals("bombnado");
	}
	
	public boolean golfClubSelected () {
		if (weaponCount == 0) {
			return false;
		}
		return weapons[selectedWeapon].equals("golf club");
	}
	
	public boolean pegasusBombsSelected () {
		if (weaponCount == 0) {
			return false;
		}
		return weapons[selectedWeapon].equals("pegasus bombs");
	}

	public class WeaponBox extends GameObject {
		
		ItemPuppet item = new ItemPuppet ();
		
		
		Textbox boxNumber = new Textbox ("empty");
		
		public WeaponBox () {
			this.setSprite(new Sprite ("resources/sprites/weaponBox.png"));
			this.useSpriteHitbox();
			boxNumber.changeBoxVisability();
		}
		
		public void setNumber (int newNumber) {
			boxNumber.changeText("~S8~" + newNumber);
		}
		
		@Override
		public void setX (double val) {
			super.setX(val);
			item.setCenterX(this.getCenterX());
			boxNumber.setX(this.getX() + 28);
		}
		
		@Override
		public void setY (double val) {
			super.setY(val);
			item.setCenterY(this.getCenterY());
			boxNumber.setY(this.getY() + 32);
		}
		
		public void setItemString (String item) {
			this.item.setString(item);
		}
		
		@Override
		public void draw () {
			super.draw();
			item.draw();
			boxNumber.draw();
		}
	}
	
	public class ItemPuppet extends GameObject {
		public ItemPuppet () {
			
		}
		
		public void setString (String item) {
			if (item.equals("golf club")) {
				this.setSprite(new Sprite ("resources/sprites/golf club.png"));
			}
			if (item.equals("bombnado")) {
				this.setSprite(new Sprite ("resources/sprites/config/bombNado.txt"));
			}
			if (item.equals("pegasus bombs")) {
				this.setSprite(new Sprite ("resources/sprites/pegasusBombs.png"));
			}
			
			
			this.useSpriteHitbox();
		}
	}
	
}
