package engine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import gameObjects.Bomb;
import gameObjects.BombMaster;
import map.Room;



public class GameCode {
	
	static int veiwX;
	static int veiwY;
	

	public static void testBitch () {
		
		
	}
	
	public static void beforeGameLogic () {
		
	}

	public static void afterGameLogic () {
		
	}

	public static void init () {
		BombMaster mast = new BombMaster ();
		mast.declare();
	}
		
	
	
	public static void gameLoopFunc () {
		
		ObjectHandler.callAll();
		
		
	}
	
	public static void renderFunc () {
		ObjectHandler.renderAll();
		Room.render();
	}
	
	public static void beforeRender() {
		
	}
	
	public static void afterRender()
	{
		
	}
		
	
	public static int getViewX() {
		return veiwX;
	}



	public static void setViewX(int newVeiwX) {
		veiwX = newVeiwX;
	}



	public static int getViewY() {
		return veiwY;
	}



	public static void setViewY(int newVeiwY) {
		veiwY = newVeiwY;
	}



	
}
