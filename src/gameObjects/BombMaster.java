package gameObjects;

import javax.swing.plaf.basic.BasicComboBoxUI.KeyHandler;

import engine.CollisionInfo;
import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import map.Room;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class BombMaster extends GameObject {
	
	public static final Sprite FORWARD = new Sprite ("resources/sprites/config/bombMaster/top/throw/front.txt");
	public static final Sprite BACK = new Sprite ("resources/sprites/config/bombMaster/top/throw/back.txt");
	public static final Sprite SIDE = new Sprite ("resources/sprites/config/bombMaster/top/throw/side.txt");
	public static final Sprite DIAGONAL_BACK = new Sprite ("resources/sprites/config/bombMaster/top/throw/diagonalBack.txt");
	public static final Sprite DIAGONAL_FORWARD = new Sprite ("resources/sprites/config/bombMaster/top/throw/diagonalFront.txt");
	
	
	public static final Sprite FORWARD_PEGASUS_TOSS = new Sprite ("resources/sprites/config/bombMaster/top/pegasusToss/front.txt");
	public static final Sprite BACK_PEGASUS_TOSS = new Sprite ("resources/sprites/config/bombMaster/top/pegasusToss/back.txt");
	public static final Sprite SIDE_PEGASUS_TOSS = new Sprite ("resources/sprites/config/bombMaster/top/pegasusToss/side.txt");
	public static final Sprite DIAGONAL_BACK_PEGASUS_TOSS = new Sprite ("resources/sprites/config/bombMaster/top/pegasusToss/diagonalBack.txt");
	public static final Sprite DIAGONAL_FORWARD_PEGASUS_TOSS = new Sprite ("resources/sprites/config/bombMaster/top/pegasusToss/diagonalFront.txt");
	
	public static final Sprite FORWARD_MASTER_BOMBS = new Sprite ("resources/sprites/player front master bomb form.png");
	public static final Sprite BACK_MASTER_BOMBS = new Sprite ("resources/sprites/player back master bombs.png");
	public static final Sprite LEFT_MASTER_BOMBS = new Sprite ("resources/sprites/player master bombs left.png");
	public static final Sprite RIGHT_MASTER_BOMBS = new Sprite ("resources/sprites/player master bombs right.png");
	public static final Sprite DIAGONAL_BACK_MASTER_BOMBS = new Sprite ("resources/sprites/player upright master bombs.png");
	public static final Sprite DIAGONAL_FORWARD_MASTER_BOMBS = new Sprite ("resources/sprites/player downleft master bombs.png");
	
	public static final Sprite FRONT_MASTER_BOMBS_SHADOW = new Sprite ("resources/sprites/player front master bomb form shadow.png");
	public static final Sprite SIDE_MASTER_BOMBS_SHADOW = new Sprite ("resources/sprites/player master bombs side shadow.png");
	public static final Sprite DIAGONAL_BACK_MASTER_BOMBS_SHADOW = new Sprite ("resources/sprites/player upright master bombs shadow.png");
	public static final Sprite DIAGONAL_FORWARD_MASTER_BOMBS_SHADOW = new Sprite ("resources/sprites/player downleft master bombs shadow.png");
	
	
	public static final Sprite FORWARD_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/front.txt");
	public static final Sprite SIDE_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/sides.txt");
	public static final Sprite DIAGONAL_LEGS_WALK = new Sprite ("resources/sprites/config/bombMaster/legs/walk/digonal.txt");
	
	
	public static final int DASH_START_SPEED = 40;
	
	boolean throwingBomb = false;

	Legs myLegs = new Legs();
	
	Shadow shad = new Shadow ();
	
	Bomb beingThrown = null;
	
	boolean dead = false;
	
	double explodeNum = 1;
	
	boolean frozen = false;
	
	boolean throwStuned = false;
	
	boolean throwingPegasusBomb = false;
	
	static Objectives objectives = new Objectives ();
	
	double dashSpeed;
	
	SwingingClub club = new SwingingClub();
	
	float shirtHue = (float) 0.0;
	boolean shirtHueDir = false;
	//0xFFB875B3 is the color of the bomb masters shirt
	int shirtSpriteHue = 0xFFB875B3;
	boolean shirtPurpledOut = false;
	
	//used for the bombmasters attack where the bomb comes from off the screen
	BombPuller pull = new BombPuller ();
	
	int dashDir = 0;
	//0 = not dashing
	//1 = dashing left
	//2 = dashing right
	//3 = dashing up
	//4 = dashing down
	//5 = dashing upLeft
	//6 = dashing upRight
	//7 = dashing downLeft
	//8 = dashing downRight
	
	public BombMaster()
	{
		this.setRenderPriority(2);
		this.setSprite(FORWARD_MASTER_BOMBS);
		myLegs.setSprite(FORWARD_LEGS_WALK);
		
		this.getAnimationHandler().setFrameTime(0);
		myLegs.getAnimationHandler().setFrameTime(100);
	}
	
	@Override
	public void frameEvent () {
		
		if (this.isCollidingChildren("NPC") && GameCode.keyPressed(KeyEvent.VK_ENTER, this) &&!frozen) {
			((NPC)this.getCollisionInfo().getCollidingObjects().get(0)).onTalk();
		}

		if (!dead) {
			if (!frozen) {
				updateSprite();
				if (objectives.hasMasterBombs()) {
					shad.updateShadowDistance(this);
					shad.alignShadow(this);
				}
			}
			this.setHitboxAttributes(this.getSprite().getWidth(), this.getSprite().getHeight());
		
			if (getX () - Room.getViewXAcurate () > 700) {
				Room.setView ((int)getX () - 700, Room.getViewYAcurate ());
			}
			if (getY () - Room.getViewYAcurate () > 400) {
				Room.setView (Room.getViewXAcurate (), (int)getY () - 400);
			}
			if (getX () - Room.getViewXAcurate () < 200) {
				Room.setView ((int)getX () - 200, Room.getViewYAcurate ());
			}
			if (getY () - Room.getViewYAcurate () < 100) {
				Room.setView (Room.getViewXAcurate (), (int)getY () - 100);
			}
		
		} else {
			
			Random r = new Random();
			
			Explosion e = new Explosion (explodeNum*1 + .1);
				
			e.declare();
			
			e.setX(this.getX() + this.hitbox().width/2 - r.nextInt(e.hitbox().width/2));
			e.setY(this.getY() + this.hitbox().width/2 - r.nextInt(e.hitbox().height/2));
			
			e.setRenderPriority(10);
			
			e.makeAsteticOnly();
				
			explodeNum = explodeNum + (r.nextDouble()*explodeNum/2);
			
			if (explodeNum >= 20) {
				e.setX(this.getX() + this.hitbox().width/2 - e.hitbox().width/2);
				e.setY(this.getY() + this.hitbox().width/2 - e.hitbox().height/2);
				
				SootPile p = new SootPile ();
				p.setX(this.getX());
				p.setY(this.getY());
				
				p.declare();
				
				if (objectives.hasMasterBombs()) {
					Sprite.replaceColor(this.getSprite(), 0xFFB875B3, shirtSpriteHue);
					shirtPurpledOut = true;
				}
				
				this.forget();
			}
		}

	}
	
	@Override
	public void draw() {
		
		if (!objectives.hasMasterBombs) {
			alignLegs();
		}
		
		if (!throwingBomb && !throwingPegasusBomb) {
			handleSecondarys();
		}
		
		
		if (!objectives.hasMasterBombs) {
			myLegs.draw();
		}
		
		super.draw();
		
	}
	
	//this method probably isen't properly named anymore
	//whatever this whole class has so many code smells by this point I could recreate the iconic "pie on the windowsil" scene with it
	//is that scene iconic?  Wait maybe im just being an idiot here 
	//this one https://www.youtube.com/watch?v=-_7oZeKBpsE from 27 seconds to 34 seconds where he like floats over to the pie
	//its a trope right?  I feel like thats a thing in a lot of stuff but I can't think of another example where somebody does that
	
	//HOLY SHIT ITS LIKE 2 HOURS LATER AND YOUTUBE RECOMENDED ME ANOTHER ONE
	//https://www.youtube.com/watch?v=4In8wOZreGQ  I fuckin knew it was a thing
	public void updateSprite() {
		if (dashDir != 0 && !throwingPegasusBomb) {
			handleDash();
		}
		
		if (!throwingBomb && !throwingPegasusBomb) {
			if (this.getAnimationHandler().getFrameTime() != 0) {
				this.getAnimationHandler().setFrameTime(0);
			}
		} else {
			if (throwingBomb) {
				if (!objectives.hasMasterBombs()) {
					animateRegularBombThrow();
				} else {
					animateMasterBombThrow();
				}
			}
		}
		
		
		if (keyDown(KeyEvent.VK_ENTER) && !throwStuned && !throwingPegasusBomb) {
			throwingBomb = true;
		}
		
		if (throwStuned && !keyDown(KeyEvent.VK_ENTER)) {
			throwStuned = false;
		}
		
		if (objectives.hasMasterBombs && !shad.declared()) {
			shad.declare(this.getX(), this.getY());
		}
		
		
		if (!throwingPegasusBomb) {
			handleWalk();
		}
		
		if (throwingPegasusBomb && this.getAnimationHandler().getFrame() == this.getSprite().getFrameCount() - 1) {
			doDash();
		}
		
		if (objectives.hasMasterBombs) {
			
			if (shirtHueDir) {
				shirtHue = (float) (shirtHue - 0.01);
				if (shirtHue < 0) {
					shirtHue = 0;
					shirtHueDir = false;
				}
			} else {
				shirtHue = (float) (shirtHue + 0.01);
				if (shirtHue > 1) {
					shirtHue = 1;
					shirtHueDir = true;
				}
			}
			
			if (!shirtPurpledOut) {
				Sprite.replaceColor(this.getSprite(), 0xFFB875B3, shirtSpriteHue);
				shirtPurpledOut = true;
			}
			
			//change all of the purple in the shirt to the new color
			shirtSpriteHue = Sprite.tweekHueifColorMatch(this.getSprite(), shirtHue, 0xFFB875B3);
			shirtPurpledOut = false;
		}
				
	}
	
	@Override
	public void setSprite (Sprite sprite) {
		if (objectives.hasMasterBombs) {
			if (this.getSprite() != null) {
				//make the shirt go back to being purple
				if (!shirtPurpledOut) {
				
					Sprite.replaceColor(this.getSprite(), 0xFFB875B3, shirtSpriteHue);
				}
				shirtPurpledOut = true;
			}
		}
		super.setSprite(sprite);
	}
	
private void doDash () {
	this.getAnimationHandler().setFrameTime(0);
	throwingPegasusBomb = false;
	
	dashSpeed = DASH_START_SPEED;
	
	Explosion e = new Explosion();
	
	e.makeAsteticOnly();
	
	e.setRenderPriority(-2);
	
	switch (dashDir) {
	case 1:
		if (objectives.hasMasterBombs()) {
			this.setSprite(LEFT_MASTER_BOMBS);
		} else {
			this.setSprite(SIDE);
		}
		this.getAnimationHandler().setFlipHorizontal(false);
		e.declare(this.getX() + 37,this.getY() + 22);
		break;
	case 2:
		if (objectives.hasMasterBombs()) {
			this.setSprite(RIGHT_MASTER_BOMBS);
		} else {
			this.setSprite(SIDE);
		}
		e.declare(this.getX() + 2,this.getY() + 19);
		break;
	case 3:
		if (objectives.hasMasterBombs()) {
			this.setSprite(BACK_MASTER_BOMBS);
		} else {
			this.setSprite(BACK);
		}
		e.declare(this.getX() + 13,this.getY() + 24);
		break;
	case 4:
		if (objectives.hasMasterBombs()) {
			this.setSprite(FORWARD_MASTER_BOMBS);
		} else {
			this.setSprite(FORWARD);
		}
		e.declare(this.getX() + 1,this.getY() + 25);
		break;
	case 5:
		if (objectives.hasMasterBombs()) {
			this.setSprite(DIAGONAL_BACK_MASTER_BOMBS);
		} else {
			this.setSprite(DIAGONAL_BACK);
		}
		e.declare(this.getX() + 49,this.getY() + 27);
		break;
	case 6:
		if (objectives.hasMasterBombs()) {
			this.setSprite(DIAGONAL_BACK_MASTER_BOMBS);
		} else {
			this.setSprite(DIAGONAL_BACK);
		}
		e.declare(this.getX() + 1,this.getY() + 19);
		break;
	case 7:
		if (objectives.hasMasterBombs()) {
			this.setSprite(DIAGONAL_FORWARD_MASTER_BOMBS);
		} else {
			this.setSprite(DIAGONAL_FORWARD);
		}
		e.declare(this.getX() + 25,this.getY() + 18);
		break;
	case 8:
		if (objectives.hasMasterBombs()) {
			this.setSprite(DIAGONAL_FORWARD_MASTER_BOMBS);
		} else {
			this.setSprite(DIAGONAL_FORWARD);
		}
		e.declare(this.getX() + 23,this.getY() + 23);
		break;
		}
}
	


private void alignLegs() {
	if (this.getSprite().equals(SIDE) && this.getAnimationHandler().flipHorizontal()) {
		myLegs.setY(this.getY() + this.getSprite().getHeight() - 5);
		myLegs.setX(this.getX() + 13);
		
	}
	
	if (this.getSprite().equals(SIDE) && !this.getAnimationHandler().flipHorizontal()) {
		myLegs.setY(this.getY() + this.getSprite().getHeight() - 4);
		myLegs.setX(this.getX() + 7);
	}
	
	if (this.getSprite().equals(FORWARD)) {
		myLegs.setY(this.getY() + this.getSprite().getHeight());
		myLegs.setX(this.getX() + 7);
	}
	
	if (this.getSprite().equals(BACK)) {
		myLegs.setY(this.getY() + this.getSprite().getHeight() - 8);
		myLegs.setX(this.getX() + 5);
	}
	if (this.getSprite().equals(DIAGONAL_FORWARD) && this.getAnimationHandler().flipHorizontal()) {
		myLegs.setY(this.getY() + this.getSprite().getHeight() - 11);
		myLegs.setX(this.getX() + 23);
	}
	if (this.getSprite().equals(DIAGONAL_FORWARD) && !this.getAnimationHandler().flipHorizontal()) {
		myLegs.setY(this.getY() + this.getSprite().getHeight() - 11);
		myLegs.setX(this.getX() -4);
	}
	
	if (this.getSprite().equals(DIAGONAL_BACK) && this.getAnimationHandler().flipHorizontal()) {
		myLegs.setY(this.getY() + this.getSprite().getHeight() - 15);
		myLegs.setX(this.getX() + 23);
	}
	if (this.getSprite().equals(DIAGONAL_BACK) && !this.getAnimationHandler().flipHorizontal()) {
		myLegs.setY(this.getY() + this.getSprite().getHeight() - 15);
		myLegs.setX(this.getX());
	}
}
	
private void handleSecondarys () {
	boolean spawnedClub = false;
	
	if (this.getSprite().equals(LEFT_MASTER_BOMBS) || (this.getSprite().equals(SIDE) && this.getAnimationHandler().flipHorizontal())) {
		
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if (useWeapon && !club.declared() && Hud.bar.golfClubSelected()) {
			club.swingLeft();
			
			club.setX(this.getX() + 12);
			club.setY(this.getY() + 25);
			spawnedClub = true;
		}
		
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX(), this.getY());
			proj.throwObj(Math.PI,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashLeft();
		}
		
	}
	
	if (this.getSprite().equals(RIGHT_MASTER_BOMBS) || (this.getSprite().equals(SIDE) && !this.getAnimationHandler().flipHorizontal())) {
		
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if (useWeapon && !club.declared()  && Hud.bar.golfClubSelected()) {
			club.swingRight();
			club.setX(this.getX() + 22);
			club.setY(this.getY() + 25);
			spawnedClub = true;
		}
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX(), this.getY());
			proj.throwObj(0,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashRight();
		}
	}
	
	if (this.getSprite().equals(FORWARD_MASTER_BOMBS) || this.getSprite().equals(FORWARD)) {
		
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if ( useWeapon && !club.declared() && Hud.bar.golfClubSelected()) {
			club.swingDown();
			club.setX(this.getX() + 13);
			club.setY(this.getY() + 40);
			spawnedClub = true;
		}		
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX(), this.getY() + 25);
			proj.throwObj(3*Math.PI/2,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashDown();
		}
	}
	
	if (this.getSprite().equals(BACK_MASTER_BOMBS) || this.getSprite().equals(BACK)) {
		
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if (useWeapon && !club.declared() && Hud.bar.golfClubSelected()) {
			club.swingUp();
			club.setX(this.getX() + 10);
			club.setY(this.getY() + 5);
			spawnedClub = true;
		}
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX(), this.getY() - 25);
			proj.throwObj(Math.PI/2,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashUp();
		}
	}
	if ((this.getSprite().equals(DIAGONAL_FORWARD_MASTER_BOMBS) ||this.getSprite().equals(DIAGONAL_FORWARD)) && this.getAnimationHandler().flipHorizontal()) {
		
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if (useWeapon && !club.declared() && Hud.bar.golfClubSelected()) {
			club.swingDownRight();
			club.setX(this.getX() + 40);
			club.setY(this.getY() + 40);
			spawnedClub = true;
		}
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX() + 20, this.getY() + 20);
			proj.throwObj(7*Math.PI/4,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashDownRight();
		}
	}
	if ((this.getSprite().equals(DIAGONAL_FORWARD_MASTER_BOMBS) || this.getSprite().equals(DIAGONAL_FORWARD)) && !this.getAnimationHandler().flipHorizontal()) {
	
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if (useWeapon && !club.declared() && Hud.bar.golfClubSelected()) {
			club.swingDownLeft();
			club.setX(this.getX() + 10);
			club.setY(this.getY() + 40);
			spawnedClub = true;
		}
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX() - 20, this.getY() + 20);
			proj.throwObj(5*Math.PI/4,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashDownLeft();
		}
	}
	
	if ((this.getSprite().equals(DIAGONAL_BACK_MASTER_BOMBS) || this.getSprite().equals(DIAGONAL_BACK)) && this.getAnimationHandler().flipHorizontal()) {
		
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if (useWeapon && !club.declared() && Hud.bar.golfClubSelected()) {
			club.swingUpLeft();
			club.setX(this.getX() + 13);
			club.setY(this.getY() + 4);
			spawnedClub = true;
		}
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX() - 20, this.getY() - 20);
			proj.throwObj(3*Math.PI/4,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashUpLeft();
		}
	}
	if ((this.getSprite().equals(DIAGONAL_BACK_MASTER_BOMBS) || this.getSprite().equals(DIAGONAL_BACK)) && !this.getAnimationHandler().flipHorizontal()) {
		
		boolean useWeapon = GameCode.keyPressed(KeyEvent.VK_SHIFT, this);
		
		if (useWeapon && !club.declared() && Hud.bar.golfClubSelected()) {
			club.swingUpRight();
			club.setX(this.getX() + 33);
			club.setY(this.getY() + 7);
			spawnedClub = true;
		}
		if (useWeapon && Hud.bar.bombNadoSelected()) {
			BombNadoProjectile proj = new BombNadoProjectile ();
			proj.declare(this.getX() + 20, this.getY() - 20);
			proj.throwObj(Math.PI/4,4);
		}
		
		if (useWeapon && Hud.bar.pegasusBombsSelected()) {
			startDashUpRight();
		}
	}
	
	if (club.declared() && !spawnedClub) {
		club.realDraw();
	}
	
}
private void handleWalk() {
	if (!keyDown('A') && !keyDown('D') && !keyDown('W') && !keyDown('S') && dashDir == 0) {
		
		if (myLegs.getAnimationHandler().getFrameTime() != 0 && !objectives.hasMasterBombs()) {
			int curFrame = myLegs.getAnimationHandler().getFrame();
			myLegs.getAnimationHandler().setFrameTime(0);
			myLegs.getAnimationHandler().setAnimationFrame(curFrame);
		}
		return;
	}
	
	
	//handles walking right
		if (keyDown('D') && !keyDown('W') && !keyDown('S')) {
		
			if (dashDir != 2) {
			dashDir = 0;
		}
			
		if (!throwingBomb && !club.declared()) {
			if (!objectives.hasMasterBombs()) {
				this.setSprite(SIDE);
				myLegs.setSprite(SIDE_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(100);
			} else {
				this.setSprite(RIGHT_MASTER_BOMBS);	
				shad.setSprite(SIDE_MASTER_BOMBS_SHADOW);
				this.getAnimationHandler().setFlipHorizontal(false);
				Random rand = new Random ();
				if (rand.nextInt(15) == 4) {
					shad.knockBackUp(this);
				}
			}
			
		}
		
		this.goX(this.getX() + 4);
		club.setX(club.getX() + 4);
	}
	
	//handles walking left
	if (keyDown('A') && !keyDown('W') && !keyDown('S')) {
		
		if (!throwingBomb && !club.declared()) {
			if (!objectives.hasMasterBombs()) {
				this.setSprite(SIDE);
				myLegs.setSprite(SIDE_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(true);
				myLegs.getAnimationHandler().setFlipHorizontal(true);
			
				myLegs.getAnimationHandler().setFrameTime(100);
			} else {
				this.setSprite(LEFT_MASTER_BOMBS);
				shad.setSprite(SIDE_MASTER_BOMBS_SHADOW);
				this.getAnimationHandler().setFlipHorizontal(false);
				Random rand = new Random ();
				if (rand.nextInt(15) == 4) {
					shad.knockBackUp(this);
				}
			}
		}
		
		this.goX(this.getX() - 4);
		club.setX(club.getX() - 4);
		
		if (dashDir != 1) {
			dashDir = 0;
		}
	}
	
	//handles walking up
	if (keyDown('W') && !keyDown('A') && !keyDown('D')) {
		
		if (!throwingBomb && !club.declared()) {
			if (!objectives.hasMasterBombs()) {
				this.setSprite(BACK);
				myLegs.setSprite(FORWARD_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(50);
			} else {
				this.setSprite(BACK_MASTER_BOMBS);
				shad.setSprite(FRONT_MASTER_BOMBS_SHADOW);
				this.getAnimationHandler().setFlipHorizontal(false);
				Random rand = new Random ();
				if (rand.nextInt(15) == 4) {
					shad.knockBackUp(this);
				}
			}
		}
		
		this.goY(this.getY() - 4);
		club.setY(club.getY() - 4);
		
		if (dashDir != 3) {
			dashDir = 0;
		}
	}
	
	
	//handles walking down
	if (keyDown('S') && !keyDown('A') && !keyDown('D')) {
		if (!throwingBomb && !club.declared()) {
			if (!objectives.hasMasterBombs()) {
				this.setSprite(FORWARD);
				myLegs.setSprite(FORWARD_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(50);
			} else {
				this.setSprite(FORWARD_MASTER_BOMBS);
				shad.setSprite(FRONT_MASTER_BOMBS_SHADOW);
				this.getAnimationHandler().setFlipHorizontal(false);
				Random rand = new Random ();
				if (rand.nextInt(15) == 4) {
					shad.knockBackUp(this);
				}
			}
		}
		
		this.goY(this.getY() + 4);
		club.setY(club.getY() + 4);
		if (dashDir != 4) {
			dashDir = 0;
		}
	}
	
	//handles walking downleft
	if (keyDown('S') && keyDown('A')) {
		
		if (!throwingBomb && !club.declared()) {
			if (!objectives.hasMasterBombs()) {
				this.setSprite(DIAGONAL_FORWARD);
				myLegs.setSprite(DIAGONAL_LEGS_WALK);
			
				this.getAnimationHandler().setFlipHorizontal(false);
				myLegs.getAnimationHandler().setFlipHorizontal(false);
			
				myLegs.getAnimationHandler().setFrameTime(100);
			} else {
				this.setSprite(DIAGONAL_FORWARD_MASTER_BOMBS);
				shad.setSprite(DIAGONAL_FORWARD_MASTER_BOMBS_SHADOW);
				
				this.getAnimationHandler().setFlipHorizontal(false);
				shad.getAnimationHandler().setFlipHorizontal(false);
				
				Random rand = new Random ();
				if (rand.nextInt(15) == 4) {
					shad.knockBackUp(this);
				}
			}
		}
		
		this.goX(this.getX() - 4);
		this.goY(this.getY() + 4);	
		club.setX(club.getX() - 4);
		club.setY(club.getY() + 4);
		
		if (dashDir != 7) {
			dashDir = 0;
		}
		
	}
	
	//handles walking downright
	if (keyDown('S') && keyDown('D') && !keyDown('A')) {
		
		if (!throwingBomb && !club.declared()) {
			if (!objectives.hasMasterBombs()) {
				this.setSprite(DIAGONAL_FORWARD);
				myLegs.setSprite(DIAGONAL_LEGS_WALK);
						
				this.getAnimationHandler().setFlipHorizontal(true);
				myLegs.getAnimationHandler().setFlipHorizontal(true);
				
				myLegs.getAnimationHandler().setFrameTime(100);
			} else {
				this.setSprite(DIAGONAL_FORWARD_MASTER_BOMBS);
				shad.setSprite(DIAGONAL_FORWARD_MASTER_BOMBS_SHADOW);
				
				this.getAnimationHandler().setFlipHorizontal(true);
				shad.getAnimationHandler().setFlipHorizontal(true);
				
				Random rand = new Random ();
				if (rand.nextInt(15) == 4) {
					shad.knockBackUp(this);
				}
			}
		}
		
		this.goX(this.getX() + 4);
		this.goY(this.getY() + 4);	
		club.setX(club.getX() + 4);
		club.setY(club.getY() + 4);
	
		if (dashDir != 8) {
			dashDir = 0;
		}
		
	}
	
	//handles walking upright
		if (keyDown('W') && keyDown('D') && !keyDown('A')) {
			if (!throwingBomb && !club.declared()) {
				if (!objectives.hasMasterBombs()) {
					this.setSprite(DIAGONAL_BACK);
					myLegs.setSprite(DIAGONAL_LEGS_WALK);
							
					this.getAnimationHandler().setFlipHorizontal(false);
					myLegs.getAnimationHandler().setFlipHorizontal(false);
					
					myLegs.getAnimationHandler().setFrameTime(100);
				} else {
					this.setSprite(DIAGONAL_BACK_MASTER_BOMBS);
					shad.setSprite(DIAGONAL_BACK_MASTER_BOMBS_SHADOW);
					
					this.getAnimationHandler().setFlipHorizontal(false);
					shad.getAnimationHandler().setFlipHorizontal(false);
					
					Random rand = new Random ();
					if (rand.nextInt(15) == 4) {
						shad.knockBackUp(this);
					}
				}
			}
			
			this.goX(this.getX() + 4);
			this.goY(this.getY() - 4);	
			club.setX(club.getX() + 4);
			club.setY(club.getY() - 4);
		
			if (dashDir != 6) {
				dashDir = 0;
			}
			
		}
		
		//handles walking upleft
		if (keyDown('W') && keyDown('A')) {
			
			if (!throwingBomb && !club.declared()) {
				if (!objectives.hasMasterBombs()) {
					this.setSprite(DIAGONAL_BACK);
					myLegs.setSprite(DIAGONAL_LEGS_WALK);
						
					this.getAnimationHandler().setFlipHorizontal(true);
					myLegs.getAnimationHandler().setFlipHorizontal(true);
				
					myLegs.getAnimationHandler().setFrameTime(100);
				} else {
					this.setSprite(DIAGONAL_BACK_MASTER_BOMBS);
					this.getAnimationHandler().setFlipHorizontal(true);
					
					shad.setSprite(DIAGONAL_BACK_MASTER_BOMBS_SHADOW);
					shad.getAnimationHandler().setFlipHorizontal(true);
					
					Random rand = new Random ();
					if (rand.nextInt(15) == 4) {
						shad.knockBackUp(this);
					}
				}
			}
			
			this.goX(this.getX() - 4);
			this.goY(this.getY() - 4);	
			club.setX(club.getX() - 4);
			club.setY(club.getY() - 4);
		
			if (dashDir != 5) {
				dashDir = 0;
			}
			
		}
}


private void animateRegularBombThrow() {
	if (this.getAnimationHandler().getFrameTime() != 50) {
		this.getAnimationHandler().setFrameTime(50);
	}
	
	if (this.getSprite().equals(BACK)) {
		
		if (this.getAnimationHandler().getFrame() >= 4 && this.getAnimationHandler().getFrame() < 6 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 4) {
			beingThrown.setX(this.getX());
			beingThrown.setY(this.getY() + 10);
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() + 3);
			beingThrown.setY(this.getY() + 9);
		}
		
		if (this.getAnimationHandler().getFrame() >= 6 && beingThrown != null) {
			beingThrown.setX(this.getX() + 6);
			beingThrown.setY(this.getY() + 2);
			
			beingThrown.throwObj(Math.PI/2,10);
			beingThrown = null;
		}
		
	}
	
	if (this.getSprite().equals(FORWARD)) {
		
		if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 8 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() -11);
			beingThrown.setY(this.getY() + 1);
		
		}
		
		if (this.getAnimationHandler().getFrame() == 6) {
			beingThrown.setX(this.getX() -13);
			beingThrown.setY(this.getY() + 5);
		}
		
		if (this.getAnimationHandler().getFrame() == 7) {
			beingThrown.setX(this.getX() -16);
			beingThrown.setY(this.getY() + 10);
		}
		
		if (this.getAnimationHandler().getFrame() >= 8 && beingThrown != null) {
			beingThrown.setX(this.getX() - 16);
			beingThrown.setY(this.getY() + 20);
			
			beingThrown.throwObj(3*Math.PI/2,10);
			beingThrown = null;
		}
		
		
	}
	
	if (this.getSprite().equals(SIDE) && !this.getAnimationHandler().flipHorizontal()) {
		
		if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 7 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() -1);
			beingThrown.setY(this.getY() + 9);
		}
		
		if (this.getAnimationHandler().getFrame() == 6) {
			beingThrown.setX(this.getX() + 9);
			beingThrown.setY(this.getY() + 3);
		}
		
		if (this.getAnimationHandler().getFrame() >= 7 && beingThrown != null) {
			beingThrown.setX(this.getX() + 21);
			beingThrown.setY(this.getY() + 7);
			
			beingThrown.throwObj(0,10);
			beingThrown = null;
		}
		
	}
	
	if (this.getSprite().equals(SIDE) && this.getAnimationHandler().flipHorizontal()) {
		
		if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 7 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() + 9);
			beingThrown.setY(this.getY() + 9);
		}
		
		if (this.getAnimationHandler().getFrame() == 6) {
			beingThrown.setX(this.getX() - 3);
			beingThrown.setY(this.getY() + 3);
		}
		
		if (this.getAnimationHandler().getFrame() >= 7 && beingThrown != null) {
			beingThrown.setX(this.getX() -16);
			beingThrown.setY(this.getY() + 7);
			
			beingThrown.throwObj(Math.PI,10);
			beingThrown = null;
		}
		
	}
	
	if (this.getSprite().equals(DIAGONAL_FORWARD) && !this.getAnimationHandler().flipHorizontal()) {
		
		if (this.getAnimationHandler().getFrame() >= 4 && this.getAnimationHandler().getFrame() < 6 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 4) {
			beingThrown.setX(this.getX() - 5);
			beingThrown.setY(this.getY() - 2);
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() - 7);
			beingThrown.setY(this.getY() + 4);
		}
		
		if (this.getAnimationHandler().getFrame() >= 6 && beingThrown != null) {
			beingThrown.setX(this.getX() -12);
			beingThrown.setY(this.getY() + 18);
			
			beingThrown.throwObj(5*Math.PI/4,10);
			beingThrown = null;
		}	
	}
	
	if (this.getSprite().equals(DIAGONAL_FORWARD) && this.getAnimationHandler().flipHorizontal()) {
		
		if (this.getAnimationHandler().getFrame() >= 4 && this.getAnimationHandler().getFrame() < 6 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 4) {
			beingThrown.setX(this.getX() + 20);
			beingThrown.setY(this.getY() - 2);
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() + 22);
			beingThrown.setY(this.getY() + 4);
		}
		
		if (this.getAnimationHandler().getFrame() >= 6 && beingThrown != null) {
			beingThrown.setX(this.getX() +28);
			beingThrown.setY(this.getY() + 18);
			
			beingThrown.throwObj(7*Math.PI/4,10);
			beingThrown = null;
		}	
	}
	
	if (this.getSprite().equals(DIAGONAL_BACK) && !this.getAnimationHandler().flipHorizontal()) {
		
		if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 8 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() + 6);
			beingThrown.setY(this.getY() + 12);
		}
		
		if (this.getAnimationHandler().getFrame() == 6) {
			beingThrown.setX(this.getX() + 11);
			beingThrown.setY(this.getY() + 10);
		}
		
		if (this.getAnimationHandler().getFrame() == 7) {
			beingThrown.setX(this.getX() +16);
			beingThrown.setY(this.getY() + 4);
			
		}
		if (this.getAnimationHandler().getFrame() >= 8 && beingThrown != null) {
			beingThrown.setX(this.getX() +29);
			beingThrown.setY(this.getY() + 6);
			
			beingThrown.throwObj(Math.PI/4,10);
			beingThrown = null;
		}
	}
	
	if (this.getSprite().equals(DIAGONAL_BACK) && this.getAnimationHandler().flipHorizontal()) {
		
		if (this.getAnimationHandler().getFrame() >= 5 && this.getAnimationHandler().getFrame() < 8 && beingThrown == null) {
			beingThrown = new Bomb (this);
			beingThrown.declare(this.getX(),this.getY());
		}
		
		if (this.getAnimationHandler().getFrame() == 5) {
			beingThrown.setX(this.getX() + 11);
			beingThrown.setY(this.getY() + 12);
		}
		
		if (this.getAnimationHandler().getFrame() == 6) {
			beingThrown.setX(this.getX() + 5);
			beingThrown.setY(this.getY() + 10);
		}
		
		if (this.getAnimationHandler().getFrame() == 7) {
			beingThrown.setX(this.getX() +5);
			beingThrown.setY(this.getY() + 4);
			
		}
		if (this.getAnimationHandler().getFrame() >= 8 && beingThrown != null) {
			beingThrown.setX(this.getX() -13);
			beingThrown.setY(this.getY() + 6);
			
			beingThrown.throwObj(3 *Math.PI/4,10);
			beingThrown = null;
		}
	}
	
	if (beingThrown != null && beingThrown.declared()) {
		if (objectives.hasCactusBombs()) {
			beingThrown.setBombSprites("cactus");
			beingThrown.setFragsType("cactus bomb fragment");
			beingThrown.doCactusEffect();
		}
	}
	
	
	
	if (this.getAnimationHandler().getFrame() == this.getSprite().getFrameCount() - 1) {
		throwingBomb = false;
	}
}
		
private void animateMasterBombThrow() {
	boolean bombJustMade = false;
	
	if (!pull.declared()) {
		pull.declare();
		
		Bomb toPull = new Bomb(this, 4);
		
		toPull.makeMasterBomb();
		
		bombJustMade = true;
		
		toPull.setRenderPriority(3);
		toPull.declare();
		
		toPull.dontFlash();
		
		toPull.turnOff();
		
		toPull.setFragsType("master bomb fragment");
		
		pull.setBomb(toPull);
	}
	
	if (this.getSprite().equals(BACK_MASTER_BOMBS)) {
		
		
		if (bombJustMade) {
			Random rand = new Random ();
			pull.getPulling().setX(Room.getViewX() + rand.nextInt(960));
			pull.getPulling().setY(Room.getViewY() + 540 + 32);
		}
		
		pull.setX(this.getX() + 6);
		pull.setY(this.getY() + 2);
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(Math.PI/2,40);
			throwingBomb = false;
		}
	}
	
	if (this.getSprite().equals(FORWARD_MASTER_BOMBS)) {
		if (bombJustMade) {
			Random rand = new Random ();
			pull.getPulling().setX(Room.getViewX() + rand.nextInt(960));
			pull.getPulling().setY(Room.getViewY() - 32);
		}
		
		pull.setX(this.getX() + 25);
		pull.setY(this.getY() - 10);
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(3*Math.PI/2,40);
			throwingBomb = false;
		}
	}
	
	if (this.getSprite().equals(LEFT_MASTER_BOMBS)) {
		if (bombJustMade) {
			Random rand = new Random ();
			pull.getPulling().setX(Room.getViewX() + 960 + 32);
			pull.getPulling().setY(Room.getViewY() 
					+ rand.nextInt(540));
		}
		
		pull.setX(this.getX() + 1);
		pull.setY(this.getY());	
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(Math.PI,40);
			throwingBomb = false;
		}
	}
	
	if (this.getSprite().equals(RIGHT_MASTER_BOMBS)) {
		if (bombJustMade) {
			Random rand = new Random ();
			pull.getPulling().setX(Room.getViewX() - 32);
			pull.getPulling().setY(Room.getViewY()  + rand.nextInt(540));
		}
		
		pull.setX(this.getX() + 8);
		pull.setY(this.getY() - 5);
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(0,40);
			throwingBomb = false;
		}
	}
	
	if (this.getSprite().equals(DIAGONAL_FORWARD_MASTER_BOMBS) && !this.getAnimationHandler().flipHorizontal()) {
		if (bombJustMade) {
			Random rand = new Random ();
			
			if (rand.nextBoolean()) {
				pull.getPulling().setX(Room.getViewX() + 960 + 32);
				pull.getPulling().setY(Room.getViewY() + rand.nextInt(270));
			} else {
				pull.getPulling().setX(Room.getViewX() + 480 + rand.nextInt(480));
				pull.getPulling().setY(Room.getViewY() -32);
			}
		}
		
		pull.setX(this.getX() -12);
		pull.setY(this.getY() + 18);
		
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(5*Math.PI/4,40);
			throwingBomb = false;
		}
	}
	
	if (this.getSprite().equals(DIAGONAL_FORWARD_MASTER_BOMBS) && this.getAnimationHandler().flipHorizontal()) {
		if (bombJustMade) {
			Random rand = new Random ();
			
			if (rand.nextBoolean()) {
				pull.getPulling().setX(Room.getViewX() - 32);
				pull.getPulling().setY(Room.getViewY() + rand.nextInt(270));
			} else {
				pull.getPulling().setX(Room.getViewX() + rand.nextInt(480));
				pull.getPulling().setY(Room.getViewY() -32);
			}
		}
		
		pull.setX(this.getX() +28);
		pull.setY(this.getY() + 18);
		
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(7*Math.PI/4,40);
			throwingBomb = false;
		}
	}
	
	if (this.getSprite().equals(DIAGONAL_BACK_MASTER_BOMBS) && !this.getAnimationHandler().flipHorizontal()) {
		
		if (bombJustMade) {
			Random rand = new Random ();
			
			if (rand.nextBoolean()) {
				pull.getPulling().setX(Room.getViewX() - 32);
				pull.getPulling().setY(Room.getViewY() + 270 + rand.nextInt(270));
			} else {
				pull.getPulling().setX(Room.getViewX() + rand.nextInt(480));
				pull.getPulling().setY(Room.getViewY() + 540 + 32);
			}
		}
		
		pull.setX(this.getX() +29);
		pull.setY(this.getY() + 6);
		
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(Math.PI/4,40);
			throwingBomb = false;
		}
	}
	
	if (this.getSprite().equals(DIAGONAL_BACK_MASTER_BOMBS) && this.getAnimationHandler().flipHorizontal()) {
		if (bombJustMade) {
			Random rand = new Random ();
			
			if (rand.nextBoolean()) {
				pull.getPulling().setX(Room.getViewX() + 960 + 32);
				pull.getPulling().setY(Room.getViewY() + 270 + rand.nextInt(270));
			} else {
				pull.getPulling().setX(Room.getViewX() + 480 + rand.nextInt(480));
				pull.getPulling().setY(Room.getViewY() + 540 + 32);
			}
		}
		
		pull.setX(this.getX() -13);
		pull.setY(this.getY() + 6);
		
		
		if (pull.isDone) {
			Bomb b = pull.getPulling();
			pull.forget();
			
			b.turnOn();
			
			pull = new BombPuller();
			
			b.throwObj(3 *Math.PI/4,40);
			throwingBomb = false;
		}
	}

}


private void handleDash () {
	

		
	myLegs.getAnimationHandler().setFrameTime(60);
	switch (dashDir) {
		case 1:
			if (!this.goX(this.getX() - dashSpeed)) {
				dashDir = 0;
			}
			break;
		case 2:
			if (!this.goX(this.getX() + dashSpeed)) {
				dashDir = 0;
			}
			break;
		case 3:
			if (!this.goY(this.getY() - dashSpeed)) {
				dashDir = 0;
			}
			break;
		case 4:
			if (!this.goY(this.getY() + dashSpeed)) {
				dashDir = 0;
			}
			break;
		case 5:
			if (!this.goX(this.getX() - dashSpeed/2)) {
				dashDir = 0;
			}
			if (!this.goY(this.getY() - dashSpeed/2)) {
				dashDir = 0;
			}
			break;
		case 6:
			if (!this.goX(this.getX() + dashSpeed/2)) {
				dashDir = 0;
			}
			if (!this.goY(this.getY() - dashSpeed/2)) {
				dashDir = 0;
			}
			break;
		case 7:
			if (!this.goX(this.getX() - dashSpeed/2)) {
				dashDir = 0;
			}
			if (!this.goY(this.getY() + dashSpeed/2)) {
				dashDir = 0;
			}
			break;
		case 8:
			if (!this.goX(this.getX() + dashSpeed/2)) {
				dashDir = 0;
			}
			if (!this.goY(this.getY() + dashSpeed/2)) {
				dashDir = 0;
			}
			break;
			}
	
	dashSpeed = dashSpeed - 1.2;
	
	if (dashSpeed <= 0) {
		dashDir = 0;
	}
	
}
	
	public void startDashLeft() {
		dashDir = 1;
		throwingPegasusBomb = true;
		this.setSprite(SIDE_PEGASUS_TOSS);
//		this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFlipHorizontal(true);
		this.getAnimationHandler().setFrameTime(100);
	}

	public void startDashRight() {
		dashDir = 2;
		throwingPegasusBomb = true;
		this.setSprite(SIDE_PEGASUS_TOSS);
//		this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFlipHorizontal(false);
		this.getAnimationHandler().setFrameTime(100);
	}
	
	public void startDashUp() {
		dashDir = 3;
		throwingPegasusBomb = true;
		this.setSprite(BACK_PEGASUS_TOSS);
	//	this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFrameTime(100);
	}
	
	public void startDashDown() {
		dashDir = 4;
		throwingPegasusBomb = true;
		this.setSprite(FORWARD_PEGASUS_TOSS);
//		this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFrameTime(100);

	}
	
	public void startDashUpLeft() {
		dashDir = 5;
		throwingPegasusBomb = true;
		this.setSprite(DIAGONAL_BACK_PEGASUS_TOSS);
	//	this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFlipHorizontal(true);
		this.getAnimationHandler().setFrameTime(100);
	}
	
	public void startDashUpRight() {
		dashDir = 6;
		throwingPegasusBomb = true;
		this.setSprite(DIAGONAL_BACK_PEGASUS_TOSS);
	//	this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFlipHorizontal(false);
		this.getAnimationHandler().setFrameTime(100);
	}
	
	public void startDashDownLeft() {
		dashDir = 7;
		throwingPegasusBomb = true;
		this.setSprite(DIAGONAL_FORWARD_PEGASUS_TOSS);
//		this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFlipHorizontal(false);
		this.getAnimationHandler().setFrameTime(100);
	}

	public void startDashDownRight() {
		dashDir = 8;
		throwingPegasusBomb = true;
		this.setSprite(DIAGONAL_FORWARD_PEGASUS_TOSS);
	//	this.getAnimationHandler().setRepeat(false);
		this.getAnimationHandler().setFlipHorizontal(true);
		this.getAnimationHandler().setFrameTime(100);
	}
	
	
	
	
	@Override
	public void gettingSploded() {
		((Hud)(ObjectHandler.getObjectsByName("Hud").get(0))).breakHeart();
	}
	
//	public void setIdleLegs() {
//		if (myLegs.getSprite().equals(DIAGONAL_LEGS_WALK)) {
//			myLegs.setSprite(DIAGONAL_LEGS_IDLE);
//		}
//		if (myLegs.getSprite().equals(SIDE_LEGS_WALK)) {
//			myLegs.setSprite(SIDE_LEGS_IDLE);
//		}
//		if (myLegs.getSprite().equals(FORWARD_LEGS_WALK)) {
//			myLegs.setSprite(FORWARD_LEGS_IDLE);
//		}
//		
//	}
	
	public void die () {
		dead = true;
		shad.forget();
	}

	public void freeze() {
		frozen = true;
	}
	
	public void unfreeze() {
		frozen = false;
		throwStuned = true;
	}
	
	
	public class Legs extends GameObject {
		
		public Legs() {
			
		}
		
	}
	
	public class BombPuller extends GameObject {
		
		Bomb toPull;
		
		boolean isDone = false;
		
		public BombPuller () {
		}
		
		public void setBomb(Bomb newPull) {
			toPull = newPull;
			toPull.throwObjTowards((int)this.getX(),(int)this.getY(), 35);
		}
		
		@Override
		public void setX (double val) {
			super.setX(val);
			toPull.throwObjTowards((int)this.getX(),(int)this.getY(), 35);
		}
		
		@Override
		public void setY (double val) {
			super.setY(val);
			toPull.throwObjTowards((int)this.getX(),(int)this.getY(), 35);
			
			if (this.getDist((int)toPull.getCenterX(), (int)toPull.getCenterY()) < 36) {
				toPull.setCenterX(this.getX());
				toPull.setCenterY(this.getY());
				isDone = true;
			}
		}
	
		public boolean isDone () {
			if (!isDone) {
				return false;
			} else {
				isDone = false;
				return true;
			}
		}
		
		public Bomb getPulling () {
			return toPull;
		}
		
	}
	
	public class Shadow extends GameObject {
		
		public static final int RIGHT_SHAD_MAX_Y_DIST = 12;
		public static final int LEFT_SHAD_MAX_Y_DIST = 12;
		public static final int FORWARD_SHAD_MAX_Y_DIST = 16;
		public static final int BACK_SHAD_MAX_Y_DIST = 16;
		
		public static final int DOWNRIGHT_SHAD_MAX_Y_DIST = 10;
		public static final int DOWNRIGHT_SHAD_MAX_X_DIST = 8;
		
		public static final int DOWNLEFT_SHAD_MAX_Y_DIST = 10;
		public static final int DOWNLEFT_SHAD_MAX_X_DIST = 8;
		
		public static final int UPRIGHT_SHAD_MAX_Y_DIST = -8;
		public static final int UPRIGHT_SHAD_MAX_X_DIST = 10;
		
		public static final int UPLEFT_SHAD_MAX_Y_DIST = -8;
		public static final int UPLEFT_SHAD_MAX_X_DIST = 10;
		
		public static final double FALL_SPEED = -.1;
		
		private double curDist = 1;
		
		private double curSpeed = FALL_SPEED;
		
		private double prevDisplaceX = -1;
		private double prevDisplaceY = -1;
		
		
		public Shadow () {
			this.useSpriteHitbox();
			this.disableCollisions();
			this.setSprite(SIDE_MASTER_BOMBS_SHADOW);
		}
		
		public void alignShadow(BombMaster mast) {
			

			if (prevDisplaceX == -1) {
				this.setX(mast.getX());
				this.setY(mast.getY());
			} else {
				this.setX(mast.getX() - prevDisplaceX);
				this.setY(mast.getY() - prevDisplaceY);
				
			}
			
			double prevX = mast.getX() - prevDisplaceX;
			double prevY = mast.getY() - prevDisplaceY;
			
			if (mast.getSprite().equals(RIGHT_MASTER_BOMBS)) {
				mast.setY(this.getY() + this.getSprite().getHeight() - mast.getSprite().getHeight() - (curDist * RIGHT_SHAD_MAX_Y_DIST));
				mast.setX(this.getX() - 14);
				
			}
			
			if (mast.getSprite().equals(LEFT_MASTER_BOMBS)) {
				mast.setY(this.getY() + this.getSprite().getHeight()- mast.getSprite().getHeight() - (curDist * LEFT_SHAD_MAX_Y_DIST));
				mast.setX(this.getX() - 8);
			}
			
			if (mast.getSprite().equals(FORWARD_MASTER_BOMBS)) {
				mast.setY(this.getY() + this.getSprite().getHeight()- mast.getSprite().getHeight() - (curDist * FORWARD_SHAD_MAX_Y_DIST));
				mast.setX(this.getX() - 12);
			}
			
			if (mast.getSprite().equals(BACK_MASTER_BOMBS)) {
				mast.setY(this.getY() + this.getSprite().getHeight()- mast.getSprite().getHeight() - (curDist * BACK_SHAD_MAX_Y_DIST));
				mast.setX(this.getX() - 13);
			}
			if (mast.getSprite().equals(DIAGONAL_FORWARD_MASTER_BOMBS) && mast.getAnimationHandler().flipHorizontal()) {
				mast.setY(this.getY() + this.getSprite().getHeight()/2 - mast.getSprite().getHeight() - (curDist * DOWNRIGHT_SHAD_MAX_Y_DIST));
				mast.setX(this.getX() - ((curDist * DOWNRIGHT_SHAD_MAX_X_DIST) +30));
			}
			if (mast.getSprite().equals(DIAGONAL_FORWARD_MASTER_BOMBS) && !mast.getAnimationHandler().flipHorizontal()) {
				mast.setY(this.getY() + this.getSprite().getHeight()/2 - mast.getSprite().getHeight() - ((curDist * DOWNLEFT_SHAD_MAX_Y_DIST) ));
				mast.setX(this.getX() + ((curDist * DOWNLEFT_SHAD_MAX_X_DIST) - 2));
			}
			
			if (mast.getSprite().equals(DIAGONAL_BACK_MASTER_BOMBS) && mast.getAnimationHandler().flipHorizontal()) {
				mast.setY(this.getY() - mast.getSprite().getHeight() + ((curDist * UPLEFT_SHAD_MAX_Y_DIST) + 12));
				mast.setX(this.getX() - ((curDist * UPLEFT_SHAD_MAX_X_DIST) + 22));
			}
			if (mast.getSprite().equals(DIAGONAL_BACK_MASTER_BOMBS) && !mast.getAnimationHandler().flipHorizontal()) {
				mast.setY(this.getY() - mast.getSprite().getHeight() + ((curDist * UPRIGHT_SHAD_MAX_Y_DIST) + 12));
				mast.setX(this.getX() + ((curDist * UPRIGHT_SHAD_MAX_X_DIST) + 5));
			}
			
			prevDisplaceX = mast.getX() - prevX;
			prevDisplaceY = mast.getY() - prevY;
		}

		
		public void updateShadowDistance(BombMaster mast) {
			curDist = curDist + curSpeed;
			
			Random rand = new Random ();
			
			if ((rand.nextInt(100) * curDist) < 5 && curSpeed < 0) {
				knockBackUp(mast);
				return;
			}
			
			if (curDist <= 0 && curSpeed < 0) {
				knockBackUp(mast);
				return;
			}
			
			if (curDist >= 1) {
				curSpeed = FALL_SPEED;
			}
		}
		
		public void knockBackUp(BombMaster mast) {
			Random rand = new Random ();
			
			curSpeed = 0.3 * (1 - curDist);
			Explosion e = new Explosion ((1 - curDist) + .3);
			
			e.makeRainbow();
			
			e.setRenderPriority(1);
			
			e.makeAsteticOnly();
			e.declare();
			e.setCenterX(this.getX() + rand.nextInt(this.getSprite().getWidth()));
			e.setCenterY(this.getY() + mast.getSprite().getHeight() + prevDisplaceY);
		}
		
	}
	
	public class SwingingClub extends GameObject {
		
		double startRotation;
		double endRoataion;
		
		ArrayList <Bomb> refectedBombs = new ArrayList <Bomb> ();
		
		public SwingingClub () {
			this.setSprite(new Sprite ("resources/sprites/golf club.png"));
			
			//Im pretty sure I remember us saying something about pixel collisions not being compadable with rotations
			//this.enablePixelCollisions();
			//this.adjustHitboxBorders();
			this.setGameLogicPriority(-10);
		}
		
		public void frameEvent () {
			
			startRotation = startRotation - .3;
			this.setDrawRotation(startRotation);
			if (startRotation < endRoataion) {
				refectedBombs = new ArrayList <Bomb> ();
				this.forget();
			}
			
			if (this.isColliding("Bomb")) {
				ArrayList <GameObject> bombs = this.getCollisionInfo().getCollidingObjects();
				
				ArrayList <GameObject> us = new ArrayList <GameObject> ();
				us.add(this);
				
				us.addAll(bombs);
				
				for (int i =0; i < refectedBombs.size(); i++) {
					Bomb cur = (Bomb) refectedBombs.get(i);
					cur.getOwners().addAll(bombs);
				}
				
				us.addAll(refectedBombs);
				
				for (int i = 0; i < bombs.size(); i++) {
					Bomb cur = (Bomb) bombs.get(i);
					
					if (!cur.getOwners().contains(this)) {
						cur.reset();
						
						refectedBombs.add(cur);
						
						us.add(GameCode.getBombMaster());
						//all of your bombs are belong to us
						cur.setOwners(us);
						
						cur.setThrowDirection(cur.getThrowDirection() + Math.PI);
					}
				}
				
				
			}
		}
		
		//dont want it getting drawn normally sience we need to draw it in a special way to get the layering right
		@Override
		public void draw() {
			
		}
		
		public void realDraw () {
			super.draw();
		}
		
		public void swingUpRight ()
		{
			startRotation = -1.0;
			endRoataion = -3.4;
			this.setHitboxAttributes(-8,-30,34, 30);
			this.declare();
		}
		
		public void swingUp ()
		{
			this.setHitboxAttributes(-16,-30,40, 30);
			startRotation = -1.9;
			endRoataion = -4.1;
			this.declare();
		}
		
		public void swingUpLeft ()
		{
			this.setHitboxAttributes(-28,-26,32, 39);
			startRotation = -2.9;
			endRoataion = -5.2;
			this.declare();
		}
		
		public void swingLeft ()
		{
			this.setHitboxAttributes(-28,-17,32, 37);
			startRotation = -3.6;
			endRoataion = -5.4;
			this.declare();
		}
		
		public void swingDownLeft ()
		{
			this.setHitboxAttributes(-28, 0,35, 27);
			startRotation = 1.8;
			endRoataion = -.5;
			this.declare();
		}
		
		public void swingDown ()
		{
			this.setHitboxAttributes(-16, 0,43, 27);
			startRotation = 1.2;
			endRoataion = -1.1;
			this.declare();
		}
		
		public void swingDownRight ()
		{
			this.setHitboxAttributes(0, -5,28, 32);
			startRotation = .6;
			endRoataion = -2.0;
			this.declare();
		}
		
		public void swingRight ()
		{
			this.setHitboxAttributes(0, -25,28, 35);
			startRotation = -.9;
			endRoataion = -2.6;
			this.declare();
		}
	}
}
