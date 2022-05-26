 package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import engine.GameAPI;
import engine.GameObject;
import engine.GameObject.HitboxInfo;
import engine.ObjectHandler;
import engine.RenderLoop;
import engine.Sprite;
import engine.SpriteParser;



public class Room {
	
	private static ArrayList <TileData> dataList;  // sorted by numeric ID
	private static HashMap <String,TileData> nameList; //indexed by tile name
	private static ArrayList <BufferedImage> tileIcons; //yeah? (sorted by numeric ID)

	private static HashMap <Long, ArrayList<GameObject>> mapObjects = new HashMap <Long, ArrayList<GameObject>>(); //objects that should be considered collisions with the map
	
	private static ArrayList<GameObject> mapObjectsUsed = new ArrayList<GameObject> ();
	
	private static int [][][] tileData; //all tiles listed by numeric ID
	private static ArrayList <Background> backgrounds;// all backgrounds sorted by layers
	
	private static ArrayList <TileEntitiy> tileEntitiys; //objects that contain code related to certain tiles
	private static HashMap <Long, TileEntitiy> positionToEntitiys;
	
	private static MapChungus [][] mapChungi; 
	
	private static int scrollX;
	private static int scrollY;
	
	private static int updateX = -1;
	private static int updateY = -1;
	
	private static byte [] inData;
	private static int readPos;
	
	private static int mapWidth;
	private static int mapHeight;
	private static int numLayers;
	
	public static int collisionLayer = 0;
	
	private static int chungusWidth = 20;
	private static int chungusHeight = 15;
	
	private static double gravity = .65625;
	
	private static boolean isLoaded = false;
	
	private static String roomName = "unloaded";
	
	private static int startMs;
	private static int bgSpeed;
	
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	public static final int SPECIAL_TILE_ID = -1;
	public static final int ZREO = 0;
	
	private Room () {
		//Non-electric boogaloo
	}
	
	private static boolean isBetween (double val, double bound1, double bound2) {
		if ((bound1 - val <= 0 && bound2 - val >= 0) || (bound1 - val >= 0 && bound2 - val <= 0)) {
			return true;
		}
		return false;
	}
	
	private static boolean checkTileCollision (int tileX, int tileY, boolean flip) {
		Graphics2D g = (Graphics2D)RenderLoop.wind.getBufferGraphics ();
		g.setColor(new Color(0x0000FF));
		if (flip) {
			//g.draw3DRect(tileY*16, tileX*16, 16, 16, true);
			return dataList.get(tileData[collisionLayer][tileX][tileY]).isSolid ();
		} else {
			//g.draw3DRect(tileX*16, tileY*16, 16, 16, true);
			return dataList.get(tileData[collisionLayer][tileY][tileX]).isSolid ();
		}
	}
	private static TileData checkTileData (int tileX, int tileY, boolean flip) {
		if (flip) {
			return dataList.get(tileData[collisionLayer][tileX][tileY]);
		} else {
			return dataList.get(tileData[collisionLayer][tileY][tileX]);
		}
	}
	/**
	 * does a collision using vectors
	 * @param x1 the x pos of the start of the line
	 * @param y1 the y pos of the start of the line
	 * @param x2 the x pos of the end of the line
	 * @param y2 the y pos of the end of the line
	 * @return true if the vector colides with a solid tile at any point
	 */
	public static boolean isColliding (double x1, double y1, double x2, double y2) {
	if (getCollisionInfo(x1,y1,x2,y2) == null) {
		return false;
		} else {
		return true;
		}
	}
	
	public static long toPackedLong (int x, int y) {
		long a = x;
		long b = y;
		a |= b << 32;
		return a;
	}
	
	public static VectorCollisionInfo getCollisionInfo (double x1, double y1, double x2, double y2) {
		
		//DIVIDE
		//TODO evaluate the usefulness of this method when TILE_WIDTH != TILE_HEIGHT
		y1 /= TILE_WIDTH;
		y2 /= TILE_WIDTH;
		x1 /= TILE_WIDTH;
		x2 /= TILE_WIDTH;
		
		//The coordinates of the tile that we are currently checking for collision
		int tileX = (int) x1;
		int tileY = (int) y1;
		
		//Easy peasy
		if (checkTileCollision (tileX, tileY, false)) {
			return new VectorCollisionInfo(checkTileData(tileX,tileY,false),x1*TILE_WIDTH,y1*TILE_HEIGHT,tileX,tileY);
		}
		
		//Flipped mode
		boolean flipped = false;
		if (Math.abs(y2 - y1) > Math.abs(x2 - x1)) {
			flipped = true;
			double temp = y2;
			y2 = x2;
			x2 = temp;
			temp = y1;
			y1 = x1;
			x1 = temp;
			int tempp = tileY;
			tileY = tileX;
			tileX = tempp;
		}
		int dir = 1;
		if (x2< x1 ) {
			dir = -1;
		}
		//Set the slope
		double m = (y2 - y1) / (x2 - x1); //THIS IS THE SLOPE JEFFREY
		double b = y1 - m * x1; 
		//Snap to next tile
		//stepX and stepY are the current point on the line that we are checking
		double stepX = x1;
		double stepY = y1;
		int steps = 0;
		try {
			while (true) {
				if ((m >= 0 && dir == 1) || (m<= 0 && dir == -1)) {
					if (steps != 0 && !(isBetween (stepY, y1, y2) && isBetween (stepX, x1, x2))) {
						return null;
					}
					if (stepX % 1 == 0) {
						stepX += dir;
					} else {
						if (dir == 1) {
						stepX = Math.ceil (stepX);
						} else {
						stepX = Math.floor (stepX);
						}
					}
					int tempY = (int)stepY;
					stepY = m * stepX + b;
					if ((int)stepY > tempY) {
						tileY++;
						if (checkTileCollision (tileX, tileY, flipped)) {
							stepY = tileY;
							stepX = (stepY - b) / m;
							if (isBetween (stepY, y1, y2) && isBetween (stepX, x1, x2)) {
								//TODO will crash with tileEnitiys
								double usedXCol = stepX*TILE_WIDTH;
								double usedYCol = stepY*TILE_WIDTH;
								int usedTileX = tileX;
								int usedTileY = tileY;
								if (flipped) {
									double temp = usedXCol;
									int tempp = tileX;
									usedXCol = usedYCol;
									usedYCol = temp;
									usedTileX = usedTileY;
									usedTileY = tempp;
								}
								return new VectorCollisionInfo(checkTileData(tileX,tileY,false),usedXCol,usedYCol,usedTileX,usedTileY);
							}
							return null;
						}
					}
					tileX= tileX + dir;
					if (checkTileCollision (tileX, tileY, flipped)) {
						stepX = tileX;
						if (dir == -1) {
							stepX++;
						}
						stepY = m * stepX + b;
						if (isBetween (stepY, y1, y2) && isBetween (stepX, x1, x2)) {
							double usedXCol = stepX*TILE_WIDTH;
							double usedYCol = stepY*TILE_WIDTH;
							int usedTileX = tileX;
							int usedTileY = tileY;
							if (flipped) {
								double temp = usedXCol;
								int tempp = tileX;
								usedXCol = usedYCol;
								usedYCol = temp;
								usedTileX = usedTileY;
								usedTileY = tempp;
							}
							return new VectorCollisionInfo(checkTileData(tileX,tileY,false),usedXCol,usedYCol,usedTileX,usedTileY);
						}
						return null;
					}
				} else {
					if (steps != 0 && !(isBetween (stepY, y1, y2) && isBetween (stepX, x1, x2))) {
						return null;
					}
					if (stepX % 1 == 0) {
						stepX += dir;
					} else {
						if (dir == 1) {
							stepX = Math.ceil (stepX);
							} else {
							stepX = Math.floor (stepX);
							}
					}
					int tempY = (int)stepY;
					stepY = m * stepX + b;
					if ((int)stepY < tempY) {
						tileY--;
						if (checkTileCollision (tileX, tileY, flipped)) {
							stepY = tileY + 1;
							stepX = ((stepY) - b) / m;
							if (isBetween (stepY, y1, y2) && isBetween (stepX, x1, x2)) {
								double usedXCol = stepX*TILE_WIDTH;
								double usedYCol = stepY*TILE_WIDTH;
								int usedTileX = tileX;
								int usedTileY = tileY;
								if (flipped) {
									double temp = usedXCol;
									int tempp = tileX;
									usedXCol = usedYCol;
									usedYCol = temp;
									usedTileX = usedTileY;
									usedTileY = tempp;
								}
								return new VectorCollisionInfo(checkTileData(tileX,tileY,false),usedXCol,usedYCol,usedTileX,usedTileY);
							}
							return null;
						}
					}
					tileX = tileX + dir;
					if (checkTileCollision (tileX, tileY, flipped)) {
						stepX = tileX;
						if (dir == -1) {
							stepX++;
						}
						stepY = m * stepX + b;
						if (isBetween (stepY, y1, y2) && isBetween (stepX, x1, x2)) {
							double usedXCol = stepX*TILE_WIDTH;
							double usedYCol = stepY*TILE_WIDTH;
							int usedTileX = tileX;
							int usedTileY = tileY;
							if (flipped) {
								double temp = usedXCol;
								int tempp = tileX;
								usedXCol = usedYCol;
								usedYCol = temp;
								usedTileX = usedTileY;
								usedTileY = tempp;
							}
							return new VectorCollisionInfo(checkTileData(tileX,tileY,false),usedXCol,usedYCol,usedTileX,usedTileY);
						}
						return null;
					}
				}
				steps++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	public static ArrayList<GameObject> getMapObjectsUsed() {
		return mapObjectsUsed;
	}

	public static void setMapObjectsUsed(ArrayList<GameObject> mapObjectsUsed) {
		Room.mapObjectsUsed = mapObjectsUsed;
	}

	/**
	 * checks to see if the given object is colliding with a solid tile
	 * @param obj the object to check
	 * @return wheather or not its touching a solid tile
	 */
	public static boolean isColliding (GameObject obj) {
		mapObjectsUsed = new ArrayList<GameObject> ();
		boolean foundCollision = false;
		for (int i = 0; i < obj.hitboxes().length; i++) {
			Rectangle hitbox = obj.hitboxes()[i];
			int startX = Math.max(hitbox.x/TILE_WIDTH,0);
			int startY = Math.max(hitbox.y/TILE_HEIGHT,0);
			int endX = Math.min((hitbox.x + hitbox.width)/TILE_WIDTH,mapWidth-1);
			int endY = Math.min((hitbox.y + hitbox.height)/TILE_HEIGHT,mapHeight-1);
			for (int wx = startX; wx <= endX; wx++ ){
				for (int wy = startY; wy <= endY; wy++) {
					int index = tileData[collisionLayer][wy][wx];
					if (mapObjects.get(toPackedLong(wx,wy)) == null || index == SPECIAL_TILE_ID) {
						if (index == SPECIAL_TILE_ID) {
							long pos = toPackedLong (wx,wy);
							positionToEntitiys.get(pos).onCollisionIntermidete(obj);
							if (!foundCollision) {
							foundCollision = positionToEntitiys.get(pos).doesColide(obj);
							}
						} else if (dataList.get(index).isSolid()) {
							foundCollision = true;
						}
				} else {
					try {
					for (int b = 0; b < mapObjects.get(toPackedLong(wx,wy)).size(); b++ ) {
						
						
						if (mapObjects.get(toPackedLong(wx,wy)).get(b).isColliding(obj) && !obj.equals(mapObjects.get(toPackedLong(wx,wy)).get(b))) {
							mapObjectsUsed.add(mapObjects.get(toPackedLong(wx,wy)).get(b));
							return true;
						}
					}
					if (dataList.get(index).isSolid()) {
						foundCollision = true;
					}
					} catch (NullPointerException e) {
						return false;
					}
				}
			}
		}
	}
	return foundCollision;
}

	public static boolean isColliding (GameObject obj, String tileId) {
		mapObjectsUsed = new ArrayList<GameObject> ();
		boolean foundCollision = false;
		for (int i = 0; i < obj.hitboxes().length; i++) {
			Rectangle hitbox = obj.hitboxes()[i];
			int startX = Math.max(hitbox.x/TILE_WIDTH,0);
			int startY = Math.max(hitbox.y/TILE_HEIGHT,0);
			int endX = Math.min((hitbox.x + hitbox.width)/TILE_WIDTH,mapWidth-1);
			int endY = Math.min((hitbox.y + hitbox.height)/TILE_HEIGHT,mapHeight-1);
			for (int wx = startX; wx <= endX; wx++ ){
				for (int wy = startY; wy <= endY; wy++) {
					int index = tileData[collisionLayer][wy][wx];
					if (index == SPECIAL_TILE_ID) {
						long pos = toPackedLong (wx,wy);
						if (positionToEntitiys.get(pos).getData().getName().equals(tileId)) {
							if (!foundCollision) {
								foundCollision = positionToEntitiys.get(pos).doesColide(obj);
								}
							if (foundCollision) {
								positionToEntitiys.get(pos).onCollisionIntermidete(obj);
							}
						}
					} else if (dataList.get(index).getName().equals(tileId)) {
						foundCollision= true;
					}
				}
			}
		}
		return foundCollision;
}
	public static MapTile[] getCollidingTiles (GameObject obj) {
		mapObjectsUsed = new ArrayList<GameObject> ();
		ArrayList<MapTile> working =new ArrayList<MapTile>();
		for (int i = 0; i < obj.hitboxes().length; i++) {
			Rectangle hitbox = obj.hitboxes()[i];
			int startX = Math.max(hitbox.x/TILE_WIDTH,0);
			int startY = Math.max(hitbox.y/TILE_HEIGHT,0);
			int endX = Math.min((hitbox.x + hitbox.width)/TILE_WIDTH,mapWidth-1);
			int endY = Math.min((hitbox.y + hitbox.height)/TILE_HEIGHT,mapHeight-1);
			for (int wx = startX; wx <= endX; wx++ ){
				for (int wy = startY; wy <= endY; wy++) {
					int index = tileData[collisionLayer][wy][wx];
					if (mapObjects.get(toPackedLong(wx,wy)) == null) {
					if (index == SPECIAL_TILE_ID) {
						long pos = toPackedLong (wx,wy);
						if (positionToEntitiys.get(pos).doesColide(obj)) {
							working.add(new MapTile (positionToEntitiys.get(pos).getData(),wx*TILE_WIDTH,wy*TILE_HEIGHT,positionToEntitiys.get(pos)));	
						}
					} else if (dataList.get(index).isSolid()) {
						working.add(new MapTile (dataList.get(index),wx*TILE_WIDTH,wy*TILE_HEIGHT));
					}
					} else{
						try {
						for (int b = 0; b < mapObjects.get(toPackedLong(wx,wy)).size(); b++ ) {
							if (mapObjects.get(toPackedLong(wx,wy)).get(b).isColliding(obj) && !obj.equals(mapObjects.get(toPackedLong(wx,wy)).get(b))) {
								mapObjectsUsed.add(mapObjects.get(toPackedLong(wx,wy)).get(b));
								ArrayList <HitboxInfo> collidingHitboxes = mapObjects.get(toPackedLong(wx,wy)).get(b).getCollidingHitboxes(obj);
								for (int c = 0; c < collidingHitboxes.size(); c++) {
									if (index != SPECIAL_TILE_ID) {
										working.add(new MapTile (dataList.get(index),mapObjects.get(toPackedLong(wx,wy)).get(b).getX() + collidingHitboxes.get(c).getXOffset(),mapObjects.get(toPackedLong(wx,wy)).get(b).getY() + collidingHitboxes.get(c).getYOffset()));
									} else {
										long pos = toPackedLong (wx,wy);
										working.add(new MapTile (positionToEntitiys.get(pos).getData(),mapObjects.get(toPackedLong(wx,wy)).get(b).getX() + collidingHitboxes.get(c).getXOffset(),mapObjects.get(toPackedLong(wx,wy)).get(b).getY() + collidingHitboxes.get(c).getYOffset()));
									}
								}
							}
						}
						if (index != SPECIAL_TILE_ID) {
							if (dataList.get(index).isSolid()) {
								working.add(new MapTile (dataList.get(index),wx*TILE_WIDTH,wy*TILE_HEIGHT));
							}
						} else {
							long pos = toPackedLong (wx,wy);
							if (positionToEntitiys.get(pos).doesColide(obj)) {
								working.add(new MapTile (positionToEntitiys.get(pos).getData(),wx*TILE_WIDTH,wy*TILE_HEIGHT,positionToEntitiys.get(pos)));	
							}
						}
						} catch (NullPointerException e) {
						}
					}
				}
			}
		}
		return working.toArray(new MapTile[0]);
	}
	public static MapTile[] getCollidingTiles (GameObject obj, String tileName) {
		mapObjectsUsed = new ArrayList<GameObject> ();
		ArrayList<MapTile> working =new ArrayList<MapTile>();
		for (int i = 0; i < obj.hitboxes().length; i++) {
			Rectangle hitbox = obj.hitboxes()[i];
			int startX = Math.max(hitbox.x/TILE_WIDTH,0);
			int startY = Math.max(hitbox.y/TILE_HEIGHT,0);
			int endX = Math.min((hitbox.x + hitbox.width)/TILE_WIDTH,mapWidth-1);
			int endY = Math.min((hitbox.y + hitbox.height)/TILE_HEIGHT,mapHeight-1);
			for (int wx = startX; wx <= endX; wx++ ){
				for (int wy = startY; wy <= endY; wy++) {
					int index = tileData[collisionLayer][wy][wx]; 			
					if (index == SPECIAL_TILE_ID) {
						long pos = toPackedLong (wx,wy);
						if (positionToEntitiys.get(pos).getData().getName().equals(tileName)) {
							working.add(new MapTile (positionToEntitiys.get(pos).getData(),wx*TILE_WIDTH,wy*TILE_HEIGHT));	
						}
					} else if (dataList.get(index).getName().equals(tileName)) {
						working.add(new MapTile (dataList.get(index),wx*TILE_WIDTH,wy*TILE_HEIGHT));
					}
				}
			}
		}
		return working.toArray(new MapTile[0]);
	}
	/**
	 * returns all tiles colliding with this solid or not
	 * @param obj the object to check against
	 * @return all tiles inside that object
	 */
public static MapTile[] getAllCollidingTiles (GameObject obj) {
		ArrayList<MapTile> working =new ArrayList<MapTile>();
		for (int i = 0; i < obj.hitboxes().length; i++) {
			Rectangle hitbox = obj.hitboxes()[i];
			int startX = Math.max(hitbox.x/TILE_WIDTH,0);
			int startY = Math.max(hitbox.y/TILE_HEIGHT,0);
			int endX = Math.min((hitbox.x + hitbox.width)/TILE_WIDTH,mapWidth-1);
			int endY = Math.min((hitbox.y + hitbox.height)/TILE_HEIGHT,mapHeight-1);
			for (int wx = startX; wx <= endX; wx++ ){
				for (int wy = startY; wy <= endY; wy++) {
					int index = tileData[collisionLayer][wy][wx];
					if (index == SPECIAL_TILE_ID) {
						long pos = toPackedLong (wx,wy);
							working.add(new MapTile (positionToEntitiys.get(pos).getData(),wx*TILE_WIDTH,wy*TILE_HEIGHT));	
					} else {
						working.add(new MapTile (dataList.get(index),wx*TILE_WIDTH,wy*TILE_HEIGHT));
					}
				}
			}
		}
		return working.toArray(new MapTile[0]);
	}
	/**
	 * returns true if the specified tile is a solid tile
	 * @param layer the layer the tile is on
	 * @param x the x coordinate of the tile
	 * @param y the y coordinate of the tile
	 * @return true if its solid like a rock or my mixtape
	 */
	public static boolean isSolid (int layer,int x, int y) {
		return dataList.get(tileData[layer][y][x]).isSolid();
	}
	public static ArrayList<TileEntitiy> getEntitiys(){
		return tileEntitiys;
	}
	/**
	 * gets the chungus that contains the tile at this position
	 * @param x the x position of the tile in question (in tiles)
	 * @param y the y position of the tile in question (in tiles)
	 * @return the map chungus asociated
	 */
	public static MapChungus getChungus (int x, int y) {	
		return mapChungi[y/chungusHeight][x/chungusWidth];
	}
	/**
	 * returns true if the specified tile is a solid tile (false if the tile is not loaded)
	 * @param name the name of the specified tile
	 * @return true if its solid like a rock or my mixtape
	 */
	public static boolean isSolid (String name) {
		try {
		return nameList.get(name).isSolid();
		} catch (NullPointerException e) {
		return false;
		}
	}
	/**
	 * renders the map and associated chungi
	 */
	public static void render () {
		
		if (updateX != -1) {
			if (updateX > scrollX) {
				if (updateX + RenderLoop.wind.getResolution()[0] < mapWidth * Room.TILE_WIDTH) {
					scrollX = updateX;	
				} else {
					scrollX = (mapWidth * TILE_WIDTH) - RenderLoop.wind.getResolution()[0];
					if (scrollX < 0) {
						scrollX = 0;
					}
				}
			} else {
				if (updateX > 0) {
					scrollX = updateX;	
				} else {
					scrollX =0;
				}
			}
			updateX = -1;
		}
		if (updateY != -1) {
			if (updateY > scrollY) {
				if (updateY + RenderLoop.wind.getResolution()[1] < mapHeight * Room.TILE_HEIGHT) {
					scrollY = updateY;	
				} else {
					scrollY = (mapHeight * TILE_HEIGHT) - RenderLoop.wind.getResolution()[1];
					if (scrollY < 0) {
						scrollY = 0;
					}
				}
			} else {
				if (updateY > 0) {
				
					scrollY = updateY;	
				} else {
					scrollY =0;
				}
			}
		
			updateY = -1;
		}
		Rectangle viewport = new Rectangle (scrollX - 20,scrollY - 20,RenderLoop.wind.getResolution()[0] + 40,RenderLoop.wind.getResolution()[1] + 40);
		if (isLoaded()) {
			//runs code on tileEntitiys
			for (int i = 0; i < tileEntitiys.size();i++) {
				tileEntitiys.get(i).frameEvent();
			}
		for (int wy = 0; wy < mapChungi.length; wy++) {
			for (int wx = 0; wx < mapChungi[0].length; wx++) {
				MapChungus currentChungus = mapChungi[wy][wx];
				
				Rectangle chungtangle = new Rectangle (currentChungus.getX()*TILE_WIDTH,currentChungus.getY()*TILE_HEIGHT,chungusWidth*TILE_WIDTH,chungusHeight*TILE_HEIGHT);
				if (!viewport.intersects(chungtangle)) {
					
					if (!currentChungus.isFree()) {
						currentChungus.freeImage();
						currentChungus.invalidate();
					}
				} else {
					currentChungus.draw();
				}
			}
		}
		}
	}
	
	/**
	 * loads the map file at the given filepath
	 * @param path the filepath to the map file
	 * @return wheater or not the loading was succesful
	 */
	public static boolean loadRoom (String path) {
		//START OF HEADER
		//Bytes 0-3: RMF# (# is version number)
		//Bytes 4-7: Map width, in tiles
		//Bytes 8-11: Map height, in tiles
		//Bytes 12-15: Number of layers
		//Bytes 16-19: Number of objects (placed)
		//END OF HEADER
		//Tileset list (background layers are excluded)
		//Object import list
		//Background list
		//Tiles
		//Object list (x, y, id, variant)
		
		//initalizes fields relating to the map
		isLoaded = false;
		dataList = new ArrayList<TileData>();
		nameList = new HashMap<String,TileData>();
		tileIcons = new ArrayList<BufferedImage> ();
		backgrounds = new ArrayList<Background>();
		tileEntitiys = new ArrayList<TileEntitiy>();
		positionToEntitiys = new HashMap<Long,TileEntitiy>();
		//purges the gameObjects
			ArrayList<ArrayList<GameObject>> objList = ObjectHandler.getChildrenByName("GameObject");
			for (int i = 0; i < objList.size (); i ++) { 
				if (objList.get (i) != null) {
					int listSize = objList.get (i).size ();
					int deletThis = 0;
					for (int j = 0; j < listSize; j ++) {
						if (objList.get (i).get (0) != null) {
							if (!objList.get (i).get (deletThis).isPersistent ()) {
								objList.get (i).get (deletThis).forget ();
							} else {
								if (roomName.equals(path)) {
									deletThis++;
								} else {
									objList.get (i).get (deletThis).forget ();
								}
							}
						}
					}
				}
			}
		//Loads the RMF file at the given filepath
		File file = null;
		FileInputStream stream = null;
		file = new File (path);
		inData = new byte[(int) file.length ()];
		try {
			stream = new FileInputStream (file);
		} catch (FileNotFoundException e1) {
			return false;
		}
		try {
			stream.read (inData);
			stream.close ();
		} catch (IOException e) {
			return false;
		}
		readPos = 0;
		char MAX_VERSION = '1';
		
		//Check header
		String fmtString = getString (3);
		if (!fmtString.equals ("RMF")) {
			System.out.println("ERROR: not an RMF (raw map file)");
			System.out.println("you most likely used the wrong map editor");
			return false;
		}
		
		String verString = getString (1);
		if (verString.charAt (0) < '1' || verString.charAt (0) > MAX_VERSION) {
			System.out.println("it apperas your map is an old version but if it worked anyway just ignore this message");
			return false;
		}
		//Read attributes
				int mapWidth = getInteger (4);
				int mapHeight = getInteger (4);
				Room.mapHeight = mapHeight;
				Room.mapWidth = mapWidth;
				int numLayers = getInteger (4) ;
				Room.numLayers = numLayers;
				int numObjects = getInteger (4);
				tileData = new int[numLayers][mapHeight][mapWidth];
		//Parse tile set list
		String tilesets = getString (';');
		String[] tilesetNameArray = tilesets.split (",");
	
		//Parse object list
		String objectString = getString (';');
		String [] objectList = objectString.split(",");
		
		//parse backgrounds
		String backgroundString = getString (';');
		String[] backgroundList = backgroundString.split (",");
		
		
		//import all tilesets
		for (int i= 0; i < tilesetNameArray.length; i++) {
			importTileset(tilesetNameArray[i]);
		}
		//import all backgrounds
	
		for(int i = 0; i < backgroundList.length; i++) {
			Background newBackground;
			if (!backgroundList[i].equals("_NULL")) {
				newBackground = new Background ("resources/backgrounds/" +backgroundList[i]);
				i = i + 1;
				double scrollHorizontal = Double.parseDouble(backgroundList[i]);
				i = i + 1;
				double scrollVertical = Double.parseDouble(backgroundList[i]);
				newBackground.setScrollRateHorizontal(scrollHorizontal);
				newBackground.setScrollRateVertiacal(scrollVertical);
				backgrounds.add(newBackground);
			} else {
				backgrounds.add(null);
			}
		}
		
		
		
		//importing tiles
		int amountOfTiles = tileIcons.size();
		int tileByteCount = getByteCount(amountOfTiles);
		for (int wl = 0; wl < numLayers; wl++) {
			if (backgrounds.get(wl)== null) {
				for (int wy = 0; wy < mapHeight; wy++) {
					for (int wx = 0; wx < mapWidth; wx++) {
						tileData [wl][wy][wx] = getInteger(tileByteCount);
						if (dataList.get(tileData[wl][wy][wx]).isSpecial()) {
							TileEntitiy enity = dataList.get(tileData[wl][wy][wx]).makeEntity();
							enity.setX(wx);
							enity.setY(wy);
							enity.setLayer(wl);
							enity.setTileData(dataList.get(tileData[wl][wy][wx]));
							enity.setTexture(tileIcons.get(tileData[wl][wy][wx]));
							tileEntitiys.add(enity);
							positionToEntitiys.put(toPackedLong(wx,wy),enity);
							tileData[wl][wy][wx] = SPECIAL_TILE_ID; 
						} 
					}
				}
			}
		}
		//sets up the collsion layer right
//		for (int i = 0; i < numLayers; i++) {
//			if (backgrounds.get(i) == null){
//				collisionLayer = i;
//			}
//		}
		//importing objects
		int widthByteCount = getByteCount(mapWidth);
		int heightByteCount = getByteCount(mapHeight);
		int objectByteCount = getByteCount(objectList.length);
		for (int i = 0; i < numObjects; i++) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
			int x = getInteger (widthByteCount);
			int y = getInteger(heightByteCount);
			int object = getInteger(objectByteCount);
			String variantInfo = getString (';');
			variantInfo = variantInfo.replace("#","&");
			variantInfo = variantInfo.replace(",","&");
			if (!objectList [object].equals("Trigger") && !objectList [object].equals("Jeffrey")) {
				GameObject objectToUse = ObjectHandler.getInstance(objectList[object]);
				objectToUse.setVariantAttributes(variantInfo);
				objectToUse.declare(x, y*16);
			} else {
				switch (objectList [object]) {
				case "Trigger":
					GameObject objectToUse = ObjectHandler.getInstance("CollisionTrigger");
					objectToUse.setVariantAttributes(variantInfo);
					objectToUse.declare(x*16, y*16);
					break;
				case "Jeffrey":
					GameObject obj = ObjectHandler.getInstance("Party");
					obj.setVariantAttributes(variantInfo);
					obj.declare(x*16, y*16);
					break;
				}
				
			}
		}
		
			
			
		//convert the map to a big number of map chungi
		int gridWidth = (int)Math.ceil((((double)mapWidth)/chungusWidth));
		int gridHidth = (int)Math.ceil((((double)mapHeight)/chungusHeight)); // short for width2
		mapChungi = new MapChungus [gridHidth][gridWidth];
		for (int wx = 0; wx < mapChungi[0].length; wx++) {
			for (int wy = 0; wy<mapChungi.length; wy++) {
				MapChungus workingChungus =  new MapChungus(wx*chungusWidth,wy*chungusHeight);
				int workingX = Math.min(workingChungus.getX() + chungusWidth,mapWidth);
				int workingY = Math.min(workingChungus.getY() + chungusHeight,mapHeight);
				for (int wwwwX = workingChungus.getX(); wwwwX <workingX; wwwwX ++) {
					for (int wwwwY = workingChungus.getY(); wwwwY <workingY; wwwwY ++) {
						for (int wwwwL = 0; wwwwL <numLayers; wwwwL ++) {
							if (tileData[wwwwL][wwwwY][wwwwX] == SPECIAL_TILE_ID) {
								workingChungus.addTileEntity(positionToEntitiys.get(toPackedLong(wwwwX,wwwwY)));
							}
						}
					}
				}
				mapChungi[wy][wx] = workingChungus;
			}
		}
		isLoaded = true;
		roomName = path;
		return false;
	}
	
	/**
	 * adds all of the tiles to the map from the given tileset
	 * @param path the path to the tileset
	 */
	public static void importTileset(String path) {
		//parses out the tiles from the tileset
		if (!path.equals("_NULL")) {
		ArrayList <String> working = new ArrayList <String>();
		working.add("grid " + TILE_WIDTH + " " + TILE_HEIGHT);
		Sprite newSprite = new Sprite (path,new SpriteParser(working));
		int toAdd = newSprite.getFrameCount();	
		for (int i = 0; i < toAdd; i++) {
			String [] fork = path.split("/|\\\\"); // dude thats deep
			String spoon = fork[fork.length -1].substring(0, fork[fork.length - 1].length() - 4); //oh god why?
			String tileName = spoon + "." + i;
			TileData current = new TileData (tileName);
			dataList.add(current);
			nameList.put(tileName, current);
			BufferedImage tile = newSprite.getFrame(i);
			tileIcons.add(tile);
		}
		} else {
			TileData current = new TileData ("_NULL");
			dataList.add(current);
			nameList.put("_NULL", current);
			Sprite newSprite = new Sprite ("resources/tilesets/transparent.png");
			BufferedImage tile = newSprite.getFrame(0);
			tileIcons.add(tile);
		}
	}
	private static String getString (int length) {
		byte[] usedData = new byte[length];
		int endPos = readPos + length;
		int i = 0;
		while (readPos < endPos) {
			usedData [i] = inData [readPos];
			readPos ++;
			i ++;
		}
		return new String (usedData);
	}
	private static int getInteger (int bytes) {
		int total = 0;
		for (int i = 0; i < bytes; i ++) {
			int toRead = inData [readPos + i];
			if (toRead < 0) {
				toRead += 256;
			}
			total += (toRead << ((bytes - 1 - i) * 8));
		}
		readPos += bytes;
		return total;
	}
	private static String getString (char endChar) {
		int len = 0;
		int i = readPos;
		while (inData [i] != endChar) {
			len ++;
			i ++;
		}
		String str = getString (len);
		readPos ++;
		return str;
	}
	private static int getByteCount (int value) {
		int i = 0;
		while (value != 0) {
			value /= 256;
			i ++;
		}
		return i;
	}
	public static String getRoomName () {
		return roomName;
	}
	/**
	 * set the points visable on the screen to whatever
	 * @param x the x coordinate to set the veiw two 2
	 * @param y the y coordinate to set the veiw too
	 */
	public static void setView (int x, int y) {	
		if (x <getViewX()) {
			System.out.println("debg");
		}
		updateX = x;
		updateY = y;
	}
	
	/**
	 * returns what point of the map the leftmost edge of the screen is on
	 * @return the x coordinate of the viewport
	 */
	public static int getViewX () {
		return scrollX;
	}
	/**
	 * returns what point of the map the top edge of the screen is on
	 * @return the y coordinate of the viewport
	 */
	 public static int getViewY () {
		return scrollY;
	}
	 /**
	 * returns what point of the map the leftmost edge of the screen is going to be updated to
	 */
		public static int getViewXAcurate () {
			if (updateX  != -1) {
				return updateX;
			} else {
				return scrollX;
			}
		}
		/**
		 * returns what point of the map the top edge of the screen is going to be updated to
		 */
		 public static int getViewYAcurate () {
			 if (updateX  != -1) {
					return updateY;
				} else {
					return scrollY;
				}
		}
		  
	/**
	 * returns the width of the map
	 * @return how THICC the map is
	 */
	public static int getWidth () {
		return mapWidth;
	}
	/**
	 * returns the height of the map
	 * @return how high the map is (420!)
	 */
	public static int getHeight () {
		return mapHeight;
	}
	
	/**
	 * returns the info about a specific type of tile
	 * @param tileName the name of the tile you want info about
	 * @return info about the tile if the tile is currently loaded null otherwise
	 */
	public static TileData getTileProperties(String tileName) {
		return nameList.get(tileName);
	}
	/**
	 * returns the info about a specific type of tile
	 * @param layer the layer the tile is on
	 * @param x the x coordinate of the tile
	 * @param y the y coordinate of the tile
	 * @return info about the tile specified
	 */
	public static TileData getTileProperties(int layer, int x, int y) {
		return dataList.get(tileData[layer][y][x]);
	}
	/**
	 * returns the name of a speicfic tile
	 * @param layer the layer the tile is on
	 * @param x the x coordinate of the tile
	 * @param y the y coordinate of the tile
	 * @return name of the tile specified
	 */
	public static String getTileName (int layer, int x, int y) {
		try {
		return dataList.get(tileData[layer][y][x]).getName();
		} catch (IndexOutOfBoundsException e) {
		long working = toPackedLong (x,y);
		return positionToEntitiys.get(working).getType();
		}
	}
	/**
	 * returns a list of the backgrounds
	 * @return duh
	 */
	public static ArrayList<Background> getBackgroundList () {
		return backgrounds;
	}
	/**
	 * gets the gravity (in pixels/frame/frame)
	 * @return how fast things go down dude
	 */
	public static double getGravity () {
		return gravity;
	}
	
	/**
	 * sets the gravity (whoa why havent scientists done this yet)
	 * @param grav the gravity to set it too
	 */
	public static void setGravity (double grav) {
		gravity = grav;
	}
	/**
	 * returns true if the tile is within the confines of the map
	 * @param x the xpos of the tile
	 * @param y the y position of the tile
	 * @return true if the tile is in the map: false otherwise
	 */
	public static boolean tileInBounds (int x, int y) {
		return !(x> mapWidth|| y > mapHeight);
	}
	/**
	 * returns true if the map is makin bank
	 * @return true if the map is rich 
	 */
	public static boolean isLoaded () {
		return isLoaded;
	}
	public static HashMap <Long, ArrayList<GameObject>> getMapObjects() {
		return mapObjects;
	}

	public static void setMapObjects(HashMap <Long, ArrayList<GameObject>> mapObjects) {
		Room.mapObjects = mapObjects;
	}
	/**
	 * Returns the MapObjects collided with in the most recent room collision call
	 * @return YEET
	 */
	public static ArrayList<GameObject> getObjectCollisionInfo () {
		return mapObjectsUsed;
	}
	
	//its a map chunk ... a big map chunk
	/**
	 * a portion of the map
	 * @author GOD 
	 *(god made map chungus)
	 */
	public static class MapChungus {
		ArrayList <BufferedImage> renderedImages; // the compressed images sorted by layer (and each layer contains all layers that wont cause problems)
		// if you don't understand this anymore your stupid
		 private ArrayList <Boolean> valid; // states wheather each of the images is valid
		
		int x;
		int y;
		
		ArrayList <TileEntitiy> tilelist;
		ArrayList<Integer> layerClassfications; // gives the index for the layers bufferedimage
		
		
		
		boolean incompleate = false;
		int width = chungusWidth;
		int height = chungusHeight;
		/**
		 * makes the map chungus
		 * @param x the x coordinate of the map chungus (in tiles)
		 * @param y the y coordinate of the map chungus (in tiles)
		 */
		public MapChungus (int x, int y) { 
			layerClassfications = new ArrayList<Integer>();
			tilelist = new ArrayList<TileEntitiy> ();
			renderedImages = new ArrayList<BufferedImage>();
			valid = new ArrayList<Boolean>();
			this.x = x;
			this.y = y;
			if (x + chungusWidth > mapWidth || y + chungusHeight > mapHeight) {
				incompleate =true;
				if (x + chungusWidth > mapWidth) {
					width = mapWidth - x ;
				}
				if (y + chungusHeight > mapHeight) {
					height = mapHeight - y;
				}
			}
			if (numLayers== 1) {
				layerClassfications.add(0);
				valid.add(false);
				renderedImages.add(null);
			} else {
			int mappedLayerIndex = 0;
			
			//really confusing code I tried to comment it out, but my guess is Imma have to figure this one out again someday (but Ill at least be able to do it with an RGB gamer keyboard)
			for (int i = 0; i < numLayers; i++) {
				if (isSpecialLayer(i)) {
					boolean extraImage =false;
					if (i != 0) {
						if (!isSpecialLayer (i-1)) {
							mappedLayerIndex = mappedLayerIndex + 1;
							extraImage = true;
						}
					}
					// set the preious layer classification
					layerClassfications.add(mappedLayerIndex);
					
					// makes an image for this previous layer
					valid.add(false);
					renderedImages.add(null);
					mappedLayerIndex = mappedLayerIndex +1;
					
					//makes an image for this layer if nessasary
					if (extraImage && i != numLayers - 1) {
						valid.add(false);
						renderedImages.add(null);
					}
				} else {
					//use the same layer for the previous one and the one before that
					layerClassfications.add(mappedLayerIndex);
				}
			}
			
			//makes an image for the final layer
			valid.add(false);
			renderedImages.add(null);
			
			}
			
		}
		/**
		 * returns whearer or not this layer can be rendered with all the rest
		 * @return whearer or not this layer can be rendered with all the rest
		 */
		public boolean isSpecialLayer (int layerNum) {
			if (backgrounds.get(layerNum) != null) {
				if (backgrounds.get(layerNum).getScrollRateHorizontal() != 1 || backgrounds.get(layerNum).getScrollRateVertical() != 1 || backgrounds.get(layerNum).getFrames ().size () > 1){
					return true;
				}
			} else {
				for (int wx = 0; wx < width; wx++) {
					for (int wy = 0; wy < height; wy++) {
						if (! ((wx + x > width) || wy + y > height )) {
							if (tileData[layerNum][wy + y][wx + x] == SPECIAL_TILE_ID) {
								return true;
							}	
						} else {
							if (wx + x > width) {
								if (wy + y > height) {
									if (tileData[layerNum][height][width] == SPECIAL_TILE_ID) {
										return true;
									}	
								} else {
									if (tileData[layerNum][wy + y][width] == SPECIAL_TILE_ID) {
										return true;
									}	
								}
							} else {
								if (tileData[layerNum][height][wx + x] == SPECIAL_TILE_ID) {
									return true;
								}
							}
						}
						
						
					}
				}
			}
			return false;
		}
		
		/**
		 * destroyes the pre rendered image when called
		 */
		public void freeImage() {
			for (int i = 0; i < renderedImages.size(); i++) {
				renderedImages.set(i, null);
			}
		}
		
		/**
		 * returns true if the image is not pre rendered
		 * @return true if the image is not pre rendered
		 */
		public boolean isFree () {
			if (renderedImages.get(0) == null) {
				return true;
			} else {
				return false;
			}
		}
		
		/**
		 * adds a special tile to the tiles rendered by this map chungus
		 * @param entity the entity to add
		 */
		public void addTileEntity (TileEntitiy entity) {
			tilelist.add(entity);
		
		}
		
		/**
		 * tells the computer that this render of the map chungus is no longer correct
		 */
		public void invalidate (int layer) {
			valid.set(layerClassfications.get(layer), false);
		}
		public void invalidate () {
			for (int i = 0; i<valid.size(); i++) {
				valid.set(i, false);
			}
		}

		/**
		 * returns wheather or not the map chungus is still valid
		 * @return wheather or not the map chungus is still valid
		 */
		public boolean isValid (int layer){
			return valid.get(layerClassfications.get(layer));	
		}
		
		/**
		 * gets the x value of the map chungi (in tiles)
		 * @return that 
		 */
		public int getX() {
			return x;
		}
		
		/**
		 * gets the y value of the map chungi (in tiles)
		 * @return that 
		 */
		public int getY() {
			return y;
		}
		
		/**
		 * draws the map chungus (and any special entitys that are associated with it)
		 */
		public void draw () {
			int currentLayer = layerClassfications.size() -1;
			for (int l = 0; l < backgrounds.size(); l++) {
				if (backgrounds.get(l) != null) {
					if (backgrounds.get(l).isNewFrame()) {
						this.invalidate(l);
					}
				}
			}
				for (int l = renderedImages.size()-1; l >=0; l--) {
					if (!this.isValid(l)) {
						renderedImages.set(l,new BufferedImage (chungusWidth*TILE_HEIGHT,chungusWidth*TILE_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR));
						Graphics g = renderedImages.get(l).getGraphics();
						while (layerClassfications.get(currentLayer)>=l) {
							if (backgrounds.get(currentLayer) == null) {
							for (int wx = 0; wx < width; wx++) {
								for (int wy = 0; wy < height; wy++) {
									if (tileData[currentLayer][wy + y][wx + x] == SPECIAL_TILE_ID){
										g.drawImage(positionToEntitiys.get(toPackedLong(wx + x,wy + y)).getTexture(),wx*TILE_WIDTH,wy*TILE_HEIGHT,null);
									
									} else {
										g.drawImage(tileIcons.get(tileData[currentLayer][wy + y][wx + x]),wx*TILE_HEIGHT,wy*TILE_WIDTH,null);
									}
								}
							}
							} else {
								//TODO get this to work with animated backgrounds
								int rasterWidth = Math.min (x*TILE_WIDTH + chungusWidth*TILE_WIDTH,mapWidth*TILE_WIDTH);
								int rasterHeight = Math.min (y*TILE_HEIGHT + chungusHeight*TILE_HEIGHT,mapHeight*TILE_HEIGHT);
								rasterWidth = Math.min(rasterWidth, backgrounds.get(currentLayer).getWidth()) - x*TILE_WIDTH;
								rasterHeight = Math.min(rasterHeight, backgrounds.get(currentLayer).getHeight()) - y*TILE_HEIGHT;
								if (rasterWidth > 0 && rasterHeight > 0){
									int backgroundFrame = backgrounds.get(currentLayer).getCurrentFrame();
									BufferedImage working = backgrounds.get(currentLayer).getFrames ().get(backgroundFrame).getSubimage(x*TILE_WIDTH, y*TILE_HEIGHT, rasterWidth, rasterHeight);
									g.drawImage(working,0,0,null);
								}
							}
							valid.set(layerClassfications.get(l), true);
							currentLayer = currentLayer - 1;
							if (currentLayer <0) {
								break;
							}
						}
					}
				}
				
				for (int l = renderedImages.size()-1; l >=0; l--) {
					Graphics g = RenderLoop.wind.getBufferGraphics();
					g.drawImage(renderedImages.get(l),x*TILE_WIDTH - scrollX, y*TILE_HEIGHT - scrollY,null);
			}
		}	
	}
	
	
}