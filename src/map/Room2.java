package map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import json.JSONUtil;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class Room2 extends GameObject {

	private int mapWidth;
	private int mapHeight;
	
	private int tileWidth;
	private int tileHeight;
	
	private ArrayList<LoadedTile> tiles;
	
	private ArrayList<MapLayer> layers;
	
	private ArrayList<MapObject> mapObjects;
	private HashMap<String, MapObject> mapObjNameMap;
	
	@Override
	public void draw () {
		for (int i = 0; i < layers.size (); i++) {
			MapLayer working = layers.get (i);
			for (int wy = 0; wy < mapHeight; wy++) {
				for (int wx = 0; wx < mapWidth; wx++) {
					int tid = working.tileData[wy][wx];
					if (tid != 0) {
						tiles.get (tid).getSprite ().draw (wx * tileWidth, wy * tileHeight);
					}
				}
			}
		}
	}
	
	public void loadMap (String path) {
		
		JSONObject mapData;
		try {
			mapData = JSONUtil.loadJSONFile (path);
			loadGlobalProperties (mapData);
			loadMapTilesets (mapData);
			loadMapLayers (mapData);
			spawnAllObjs ();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		declare (0, 0);
		
	}
	
	public void loadGlobalProperties (JSONObject mapData) {
		
		//Map width and height
		mapWidth = mapData.getInt ("width");
		mapHeight = mapData.getInt ("height");
		
		//Tile width and height
		tileWidth = mapData.getInt ("tilewidth");
		tileHeight = mapData.getInt ("tileheight");
		
		//Other global properties of the map are unimportant/reserved for use by Tiled
		
	}
	
	public void loadMapTilesets (JSONObject mapData) {
		JSONArray mapSets = mapData.getJSONArray ("tilesets");
		for (int i = 0; i < mapSets.getContents ().size (); i++) {
			String fname = ((JSONObject)mapSets.get (i)).getString ("source");
			String path = "resources/tilesets/" + fname;
			try {
				JSONObject tsJSON = JSONUtil.loadJSONFile (path);
				System.out.println (fname);
				Tileset ts = new Tileset (tsJSON, fname.split ("\\.")[0]);
				loadTiles (ts);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadMapLayers (JSONObject mapData) {
		JSONArray mapLayers = mapData.getJSONArray ("layers");
		for (int i = 0; i < mapLayers.getContents ().size (); i++) {
			MapLayer working = new MapLayer ((JSONObject)mapLayers.get (i));
			loadLayer (working);
		}
	}
	
	public ArrayList<LoadedTile> getLoadedTiles () {
		
		//Create the loaded tile list if it doesn't exist
		if (tiles == null) {
			tiles = new ArrayList<LoadedTile> ();
			tiles.add (new LoadedTile (new Tileset (), 0));
		}
		
		//Return the tile list
		return tiles;
		
	}
	
	public ArrayList<MapLayer> getMapLayers () {
		
		//Create the loaded tile list if it doesn't exist
		if (layers == null) {
			layers = new ArrayList<MapLayer> ();
		}
		
		//Return the tile list
		return layers;
		
	}
	
	public ArrayList<MapObject> getMapObjectsList () {
		
		//Create the map objects list if it doesn't exist
		if (mapObjects == null) {
			mapObjects = new ArrayList<MapObject> ();
		}
		
		//Return the object list
		return mapObjects;
		
	}
	
	public HashMap<String, MapObject> getMapObjectsMap () {
		
		//Create the map objects list if it doesn't exist
		if (mapObjNameMap == null) {
			mapObjNameMap = new HashMap<String, MapObject> ();
		}
		
		//Return the object list
		return mapObjNameMap;
		
	}
	
	public void loadTiles (Tileset tileset) {
		for (int i = 0; i < tileset.getSize (); i++) {
			LoadedTile curr = new LoadedTile (tileset, i);
			curr.assignIndex (getLoadedTiles ().size ());
			getLoadedTiles ().add (curr);
		}
	}
	
	public void loadLayer (MapLayer layer) {
		getMapLayers ().add (layer);
	}
	
	public void spawnAllObjs () {
		
		for (int i = 0; i < getMapObjectsList ().size (); i++) {
			MapObject curr = getMapObjectsList ().get (i);
			if (curr.getType ().equals ("tile")) {
				LoadedTile tileType = curr.getTile ();
				String objTypename = tileType.getFromSet ().getName ();
				String objType = "gameObjects." + objTypename; //TODO do this dynamically (or support multiple packages statically)
				try {
					Class<?> objClass = Class.forName (objType);
					GameObject newObj = (GameObject)objClass.getConstructor ().newInstance ();
					newObj.declare (curr.pos.x, curr.pos.y);
					if (curr.getRawProperties () != null) {
						//Set all the variant attributes as needed
						JSONArray propertyArr = curr.getRawProperties ();
						for (int j = 0; j < propertyArr.getContents ().size (); j++) {
							JSONObject working = (JSONObject)propertyArr.get (j);
							newObj.setVariantAttribute (working.getString ("name"), working.getString ("value")); //TODO use the type parameter? (probably not since variants are all Strings)
						}
					}
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					System.out.println ("Error: failed to instantiate object of type " + objType + " for MapObject " + curr.getId ());
				}
			}
		}
		
	}
	
	private class MapLayer {
		
		private String layerType;
		
		private int[][] tileData;
		
		public MapLayer (JSONObject layer) {
			
			//Read in the layer type
			layerType = layer.getString ("type");
			
			//TODO read misc. layer properties
			
			//Processing for tile layer
			tileData = new int[mapHeight][mapWidth];
			if (layerType.equals ("tilelayer")) {
				JSONArray layerData = layer.getJSONArray ("data");
				for (int wy = 0; wy < mapHeight; wy++) {
					for (int wx = 0; wx < mapWidth; wx++) {
						tileData[wy][wx] = (int)layerData.get (wy * mapWidth + wx);
					}
				}
			}
			
			//Processing for object layer
			if (layerType.equals ("objectgroup")) {
				JSONArray objs = layer.getJSONArray ("objects");
				for (int i = 0; i < objs.getContents ().size (); i++) {
					JSONObject curr = (JSONObject)objs.get (i);
					MapObject newObj = new MapObject (curr);
					getMapObjectsList ().add (newObj);
					if (!newObj.getName ().equals ("")) {
						getMapObjectsMap ().put (newObj.getName (), newObj);
					}
				}
			}
			
		}
		
	}
	
	private class LoadedTile {
		
		private Tileset fromSet;
		private int fromIndex;
		
		private Sprite tileImg;
		private int loadedIndex;
		
		public LoadedTile (Tileset fromSet, int fromIndex) {
			
			//Set params
			this.fromSet = fromSet;
			this.fromIndex = fromIndex;
			
			//Set the appropriate sprite
			tileImg = fromSet.getTile (fromIndex);
			
		}
		
		public void assignIndex (int loadedIndex) {
			this.loadedIndex = loadedIndex;
		}
		
		public Sprite getSprite () {
			return tileImg;
		}
		
		public Tileset getFromSet () {
			return fromSet;
		}
		
	}
	
	private class Tileset {
		
		private int tileWidth;
		private int tileHeight;
		private Sprite[] tiles;
		
		private String name;
		
		//No-arg constructor creates the empty tileset
		public Tileset () {
			tileWidth = Room2.this.tileWidth;
			tileHeight = Room2.this.tileHeight;
			tiles = new Sprite[1];
			tiles[0] = null;
		}
		
		public Tileset (JSONObject tileset, String name) {
			
			tileWidth = tileset.getInt ("tilewidth");
			tileHeight = tileset.getInt ("tileheight");
			this.name = name;
			loadTiles (tileset);
			
		}
		
		public int getTileWidth () {
			return tileWidth;
		}
		
		public int getTileHeight () {
			return tileHeight;
		}
		
		public Sprite getTile (int index) {
			return tiles[index];
		}
		
		public int getSize () {
			return tiles.length;
		}
		
		public String getName () {
			return name;
		}
		
		private void loadTiles (JSONObject tileset) {
			
			//Load the sprite and get its BufferedImage
			File f = new File (tileset.getString ("image"));
			System.out.println (f.getName ());
			Sprite tilesImg = new Sprite ("resources/tilesets/" + f.getName ());
			BufferedImage rawImg = tilesImg.getFrame (0);
			
			//Create the tile array
			tiles = new Sprite[tileset.getInt ("tilecount")];
			
			//Populate the tile array from the source image
			int numColumns = tileset.getInt ("columns");
			int tIndex = 0;
			int wy = 0;
			while (tIndex < tiles.length) {
				for (int wx = 0; wx < numColumns && tIndex < tiles.length; wx++) {
					//Parse out the tile here
					BufferedImage tileImg = rawImg.getSubimage (getTileWidth () * wx, getTileHeight () * wy, getTileWidth (), getTileHeight ());
					Sprite tileSprite = new Sprite (tileImg);
					tiles[tIndex] = tileSprite;
					tIndex++;
				}
				wy++;
			}
			
		}
		
	}
	
	public class MapObject extends GameObject {
		
		private int id;
		
		private double width;
		private double height;
		private Point pos;
		private ArrayList<Point> vertices;
		private String type;
		private int tileId;
		private String text;
		private JSONArray properties;
		private String name;
		
		public MapObject (JSONObject params) {
			assignType (params);
			loadProperties (params);
			declare ();
			this.setRenderPriority (69);
		}
		
		public void assignType (JSONObject params) {
			
			if (params.get ("polygon") != null) {
				type = "polygon";
			} else if (params.get ("polyline") != null) {
				type = "polyline";
			} else if (params.get ("text") != null) {
				type = "text";
			} else if (params.get ("gid") != null) {
				type = "tile";
			} else if (params.get ("ellipse") != null && params.get ("ellipse").equals (Boolean.TRUE)) {
				type = "ellipse";
			} else if (params.get ("point") != null && params.get ("point").equals (Boolean.TRUE)) {
				type = "point";
			} else {
				//Rectangle by default
				type = "rectangle";
			}
			
		}
		
		public void loadProperties (JSONObject params) {
			id = params.getInt ("id");
			pos = new Point (params.getDouble ("x"), params.getDouble ("y"));
			width = params.getDouble ("width");
			height = params.getDouble ("height");
			properties = (JSONArray)params.get ("properties");
			name = (String)params.get ("name");
			JSONArray pts;
			switch (type) {
				case "polygon":
					vertices = new ArrayList<Point> ();
					pts = params.getJSONArray ("polygon");
					for (int i = 0; i < pts.getContents ().size (); i++) {
						JSONObject pt = (JSONObject)pts.get (i);
						vertices.add (new Point (pt.getDouble ("x"), pt.getDouble ("y")));
					}
					break;
				case "polyline":
					vertices = new ArrayList<Point> ();
					pts = params.getJSONArray ("polyline");
					for (int i = 0; i < pts.getContents ().size (); i++) {
						JSONObject pt = (JSONObject)pts.get (i);
						vertices.add (new Point (pt.getDouble ("x"), pt.getDouble ("y")));
					}
					break;
				case "text":
					text = params.getJSONObject ("text").getString ("text");
					break;
				case "tile":
					tileId = params.getInt ("gid");
					pos.y -= height; //For some reason, tiles SPECIFICALLY and ONLY TILES use their bottom-left corner for their position
					break;
				case "ellipse":
					//Nothing to do here
					break;
				case "point":
					//Also nothing to do here
					break;
				case "rectangle":
					//Still nothing to do here
					break;
				default:
					break;
			}
		}
		
		public boolean hasName () {
			return name == null;
		}
		
		public String getName () {
			return name;
		}
		
		public int getId () {
			return id;
		}
		
		public String getType () {
			return type;
		}
		
		public Point getPos () {
			return pos;
		}
		
		public LoadedTile getTile () {
			return getLoadedTiles ().get (tileId);
		}
		
		public ArrayList<Point> getVertices () {
			return vertices;
		}
		
		public JSONArray getRawProperties () {
			return properties;
		}
		
		@Override
		public void draw () {
			Graphics2D g = (Graphics2D)RenderLoop.wind.getBufferGraphics ();
			g.setColor (Color.BLACK);
			switch (type) {
			case "polygon":
				for (int i = 0; i < vertices.size (); i++) {
					Point fromPt = vertices.get (i);
					Point toPt = vertices.get ((i + 1) % vertices.size ());
					g.drawLine ((int)(pos.x + fromPt.x), (int)(pos.y + fromPt.y), (int)(pos.x + toPt.x), (int)(pos.y + toPt.y));
				}
				break;
			case "polyline":
				for (int i = 0; i < vertices.size () - 1; i++) {
					Point fromPt = vertices.get (i);
					Point toPt = vertices.get ((i + 1) % vertices.size ());
					g.drawLine ((int)(pos.x + fromPt.x), (int)(pos.y + fromPt.y), (int)(pos.x + toPt.x), (int)(pos.y + toPt.y));
				}
				break;
			case "text":
				g.drawString (text, (int)pos.x, (int)pos.y);
				break;
			case "tile":
				Sprite spr = tiles.get (tileId).getSprite ();
				g.drawImage (spr.getFrame (0), (int)pos.x, (int)pos.y, (int)(pos.x + width), (int)(pos.y + height), 0, 0, spr.getWidth (), spr.getHeight (), null);
				break;
			case "ellipse":
				g.drawOval ((int)pos.x, (int)pos.y, (int)width, (int)height);
				break;
			case "point":
				g.fillRect ((int)pos.x - 2, (int)pos.y - 2, 4, 4);
				break;
			case "rectangle":
				g.drawRect ((int)pos.x, (int)pos.y, (int)width, (int)height);
				break;
			default:
				break;
			}
		}

	}
	
	private class Point {
		
		public double x;
		public double y;
		
		public Point (double x, double y) {
			this.x = x;
			this.y = y;
		}
		
	}
	
}
