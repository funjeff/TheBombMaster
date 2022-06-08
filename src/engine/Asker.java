package engine;

import java.util.ArrayList;

public class Asker {
	Actor ask;
	ArrayList <Integer> heldKeys;
	
	public Asker (Actor obj) {
		ask = obj;
		heldKeys = new ArrayList <Integer> ();
	}
	 
	public boolean isAsker(Actor isHim) {
		return ask.equals(isHim);
	}
	
	public ArrayList <Integer> getKeys (){
		return heldKeys;
	}
	  
  }