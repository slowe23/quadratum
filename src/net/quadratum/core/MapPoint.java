package net.quadratum.core;

public class MapPoint {
	
	/** The X coordinate of this Point. */
	public int _x;
	/** The Y coordinate of this Point. */
	public int _y;
	
	public MapPoint(int x, int y) {
		_x = x;
		_y = y;
	}
	
	/**
	 * The copy constructor for MapPoint.
	 * @param point the MapPoint to copy
	 */
	public MapPoint(MapPoint point)
	{
		_x = point._x;
		_y = point._y;
	}
}
