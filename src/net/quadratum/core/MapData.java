package net.quadratum.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MapData implements Serializable {
	
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -6370513093568134333L;

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
	
	/**
	 * Constructor for MapData will null contents.
	 */
	public MapData() {
		_terrain = null;
		_placementArea = null;
	}
	
	/** Terrain of the map. */
	public int[][] _terrain;
	
	/** Coordinates where a Player can place units. */
	public Set<MapPoint> _placementArea;
}
