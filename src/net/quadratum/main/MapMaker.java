package net.quadratum.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import net.quadratum.core.MapPoint;
import net.quadratum.core.TerrainConstants;

/**
 * Creates a random map given certain constraints.
 */
public class MapMaker {
	
	public static char delimiter = '|';
	//By default, clump water heavily, mountains moderately, and resources somewhat
	private static final float WATER_CLUMP = 0.95f;
	private static final float MTN_CLUMP = 0.67f;
	private static final float RES_CLUMP = 0.4f;
	
	private static final float WATER_FREQ =  0.1f;
	private static final float BUNKER_FREQ = 0.02f;
	private static final float MTN_FREQ =    0.06f;
	private static final float LAVA_FREQ =   0.02f;
	private static final float RES_FREQ =    0.06f;
	
	
	public static void main(String[] args) {
		String path = generateMap("random1", 10, 10, 2, 5, true, true, true, true, false);
	}
	
	public static String generateMap(
			String filename, int width, int height, int players, int units,
			boolean water, boolean bunkers, boolean mtns, boolean resources,
			boolean lava, float[] frequencies)
	{
		
		PrintWriter out = null;
		if( ! filename.startsWith("maps/") )
			filename = "maps/" + filename;
		if( ! filename.endsWith(".qmap") || filename.endsWith(".qmp") )
			filename = filename + ".qmap";
		try {
			out = new PrintWriter(new FileWriter(filename));
			
			
			Random rng = new Random();
			//assume positive inputs
			
			int area = width * height;
			float dArea = ((float) area);
			
			int numWater = (water ? Math.round(dArea * frequencies[0]) : 0);
			int numBunkers = (bunkers ? Math.round(dArea * frequencies[1]) : 0);
			int numMtns = (mtns ? Math.round(dArea * frequencies[2]) : 0);
			int numRes = (resources ? Math.round(dArea * frequencies[3]) : 0);
			int numLava = (lava ? Math.round(dArea * frequencies[4]) : 0);
			
			// make simple starting areas
			MapPoint[][] startSq = new MapPoint[players][units + 5];
			int perimeter = 2 * (width + height);
			for (int i = 0; i<players; i++) {
				int seed = perimeter * i / players;
				MapPoint base;
				if(seed < width)
					base = new MapPoint(seed, 0);
				else {
					seed-=(width+1);
					if(seed < height)
						base = new MapPoint(width-1, seed);
					else {
						seed -= (height +1);
						if(seed < width)
							base = new MapPoint(width - seed - 1, height - 1);
						else {
							seed -= (width+1);
							base = new MapPoint(0, height - seed - 1);
						}
					}
				}
				
				startSq[i][0] = base;
				getStartingArea(startSq[i], width, height);
			}
			
			// Place the terrain elements
			int[][] map = new int[width][height];
						
			MapPoint[] waterSq = new MapPoint[numWater];
			MapPoint[] bunkerSq = new MapPoint[numBunkers];
			MapPoint[] mtnSq = new MapPoint[numMtns];
			MapPoint[] resSq = new MapPoint[numRes];
			MapPoint[] lavaSq = new MapPoint[numLava];

			
			if(water) {
				waterSq[0] = new MapPoint(rng.nextInt(width), rng.nextInt(height));
				map[waterSq[0]._x][waterSq[0]._y] += TerrainConstants.WATER;
				MapPoint last = waterSq[0];
				for(int i = 1; i < numWater; i++) {
					// stay clumped?
					if(rng.nextFloat() < WATER_CLUMP) {
						ArrayList<MapPoint> adjacent = getClumped(last, map, TerrainConstants.WATER);
						if(adjacent == null || adjacent.size() ==0) {
							//go to second half of loop	
						} else {
							waterSq[i] = adjacent.get(rng.nextInt(adjacent.size()));
							map[waterSq[i]._x][waterSq[i]._y] += TerrainConstants.WATER;
							continue;
						}
					}
					// don't stay clumped
					ArrayList<MapPoint> available = getAvailable(map, TerrainConstants.WATER);
					if(available == null || available.size() == 0)
						break;
					waterSq[i] = available.get(rng.nextInt(available.size()));
					map[waterSq[i]._x][waterSq[i]._y] += TerrainConstants.WATER;
					last = waterSq[i];
				}
			}
			
			if(bunkers) {
				for(int i = 0; i < numBunkers; i++) {	
					ArrayList<MapPoint> available = getAvailable(map, TerrainConstants.BUNKER);
					if(available == null || available.size() == 0)
						break;
					bunkerSq[i] = available.get(rng.nextInt(available.size()));
					map[bunkerSq[i]._x][bunkerSq[i]._y] += TerrainConstants.BUNKER;
				}
			}
			
			if(mtns) {
				mtnSq[0] = new MapPoint(rng.nextInt(width), rng.nextInt(height));
				map[mtnSq[0]._x][mtnSq[0]._y] += TerrainConstants.MOUNTAIN;
				MapPoint last = mtnSq[0];
				for(int i = 1; i < numMtns; i++) {
					// stay clumped?
					if(rng.nextFloat() < MTN_CLUMP) {
						ArrayList<MapPoint> adjacent = getClumped(last, map, TerrainConstants.MOUNTAIN);
						if(adjacent == null || adjacent.size() ==0) {
							//go to second half of loop	
						} else {
							mtnSq[i] = adjacent.get(rng.nextInt(adjacent.size()));
							map[mtnSq[i]._x][mtnSq[i]._y] += TerrainConstants.MOUNTAIN;
							continue;
						}
					}
					// don't stay clumped
					ArrayList<MapPoint> available = getAvailable(map, TerrainConstants.MOUNTAIN);
					if(available == null || available.size() == 0)
						break;
					mtnSq[i] = available.get(rng.nextInt(available.size()));
					map[mtnSq[i]._x][mtnSq[i]._y] += TerrainConstants.MOUNTAIN;
					last = mtnSq[i];
				}
			}
			
			if(resources) {
				resSq[0] = new MapPoint(rng.nextInt(width), rng.nextInt(height));
				map[resSq[0]._x][resSq[0]._y] += TerrainConstants.RESOURCES;
				MapPoint last = resSq[0];
				for(int i = 1; i < numRes; i++) {
					// stay clumped?
					if(rng.nextFloat() < RES_CLUMP) {
						ArrayList<MapPoint> adjacent = getClumped(last, map, TerrainConstants.RESOURCES);
						if(adjacent == null || adjacent.size() ==0) {
							//go to second half of loop	
						} else {
							resSq[i] = adjacent.get(rng.nextInt(adjacent.size()));
							map[resSq[i]._x][resSq[i]._y] += TerrainConstants.RESOURCES;
							continue;
						}
					}
					// don't stay clumped
					ArrayList<MapPoint> available = getAvailable(map, TerrainConstants.RESOURCES);
					if(available == null || available.size() == 0)
						break;
					resSq[i] = available.get(rng.nextInt(available.size()));
					map[resSq[i]._x][resSq[i]._y] += TerrainConstants.RESOURCES;
					last = resSq[i];
				}
			}
			
			if(lava) {
				for(int i = 0; i < numLava; i++) {
					ArrayList<MapPoint> available = getAvailable(map, TerrainConstants.IMPASSABLE);
					if(available == null || available.size() == 0)
						break;
					lavaSq[i] = available.get(rng.nextInt(available.size()));
					map[lavaSq[i]._x][lavaSq[i]._y] += TerrainConstants.IMPASSABLE;
				}
			}
			
			
			out = new PrintWriter(new FileWriter(filename));
			
			//  width|x
			StringBuilder sb = new StringBuilder();
			sb.append("width");
			sb.append(delimiter);
			sb.append(width);
			out.println(sb.toString());
			
			// height|y
			sb = new StringBuilder();
			sb.append("height");
			sb.append(delimiter);
			sb.append(height);
			out.println(sb.toString());
			
			//players|n
			sb = new StringBuilder();
			sb.append("players");
			sb.append(delimiter);
			sb.append(players);
			out.println(sb.toString());
			
			//  start|coord pairs
			for(MapPoint[] player : startSq) {
				sb = new StringBuilder();
				sb.append("start");
				for(MapPoint p : player) {
					sb.append(delimiter);
					sb.append(p._x);
					sb.append(',');
					sb.append(p._y);
				}
				out.println(sb.toString());
			}
			
			//  water|coord pairs
			if(water) {
				sb = new StringBuilder();
				sb.append("water");
				for(MapPoint p : waterSq) {
					sb.append(delimiter);
					sb.append(p._x);
					sb.append(',');
					sb.append(p._y);
				}
				out.println(sb.toString());
			}
			
			//bunkers|
			if(bunkers) {
				sb = new StringBuilder();
				sb.append("bunkers");
				sb.append(delimiter);
				for(MapPoint p : bunkerSq) {
					sb.append(p._x);
					sb.append(',');
					sb.append(p._y);
					sb.append(delimiter);
				}
				out.println(sb.toString());
			}
			
			if (mtns) {
				//mountains|
				sb = new StringBuilder();
				sb.append("mountains");
				sb.append(delimiter);
				for (MapPoint p : mtnSq) {
					sb.append(p._x);
					sb.append(',');
					sb.append(p._y);
					sb.append(delimiter);
				}
				out.println(sb.toString());
			}
			if (resources) {
				//resources
				sb = new StringBuilder();
				sb.append("resources");
				sb.append(delimiter);
				for (MapPoint p : resSq) {
					sb.append(p._x);
					sb.append(',');
					sb.append(p._y);
					sb.append(delimiter);
				}
				out.println(sb.toString());
			}
			if (lava) {
				//lava
				sb = new StringBuilder();
				sb.append("impassable");
				sb.append(delimiter);
				for (MapPoint p : lavaSq) {
					sb.append(p._x);
					sb.append(',');
					sb.append(p._y);
					sb.append(delimiter);
				}
				out.println(sb.toString());
			}
			
			
		} catch (IOException e) {
			System.err.print("Error writing random map to file: ");
			System.err.println(e.getMessage());
			return null;
		} finally {
			try {
				out.close();
			} catch (Exception e) {}
		}
		
		return filename;
	}
	
	public static String generateMap(
			String filename, int width, int height, int players, int units,
			boolean water, boolean bunkers, boolean mtns, boolean resources,
			boolean lava) {
		float[] freqs = new float[5];
		freqs[0] = WATER_FREQ;
		freqs[1] = BUNKER_FREQ;
		freqs[2] = MTN_FREQ;
		freqs[3] = RES_FREQ;
		freqs[4] = LAVA_FREQ;
		
		return generateMap(filename, width, height, players, units, water,
						   bunkers, mtns, resources, lava, freqs);
	}
	
	/*
	 * Find all valid locations recursively adjacent to point
	 */
	private static ArrayList<MapPoint> getClumped(MapPoint p, int[][] map, int type) {
		int width = map.length;
		int height = map[0].length;
		boolean[][] checked = new boolean[width][height];
		checked[p._x][p._y] = true;
		
		ArrayList<MapPoint> ret = new ArrayList<MapPoint>();
		
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				int x = p._x+i;
				int y = p._y+j;
				if(inBounds(x, y, width, height) && !checked[x][y]) {
					if(TerrainConstants.isOfType(map[x][y], type)) {
						getClumped(x, y, map, type, checked, ret);
					}
					else ret.add(new MapPoint(x, y));
					checked[x][y] = true;
				}
			}
		}
		
		return ret;
	}
	
	private static void getClumped(int x, int y, int[][] map, int type,
								boolean[][] checked, ArrayList<MapPoint> ret) {
		checked[x][y] = true;
		int width = map.length;
		int height = map[0].length;
		
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				int xn = x+i;
				int yn = y+j;
				if(inBounds(xn, yn, width, height) && !checked[xn][yn]) {
					if( TerrainConstants.isOfType(map[xn][yn], type) ) {
						getClumped(xn, yn, map, type, checked, ret);
					}
					else {
						checked[xn][yn] = true;
						ret.add(new MapPoint(xn, yn));
					}
				}
			}
		}
		
	}
	
	private static ArrayList<MapPoint> getAvailable(int[][]map, int type) {
		ArrayList<MapPoint> ret = new ArrayList<MapPoint>();
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				if( ! TerrainConstants.isOfType(map[i][j], type) )
					ret.add(new MapPoint(i, j));
			}
		}
		return ret;
	}
	
	private static void getStartingArea(MapPoint[] out, int width, int height) {
		int tot = out.length;
		int found = 1;
		boolean[][] done = new boolean[width][height];
		done[out[0]._x][out[0]._y] = true;
		for (int i = 1; i < tot; i++) {
			MapPoint last = out[i-1];
			MapPoint p = new MapPoint(last._x-1, last._y);
			if(inBounds(p, width, height) && !(done[p._x][p._y])) {
				out[found] = p;
				found++;
				if(found == tot)
					return;
				done[p._x][p._y] = true;
			}
			
			p = new MapPoint(last._x+1, last._y);
			if(inBounds(p, width, height) && !(done[p._x][p._y])) {
				out[found] = p;
				found++;
				if(found == tot)
					return;
				done[p._x][p._y] = true;
			}
			
			p = new MapPoint(last._x, last._y-1);
			if(inBounds(p, width, height) && !(done[p._x][p._y])) {
				out[found] = p;
				found++;
				if(found == tot)
					return;
				done[p._x][p._y] = true;
			}
			
			p = new MapPoint(last._x, last._y+1);
			if(inBounds(p, width, height) && !(done[p._x][p._y])) {
				out[found] = p;
				found++;
				if(found == tot)
					return;
				done[p._x][p._y] = true;
			}
		}
	}
	
	private static boolean inBounds(MapPoint p, int width, int height) {
		return inBounds(p._x, p._y, width, height);
	}
	
	private static boolean inBounds(int x, int y, int width, int height) {
		return (x > -1 && x < width && y > -1 && y < height);
	}

}
///*
///** Constant representing land. 
//public static final int LAND = 0;
///** Constant representing water. */
//public static final int WATER = 1;
///** Constant representing a bunker overlay. */
//public static final int BUNKER = 2;
///** Constant representing mountains. */
//public static final int MOUNTAIN = 4;
///** Constant representing a resource-gathering location. */
//public static final int RESOURCES = 8;
//*/