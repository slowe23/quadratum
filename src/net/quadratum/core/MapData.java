package net.quadratum.core;

import java.util.Set;

public class MapData {
	
	/**
	 * Constructor for MapData.
	 * @param terrain the terrain
	 * @param placementLocations possible unit placement locations
	 */
	MapData(int[][] terrain, Set<Point> placementLocations)
	{
		_terrain = terrain;
		_placementArea = placementLocations;
	}
	
	/** Terrain of the map. */
	int[][] _terrain;
	
	/** Coordinates where a Player can place units. */
	Set<Point> _placementArea;
}
