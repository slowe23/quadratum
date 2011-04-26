package net.quadratum.core;

import java.util.Set;
import java.util.HashSet;

public class MapData {
	
	/**
	 * Constructor for MapData.
	 * @param terrain the terrain
	 * @param placementLocations possible unit placement locations
	 */
	public MapData(int[][] terrain, Set<MapPoint> placementLocations)
	{
		// Copy placement locations
		_placementArea = new HashSet<MapPoint>();
		for(MapPoint point : placementLocations)
		{
			_placementArea.add(new MapPoint(point));
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
	public int[][] _terrain;
	
	/** Coordinates where a Player can place units. */
	public Set<MapPoint> _placementArea;
}
