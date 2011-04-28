package net.quadratum.core;

public class TerrainConstants
{
	public static final int LAND = 0;
	public static final int WATER = 1;
	public static final int BUNKER = 2;
	public static final int MOUNTAIN = 4;
	public static final int RESOURCES = 8;
	
	public static boolean isOfType(int terrain, int type) {
		if(type==LAND)
			return !(isOfType(terrain, WATER));
		
		return (terrain & type)!=0;
	}
}