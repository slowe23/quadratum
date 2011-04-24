package net.quadratum.core;

import java.util.Set;
import java.util.HashSet;

public class MapData {
	
	/**
	 * Constructor for MapData.
	 * @param terrain the terrain
	 * @param placementLocations possible unit placement locations
	 */
	MapData(int[][] terrain, Set<Point> placementLocations)
	{
		// Copy placement locations
		_placementArea = new HashSet<Point>();
		for(Point point : placementLocations)
		{
			_placementArea.add(new Point(point));
		}
		// Copy terrain
		_terrain = new int[terrain.length][terrain[0].length];
		for(int i = 0; i < terrain.length; i++)
		{
			for(int j = 0; j < terrain[i].length; j++)
			{
				_terrain[i][j] = terrain[i][j];
			}
		}
	}
	
	/** Terrain of the map. */
	int[][] _terrain;
	
	/** Coordinates where a Player can place units. */
	Set<Point> _placementArea;
}
