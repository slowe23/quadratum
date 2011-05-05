package net.quadratum.core;

public class TerrainConstants
{
	/** Constant representing land. */
	public static final int LAND = 0;
	/** Constant representing water. */
	public static final int WATER = 1;
	/** Constant representing a bunker overlay. */
	public static final int BUNKER = 2;
	/** Constant representing mountains. */
	public static final int MOUNTAIN = 4;
	/** Constant representing a resource-gathering location. */
	public static final int RESOURCES = 8;
	
	/**
	 * Checks if the given terrain is of the given type.
	 * @param terrain the terrain to check
	 * @param type the type to check against
	 * @return true if the terrain is of the type given, false otherwise.
	 */
	public static boolean isOfType(int terrain, int type) {
		if(type==LAND)
			return !(isOfType(terrain, WATER));
		
		return (terrain & type)!=0;
	}
}